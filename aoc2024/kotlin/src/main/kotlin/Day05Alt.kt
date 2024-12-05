import Day05.middle
import util.AdventOfCode

object Day05Alt : AdventOfCode("day05.txt") {
    private fun pageOrdering(before: Map<Int, List<Int>>) =
        Comparator<Int> { a, b ->
            when {
                b in before.getOrDefault(a, listOf()) -> -1
                a in before.getOrDefault(b, listOf()) -> 1
                else -> 0
            }
        }

    private fun part1(input: String): Int {
        val (before, updates) = Day05.parse(input)
        return updates.filter { it.sortedWith(pageOrdering(before)) == it }.sumOf { it.middle }
    }

    private fun part2(input: String): Int {
        val (before, updates) = Day05.parse(input)
        return updates
            .mapNotNull {
                when (val sorted = it.sortedWith(pageOrdering(before))) {
                    it -> null
                    else -> sorted
                }
            }.sumOf { it.middle }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
