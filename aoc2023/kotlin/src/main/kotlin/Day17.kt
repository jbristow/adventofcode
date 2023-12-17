import Day17.Direction.East
import Day17.Direction.North
import Day17.Direction.South
import Day17.Direction.West
import util.AdventOfCode
import util.Point2d
import java.util.PriorityQueue

object Day17 : AdventOfCode() {
    private fun List<String>.toHeatLossMap(): Map<Point2d, Long> {
        return flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                Point2d(x, y) to c.digitToInt().toLong()
            }
        }.toMap()
    }

    sealed class Direction(val offset: Point2d) {
        data object North : Direction(Point2d(0, -1))

        data object East : Direction(Point2d(1, 0))

        data object South : Direction(Point2d(0, 1))

        data object West : Direction(Point2d(-1, 0))

        companion object {
            val all: Sequence<Direction>
                get() = sequenceOf(North, East, South, West)
        }
    }

    data class Move(val point: Point2d, val direction: Direction? = null, val count: Int = 0, val heatLoss: Long = 0) : Comparable<Move> {
        override fun compareTo(other: Move): Int {
            return if (other.heatLoss == this.heatLoss) {
                point.manhattanDistance(Point2d(0, 0)).compareTo(other.point.manhattanDistance(Point2d(0, 0)))
            } else {
                heatLoss.compareTo(other.heatLoss)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Move) {
                return false
            }
            return point == other.point && direction == other.direction && count == other.count
        }

        override fun hashCode(): Int {
            return point.hashCode() + direction.hashCode() + count.hashCode() * 17
        }
    }

    private tailrec fun navigate(
        map: Map<Point2d, Long>,
        points: PriorityQueue<Move>,
        target: Point2d,
        bestHeatLoss: MutableMap<Move, Long> = mutableMapOf(),
    ): Long {
        if (points.isEmpty()) {
            return bestHeatLoss.filterKeys { it.point == target }.values.first()
        }

        val current = points.poll()
        if (current.heatLoss < (bestHeatLoss[current] ?: Long.MAX_VALUE)) {
            bestHeatLoss[current] = current.heatLoss
        } else {
            return navigate(map, points, target, bestHeatLoss)
        }
        Direction.all.filter {
            current.direction !=
                when (it) {
                    East -> West
                    West -> East
                    North -> South
                    South -> North
                }
        }.map {
            it to current.point + it.offset
        }.filter { it.second in map }
            .map { (direction, nextPoint) ->
                Move(
                    nextPoint,
                    direction,
                    if (direction == current.direction) {
                        current.count + 1
                    } else {
                        1
                    },
                    current.heatLoss + map.getValue(nextPoint),
                )
            }
            .filter { it.count <= 3 }
            .filter {
                it.heatLoss < (bestHeatLoss[it] ?: Long.MAX_VALUE)
            }.forEach {
                points.add(it)
            }

        return navigate(map, points, target, bestHeatLoss)
    }

    data class UltraMove(val point: Point2d, val direction: Direction? = null, val heatLoss: Long = 0) : Comparable<UltraMove> {
        override fun compareTo(other: UltraMove): Int {
            return if (other.heatLoss == this.heatLoss) {
                point.manhattanDistance(Point2d(0, 0)).compareTo(other.point.manhattanDistance(Point2d(0, 0)))
            } else {
                heatLoss.compareTo(other.heatLoss)
            }
        }
    }

    private tailrec fun ultraNavigate(
        map: Map<Point2d, Long>,
        points: PriorityQueue<UltraMove>,
        target: Point2d,
        bestHeatLoss: MutableMap<Pair<Point2d, Direction>, Long> = mutableMapOf(),
    ): Long {
        if (points.isEmpty()) {
            return bestHeatLoss.filterKeys { k -> k.first == target }.toList().minOf { it.second }
        }

        val current = points.poll()
        if (current.point == target) {
            return current.heatLoss
        }
        if (current.direction != null && current.heatLoss < (bestHeatLoss[current.point to current.direction] ?: Long.MAX_VALUE)) {
            bestHeatLoss[current.point to current.direction] = current.heatLoss
        } else if (current.direction != null) {
            return ultraNavigate(map, points, target, bestHeatLoss)
        }
        Direction.all
            .filter { it != current.direction }
            .filter {
                // no backsies
                current.direction !=
                    when (it) {
                        East -> West
                        West -> East
                        North -> South
                        South -> North
                    }
            }
            .flatMap { dir ->
                val threePointsInDir = (1..3).map { n -> dir.offset * n + current.point }.filter { it in map }
                if (threePointsInDir.size == 3) {
                    val threePointsHeatLoss = threePointsInDir.fold(current.heatLoss) { hl, p -> hl + map.getValue(p) }
                    (4..10).map { n -> (dir.offset * n + current.point) }
                        .filter { it in map }
                        .runningFold(
                            UltraMove(
                                point = current.point,
                                direction = dir,
                                heatLoss = threePointsHeatLoss,
                            ),
                        ) { acc, p -> acc.copy(point = p, heatLoss = acc.heatLoss + map.getValue(p)) }
                        .drop(1)
                } else {
                    listOf()
                }
            }
            .filter {
                it.heatLoss < (bestHeatLoss[it.point to it.direction] ?: Long.MAX_VALUE)
            }.forEach { points.add(it) }
        return ultraNavigate(map, points, target, bestHeatLoss)
    }

    private fun List<String>.getBottomRight() = Point2d(this.maxOf { it.length } - 1, this.size - 1)

    private fun part1(input: List<String>): Long {
        return navigate(input.toHeatLossMap(), PriorityQueue(listOf(Move(Point2d(0, 0)))), input.getBottomRight())
    }

    private fun part2(input: List<String>): Long {
        return ultraNavigate(input.toHeatLossMap(), PriorityQueue(listOf(UltraMove(Point2d(0, 0)))), input.getBottomRight())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 15")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
