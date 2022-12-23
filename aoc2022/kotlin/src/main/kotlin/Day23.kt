import util.AdventOfCode
import util.Point2d
import util.Point2dRange

object Day23 : AdventOfCode() {

    sealed class Direction(val x: Int, val y: Int) {
        sealed interface Primary
        sealed interface Null

        object North : Primary, Direction(0, -1)
        object East : Primary, Direction(1, 0)
        object South : Primary, Direction(0, 1)
        object West : Primary, Direction(-1, 0)

        object NorthWest : Direction(-1, -1)
        object NorthEast : Direction(1, -1)
        object SouthEast : Direction(1, 1)
        object SouthWest : Direction(-1, 1)

        object Blocked : Null, Direction(0, 0)
        object NoMoveNeeded : Null, Direction(0, 0)

        override fun toString(): String {
            return this.javaClass.simpleName
        }
    }

    operator fun Direction.plus(b: Point2d): Point2d {
        return Point2d(x + b.x, y + b.y)
    }

    operator fun Point2d.plus(b: Direction): Point2d {
        return Point2d(x + b.x, y + b.y)
    }

    private val above = setOf(Direction.North, Direction.NorthEast, Direction.NorthWest)
    private val below = setOf(Direction.South, Direction.SouthEast, Direction.SouthWest)
    private val left = setOf(Direction.West, Direction.NorthWest, Direction.SouthWest)
    private val right = setOf(Direction.East, Direction.NorthEast, Direction.SouthEast)

    private fun decision(p: Point2d, elves: Set<Point2d>, order: List<Direction.Primary>): Direction {
        if (p.neighbors.none { it in elves && it != p }) {
            return Direction.NoMoveNeeded
        }

        val decision = order.firstOrNull {
            when (it) {
                Direction.North -> above
                Direction.South -> below
                Direction.East -> right
                Direction.West -> left
            }.map { n -> n + p }.none { n -> n in elves }
        } ?: Direction.Blocked

        return decision as Direction
    }

    private tailrec fun step(
        elves: Set<Point2d>,
        order: List<Direction.Primary> = listOf(Direction.North, Direction.South, Direction.West, Direction.East),
        round: Int = 0
    ): Set<Point2d> {
        if (round == 10) {
            return elves
        }
        val newElves =
            elves.map { elf -> (elf + decision(elf, elves, order)) to elf }.groupBy({ it.first }, { it.second })
                .toList().flatMap {
                    if (it.second.size > 1) {
                        it.second
                    } else {
                        listOf(it.first)
                    }
                }.toSet()

        return step(newElves, order.drop(1) + order.first(), round + 1)
    }

    private tailrec fun stepForever(
        elves: Set<Point2d>,
        order: List<Direction.Primary> = listOf(Direction.North, Direction.South, Direction.West, Direction.East),
        round: Int = 0
    ): Int {
        val decisions = elves.map { elf -> decision(elf, elves, order) to elf }

        if (decisions.all { it.first == Direction.NoMoveNeeded }) {
            return round + 1
        }

        val newElves = decisions.map { (it.first + it.second) to it.second }
            .groupBy({ it.first }, { it.second }).toList()
            .flatMap {
                if (it.second.size > 1) {
                    it.second
                } else {
                    listOf(it.first)
                }
            }.toSet()

        return stepForever(newElves, order.drop(1) + order.first(), round + 1)
    }

    private fun List<String>.toElves() =
        flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> Point2d(x, y)
                    else -> null
                }
            }
        }.toSet()

    private fun part1(input: List<String>): Int {
        return Point2dRange(step(input.toElves())).count { it !in step(input.toElves()) }
    }

    private fun part2(input: List<String>): Int {
        return stepForever(input.toElves())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 23")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
