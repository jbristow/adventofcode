import util.AdventOfCode
import util.Point2d
import util.Point2dRange
import util.frequency
import kotlin.math.abs

object Day10 : AdventOfCode() {
    sealed class Direction(val offset: Point2d) {
        data object North : Direction(Point2d(0, -1))

        data object South : Direction(Point2d(0, 1))

        data object East : Direction(Point2d(1, 0))

        data object West : Direction(Point2d(-1, 0))
    }

    sealed class PipeTile(vararg directions: Direction) {
        val directions = directions.toList()

        data object Vertical : PipeTile(Direction.North, Direction.South)

        data object Horizontal : PipeTile(Direction.East, Direction.West)

        data object BendNE : PipeTile(Direction.North, Direction.East)

        data object BendNW : PipeTile(Direction.North, Direction.West)

        data object BendSE : PipeTile(Direction.South, Direction.East)

        data object BendSW : PipeTile(Direction.South, Direction.West)

        data object Start : PipeTile(Direction.North, Direction.South, Direction.East, Direction.West)
    }

    private fun Char.toPipeTile(): PipeTile? {
        return when (this) {
            '|' -> PipeTile.Vertical
            '-' -> PipeTile.Horizontal
            'L' -> PipeTile.BendNE
            'J' -> PipeTile.BendNW
            'F' -> PipeTile.BendSE
            '7' -> PipeTile.BendSW
            'S' -> PipeTile.Start
            else -> null
        }
    }

    private fun List<String>.toPipeMap(): Map<Point2d, PipeTile> {
        val map =
            flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, c ->
                    when (val pt = c.toPipeTile()) {
                        null -> null
                        else -> Point2d(x, y) to pt
                    }
                }
            }.toMap()

        return map
    }

    private tailrec fun navigate(
        map: Map<Point2d, PipeTile>,
        queue: MutableList<Point2d>,
        distances: MutableMap<Point2d, Long> = queue.associateWith { 1L }.toMutableMap(),
    ): Map<Point2d, Long> {
        if (queue.isEmpty()) {
            return distances
        }
        val current = queue.removeAt(0)
        val currDist = distances.getValue(current)

        val newPoints =
            map[current]
                ?.let { it.directions.map { d -> current + d.offset } }
                ?.filter {
                    when (val d = distances[it]) {
                        null -> true
                        else -> {
                            d > currDist + 1
                        }
                    }
                }
                ?: return navigate(map, queue, distances)
        newPoints.forEach {
            distances[it] = currDist + 1
            queue.add(it)
        }
        return navigate(map, queue, distances)
    }

    private fun part1(input: List<String>): Long {
        val map = input.toPipeMap()
        val start = map.filterValues { it == PipeTile.Start }.keys.first()

        val firstSteps =
            map.filter { (p, t) -> p in start.neighbors && start in t.directions.map { p + it.offset } }.keys.toMutableList()

        return navigate(map - start, firstSteps).maxBy { it.value }.value
    }

    private fun Point2d.inside(boundary: Map<Point2d, PipeTile>): Boolean {
        val freq =
            if (y == 0) {
                emptyList()
            } else {
                Point2dRange(x..x, 0..<y)
            }.mapNotNull { boundary[it] }.frequency()

        val hCount = freq[PipeTile.Horizontal] ?: 0
        val westWalls = abs((freq[PipeTile.BendSW] ?: 0) - (freq[PipeTile.BendNW] ?: 0))
        val eastWalls = abs((freq[PipeTile.BendSE] ?: 0) - (freq[PipeTile.BendNE] ?: 0))

        return (hCount + westWalls) % 2 == 1 && (hCount + eastWalls) % 2 == 1
    }

    private fun determineStartTileType(
        map: Map<Point2d, PipeTile>,
        start: Point2d,
    ): PipeTile {
        val n = map[start + Direction.North.offset]?.directions?.contains(Direction.South) ?: false
        val s = map[start + Direction.South.offset]?.directions?.contains(Direction.North) ?: false
        val e = map[start + Direction.East.offset]?.directions?.contains(Direction.West) ?: false
        val w = map[start + Direction.West.offset]?.directions?.contains(Direction.East) ?: false

        return when {
            n && e -> PipeTile.BendNE
            n && w -> PipeTile.BendNW
            n && s -> PipeTile.Vertical
            s && e -> PipeTile.BendSE
            s && w -> PipeTile.BendSW
            e && w -> PipeTile.Horizontal
            else -> throw Exception("UNKNOWN COMBO: n=$n, s=$s, e=$e, w=$w")
        }
    }

    private fun part2(input: List<String>): Long {
        val map = input.toPipeMap().toMutableMap()
        val start = map.filterValues { it == PipeTile.Start }.keys.first()

        map[start] = determineStartTileType(map, start)

        val loop = navigate(map, mutableListOf(start)).mapValues { (k, _) -> map.getValue(k) }

        return Point2dRange(loop).filter { it !in loop && it.inside(loop) }.size.toLong()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
