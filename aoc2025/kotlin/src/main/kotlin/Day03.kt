import util.AdventOfCode
import kotlin.collections.map
import kotlin.text.map

object Day03 : AdventOfCode() {
    private const val TEN = 10

    fun String.toBatteries() = map { it.digitToInt() }

    fun List<String>.toBatteryBanks() = map { it.toBatteries() }

    tailrec fun findBigNum(
        row: List<Int>,
        maxLen: Int,
        digits: Long = 0,
    ): Long {
        if (maxLen <= 0) {
            return digits
        }

        val firstBig = row.dropLast(maxLen - 1).max()
        val rest = row.dropWhile { it < firstBig }.drop(1)
        return findBigNum(rest, maxLen - 1, digits * TEN + firstBig)
    }

    private fun part1(input: List<String>): Long =
        input.toBatteryBanks()
            .sumOf { findBigNum(it, maxLen = 2) }

    private fun part2(input: List<String>): Long =
        input.toBatteryBanks()
            .sumOf { row -> findBigNum(row, maxLen = 12) }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 3")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
