package net.jondotcomdotorg.aoc.y2015

import net.jondotcomdotorg.aoc.y2015.Day10.toSeeAndSay
import util.AdventOfCode

object Day10 : AdventOfCode() {
    val input = "1321131112"

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10:")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

    fun String.toSeeAndSay() =
        input.fold(listOf<Pair<Int, Int>>()) { acc, c ->
            if (acc.lastOrNull()?.first == c.digitToInt()) {
                acc.dropLast(1) + (acc.last().first to (acc.last().second + 1))
            } else {
                acc + (c.digitToInt() to 1)
            }
        }

    private fun part1(): Int {
        return expand(input.toSeeAndSay(), 40).sumOf { it.second }
    }

    private fun part2(): Int {
        return expand(input.toSeeAndSay(), 50).sumOf { it.second }
    }

    private tailrec fun expand(input: List<Pair<Int, Int>>, steps: Int): List<Pair<Int, Int>> {
        if (steps == 0) {
            return input
        }
        val next = input.fold(mutableListOf<Pair<Int, Int>>()) { acc, (digit, times) ->
            val prev = acc.removeLastOrNull()
            if (times == digit && times == prev?.first) {
                acc.add(prev.first to (prev.second + 2))
            } else if (times == prev?.first) {
                acc.add(prev.first to (prev.second + 1))
                acc.add(digit to 1)
            } else if (times == digit) {
                if (prev != null) acc.add(prev)
                acc.add(digit to 2)
            } else {
                if (prev != null) acc.add(prev)
                acc.add(times to 1)
                acc.add(digit to 1)
            }
            acc
        }

        return expand(next, steps - 1)
    }
}
