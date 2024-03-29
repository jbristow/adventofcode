import util.AdventOfCode
import util.Point2d
import util.Point2dRange

object Day13 : AdventOfCode() {
    private val foldRegex = Regex("""fold along (.)=(\d+)""")

    private fun List<String>.toDots() =
        takeWhile { it.isNotBlank() }
            .map { it.split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) } }
            .toSet()

    fun Point2d.foldAlongX(line: Int): Point2d {
        return when {
            x > line -> copy(x = 2 * line - x)
            else -> this
        }
    }

    fun Point2d.foldAlongY(line: Int): Point2d {
        return when {
            y > line -> copy(y = 2 * line - y)
            else -> this
        }
    }

    private fun buildFoldingFn(direction: String, line: Int): (Point2d) -> Point2d {
        return when (direction) {
            "x" -> { point: Point2d -> point.foldAlongX(line) }
            else -> { point: Point2d -> point.foldAlongY(line) }
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
        println(
            Point2dRange(dots).joinToString {
                when (it) {
                    in dots -> "#"
                    else -> "."
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
