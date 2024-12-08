import util.AdventOfCode
import util.Point2d
import util.Point2dRange
import kotlin.math.absoluteValue

object Day08 : AdventOfCode() {
    fun part1(input: List<String>): Int {
        val antennae = parseAntennaeGroups(input)
        val range = Point2dRange(input.first().indices, input.indices)
        return antennae
            .flatMap { (_, v) -> calculateAntinodes(v) }
            .filter { v -> v in range }
            .toSet()
            .size
    }

    private tailrec fun calculateAntinodes(
        input: List<Point2d>,
        output: MutableList<Point2d> = mutableListOf(),
    ): List<Point2d> {
        if (input.isEmpty()) {
            return output
        }
        val current = input.first()
        val nextInput = input.drop(1)
        output.addAll(
            nextInput.flatMap { p ->
                handlePair(current, p)
            },
        )
        return calculateAntinodes(nextInput, output)
    }

    private fun handlePair(
        current: Point2d,
        p: Point2d,
    ): List<Point2d> {
        val xDiff = (current.x - p.x).absoluteValue
        val yDiff = (current.y - p.y).absoluteValue
        return when {
            current.x < p.x && current.y < p.y -> {
                listOf(
                    current + Point2d(-xDiff, -yDiff),
                    p + Point2d(xDiff, yDiff),
                )
            }

            current.x < p.x && current.y > p.y -> {
                listOf(
                    current + Point2d(-xDiff, yDiff),
                    p + Point2d(xDiff, -yDiff),
                )
            }

            current.x > p.x && current.y > p.y -> {
                listOf(
                    current + Point2d(xDiff, yDiff),
                    p + Point2d(-xDiff, -yDiff),
                )
            }

            else -> {
                listOf(
                    current + Point2d(xDiff, -yDiff),
                    p + Point2d(-xDiff, yDiff),
                )
            }
        }
    }

    private tailrec fun calculateAntinodesExtended(
        input: List<Point2d>,
        range: Point2dRange,
        output: MutableList<Point2d> = mutableListOf(),
    ): List<Point2d> {
        if (input.isEmpty()) {
            return output
        }
        val current = input.first()
        val nextInput = input.drop(1)
        output.addAll(
            nextInput.flatMap { p ->
                handlePairExtended(current, p, range)
            },
        )
        return calculateAntinodesExtended(nextInput, range, output)
    }

    private fun handlePairExtended(
        current: Point2d,
        p: Point2d,
        range: Point2dRange,
    ): List<Point2d> {
        val xDiff = (current.x - p.x).absoluteValue
        val yDiff = (current.y - p.y).absoluteValue
        return when {
            current.x < p.x && current.y < p.y -> {
                listOf(
                    allAntinodes(current, range, Point2d(-xDiff, -yDiff)),
                    allAntinodes(p, range, Point2d(xDiff, yDiff)),
                )
            }

            current.x < p.x && current.y > p.y -> {
                listOf(
                    (allAntinodes(current, range, Point2d(-xDiff, yDiff))),
                    (allAntinodes(p, range, Point2d(xDiff, -yDiff))),
                )
            }

            current.x > p.x && current.y > p.y -> {
                listOf(
                    (allAntinodes(current, range, Point2d(xDiff, yDiff))),
                    (allAntinodes(p, range, Point2d(-xDiff, -yDiff))),
                )
            }

            else -> {
                listOf(
                    (allAntinodes(current, range, Point2d(xDiff, -yDiff))),
                    (allAntinodes(p, range, Point2d(-xDiff, yDiff))),
                )
            }
        }.flatten()
    }

    private fun allAntinodes(
        current: Point2d,
        range: Point2dRange,
        diff: Point2d,
    ): MutableList<Point2d> {
        val pointsA = mutableListOf<Point2d>()
        var pointA = current
        while (pointA in range) {
            pointsA.add(pointA)
            pointA += diff
        }
        return pointsA
    }

    fun part2(input: List<String>): Int {
        val antennae = parseAntennaeGroups(input)
        val range = Point2dRange(input.first().indices, input.indices)

        return antennae
            .flatMap { (_, v) -> calculateAntinodesExtended(v, range) }
            .filter { v -> v in range }
            .toSet()
            .size
    }

    private fun parseAntennaeGroups(input: List<String>): Map<Char, List<Point2d>> =
        input
            .flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, value ->
                    when (value) {
                        '.' -> null
                        else -> value to Point2d(x, y)
                    }
                }
            }.groupBy({ it.first }, { it.second })

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
