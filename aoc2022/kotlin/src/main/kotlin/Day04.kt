import util.AdventOfCode

object Day04 : AdventOfCode() {

    private fun IntRange.overlapsCompletely(b: IntRange) =
        (this.first <= b.first && this.last >= b.last) || (this.first >= b.first && this.last <= b.last)

    private fun IntRange.overlapsAtAll(b: IntRange): Boolean =
        this.first in b || this.last in b || b.first in this || b.last in this

    private fun Sequence<String>.parse() = map { it.parseLine() }

    private fun String.parseLine() =
        this.split(",").map { it.split('-').let { (start, end) -> start.toInt()..end.toInt() } }

    fun part1(input: Sequence<String>) =
        input.parse().count { (a, b) -> a.overlapsCompletely(b) }

    fun part2(input: Sequence<String>) =
        input.parse().count { (a, b) -> a.overlapsAtAll(b) }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 4")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
