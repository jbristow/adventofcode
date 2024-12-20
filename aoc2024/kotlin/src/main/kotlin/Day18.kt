import util.AdventOfCode
import util.Djikstra.djikstra
import util.Point2d
import util.Point2dRange

object Day18 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 18")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun part1(input: List<String>): Int? {
        val corruption = input.map { it.split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) } }.take(1024)
        val start = Point2d(0, 0)
        val end = Point2d(70, 70)
        val grid = Point2dRange(start, end).filter { it !in corruption }.toSet()
        return djikstra(start, { it == end }, grid, { it.orthoNeighbors.toList<Point2d>() })?.first
    }

    private fun part2(input: List<String>): String {
        val corruption = input.map { it.split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) } }
        val start = Point2d(0, 0)
        val end = Point2d(70, 70)

        return BinarySearch(start, end, corruption).search().let { "${it.x},${it.y}" }
    }

    class BinarySearch(
        val start: Point2d,
        val end: Point2d,
        val corruption: List<Point2d>,
    ) {
        val grid = Point2dRange(start, end)
        tailrec fun search(
            lowerBound: Int = 1025,
            upperBound: Int = corruption.size,
        ): Point2d {
            val currentCorruption = corruption.take(lowerBound)
            val corruptedGrid = grid - currentCorruption
            val d = djikstra(
                start,
                { it == end },
                corruptedGrid.toSet(),
                { it.orthoNeighbors.filter { it in grid } },
            )
            return when {
                d == null && lowerBound == upperBound -> currentCorruption.last()
                d == null -> search(lowerBound / 2, lowerBound)
                lowerBound + 1 == upperBound -> search(upperBound, upperBound)
                else -> search((lowerBound + upperBound) / 2, upperBound)
            }
        }
    }
}
