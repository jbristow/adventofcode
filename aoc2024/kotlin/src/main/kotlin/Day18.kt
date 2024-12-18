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
        val totalCorruption = input.map { it.split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) } }
        val start = Point2d(0, 0)
        val end = Point2d(70, 70)

        val output = totalCorruption.indices.drop(1024).stream().parallel()
            .map { n ->
                val corruption = totalCorruption.take(n)
                val corruptionSet = corruption.toSet()
                val grid = Point2dRange(start, end).filter { p -> p !in corruptionSet }.toSet()
                val d = djikstra(
                    start,
                    { it == end },
                    grid,
                    { it.orthoNeighbors.filter { it in grid } },
                )
                if (d == null) {
                    corruption.last()
                } else {
                    null
                }
            }.filter { it != null }
            .findFirst()
        return output.orElseThrow().let { p -> "${p?.x},${p?.y}" }
    }
}
