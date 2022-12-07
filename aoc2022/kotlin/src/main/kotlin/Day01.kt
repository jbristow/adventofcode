import util.AdventOfCode

object Day01 : AdventOfCode() {

    fun part1(input: String) =
        input.split("\n\n")
            .maxOfOrNull { it.lines().sumOf(String::toInt) }

    fun part2(input: String) =
        input.split("\n\n")
            .map { it.lines().sumOf(String::toInt) }
            .sorted()
            .takeLast(3)
            .sum()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
