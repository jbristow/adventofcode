import util.AdventOfCode
import kotlin.math.abs

object Day07 : AdventOfCode() {
    fun part1(input: String): Int {
        val crabs = input.split(",").map { it.toInt() }
        val minPos = crabs.minOrNull()!!
        val maxPos = crabs.maxOrNull()!!

        return (minPos..maxPos).minOf { crabs.sumOf { crab -> abs(crab - it) } }
    }

    fun Int.triangleNumber() = this * (this + 1) / 2

    fun part2(input: String): Int {
        val crabs = input.split(",").map { it.toInt() }
        val minPos = crabs.minOrNull()!!
        val maxPos = crabs.maxOrNull()!!

        return (minPos..maxPos).minOf { crabs.sumOf { crab -> abs(crab - it).triangleNumber() } }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 7")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
