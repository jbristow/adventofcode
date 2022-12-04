import util.AdventOfCode

object Day03 : AdventOfCode() {
    fun Char.priority() =
        this.code - if (this.isLowerCase()) 96 else 38

    fun part1(input: Sequence<String>) =
        input.flatMap {
            it.substring(0, it.length / 2).toSet().intersect(it.substring(it.length / 2).toSet())
        }.sumOf { it.priority() }

    fun part2(input: Sequence<String>) =
        input.map { it.toSet() }
            .chunked(3) { it.reduce(Set<Char>::intersect) }
            .flatten()
            .sumOf { it.priority() }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 3")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
