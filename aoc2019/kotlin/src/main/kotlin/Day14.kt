import arrow.core.Option
import arrow.core.getOption
import arrow.core.some
import arrow.optics.extensions.list.cons.cons
import arrow.optics.optics
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths


fun String.toReaction() = split(" => ").let { (list, material) ->
    Reaction(
        list = list.split(", ").map(String::toMaterial),
        material = material.toMaterial()
    )
}

fun String.toMaterial() = split(" ").let { Material(it[0].toBigInteger(), it[1]) }


@optics
data class Material(val amount: BigInteger, val name: String) {
    companion object
}

@optics
data class Reaction(val list: List<Material>, val material: Material) {
    companion object
}

@optics
data class Edge(val input: String, val output: String) {
    companion object
}

fun toEdge(o: Material) = { m: Material ->
    Edge(Material.name.get(m), Material.name.get(o))
}

tailrec fun findEdges(reactions: List<Reaction>, edges: List<Edge> = emptyList()): List<Edge> {
    return when {
        reactions.isEmpty() -> edges
        else -> {
            val (inputs, output) = reactions.first()
            findEdges(reactions.drop(1), edges + inputs.map(toEdge(output)))
        }
    }
}

fun fromE(x: String) = { e: Edge -> e.input == x }
fun toE(x: String) = { e: Edge -> e.output == x }
fun noneFrom(es: List<Edge>) = { e: Edge -> es.none(fromE(e.input)) }

fun topoSort(rs: List<Reaction>): List<String> {
    tailrec fun topoSortPrime(edges: List<Edge>, stack: List<String>, result: List<String>): List<String> {
        return when {
            stack.isEmpty() -> result
            else -> {
                val here = stack.first()
                val nstack = stack.drop(1)
                val (incoming, edgesPrime) = edges.partition(toE(here))
                val next = incoming.filter(noneFrom(edgesPrime)).map(Edge::input)
                val stackPrime = nstack + next
                topoSortPrime(edgesPrime, stackPrime, here.cons(result))
            }
        }
    }
    return topoSortPrime(findEdges(rs), listOf("FUEL"), emptyList()).reversed()
}

fun requirements(
    rs: List<Reaction>,
    outputMap: Map<String, Pair<BigInteger, List<Material>>> = emptyMap()
): Map<String, Pair<BigInteger, List<Material>>> {
    return when {
        rs.isEmpty() -> outputMap
        else -> {
            val (inputs, material) = rs.first()
            val (quantity, name) = material
            requirements(rs.drop(1), outputMap.plus(name to (quantity to inputs)))
        }
    }
}

fun <K, V> alter(f: (Option<V>) -> Option<V>, k: K, m: Map<K, V>) = f(m.getOption(k)).fold({ m }, { m + (k to it) })
fun add(q: BigInteger) = { qPrime: Option<BigInteger> -> qPrime.fold({ q }, { it + q }).some() }

fun quantitiesNeeded(rs: List<Reaction>, fuel: BigInteger): Map<String, BigInteger> {
    val reqs = requirements(rs)
    fun quantityNeeded(neededByName: Map<String, BigInteger>, name: String): Map<String, BigInteger> {
        return when (name) {
            !in reqs -> neededByName
            else -> {
                val (makesQuantity, inputs) = reqs[name] ?: error("Name not found")
                val needQuantity = neededByName[name]!!
                val scale =
                    (needQuantity / makesQuantity) + (if (needQuantity % makesQuantity > BigInteger.ZERO) BigInteger.ONE else BigInteger.ZERO)
                val neededByNamePrime = alter(add(needQuantity), name, neededByName)
                val addNeeded = { n: Map<String, BigInteger>, m: Material -> alter(add(scale * m.amount), m.name, n) }
                inputs.fold(neededByNamePrime, addNeeded)
            }
        }
    }
    return topoSort(rs).fold(mapOf("FUEL" to fuel), ::quantityNeeded)
}

fun oreNeededForFuel(rs: List<Reaction>, fuel: BigInteger): BigInteger =
    quantitiesNeeded(rs, fuel)["ORE"] ?: BigInteger.ZERO

tailrec fun binarySearch(
    target: BigInteger,
    searchFn: (BigInteger) -> BigInteger,
    highIndex: BigInteger,
    lowIndex: BigInteger
): BigInteger {
    val currentIndex = (highIndex + lowIndex) / BigInteger.TWO
    val sub = searchFn(currentIndex)
    return when {
        highIndex <= lowIndex ||
                sub > target && highIndex == currentIndex ||
                lowIndex == currentIndex -> listOf(highIndex, lowIndex).min()!!
        sub == target -> currentIndex
        searchFn(lowIndex) == target -> lowIndex
        searchFn(highIndex) == target -> highIndex
        sub > target -> binarySearch(target, searchFn, currentIndex, lowIndex)
        else -> binarySearch(target, searchFn, highIndex, currentIndex)
    }
}

object Day14 {

    private const val FILENAME = "src/main/resources/day14.txt"
    private val fileData = Files.readAllLines(Paths.get(FILENAME))
    fun part1() {
        println("Part 1:")
        println(oreNeededForFuel(fileData.map(String::toReaction), BigInteger.ONE))
    }

    fun part2() {
        val reactions = fileData.map(String::toReaction)
        println("Part 2:")
        println(
            binarySearch(
                BigInteger.valueOf(1000000000000),
                { oreNeededForFuel(reactions, it) },
                BigInteger.valueOf(10000000),
                BigInteger.ONE
            )
        )
    }
}

fun main() {
    Day14.part1()
    Day14.part2()
}