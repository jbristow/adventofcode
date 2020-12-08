import Day07.FILENAME
import Day07.part1
import Day07.part2
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object Day07 {
    const val FILENAME = "src/main/resources/day07.txt"

    fun processLine(line: String): Pair<String, List<Pair<Int, String>>> {
        val splitLine = line.split(" bags contain ")
        val key = splitLine[0]
        val values = splitLine[1].trim('.').split(", ").map {
            val extracted = """(\d+|no) (.*) bags?""".toRegex().matchEntire(it)!!
            when (val amount = extracted.groupValues[1]) {
                "no" -> 0
                else -> amount.toInt()
            } to extracted.groupValues[2]
        }
        return key to values.filter { it.first != 0 }
    }

    tailrec fun possibleContainers(
        toConsider: Set<String>,
        seen: Set<String> = setOf(),
        bagMap: Map<String, List<Pair<Int, String>>>
    ): Set<String> =
        when {
            toConsider.isEmpty() -> seen
            else -> {
                val nextBag = toConsider.first()
                val moreToConsider = bagMap.filter { (_, values) ->
                    values.any { v: Pair<Int, String> ->
                        v.second.contains(nextBag)
                    }
                }.keys - seen
                possibleContainers(toConsider.drop(1) union moreToConsider, seen + nextBag, bagMap)
            }
        }

    fun part1(input: List<String>): Int {
        val bagMap = input.map { processLine(it) }.toMap()
        return possibleContainers(setOf("shiny gold"), bagMap = bagMap).size - 1
    }

    tailrec fun countUpBags(
        toConsider: List<Pair<Int, String>>,
        bags: Int = 0,
        bagMap: Map<String, List<Pair<Int, String>>>
    ): Int =
        when {
            toConsider.isEmpty() -> bags
            else -> {
                val (numBags, key) = toConsider.first()
                countUpBags(
                    toConsider.drop(1) + bagMap[key]!!.map { (n, v) -> n * numBags to v },
                    bags + numBags,
                    bagMap
                )
            }
        }

    fun part2(input: List<String>): Int {
        val bagMap: Map<String, List<Pair<Int, String>>> = input.map { processLine(it) }.toMap()
        return countUpBags(listOf(1 to "shiny gold"), bagMap = bagMap) - 1
    }

    fun generateGraph(filename: String, data: List<String>) {
        val bags = data.asSequence().map { processLine(it) }
        val m = bags.flatMap { (key, vals) ->
            vals.map { (n, k2) ->
                """  "$k2" -> "$key" [weight=$n];"""
            }
        }.joinToString("\n")
        val nodes =
            bags.toMap().keys.joinToString("\n") { "  \"$it\" [style=filled,fillcolor=${it.split(" ").last()}];" }
        Files.writeString(
            Paths.get(filename),
            "digraph {\n" +
                "$nodes\n$m\n}",
            StandardOpenOption.CREATE
        )
    }
}

val testData = """
    light red bags contain 1 bright white bag, 2 muted yellow bags.
    dark orange bags contain 3 bright white bags, 4 muted yellow bags.
    bright white bags contain 1 shiny gold bag.
    muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
    shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
    dark olive bags contain 3 faded blue bags, 4 dotted black bags.
    vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
    faded blue bags contain no other bags.
    dotted black bags contain no other bags.
""".trimIndent().lines()

fun main() {
    val data = Files.readAllLines(Paths.get(FILENAME))
    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
    Day07.generateGraph("day07.dot", data)
    Day07.generateGraph(
        "day07.testData.dot", testData
    )
}
