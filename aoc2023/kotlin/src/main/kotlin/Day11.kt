import util.AdventOfCode
import util.Point2dL
import util.Point2dL.Companion.manhattanDistance

object Day11 : AdventOfCode() {
    fun List<String>.toGalaxies(distance: Long = 2): List<Point2dL> {
        val clearRows = this.indices.filter { this[it].none { c -> c == '#' } }
        val clearCols = (0 until this.maxOf { it.length }).filter { this.none { line -> line[it] == '#' } }

        return flatMapIndexed { y, line ->
            val expandedY = (distance - 1) * clearRows.count { it < y } + y
            line.mapIndexedNotNull { x, c ->
                val expandedX = (distance - 1) * clearCols.count { it < x } + x
                when (c) {
                    '#' -> Point2dL(expandedX.toLong(), expandedY.toLong())
                    else -> null
                }
            }
        }
    }

    fun <T> List<T>.cartesian(): Sequence<Pair<T, T>> {
        return indices.asSequence().flatMap { a ->
            (a + 1 until size).asSequence().map { b ->
                this[a] to this[b]
            }
        }
    }

    private fun part1(input: List<String>): Long {
        val galaxies = input.toGalaxies()
        return galaxies.cartesian().sumOf { (a, b) -> a.manhattanDistance(b) }
    }

    private fun part2(input: List<String>): Long {
        val galaxies = input.toGalaxies(1_000_000)
        return galaxies.cartesian().sumOf { (a, b) -> a.manhattanDistance(b) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 11")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
