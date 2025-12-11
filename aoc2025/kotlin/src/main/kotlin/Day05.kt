import util.AdventOfCode
import util.merge
import java.util.Comparator.comparing

object Day05 : AdventOfCode() {

    private fun String.parseToRanges(): List<LongRange> =
        lines().map { line ->
            line.split("-")
                .let { it[0].toLong().rangeTo(it[1].toLong()) }
        }

    private fun part1(input: String): Int {
        val (rangeInput, ingredientsInput) = input.split("\n\n")
        val rawRanges = rangeInput.parseToRanges()

        val ingredients = ingredientsInput.lines().map { it.toLong() }

        return ingredients.count { ingredient -> rawRanges.any { ingredient in it } }
    }

    private tailrec fun countFresh(
        ranges: List<LongRange>,
        output: Long = 0,
    ): Long {
        if (ranges.isEmpty()) {
            return output
        }
        val head = ranges.first()
        val tail = ranges.drop(1)

        val (overlapping, disjoint) = tail.partition {
            (it.first <= head.last) || (it.last <= head.first)
        }

        return if (overlapping.isEmpty()) {
            countFresh(tail, output + (head.last - head.first) + 1)
        } else {
            val merged = overlapping.flatMap { head.merge(it) }.toSet().toList()
            countFresh(merged + disjoint, output)
        }
    }

    private fun part2(input: String): Long {
        val (rangeInput, _) = input.split("\n\n")
        val rawRanges = rangeInput.parseToRanges()
            .sortedWith(
                comparing(LongRange::first)
                    .thenComparing(LongRange::last),
            )

        return countFresh(rawRanges)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
