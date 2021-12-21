import util.AdventOfCode
import util.Point2d
import util.Point2dRange

object Day20 : AdventOfCode() {
    sealed class Pixel {
        object Dark : Pixel()
        object Light : Pixel()

        override fun toString(): String {
            return when (this) {
                Dark -> "."
                Light -> "#"
            }
        }
    }

    private fun Char.toPixel(): Pixel {
        return when (this) {
            '#' -> Pixel.Light
            '.' -> Pixel.Dark
            else -> throw IllegalArgumentException("Unknown pixel type '$this'")
        }
    }

    private fun Pixel.toDigit(): String {
        return when (this) {
            Pixel.Dark -> "0"
            Pixel.Light -> "1"
        }
    }

    private fun Point2d.grid(): List<Point2d> {
        return listOf(
            Point2d(x - 1, y - 1),
            Point2d(x, y - 1),
            Point2d(x + 1, y - 1),
            Point2d(x - 1, y),
            this,
            Point2d(x + 1, y),
            Point2d(x - 1, y + 1),
            Point2d(x, y + 1),
            Point2d(x + 1, y + 1)
        )
    }

    private fun List<String>.process(): Pair<List<Pixel>, Map<Point2d, Pixel>> {
        val iea = first().map { it.toPixel() }
        val image = drop(2)
            .flatMapIndexed { y, row -> row.mapIndexed { x, c -> Point2d(x, y) to c.toPixel() } }
            .toMap()
            .filterValues { it == Pixel.Light }
        return iea to image
    }

    fun part1(input: List<String>): Int {
        val (iea, image) = input.process()
        return image.enhance(iea, Pixel.Dark).enhance(iea, iea[0]).count { (_, v) -> v == Pixel.Light }
    }

    private fun Map<Point2d, Pixel>.enhance(iea: List<Pixel>, default: Pixel = iea[0]) =
        Point2dRange(this)
            .associateWith {
                it.grid().joinToString("") { px -> (this[px] ?: default).toDigit() }.toInt(2)
            }.mapValues { iea[it.value] }

    fun part2(input: List<String>): Int {
        val (iea, image) = input.process()

        return (0 until 25).fold(image) { img, _ -> img.enhance(iea, Pixel.Dark).enhance(iea, iea[0]) }
            .count { (_, v) -> v == Pixel.Light }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
