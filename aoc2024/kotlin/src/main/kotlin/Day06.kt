import Day06.Outcome.Looped
import util.AdventOfCode
import util.Direction
import util.Point2d
import util.Point2dRange
import util.Up
import java.util.stream.Collectors
import kotlin.streams.asStream

object Day06 : AdventOfCode() {
    data class Guard(
        var position: Point2d,
        var direction: Direction = Up,
        val loopChecks: MutableSet<Pair<Point2d, Direction>> = LinkedHashSet(listOf(position to direction)),
        var hasLooped: Boolean = false,
    ) {
        fun turn() {
            val newDirection = direction.turnRight()
            direction = newDirection
            hasLooped = (position to newDirection) in loopChecks
            loopChecks.add(position to newDirection)
        }

        fun move() {
            val newPosition = position + direction.offset
            position = newPosition
            hasLooped = (newPosition to direction) in loopChecks
            loopChecks.add(newPosition to direction)
        }
    }

    private fun part1(input: List<String>): Int {
        val rocks = makeObstacles(input)
        val range = Point2dRange(input.first().indices, input.indices)
        val start = findStartPosition(input)

        return Guard(start).patrol(rocks, range).distance
    }

    private fun part2(input: List<String>): Long {
        val rocks = makeObstacles(input)
        val start = findStartPosition(input)
        val range = Point2dRange(input.first().indices, input.indices)

        val pass1GuardPath = Guard(start).patrol(rocks, range).path

        val loopRocks =
            pass1GuardPath
                .asSequence()
                .asStream()
                .parallel()
                .map { it.first }
                .distinct()
                .filter { it !in rocks && it != start }
                .map {
                    it to Guard(start).patrol(rocks + it, range)
                }.filter { it.second is Looped }
                .map { it.first }
                .collect(Collectors.toSet())

        return loopRocks.stream().count()
    }

    private fun findStartPosition(input: List<String>) =
        input
            .flatMapIndexed { y, line ->
                line.mapIndexed { x, c -> (c to Point2d(x, y)) }
            }.find { (c, _) -> c == '^' }!!
            .second

    private fun makeObstacles(input: List<String>) =
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

    sealed class Outcome {
        val distance: Int
            get() = path.map { it.first }.distinct().size
        abstract val path: Set<Pair<Point2d, Direction>>

        class OutOfBounds(
            override val path: Set<Pair<Point2d, Direction>>,
        ) : Outcome()

        data object Looped : Outcome() {
            override val path: Set<Pair<Point2d, Direction>>
                get() = throw IllegalStateException("Looped results have no path.")
        }
    }

    private tailrec fun Guard.patrol(
        rocks: Set<Point2d>,
        range: Point2dRange,
    ): Outcome {
        val potentialStep = this.position + this.direction.offset
        return when {
            hasLooped -> Looped
            potentialStep !in range -> Outcome.OutOfBounds(loopChecks)
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
