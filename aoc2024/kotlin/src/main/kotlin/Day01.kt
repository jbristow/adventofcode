import util.AdventOfCode
import util.frequency
import kotlin.math.absoluteValue

object Day01 : AdventOfCode() {

    private fun splitLine(line: String) =
        line
            .split("""\s+""".toRegex())
            .map { it.toLong() }

    private fun extractLists(input: List<String>) =
        input
            .map(::splitLine)
            .fold(mutableListOf<Long>() to mutableListOf<Long>()) { (accA, accB), (a, b) ->
                accA.add(a)
                accB.add(b)
                accA to accB
            }

    private fun part1(input: List<String>) =
        extractLists(input)
            .let { (aList, bList) -> aList.sorted().zip(bList.sorted()) }
            .sumOf { (a, b) -> (a - b).absoluteValue }

    private fun part2(input: List<String>) =
        extractLists(input).let { (aList, bList) ->
            val bFreq = bList.frequency()
            aList.sumOf { a -> bFreq.getOrDefault(a, 0) * a }
        }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
