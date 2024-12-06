import Day06.Outcome.Looped
import util.AdventOfCode
import util.Direction
import util.Point2d
import util.Point2dRange
import util.Up
import kotlin.streams.asStream

object Day06 : AdventOfCode() {
    data class Guard(
        var position: Point2d,
        var direction: Direction = Up,
        val seen: MutableSet<Point2d> = mutableSetOf(position),
        val loopChecks: MutableSet<Pair<Point2d, Direction>> = mutableSetOf(position to direction),
        var hasLooped: Boolean = false,
    ) {
        val initial = position to direction

        fun turn() {
            val newDirection = direction.turnRight()
            direction = newDirection
            hasLooped = (position to newDirection) in loopChecks
            loopChecks.add(position to newDirection)
        }

        fun move() {
            val newPosition = position + direction.offset
            position = newPosition
            seen.add(newPosition)
            hasLooped = (newPosition to direction) == initial
            loopChecks.add(newPosition to direction)
        }
    }

    fun part1(input: List<String>): Int {
        val rocks =
            input
                .flatMapIndexed { y, line ->
                    line
                        .mapIndexed { x, c ->
                            when (c) {
                                '#' -> Point2d(x, y)
                                else -> null
                            }
                        }.filterNotNull()
                }.toSet()
        val range = Point2dRange(0 until input.first().length, 0 until input.size)
        val start =
            input
                .flatMapIndexed { y, line ->
                    line.mapIndexed { x, c -> (c to Point2d(x, y)) }
                }.find { (c, _) -> c == '^' }!!
                .second
        val guard = Guard(start)

        return guard.patrol(rocks, range).distance
    }

    fun part2(input: List<String>): Long {
        val rocks =
            input
                .flatMapIndexed { y, line ->
                    line
                        .mapIndexed { x, c ->
                            when (c) {
                                '#' -> Point2d(x, y)
                                else -> null
                            }
                        }.filterNotNull()
                }.toSet()
        val start =
            input
                .flatMapIndexed { y, line ->
                    line.mapIndexed { x, c -> (c to Point2d(x, y)) }
                }.find { (c, _) -> c == '^' }!!
                .second

        val range = Point2dRange(0 until input.first().length, 0 until input.size)

        val pass1GuardPath = Guard(start).patrol(rocks, range).path
        return pass1GuardPath
            .asSequence()
            .asStream()
            .parallel()
            .filter { it !in rocks && it != start }
            .map {
                Guard(start).patrol(rocks + it, range)
            }.filter { it is Looped }
            .count()
    }

    sealed class Outcome(
        val distance: Int,
        val path: Set<Point2d>,
    ) {
        class OutOfBounds(
            distance: Int,
            path: Set<Point2d>,
        ) : Outcome(distance, path)

        class Looped(
            distance: Int,
            path: Set<Point2d>,
        ) : Outcome(distance, path)
    }

    private tailrec fun Guard.patrol(
        rocks: Set<Point2d>,
        range: Point2dRange,
    ): Outcome {
        val potentialStep = this.position + this.direction.offset
        return when {
            hasLooped -> Looped(seen.size, seen)
            potentialStep !in range -> Outcome.OutOfBounds(seen.size, seen)
            potentialStep in rocks -> {
                turn()
                patrol(rocks, range)
            }
            else -> {
                move()
                patrol(rocks, range)
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
