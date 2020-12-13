import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.plus
import java.util.stream.Collectors

object Day11 : AdventOfCode() {
    sealed class Seat {
        object Empty : Seat()
        object Occupied : Seat()
        companion object {
            fun from(c: Char) =
                when (c) {
                    'L' -> Empty
                    '#' -> Occupied
                    else -> null
                }
        }

        override fun toString() =
            when (this) {
                Empty -> "Empty"
                Occupied -> "Occupied"
            }

        fun toGlyph() =
            when (this) {
                Empty -> "L"
                Occupied -> "#"
            }
    }

    fun Seat.nextState(neighbors: List<Seat>) =
        when {
            this is Seat.Empty && neighbors.all { it is Seat.Empty } -> Seat.Occupied
            this is Seat.Occupied && neighbors.count { it is Seat.Occupied } > 3 -> Seat.Empty
            else -> this
        }

    fun Seat.nextState2(neighbors: List<Seat>) =
        when {
            this is Seat.Empty && neighbors.all { it is Seat.Empty } -> Seat.Occupied
            this is Seat.Occupied && neighbors.count { it is Seat.Occupied } > 4 -> Seat.Empty
            else -> this
        }

    fun Point2d.neighbors(map: Map<Point2d, Seat>) =
        listOf(
            Point2d(x + 1, y + 1),
            Point2d(x + 1, y),
            Point2d(x + 1, y - 1),
            Point2d(x, y + 1),
            Point2d(x, y - 1),
            Point2d(x - 1, y + 1),
            Point2d(x - 1, y),
            Point2d(x - 1, y - 1),
        ).mapNotNull(map::get)

    fun Point2d.seenNeighbors(map: Map<Point2d, Seat>, maxX: Int, maxY: Int): List<Seat> {
        fun Point2d.lookingAt(direction: Point2d) =
            generateSequence(this) { it + direction }.drop(1)
                .takeWhile { it.x in (0 until maxX) && it.y in (0 until maxY) }
                .firstOrNull { it in map }
        return listOfNotNull(
            lookingAt(Point2d(-1, -1)),
            lookingAt(Point2d(-1, 0)),
            lookingAt(Point2d(-1, 1)),
            lookingAt(Point2d(0, -1)),
            lookingAt(Point2d(0, 1)),
            lookingAt(Point2d(1, -1)),
            lookingAt(Point2d(1, 0)),
            lookingAt(Point2d(1, 1))
        ).mapNotNull(map::get)
    }

    tailrec fun findLoop(
        map: Map<Point2d, Seat>,
        displayFn: (Map<Point2d, Seat>) -> String,
        lastSeen: String = displayFn(map),
        stepFn: (Map<Point2d, Seat>) -> Map<Point2d, Seat>,
    ): Int {
        val nextmap = stepFn(map)
        return when (val currSeen = displayFn(nextmap)) {
            lastSeen -> nextmap.values.count { it is Seat.Occupied }
            else -> findLoop(nextmap, displayFn, currSeen, stepFn)
        }
    }

    private fun Map<Point2d, Seat>.output(maxX: Int, maxY: Int): String {
        return (0 until maxY).joinToString("\n") { y ->
            (0 until maxX).joinToString("") { x ->
                this[Point2d(x, y)]?.toGlyph() ?: "."
            }
        }
    }

    fun part1(data: List<String>): Int {

        val maxX = data.first().length
        val maxY = data.size

        val map = data.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (val s = Seat.from(c)) {
                    null -> null
                    else -> Point2d(x, y) to s
                }
            }
        }.toMap()

        return findLoop(map, displayFn = { it.output(maxX, maxY) }) {
            it.toList().parallelStream().map { (k: Point2d, v: Seat) ->
                k to v.nextState(k.neighbors(it))
            }.collect(Collectors.toMap({ (k, _) -> k }, { (_, v) -> v }))
        }
    }

    fun part2(data: List<String>): Int {

        val maxX = data.first().length
        val maxY = data.size

        val map = data.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (val s = Seat.from(c)) {
                    null -> null
                    else -> Point2d(x, y) to s
                }
            }
        }.toMap()

        return findLoop(map, displayFn = { it.output(maxX, maxY) }) {
            it.toList().parallelStream().map { (k: Point2d, v: Seat) ->
                k to v.nextState2(k.seenNeighbors(it, maxX, maxY))
            }.collect(Collectors.toMap({ (k, _) -> k }, { (_, v) -> v }))
        }
    }
}

fun main() {
    println("Step 1: ${Day11.part1(Day11.inputFileLines)}")
    println("Step 2: ${Day11.part2(Day11.inputFileLines)}")
}
