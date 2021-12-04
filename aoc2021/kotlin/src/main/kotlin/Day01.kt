import util.AdventOfCode

object Day01 : AdventOfCode() {

    fun part1(input: List<Int>): Int {
        return input.windowed(2, 1).count { (a, b) -> a < b }
    }

    fun part2(input: List<Int>) =
        part1(input.windowed(3, 1).map(List<Int>::sum))

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileInts)}")
        println("\tPart 2: ${part2(inputFileInts)}")
    }
}
