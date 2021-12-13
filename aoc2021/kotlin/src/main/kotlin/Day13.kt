import util.AdventOfCode
import util.Point2d

object Day13 : AdventOfCode() {
    private val foldRegex = Regex("""fold along (.)=(\d+)""")

    private fun List<String>.toDots() =
        takeWhile { it.isNotBlank() }
            .map { it.split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) } }
            .toSet()

    fun foldAlongX(it: Point2d, line: Int): Point2d {
        return when {
            it.x > line -> Point2d(line - (it.x - line), it.y)
            else -> it
        }
    }

    fun foldAlongY(it: Point2d, line: Int): Point2d {
        return when {
            it.y > line -> Point2d(it.x, line - (it.y - line))
            else -> it
        }
    }

    private fun buildFoldingFn(direction: String, line: Int): (Point2d) -> Point2d {
        return when (direction) {
            "x" -> { point: Point2d -> foldAlongX(point, line) }
            else -> { point: Point2d -> foldAlongY(point, line) }
        }
    }

    private fun String.toInstruction() =
        foldRegex.matchEntire(this)!!
            .let { match -> buildFoldingFn(match.groupValues[1], match.groupValues[2].toInt()) }

    private fun List<String>.toInstructionList() = map { it.toInstruction() }.toList()

    fun part1(input: List<String>): Int {
        val dots = input.toDots()
        val instructions = input.drop(dots.count() + 1).toInstructionList()
        val foldedDots = instructions.take(1).fold(dots) { ds, foldFn -> ds.map(foldFn).toSet() }
        return foldedDots.count()
    }

    private fun printGrid(dots: Set<Point2d>) {
        val minY = dots.minOf { it.y }
        val maxY = dots.maxOf { it.y }
        val minX = dots.minOf { it.x }
        val maxX = dots.maxOf { it.x }
        println(
            (minY..maxY).joinToString("\n") { y ->
                (minX..maxX).joinToString("") { x ->
                    when {
                        Point2d(x, y) in dots -> "#"
                        else -> "."
                    }
                }
            }
        )
    }

    fun part2(input: List<String>): Int {
        val dots = input.toDots()
        val instructions = input.drop(dots.count() + 1).toInstructionList()
        val foldedDots = instructions.fold(dots) { ds, foldFn -> ds.map(foldFn).toSet() }
        printGrid(foldedDots)
        return foldedDots.count()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 13")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
