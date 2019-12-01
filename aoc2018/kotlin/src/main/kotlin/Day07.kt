import mu.KotlinLogging
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.Traversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Element
import org.apache.tinkerpop.gremlin.structure.Property
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import java.io.File

fun String.parseInstruction() = this[5] to this[36]

const val stepLabel = "Step"

typealias Step = Pair<Char, Char>

val log = KotlinLogging.logger {}

object Day07 {

    private val log = KotlinLogging.logger {}
    fun answer1(g: GraphTraversalSource, input: List<Step>): String {
        populateGraphDB(input, g)
        return g.processSteps("")
    }


    fun answer2(
        g: GraphTraversalSource,
        input: List<Step>,
        helpers: Long,
        offset: Int
    ): Int? {
        populateGraphDB(input, g, offset)
        return g.workOnSteps(0, helpers + 1)
    }

    private fun populateGraphDB(
        input: List<Step>,
        g: GraphTraversalSource,
        offset: Int = 0
    ) {
        input.fold(g.V().hasLabel(stepLabel).drop()) { t: GraphTraversal<*, *>, (a, b) ->
            t.V().upsertStep(b, offset)
                .addE("edge")
                .from(US.V<Vertex>().upsertStep(a, offset))
        }.iterate()
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            File("src/main/resources/day07.txt").readLines()
                .map(String::parseInstruction)

        val cluster = Cluster.build()
            .addContactPoint("localhost")
            .port(8182)
            .create()
        try {
            val g = EmptyGraph.instance().traversal()
                .withRemote(DriverRemoteConnection.using(cluster))

            log.info { "answer 1: ${this.answer1(g, input)}" }
            log.info { "answer 2: ${this.answer2(g, input, 5, 60)}" }
        } catch (e: Exception) {
            log.error(e) { "Problem finding answers" }
        } finally {
            cluster.close()
        }
    }
}


object US {
    private fun <A> start() = DefaultGraphTraversal<A, A>()

    fun <A : Element, B> values(vararg propertyKeys: String) =
        start<A>().values<B>(*propertyKeys)!!

    fun <A> unfold() = start<A>().unfold<A>()!!

    fun <A> addV(vertexLabel: String) =
        start<A>().addV(vertexLabel)!!

    fun inE(vararg edgeLabels: String) =
        start<Vertex>().inE(*edgeLabels)!!

    @Suppress("FunctionName")
    fun <A> V(vararg vertexIdsOrElements: Any) =
        start<A>().V(*vertexIdsOrElements)!!

    fun <A : Element, B> properties(vararg propertyKeys: String) =
        start<A>().properties<B>(*propertyKeys)!!

    fun <A : Element> label() = start<A>().label()!!
    fun <A : Property<*>, B> value() = start<A>().value<B>()!!

    fun <A> not(notTraversal: Traversal<*, *>) =
        start<A>().not(notTraversal)!!
}

private tailrec fun GraphTraversalSource.processSteps(chain: String): String {
    val next = V().hasLabel(stepLabel)
        .not(US.inE())
        .order().by("name")
        .properties<Char>("name")
        .value<Char>().limit(1).tryNext()
    return when {
        !next.isPresent -> chain
        else -> {
            V().has("name", next.get()).drop().iterate()
            processSteps(chain + next.get())
        }
    }
}

private tailrec fun GraphTraversalSource.workOnSteps(
    time: Int,
    workers: Long
): Int {
    val noInputs = V().hasLabel(stepLabel).not(US.inE())
        .group<Char, Map<String, Int>>()
        .by(US.values<Element, Char>("name"))
        .by(
            US.properties<Element, Int>("timeLeft", "total")
                .group<Element, Map<String, Int>>()
                .by(US.label<Element>())
                .by(US.value<Property<Int>, Int>())
        ).next()
        .asSequence()
        .sortedBy<Map.Entry<Char, Map<String, Int>>, Int> { (name, p) ->
            when {
                p["total"]!! - p["timeLeft"]!! > 0 -> -1000 + name.toInt()
                else -> 1000 + name.toInt()
            }
        }.toList()
        .take(workers.toInt())
    return when {
        noInputs.isEmpty() -> time
        else -> {
            log.debug { noInputs.map { (k, v) -> k to v["timeLeft"] } }
            val timeStep = decrementOrDrop(noInputs)
            workOnSteps(time + timeStep, workers)
        }
    }
}

private fun GraphTraversalSource.decrementOrDrop(
    noInputs: List<Map.Entry<Char, Map<String, Int>>>
): Int {
    val minStep = noInputs.map { it.value["timeLeft"]!! }.min()!!
    noInputs.forEach { (label, p) ->
        when {
            (p["timeLeft"]!! > minStep) ->
                V().has("name", label)
                    .property("timeLeft", p["timeLeft"]!! - minStep)
                    .next()
            else -> V().has("name", label).drop().iterate()
        }
    }
    return minStep
}


fun <S, E> GraphTraversal<S, E>.upsertStep(
    name: Char,
    offset: Int = 0
) =
    has(stepLabel, "name", name).fold()
        .coalesce(
            US.unfold(),
            US.addV<Vertex>(stepLabel)
                .property("name", name)
                .property("timeLeft", name.toInt() - 'A'.toInt() + offset + 1)
                .property("total", name.toInt() - 'A'.toInt() + offset + 1)
        )!!
