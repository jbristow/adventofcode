import util.Point2d
import util.Point2d.Companion.plus
import java.nio.file.Files
import java.nio.file.Paths

object Day11 {
    const val FILENAME = "src/main/resources/day11.txt"

    val fileData = Files.readAllLines(Paths.get(FILENAME))

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

    fun Seat?.toString() = when (this) {
        null -> "."
        is Seat.Empty -> "L"
        is Seat.Occupied -> "#"
    }

    fun Point2d.neighbors() =
        listOf(
            Point2d(x + 1, y + 1),
            Point2d(x + 1, y),
            Point2d(x + 1, y - 1),
            Point2d(x, y + 1),
            Point2d(x, y - 1),
            Point2d(x - 1, y + 1),
            Point2d(x - 1, y),
            Point2d(x - 1, y - 1),
        )

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
        ).map { map[it]!! }
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

        return findLoop(map, maxX, maxY) {
            mapValues { (k: Point2d, v: Seat) ->
                v.nextState(k.neighbors().mapNotNull { this[it] })
            }
        }
    }

    tailrec fun findLoop(
        map: Map<Point2d, Seat>,
        maxX: Int,
        maxY: Int,
        lastSeen: String = map.output(maxX, maxY),
        stepFn: Map<Point2d, Seat>.() -> Map<Point2d, Seat>,
    ): Int {
        val nextmap = map.stepFn()
        return when (val currSeen = nextmap.output(maxX, maxY)) {
            lastSeen -> nextmap.values.count { it is Seat.Occupied }
            else -> findLoop(nextmap, maxX, maxY, currSeen, stepFn)
        }
    }

    private fun Map<Point2d, Seat>.output(maxX: Int, maxY: Int): String {
        return (0 until maxY).joinToString("\n") { y ->
            (0 until maxX).joinToString("") { x ->
                this[Point2d(x, y)].toString()
            }
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


        return findLoop(map, maxX, maxY) {
            mapValues { (k: Point2d, v: Seat) ->
                v.nextState2(k.seenNeighbors(this, maxX, maxY))
            }
        }
    }
}

fun main() {
    println("hello")
    println("Step 1: ${Day11.part1(Day11.fileData)}")
    println("Step 2: ${Day11.part2(Day11.fileData)}")
}