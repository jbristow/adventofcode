import util.AdventOfCode
import kotlin.math.absoluteValue

object Day02 : AdventOfCode() {
    val sample = """7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9""".lines()

    private fun part1(input: List<String>) =
        parse(input).count { it.isSafe() }

    private fun parse(input: List<String>): List<List<Long>> {
        return input.map { report -> report.split("""\s+""".toRegex()).map { it.toLong() } }
    }

    private fun part2(input: List<String>) =
        parse(input).map { report ->
            report.indices.map {
                report.take(it) + report.drop(it + 1)
            }
        }.count { reports ->
            reports.any { it.isSafe() }
        }

    private fun List<Long>.isSafe(): Boolean {
        val windows = windowed(2, 1)
        return windows.all { (a, b) -> (a - b).absoluteValue in 1..3 } &&
            (windows.all { (a, b) -> a > b } ||
                windows.all { (a, b) -> a < b })
    }


    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
