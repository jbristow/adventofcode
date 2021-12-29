package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import util.Point2d
import util.plus

object Day03 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day03:")
        println("\tPart 1: ${part1(inputFile)}")
        println("\tPart 2: ${part2(inputFile)}")
    }

    sealed class Direction(val delta: Point2d) {
        object North : Direction(Point2d(0, 1))
        object South : Direction(Point2d(0, -1))
        object East : Direction(Point2d(1, 0))
        object West : Direction(Point2d(-1, 0))
    }

    private fun Char.toDirection(): Direction {
        return when (this) {
            '^' -> Direction.North
            'v' -> Direction.South
            '>' -> Direction.East
            '<' -> Direction.West
            else -> throw IllegalArgumentException("Unknown direction $this")
        }
    }

    private fun part1(input: String): Int {
        return input.map { it.toDirection() }
            .runningFold(Point2d(0, 0)) { acc, it -> acc + it.delta }
            .distinct()
            .count()
    }

    private fun part2(input: String) =
        input.asSequence()
            .map { it.toDirection() }
            .chunked(2)
            .runningFold(listOf(Point2d(0, 0), Point2d(0, 0))) { (santa, robot), (santaDir, robotDir) ->
                listOf(santa + santaDir.delta, robot + robotDir.delta)
            }.flatten()
            .distinct()
            .count()
}
