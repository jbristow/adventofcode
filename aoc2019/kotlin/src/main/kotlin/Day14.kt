import arrow.optics.extensions.list.cons.cons
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.ceil

data class ElementPiece(val amount: Int, val id: String)

fun String.toElementPiece() =
    split(" ").let { ElementPiece(it[0].toInt(), it[1]) }


object Day14 {

    private const val FILENAME = "src/main/resources/day14.txt"
    private val fileData = Files.readAllLines(Paths.get(FILENAME))

    val chemMapR = fileData.flatMap {
        val (inputs, output) = it.split(" => ")
        listOf(
            output.toElementPiece()
                    to inputs.split(", ").map(String::toElementPiece)
        )
    }.toMap()


    fun part1() {
        val labelsOnly = chemMapR.mapValues { it -> it.value.map(ElementPiece::id) }.mapKeys { it.key.id }
        val trace = traceMap(labelsOnly, listOf(setOf("FUEL")))
        println(trace.joinToString("\n"))
    }

    private tailrec fun traceMap(labelMap: Map<String, List<String>>, steps: List<Set<String>>): List<Set<String>> {
        val head = steps.first()
        return if (head.isEmpty()) {
            steps.drop(1)
        } else {
            traceMap(labelMap, head.mapNotNull { it -> labelMap[it] }.flatten().toSet().cons(steps))
        }
    }


    tailrec fun gcd(a: Int, b: Int): Int =
        if (b == 0)
            a
        else
            gcd(b, a % b)

    fun lcm(a: Int, b: Int): Int = a * b / gcd(a, b)

    private tailrec fun equivalence(items: List<ElementPiece>): List<ElementPiece> {
        println(items)
        return when {
            items.all { it.id == "ORE" } -> items
            items.first().id == "ORE" -> equivalence(items.drop(1) + items.first())
            else -> {

                val head = items.first()
//                println("head:$head")
                val tail = items.drop(1)
                val matches = chemMapR.filterKeys { it.id == head.id }.toList()
                val (output, mapping) = matches.first()
//                println("output:$output ~~ $mapping")
                val mult = ceil(head.amount.toDouble() / output.amount.toDouble()).toInt()
                val adj = mapping.map { it.copy(amount = it.amount * mult) }
//                println("adjusted:$adj")
                val newItems =
                    (tail + adj).groupBy { it.id }.values.map { epl ->
                        epl.first().copy(
                            amount = epl.sumBy { ep -> ep.amount }
                        )
                    }
//                println("ni:$newItems")
                println()
                equivalence(newItems)
            }
        }
    }
}

fun main() {
    Day14.part1()
}