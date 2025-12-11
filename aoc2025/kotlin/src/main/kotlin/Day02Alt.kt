import util.AdventOfCode

object Day02Alt : AdventOfCode("day02.txt") {
    private val PART1_RE = """^(\d+)\1$""".toRegex()
    private val PART2_RE = """^(\d+)\1+$""".toRegex()
    private fun String.toRange() = split("-").let { x -> x[0].toLong().rangeTo(x[1].toLong()) }
    private fun String.splitToRanges() = split(",").map { it.toRange() }.asSequence()
    private fun String.splitToSequence() = splitToRanges().flatMap { range -> range.asSequence() }

    private fun part1(input: String) =
        input.splitToSequence()
            .filter { it.toString().matches(PART1_RE) }
            .sum()

    private fun part2(input: String): Long =
        input.splitToSequence()
            .filter { it.toString().matches(PART2_RE) }
            .sum()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
