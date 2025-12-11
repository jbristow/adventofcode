import util.AdventOfCode

object Day02 : AdventOfCode() {
    private fun String.toRange() = split("-").let { x -> x[0].toLong().rangeTo(x[1].toLong()) }
    private fun String.splitToRanges() = split(",").map { it.toRange() }.asSequence()
    private fun String.splitToSequence() = splitToRanges().flatMap { range -> range.asSequence().map { it.toString() } }

    private fun part1(input: String) =
        input.splitToSequence()
            .filter { it.length % 2 == 0 }
            .filter {
                it.take(it.length / 2).repeat(2) == it
            }.sumOf { it.toLong() }

    private fun part2(input: String): Long =
        input.splitToSequence()
            .filter { id ->
                1.rangeTo(id.length / 2).filter { id.length % it == 0 }
                    .any {
                        id.take(it).repeat(id.length / it) == id
                    }
            }.sumOf { it.toLong() }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
