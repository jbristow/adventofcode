import arrow.core.memoize
import util.AdventOfCode
import util.Point2d

object Day14 : AdventOfCode() {
    private fun List<String>.parse(): Map<Point2d, Rock> {
        return flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    'O' -> Point2d(x, y) to Rock.Round
                    '#' -> Point2d(x, y) to Rock.Cubic
                    else -> null
                }
            }
        }.toMap()
    }

    sealed interface Rock {
        data object Round : Rock

        data object Cubic : Rock
    }

    sealed class Direction(val offset: Point2d) {
        data object North : Direction(Point2d(0, -1))

        data object South : Direction(Point2d(0, 1))

        data object East : Direction(Point2d(1, 0))

        data object West : Direction(Point2d(-1, 0))

        fun boundaryCheck(
            p: Point2d,
            rowCount: Int,
            colCount: Int,
        ): Boolean {
            return when (this) {
                East -> p.x + 1 < colCount
                North -> p.y > 0
                South -> p.y + 1 < rowCount
                West -> p.x > 0
            }
        }

        fun next(): Direction {
            return when (this) {
                North -> West
                East -> North
                South -> East
                West -> South
            }
        }
    }

    private tailrec fun tiltRaw(
        map: Map<Point2d, Rock>,
        direction: Direction,
        rowCount: Int,
        colCount: Int,
    ): Map<Point2d, Rock> {
        val rocks =
            map.filterValues {
                it is Rock.Round
            }.filterKeys { direction.boundaryCheck(it, rowCount, colCount) }.filterKeys { map[(it + direction.offset)] == null }

        if (rocks.isEmpty()) {
            return map
        }
        val nextMap = map.toMutableMap()
        rocks.forEach { (point, rock) ->
            nextMap.remove(point)
            nextMap[point + direction.offset] = rock
        }

        return tiltRaw(nextMap, direction, rowCount, colCount)
    }

    private fun part1(input: List<String>): Long {
        val map = input.parse()

        val rowCount = input.size
        val colCount = input.maxOf { it.length }

        return load(tilt(map, Direction.North, rowCount, colCount), rowCount)
    }

    private fun cycleRaw(
        map: Map<Point2d, Rock>,
        rowCount: Int,
        colCount: Int,
    ): Map<Point2d, Rock> {
        return listOf(
            Direction.North,
            Direction.West,
            Direction.South,
            Direction.East,
        ).fold(map) { acc, curr -> tilt(acc, curr, rowCount, colCount) }
    }

    private fun loadRaw(
        map: Map<Point2d, Rock>,
        rowCount: Int,
    ): Long {
        return map.filterValues { it is Rock.Round }.keys.sumOf { rowCount - it.y }.toLong()
    }

    val tilt = ::tiltRaw.memoize()
    val cycle = ::cycleRaw.memoize()
    val load = ::loadRaw.memoize()

    private tailrec fun tiltAWhirl(
        map: Map<Point2d, Rock>,
        rowCount: Int,
        colCount: Int,
        cycle: Long = 0,
        seen: MutableMap<Long, Long> = mutableMapOf(),
    ): Long {
        if (cycle % 1_00 == 0L) {
            val load = load(map, rowCount)
            if (load in seen) {
                val end = cycle
                val start = seen.getValue(load)
                val offset = (1_000_000_000 - start) % (end - start) + start
                return seen.filterValues { it == offset }.keys.first()
            }
            seen[load] = cycle
        }
        if (cycle > 1_000_000) {
            return load(map, rowCount)
        }
        val nextMap = cycle(map, rowCount, colCount)

        return tiltAWhirl(nextMap, rowCount, colCount, cycle + 1, seen)
    }

    private fun part2(input: List<String>): Long {
        val map = input.parse()
        val rowCount = input.size
        val colCount = input.maxOf { it.length }
        return tiltAWhirl(map, rowCount, colCount)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 14")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
