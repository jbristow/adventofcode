import util.AdventOfCode
import util.Direction
import util.Point2d

object Day16 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        val grid = inputFileLines.parse()
        val start = grid.filterValues { it is Tile.Start }.keys.first()
        val end = grid.filterValues { it is Tile.End }.keys.first()
        val (distMap, _) = initializeMap(grid, start)
        println("Day 16")
        println("\tPart 1: ${part1(end, distMap)}")
        println("\tPart 2: ${part2(start, end, distMap)}")
    }

    sealed class Tile {
        data object Empty : Tile()
        data object Start : Tile()
        data object End : Tile()
    }

    data class ReindeerState(
        val position: Point2d,
        val direction: Direction,
    ) {
        fun move(): ReindeerState = this.copy(position = this.position + direction.offset)

        fun turnRight(): ReindeerState = this.copy(direction = this.direction.turnRight())

        fun turnLeft(): ReindeerState = this.copy(direction = this.direction.turnLeft())
    }

    private fun part1(
        end: Point2d,
        distMap: Map<ReindeerState, Int>,
    ): Int? =
        distMap.filterKeys { k ->
            k.position == end
        }.values.minOrNull()

    class Pathfinder(
        private val distMap: Map<ReindeerState, Int>,
        private val start: Point2d,
    ) {
        private val cache = mutableMapOf<ReindeerState, Set<Point2d>>()

        fun findPath(current: ReindeerState): Set<Point2d> {
            if (current.position == start) {
                return setOf(current.position)
            }
            if (current in cache) {
                return cache.getValue(current)
            }
            val currDist = distMap.getValue(current)
            val previous =
                distMap.filter { (_, v) -> v + 1 == currDist || v + 1000 == currDist }.keys.filter {
                    it.move().position == current.position ||
                        it.turnRight() == current ||
                        it.turnLeft() == current
                }
            val output: Set<Point2d> = (previous.flatMap { findPath(it) } + current.position).toSet()

            cache[current] = output
            return output
        }
    }

    private fun part2(
        start: Point2d,
        end: Point2d,
        distMap: Map<ReindeerState, Int>,
    ): Int {
        val bestEnd = distMap.filterKeys { k -> k.position == end }.toList().minBy { it.second }.first
        return Pathfinder(distMap, start).findPath(bestEnd).count()
    }

    private fun initializeMap(
        grid: Map<Point2d, Tile>,
        start: Point2d,
    ): Pair<Map<ReindeerState, Int>, Map<ReindeerState, ReindeerState>> {
        val initialState = ReindeerState(start, direction = Direction.East)
        return distMap(
            initialState,
            grid.keys.flatMap { p -> Direction.all.map { ReindeerState(p, it) } }.toSet(),
            { reindeerState: ReindeerState ->
                listOf(
                    reindeerState.turnRight(),
                    reindeerState.move(),
                    reindeerState.turnLeft(),
                ).filter { r -> r.position in grid }
            },
            { rState1: ReindeerState, rState2: ReindeerState ->
                if (rState1.position == rState2.position && rState1.direction != rState2.direction) {
                    1000
                } else if (rState1.position != rState2.position && rState1.direction == rState2.direction) {
                    1
                } else {
                    throw Exception("Illegal neighbors: $rState1 $rState2")
                }
            },
        )
    }

    private fun List<String>.parse(): Map<Point2d, Tile> =
        this.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                val p = Point2d(x, y)
                when (c) {
                    '.' -> p to Tile.Empty
                    'S' -> p to Tile.Start
                    'E' -> p to Tile.End
                    else -> null
                }
            }
        }.toMap()

    fun <P> distMap(
        start: P,
        inQ: Set<P>,
        neighborFn: (P) -> List<P>,
        distanceFn: (P, P) -> Int = { _, _ -> 1 },
    ): Pair<Map<P, Int>, Map<P, P>> {
        val q = inQ.toMutableSet()
        val dist: MutableMap<P, Int> = mutableMapOf(start to 0)
        val prev: MutableMap<P, P> = mutableMapOf()
        tailrec fun djikstraPrime(): Pair<Map<P, Int>, Map<P, P>> {
            return when (val u = q.filter { it in dist }.minByOrNull { dist.getValue(it) }) {
                null -> {
                    return dist to prev
                }

                else -> {
                    val updates =
                        neighborFn(u).filter { v ->
                            u in q && (dist[v] == null || dist[v]!! > ((dist[u] ?: 0) + distanceFn(u, v)))
                        }

                    q.remove(u)
                    dist.putAll(updates.map { it to (dist[u] ?: 0) + distanceFn(u, it) })
                    prev.putAll(updates.map { it to u })
                    djikstraPrime()
                }
            }
        }
        return djikstraPrime()
    }
}
