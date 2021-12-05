import util.AdventOfCode
import util.Point2d
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.max
import kotlin.math.min

object Day05 : AdventOfCode() {

    fun part1(input: List<String>): Int {
        val lines = input.toLine2ds()

        val points = lines.listAllPoints()

        val mappedPoints =
            points.map { p ->
                p to lines.count { line ->
                    legalSlope(line.slope) &&
                        line.inBounds(p) &&
                        line.slope.onLine(p, line.start)
                }
            }
        // for debug/swag
        // writeOutputFile("day05part01.txt", mappedPoints.toMap(), lines)

        return mappedPoints.count { (_, v) -> v > 1 }
    }

    private fun List<String>.toLine2ds(): List<Line2d> {
        return map {
            it.split(" -> ")
                .map { ps ->
                    ps.split(",")
                        .map(String::toInt)
                }.map { (x, y) -> Point2d(x, y) }
        }.map { (a, b) -> Line2d(a, b) }
    }

    private fun legalSlope(slope: Slope): Boolean {
        return slope is Slope.Horizontal || slope is Slope.Vertical
    }

    sealed class Slope {
        abstract fun onLine(candidate: Point2d, start: Point2d): Boolean
        class Regular(private val value: Double) : Slope() {
            override fun onLine(candidate: Point2d, start: Point2d) =
                (candidate.y.toDouble() - start.y) == value * (candidate.x - start.x)
        }

        object Vertical : Slope() {
            override fun onLine(candidate: Point2d, start: Point2d) = candidate.x == start.x
        }

        object Horizontal : Slope() {
            override fun onLine(candidate: Point2d, start: Point2d) = candidate.y == start.y
        }

        object SinglePoint : Slope() {
            override fun onLine(candidate: Point2d, start: Point2d) =
                candidate.y == start.y && candidate.x == candidate.y
        }
    }

    data class Line2d(val start: Point2d, val end: Point2d) {
        val minX = min(start.x, end.x)
        val maxX = max(start.x, end.x)
        val minY = min(start.y, end.y)
        val maxY = max(start.y, end.y)
        val slope: Slope = when {
            start.x == end.x && start.y == end.y -> Slope.SinglePoint
            start.x == end.x -> Slope.Vertical
            start.y == end.y -> Slope.Horizontal
            else -> Slope.Regular(((end.y - start.y) / (end.x - start.x)).toDouble())
        }

        fun inBounds(candidate: Point2d): Boolean {
            return (candidate.x in (minX..maxX) && candidate.y in (minY..maxY))
        }
    }

    fun part2(input: List<String>): Int {
        val lines = input.toLine2ds()

        val points = lines.listAllPoints()

        val mappedPoints =
            points.map { p ->
                p to lines.count { line ->
                    line.inBounds(p) &&
                        line.slope.onLine(p, line.start)
                }
            }
        // writeOutputFile("day05part02.txt", mappedPoints.toMap(), lines)
        return mappedPoints.count { (_, v) -> v > 1 }
    }

    private fun List<Line2d>.listAllPoints(): List<Point2d> {
        val minX = minOf { it.minX }
        val maxX = maxOf { it.maxX }
        val minY = minOf { it.minY }
        val maxY = maxOf { it.maxY }

        return (minX..maxX).flatMap { x -> (minY..maxY).map { y -> Point2d(x, y) } }
    }

    private fun writeOutputFile(fname: String, mpm: Map<Point2d, Int>, lines: List<Line2d>) {
        Files.newBufferedWriter(Path.of(fname)).use { bw ->
            val minX = lines.minOf { it.minX }
            val maxX = lines.maxOf { it.maxX }
            val minY = lines.minOf { it.minY }
            val maxY = lines.maxOf { it.maxY }
            bw.write(((minY..maxY).joinToString("\n") { y ->
                (minX..maxX).joinToString("") { x ->
                    val p = Point2d(x, y)
                    when (p in mpm && mpm[p] != 0) {
                        true -> "${mpm[p]}"
                        else -> "."
                    }
                }
            }))
            bw.newLine()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}