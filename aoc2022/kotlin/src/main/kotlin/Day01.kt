import util.AdventOfCode

object Day01 : AdventOfCode() {
    fun String.splitByElf(): List<String> = split("\n\n")

    fun String.calculateElfLoad(): Int = lines().sumOf(String::toInt)

    fun part1(input: String) = input.splitByElf().maxOfOrNull { it.calculateElfLoad() }


    fun part2(input: String) = input.splitByElf().map { it.calculateElfLoad() }.sorted().takeLast(3).sum()


    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
