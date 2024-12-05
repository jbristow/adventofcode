import util.*

object Day04 : AdventOfCode() {
    val directions =
        listOf(
            (0..3).map { Up.offset * it },
            (0..3).map { (Up.offset + Right.offset) * it },
            (0..3).map { Right.offset * it },
            (0..3).map { (Right.offset + Down.offset) * it },
            (0..3).map { Down.offset * it },
            (0..3).map { (Down.offset + Left.offset) * it },
            (0..3).map { Left.offset * it },
            (0..3).map { (Left.offset + Up.offset) * it },
        )

    private fun part1(input: List<String>): Int {
        val grid =
            input
                .flatMapIndexed { y, line ->
                    line.mapIndexed { x, c -> Point2d(x, y) to c }
                }.toMap()
        val gridRange = Point2dRange(grid)
        return gridRange.sumOf { point ->
            directions
                .map { direction ->
                    direction.map { point + it }.map { p -> grid[p] ?: '.' }.joinToString("")
                }.filter { it.length == 4 }
                .count { s -> s == "XMAS" }
        }
    }

    private fun part2(input: List<String>): Int {
        val grid =
            input
                .flatMapIndexed { y, line ->
                    line.mapIndexed { x, c -> Point2d(x, y) to c }
                }.toMap()
        return Point2dRange(grid).count { p ->
            val a = "${grid[p + Up.offset + Left.offset] ?: '.'}${grid[p]}${grid[p + Down.offset + Right.offset] ?: '.'}"
            val b = "${grid[p + Down.offset + Left.offset] ?: '.'}${grid[p]}${grid[p + Up.offset + Right.offset] ?: '.'}"
            (a == "MAS" || a == "SAM") && (b == "MAS" || b == "SAM")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 4")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
