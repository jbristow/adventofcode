import util.AdventOfCode
import util.Point2d

object Day05Alt : AdventOfCode("day05.txt") {

    fun part1(input: List<String>): Int {

        return input.asSequence()
            .map { it.toLine2d() }
            .filter { it.slope is Slope.Horizontal || it.slope is Slope.Vertical }
            .flatMap { it.points }
            .groupBy { it }
            .count { it.value.size > 1 }
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { it.toLine2d() }
            .flatMap { it.points }
            .groupBy { it }
            .count { it.value.size > 1 }
    }

    private fun String.toPoint2d() = split(",").map { it.toInt() }.let { (x, y) -> Point2d(x, y) }

    private fun String.toLine2d() = split(" -> ").map { it.toPoint2d() }.let { (a, b) -> Line2d(a, b) }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5 alt")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    data class Line2d(val start: Point2d, val end: Point2d) {
        private val rangeX = if (start.x < end.x) start.x..end.x else start.x downTo end.x
        private val rangeY = if (start.y < end.y) start.y..end.y else start.y downTo end.y
        val points: List<Point2d>
            get() {
                return when (slope) {
                    is Slope.Horizontal -> rangeX.map { Point2d(it, start.y) }
                    is Slope.Vertical -> rangeY.map { Point2d(start.x, it) }
                    is Slope.SinglePoint -> listOf(start)
                    is Slope.Regular -> {
                        rangeX.zip(rangeY).map { Point2d(it.first, it.second) }
                    }
                }
            }
        val slope: Slope = when {
            start.x == end.x && start.y == end.y -> Slope.SinglePoint
            start.x == end.x -> Slope.Vertical
            start.y == end.y -> Slope.Horizontal
            else -> Slope.Regular
        }
    }

    sealed class Slope {
        object Regular : Slope()
        object Vertical : Slope()
        object Horizontal : Slope()
        object SinglePoint : Slope()
    }
}