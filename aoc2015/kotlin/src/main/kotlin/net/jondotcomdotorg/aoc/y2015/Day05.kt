package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day05 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day05:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    private val String.isNice: Boolean
        get() {
            return this.count { it in "aeiou" } >= 3 &&
                this.windowed(2, 1).any { it[0] == it[1] } &&
                "ab" !in this &&
                "cd" !in this &&
                "pq" !in this &&
                "xy" !in this
        }

    private val String.isNicer: Boolean
        get() {
            return Regex("""(..).*\1""").containsMatchIn(this) &&
                Regex("""(.).\1""").containsMatchIn(this)
        }

    private fun part1(input: Sequence<String>) = input.count { it.isNice }

    private fun part2(input: Sequence<String>) = input.count { it.isNicer }
}
