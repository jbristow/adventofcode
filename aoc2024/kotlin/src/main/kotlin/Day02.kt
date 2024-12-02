import util.AdventOfCode
import kotlin.math.absoluteValue

object Day02 : AdventOfCode() {
    private fun parse(input: List<String>) = input.map { report -> report.split("""\s+""".toRegex()).map { it.toLong() } }

    private fun isSafe(report: List<Long>): Boolean {
        val windows = report.windowed(2, 1)
        val firstWindowDirection = windows.first().let { (a, b) -> a.compareTo(b) }
        return firstWindowDirection != 0 &&
            windows.all { (a, b) -> a.compareTo(b) == firstWindowDirection && (a - b).absoluteValue in 1..3 }
    }

    private fun part1(input: List<String>) = parse(input).count(::isSafe)

    private fun part2(input: List<String>) =
        parse(input)
            .map { report ->
                report.indices.map {
                    report.take(it) + report.drop(it + 1)
                }
            }.count { it.any(::isSafe) }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
