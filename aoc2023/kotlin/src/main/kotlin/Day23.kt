import util.AdventOfCode
import util.Direction
import util.Direction.East
import util.Direction.North
import util.Direction.South
import util.Direction.West
import util.Edge
import util.Point2d

object Day23 : AdventOfCode() {
    sealed interface Tile {
        data object Path : Tile

        data class Slope(val direction: Direction) : Tile {
            override fun toString(): String {
                return when (this.direction) {
                    North -> "^"
                    East -> ">"
                    South -> "v"
                    West -> "<"
                }
            }
        }
    }

    private fun List<String>.toTileMap(): Map<Point2d, Tile> {
        return this.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '.' -> Point2d(x, y) to Tile.Path
                    '^' -> Point2d(x, y) to Tile.Slope(North)
                    'v' -> Point2d(x, y) to Tile.Slope(South)
                    '>' -> Point2d(x, y) to Tile.Slope(East)
                    '<' -> Point2d(x, y) to Tile.Slope(West)
                    else -> null
                }
            }
        }.toMap()
    }

    private fun part1(lines: List<String>): Int {
        val map = lines.toTileMap()
        val longestPath = Navigator1(map).findLongest()

        return longestPath
    }

    class Navigator2(val map: Set<Point2d>) {
        private val start = map.find { it.y == 0 }!!
        private val end = map.find { it.y == map.maxOf { k -> k.y } }!!
        private val neighbors = map.associateWith { it.orthoNeighbors.filter { n -> n in map } }.toMutableMap()
        private val edges = merge(mutableListOf(listOf(start)))

        private tailrec fun merge(
            q: MutableList<List<Point2d>> = mutableListOf(),
            finalEdges: MutableSet<Edge<Point2d, Long>> = mutableSetOf(),
        ): Set<Edge<Point2d, Long>> {
            if (q.isEmpty()) {
                return finalEdges
            }
            val currentPath = q.removeFirst()
            val currentPoint = currentPath.last()
            val ns = neighbors.getValue(currentPoint).filter { it !in currentPath }

            if (ns.isEmpty()) {
                finalEdges.add(Edge(currentPath.first(), currentPoint, currentPath.size - 1L))
            } else if (ns.size == 1) {
                val next = ns.first()
                q.add(currentPath + next)
            } else {
                val nextEdge = Edge(currentPath.first(), currentPath.last(), currentPath.size - 1L)
                if (finalEdges.none { it.a in nextEdge && it.b in nextEdge }) {
                    finalEdges.add(nextEdge)
                    q.addAll(ns.map { n -> listOf(currentPoint, n) })
                }
            }
            return merge(q, finalEdges)
        }

        fun findLongest() = navigate(start) ?: throw Exception("No longest path?")

        private fun navigate(
            node: Point2d,
            seen: Set<Point2d> = setOf(),
        ): Long? {
            if (node == end) {
                return 0
            }

            val nextSeen = seen + node
            val neighbors = edges.filter { node in it }
            return if (neighbors.isEmpty()) {
                null
            } else {
                neighbors.mapNotNull {
                    val n =
                        when (it.a) {
                            node -> it.b
                            else -> it.a
                        }

                    if (n in seen) {
                        null
                    } else {
                        navigate(n, nextSeen)?.let { d ->
                            d + it.dist
                        }
                    }
                }.maxOrNull()
            }
        }
    }

    class Navigator1(val map: Map<Point2d, Tile>) {
        val start = map.keys.find { it.y == 0 }!!
        private val end = map.keys.find { it.y == map.keys.maxOf { k -> k.y } }!!

        fun findLongest(): Int {
            return navigate(start)
        }

        private fun navigate(
            node: Point2d,
            parent: Point2d? = null,
            depth: Int = 0,
        ): Int {
            if (node == end) {
                return depth
            }
            val next =
                when (val tile = map[node]) {
                    Tile.Path -> node.orthoNeighbors.filter { n -> n in map }
                    is Tile.Slope -> listOf(node + tile.direction.offset)
                    else -> listOf()
                }.filter { it in map && it != parent }

            if (next.isEmpty()) {
                return 0
            }
            return next.maxOf { navigate(it, node, depth + 1) }
        }
    }

    private fun part2(lines: List<String>): Long {
        val map = lines.toTileMap().keys

        return Navigator2(map).findLongest()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 23")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
