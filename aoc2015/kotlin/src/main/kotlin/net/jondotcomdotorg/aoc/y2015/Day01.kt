package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day01 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day01:")
        println("\tPart 1: ${part1(inputFile)}")
        println("\tPart 2: ${part2(inputFile)}")
    }

    private fun part1(input: String) =
        input.fold(0) { acc, it ->
            acc + when (it) {
                '(' -> 1
                ')' -> -1
                else -> throw IllegalArgumentException("Unknown char: $it")
            }
        }

    private tailrec fun String.goToBasement(step: Int = 0, floor: Int = 0): Int {
        if (this.isEmpty()) {
            throw Exception("Santa never got to the basement.")
        }
        if (floor == -1) {
            return step
        }

        val floorDelta = when (this.first()) {
            '(' -> 1
            ')' -> -1
            else -> throw IllegalArgumentException("Unknown char: ${this.first()}")
        }
        return this.drop(1).goToBasement(step + 1, floor + floorDelta)
    }

    private fun part2(input: String) = input.goToBasement()
}
