import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.manhattanDistance
import util.Point2dRange
import java.util.PriorityQueue

object Day24 : AdventOfCode() {

    private val start = Point2d(0, -1)

    sealed class Blizzard(private val maxn: Int, private val points: List<Point2d>) {
        class Horizontal(maxn: Int, points: List<Point2d>) : Blizzard(maxn, points)
        class Vertical(maxn: Int, points: List<Point2d>) : Blizzard(maxn, points)

        override fun toString() = "${this.javaClass.simpleName}[$points]"

        operator fun get(time: Int) = points[time % maxn]
    }

    private fun Point2dRange.ltr(x: Int, y: Int): Blizzard {
        return Blizzard.Horizontal(
            xRange.last + 1,
            xRange.withIndex().map { (time, _) -> Point2d(x + time, y) }
                .map { p -> p.copy(x = p.x % (xRange.last + 1)) }
        )
    }

    private fun Point2dRange.rtl(x: Int, y: Int): Blizzard {
        return Blizzard.Horizontal(
            xRange.last + 1,
            xRange.withIndex().map { (time, _) -> Point2d((x - time) + (xRange.last + 1), y) }
                .map { p -> p.copy(x = p.x % (xRange.last + 1)) }
        )
    }

    private fun Point2dRange.ttb(x: Int, y: Int): Blizzard {
        return Blizzard.Vertical(
            yRange.last + 1,
            yRange.withIndex().map { (time, _) -> Point2d(x, time + y) }
                .map { p -> p.copy(y = p.y % (yRange.last + 1)) }
        )
    }

    private fun Point2dRange.btt(x: Int, y: Int): Blizzard {
        return Blizzard.Vertical(
            yRange.last + 1,
            yRange.withIndex().map { (time, _) -> Point2d(x, (y - time) + (yRange.last + 1)) }
                .map { p -> p.copy(y = p.y % (yRange.last + 1)) }
        )
    }

    private fun List<String>.parsePoints() =
        drop(1).dropLast(1).flatMapIndexed { y: Int, line: String ->
            line.drop(1).dropLast(1).mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> null
                    else -> Point2d(x, y)
                }
            }
        }

    private fun List<String>.parseBlizzards(pointRange: Point2dRange) =
        drop(1).dropLast(1).flatMapIndexed { y, line ->
            line.drop(1).dropLast(1).mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> null
                    '.' -> null
                    '>' -> pointRange.ltr(x, y)
                    '<' -> pointRange.rtl(x, y)
                    'v' -> pointRange.ttb(x, y)
                    '^' -> pointRange.btt(x, y)
                    else -> throw Exception("haven't done $c yet")
                }
            }
        }

    data class Progress(
        val position: Point2d,
        val distance: Long,
        val time: Int
    ) : Comparable<Progress> {
        override fun compareTo(other: Progress): Int {
            if (time == other.time) {
                return distance.compareTo(other.distance)
            }
            return time.compareTo(other.time)
        }
    }

    private tailrec fun pathfind(
        exit: Point2d,
        blizzards: List<Blizzard>,
        points: Set<Point2d>,
        xMod: Int,
        yMod: Int,
        pq: PriorityQueue<Progress>,
        seen: MutableMap<String, Long> = mutableMapOf()
    ): Int {
        if (pq.isEmpty()) {
            throw Exception("Could not arrive")
        }

        val current = pq.remove()

        if (current.distance == 1L) {
            return current.time + 1
        }

        current.position.orthoNeighbors.filter { it in points }
            .filter { blizzards.none { b -> b[current.time + 1] == it } }
            .forEach {
                val key = "$it|${(current.time + 1) % xMod}|${(current.time + 1) % yMod}"
                if ((seen[key] ?: Long.MAX_VALUE) > it.manhattanDistance(exit)) {
                    seen[key] = it.manhattanDistance(exit)
                    pq.add(Progress(it, it.manhattanDistance(exit), current.time + 1))
                }
            }

        if (blizzards.none { b -> b[current.time + 1] == current.position }) {
            val key = "${current.position}|${(current.time + 1) % xMod}|${(current.time + 1) % yMod}"
            if ((seen[key] ?: Long.MAX_VALUE) > current.distance) {
                seen[key] = current.position.manhattanDistance(exit)
                pq.add(current.copy(time = current.time + 1))
            }
        }

        return pathfind(exit, blizzards, points, xMod, yMod, pq, seen)
    }

    private fun part1(points: List<Point2d>, pointRange: Point2dRange, blizzards: List<Blizzard>, exit: Point2d): Int {
        return pathfind(
            exit,
            blizzards,
            points.toSet(),
            xMod = pointRange.xRange.last + 1,
            yMod = pointRange.yRange.last + 1,
            PriorityQueue(listOf(Progress(start, start.manhattanDistance(exit), 0)))
        )
    }

    private fun part2(points: List<Point2d>, pointRange: Point2dRange, blizzards: List<Blizzard>, exit: Point2d): Int {
        val tripOut1 = pathfind(
            exit,
            blizzards,
            points.toSet(),
            xMod = pointRange.xRange.last + 1,
            yMod = pointRange.yRange.last + 1,
            PriorityQueue(listOf(Progress(start, start.manhattanDistance(exit), 0)))
        )
        val tripBack = pathfind(
            start,
            blizzards,
            points.toSet(),
            xMod = pointRange.xRange.last + 1,
            yMod = pointRange.yRange.last + 1,
            PriorityQueue(listOf(Progress(exit, start.manhattanDistance(exit), tripOut1)))
        )

        return pathfind(
            exit,
            blizzards,
            points.toSet(),
            xMod = pointRange.xRange.last + 1,
            yMod = pointRange.yRange.last + 1,
            PriorityQueue(listOf(Progress(start, start.manhattanDistance(exit), tripBack)))
        )
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = inputFileLines
        val points = input.parsePoints()
        val pointRange = Point2dRange(points)
        val blizzards = input.parseBlizzards(pointRange)
        val exit = Point2d(pointRange.xRange.last, pointRange.yRange.last + 1)
        println("Day 24")
        println("\tPart 1: ${part1(points, pointRange, blizzards, exit)}")
        println("\tPart 2: ${part2(points, pointRange, blizzards, exit)}")
    }
}
