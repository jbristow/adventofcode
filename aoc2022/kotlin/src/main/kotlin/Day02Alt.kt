import util.AdventOfCode

object Day02Alt : AdventOfCode("day02.txt") {
    fun pointsByPlay(input: String) = when (input) {
        "A X" -> 4
        "A Y" -> 8
        "A Z" -> 3
        "B X" -> 1
        "B Y" -> 5
        "B Z" -> 9
        "C X" -> 7
        "C Y" -> 2
        "C Z" -> 6
        else -> throw IllegalArgumentException("Unknown combination: $input")
    }

    fun pointsByOutcome(input: String) = when (input) {
        "A X" -> 3
        "A Y" -> 4
        "A Z" -> 8
        "B X" -> 1
        "B Y" -> 5
        "B Z" -> 9
        "C X" -> 2
        "C Y" -> 6
        "C Z" -> 7
        else -> throw IllegalArgumentException("Unknown combination: $input")
    }

    fun part1(input: List<String>) =
        input.sumOf(::pointsByPlay)

    fun part2(input: List<String>) =
        input.sumOf(::pointsByOutcome)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
