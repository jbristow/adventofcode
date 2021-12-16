import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.manhattanDistance
import util.Point2d.Companion.plus
import java.util.PriorityQueue

object Day15 : AdventOfCode() {
    fun part1(input: List<String>): Int {
        val risks = toRiskMap(input)
        return aStar(Point2d(0, 0), Point2d(risks.keys.maxOf { it.x }, risks.keys.maxOf { it.y }), risks)
    }

    fun part2(input: List<String>): Int {
        val risks = toRiskMap(input).toMutableMap()
        val xWidth = input.first().count()
        val yWidth = input.size
        val allRisks = risks.toMutableMap()
        (0..4).forEach { yFactor ->
            (0..4).forEach { xFactor ->
                if (yFactor > 0 || xFactor > 0) {
                    risks.forEach { (k, v) ->
                        val newPoint = k + Point2d(xFactor * xWidth, yFactor * yWidth)
                        val riskOffset = Point2d(xFactor, yFactor).manhattanDistance().toInt()
                        allRisks[newPoint] = when {
                            riskOffset + v > 9 -> (riskOffset + v) - 9
                            else -> riskOffset + v
                        }
                    }
                }
            }
        }

        return aStar(Point2d(0, 0), Point2d(xWidth * 5 - 1, yWidth * 5 - 1), allRisks)
    }

    private fun toRiskMap(input: List<String>) =
        input.flatMapIndexed { y, row -> row.mapIndexed { x, risk -> Point2d(x, y) to risk.digitToInt() } }.toMap()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 15")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private tailrec fun reconstructPath(
        cameFrom: Map<Point2d, Point2d>,
        risk: Map<Point2d, Int>,
        current: Point2d,
        totalRisk: Int = 0
    ): Int {
        if (current in cameFrom) {
            return reconstructPath(cameFrom, risk, cameFrom[current]!!, totalRisk + risk[current]!!)
        }
        return totalRisk
    }

    private fun aStar(start: Point2d, goal: Point2d, risks: Map<Point2d, Int>): Int {
        // stolen/adapted from https://en.wikipedia.org/wiki/A*_search_algorithm
        // The set of discovered nodes that may need to be (re-)expanded.
        // Initially, only the start node is known.
        // This is usually implemented as a min-heap or priority queue rather than a hash-set.
        val openSet = PriorityQueue<Point2d> { a, b -> b.manhattanDistance(goal).compareTo(a.manhattanDistance(goal)) }
        openSet.add(start)

        // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from start
        // to n currently known.
        val cameFrom = mutableMapOf<Point2d, Point2d>()

        // For node n, gScore[n] is the cost of the cheapest path from start to n currently known.
        val gScore = mutableMapOf<Point2d, Int>()
        gScore[start] = 0

        // For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
        // how short a path from start to finish can be if it goes through n.
        val fScore = mutableMapOf<Point2d, Int>()
        fScore[start] = 0

        return aStarInner(openSet, goal, cameFrom, gScore, fScore, risks)
    }

    private tailrec fun aStarInner(
        openSet: PriorityQueue<Point2d>,
        goal: Point2d,
        cameFrom: MutableMap<Point2d, Point2d>,
        gScore: MutableMap<Point2d, Int>,
        fScore: MutableMap<Point2d, Int>,
        risks: Map<Point2d, Int>
    ): Int {
        if (openSet.isEmpty()) {
            throw Exception("No possible path.")
        }
        val current = openSet.remove()
        if (current == goal) {
            return reconstructPath(cameFrom, risks, current)
        }

        current.orthoNeighbors.filter { it in risks }.forEach { neighbor ->
            val tentativeGScore = gScore[current]!! + risks[neighbor]!!
            if (neighbor !in gScore || tentativeGScore < gScore[neighbor]!!) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = (tentativeGScore + neighbor.manhattanDistance(goal)).toInt()
                if (neighbor !in openSet) {
                    openSet.add(neighbor)
                }
            }
        }
        return aStarInner(openSet, goal, cameFrom, gScore, fScore, risks)
    }
}
