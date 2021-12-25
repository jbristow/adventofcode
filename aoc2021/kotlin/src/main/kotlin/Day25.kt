import util.AdventOfCode
import util.Point2d

object Day25 : AdventOfCode() {
    sealed class SeaCucumber {
        object East : SeaCucumber()
        object South : SeaCucumber()
    }

    fun part1(input: List<String>): Int {
        val xWidth = input.first().length
        val yWidth = input.size

        val cucumbers = input.flatMapIndexed { y: Int, row: String ->
            row.toList().mapIndexedNotNull { x, c ->
                when (c) {
                    '>' -> Point2d(x, y) to SeaCucumber.East
                    'v' -> Point2d(x, y) to SeaCucumber.South
                    else -> null
                }
            }
        }.toMap().toMutableMap()

        return step(cucumbers, xWidth, yWidth)
    }

    private fun printGrid(
        xWidth: Int,
        yWidth: Int,
        cucumbers1: Map<Point2d, SeaCucumber>,
    ) {
        println((0 until yWidth).joinToString("\n") { y ->
            (0 until xWidth).joinToString("") { x ->
                when (cucumbers1[Point2d(x, y)]) {
                    SeaCucumber.East -> ">"
                    SeaCucumber.South -> "v"
                    else -> "."
                }
            }
        })
    }

    private tailrec fun step(
        cucumbers: MutableMap<Point2d, SeaCucumber>,
        xWidth: Int,
        yWidth: Int,
        step: Int = 0,
    ): Int {
        val eastMoves = cucumbers.filterValues { it is SeaCucumber.East }
            .map { (p, sc) -> (p to p.copy(x = (p.x + 1) % xWidth)) to sc }
            .filter { (m, _) -> m.second !in cucumbers }
        eastMoves.forEach { (move, sc) ->
            val (start, dest) = move
            cucumbers.remove(start)
            cucumbers[dest] = sc
        }

        val southMoves = cucumbers.filterValues { it is SeaCucumber.South }
            .map { (p, sc) -> (p to p.copy(y = (p.y + 1) % yWidth)) to sc }
            .filter { (m, _) -> m.second !in cucumbers }
        southMoves.forEach { (move, sc) ->
            val (start, dest) = move
            cucumbers.remove(start)
            cucumbers[dest] = sc
        }
        if (southMoves.isEmpty() && eastMoves.isEmpty()) {
            printGrid(xWidth, yWidth, cucumbers)
            return step + 1
        }
        return step(cucumbers, xWidth, yWidth, step + 1)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 25")
        println("\tPart 1: ${part1(inputFileLines)}")
    }
}
