import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.plus
import util.Point2dRange

object Day14 : AdventOfCode() {
    private val sandGenerator = Point2d(500, 0)
    private val down = Point2d(0, 1)
    private val downAndLeft = Point2d(-1, 1)
    private val downAndRight = Point2d(1, 1)

    private fun String.parseLine(): List<List<Point2d>> {
        return split(" -> ").map { point -> point.parsePoint() }.windowed(2)
    }

    private fun String.parsePoint(): Point2d {
        return split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) }
    }

    sealed interface Disposition {
        class Settled(val location: Point2d) : Disposition
        object Void : Disposition
    }

    private tailrec fun dropSand(rocks: Set<Point2d>, maxY: Int, grains: Int = 0): Int {
        return when (val restingPoint = sandFalls(Point2d(500, 0), rocks, maxY)) {
            is Disposition.Void -> grains
            is Disposition.Settled -> {
                dropSand(rocks + restingPoint.location, maxY, grains + 1)
            }
        }
    }

    private val fallDirs = listOf(down, downAndLeft, downAndRight)

    private tailrec fun sandFalls(location: Point2d, rocks: Set<Point2d>, maxY: Int): Disposition {
        if (location.y >= maxY) {
            return Disposition.Void
        }

        return when (val nextLocation = fallDirs.map { it + location }.firstOrNull { it !in rocks }) {
            null -> Disposition.Settled(location)
            else -> sandFalls(nextLocation, rocks, maxY)
        }
    }

    private tailrec fun sandFallsFloor(location: Point2d, rocks: Set<Point2d>, maxY: Int): Point2d {
        return when (val nextLoc = fallDirs.map { it + location }.firstOrNull { it !in rocks && it.y < maxY }) {
            null -> location
            else -> sandFallsFloor(nextLoc, rocks, maxY)
        }
    }

    private tailrec fun dropSandFloor(rocks: Set<Point2d>, maxY: Int, grains: Int = 0): Int {
        if (sandGenerator in rocks) {
            return grains
        }
        val restingPoint = sandFallsFloor(sandGenerator, rocks, maxY)
        return dropSandFloor(rocks + restingPoint, maxY, grains + 1)
    }

    fun part1(input: Sequence<String>): Int {
        val rocks = input.flatMap { it.parseLine() }
            .flatMap { Point2dRange(it).toList() }
            .toSet()
        val rockRange = Point2dRange(rocks)
        return dropSand(rocks, rockRange.yRange.last)
    }

    fun part2(input: Sequence<String>): Int {
        val rocks = input.flatMap { it.parseLine() }
            .flatMap { Point2dRange(it).toList() }
            .toSet()
        val rockRange = Point2dRange(rocks)
        return dropSandFloor(rocks, rockRange.yRange.last + 2)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 14")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
