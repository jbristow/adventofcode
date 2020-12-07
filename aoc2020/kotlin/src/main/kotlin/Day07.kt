import Day07.FILENAME
import Day07.part1
import Day07.part2
import java.nio.file.Files
import java.nio.file.Paths

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
}

fun main() {
    val data = Files.readAllLines(Paths.get(FILENAME))
    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}