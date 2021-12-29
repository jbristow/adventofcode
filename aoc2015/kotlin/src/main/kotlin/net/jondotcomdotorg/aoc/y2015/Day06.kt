package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day06 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day06:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    val instructionRe = Regex("""(toggle|(?:turn (?:on|off))) (\d+),(\d+) through (\d+),(\d+)""")

    fun Sequence<String>.toInstructions(): Sequence<Pair<String, Set<Int>>> {
        return map {
            instructionRe.matchEntire(it)?.groupValues?.drop(1)
                ?: throw IllegalArgumentException("Bad input $it")
        }.map { groups ->
            groups[0] to run {
                (groups[1].toInt()..groups[3].toInt()).flatMap { x ->
                    (groups[2].toInt()..groups[4].toInt()).map { y -> y * 1000 + x }
                }.toSet()
            }
        }
    }

    private fun part1(input: Sequence<String>): Int {
        return input.toInstructions().fold(setOf<Int>()) { acc, (instr, points) ->
            when (instr) {
                "turn on" -> acc.union(points)
                "turn off" -> acc - points
                else -> (acc - points).union(points - acc)
            }
        }.size
    }

    private fun part2(input: Sequence<String>): Int {
        return input.toInstructions().fold(mutableMapOf<Int, Int>()) { acc, (instr, points) ->
            when (instr) {
                "turn on" -> points.forEach { acc[it] = (acc[it] ?: 0) + 1 }
                "turn off" -> points.filter { (acc[it] ?: 0) > 0 }.forEach { acc[it] = acc[it]!! - 1 }
                else -> points.forEach { acc[it] = (acc[it] ?: 0) + 2 }
            }
            acc
        }.values.sum()
    }
}
