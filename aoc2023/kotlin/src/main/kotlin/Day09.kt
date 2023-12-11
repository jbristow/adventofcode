import util.AdventOfCode

object Day09 : AdventOfCode() {
    private fun Sequence<String>.toLongLists(): Sequence<List<Long>> {
        return this.map { line -> line.split("""\s+""".toRegex()).map { it.toLong() } }
    }

    private fun history(current: List<Long>): Sequence<List<Long>> {
        return generateSequence(current) {
            if (it.all { v -> v == 0L }) {
                null
            } else {
                it.windowed(2).map { (a, b) -> b - a }
            }
        }
    }

    private fun computeNext(histories: Sequence<List<Long>>) = histories.sumOf { it.last() }

    private fun computePreceding(histories: Sequence<List<Long>>) = histories.map { it.first() }.toList().reduceRight { a, b -> a - b }

    private fun part1(input: Sequence<String>) =
        input.toLongLists()
            .map { history(it) }
            .sumOf { computeNext(it) }

    private fun part2(input: Sequence<String>) =
        input.toLongLists()
            .map { history(it) }
            .sumOf { computePreceding(it) }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 9")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
