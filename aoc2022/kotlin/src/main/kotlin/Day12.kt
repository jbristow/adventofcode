import util.AStarSearch
import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.manhattanDistance

object Day12 : AdventOfCode() {

    data class Topomap(val heightMap: Map<Point2d, Int>, val startLoc: Point2d, val endLoc: Point2d)

    private fun parseInput(input: List<String>): Topomap {
        val heightCharMap = input.flatMapIndexed { y, row ->
            row.mapIndexed { x, height -> Point2d(x, y) to height }
        }.toMap()
        val startLoc = heightCharMap.filterValues { it == 'S' }.keys.first()
        val endLoc = heightCharMap.filterValues { it == 'E' }.keys.first()
        val heightMap = heightCharMap.mapValues { (_, v) ->
            when (v) {
                'S' -> 0
                'E' -> 'z'.code - 'a'.code
                else -> v.code - 'a'.code
            }
        }
        return Topomap(heightMap, startLoc, endLoc)
    }

    private fun part1(input: List<String>): Int {
        val (heightMap, startLoc, endLoc) = parseInput(input)
        val output = AStarSearch.orthogonalShortestPath(start = startLoc, end = endLoc, spaces = heightMap.keys) {
            val currHeight = heightMap[it]!!
            it.orthoNeighbors.filter { neighbor ->
                neighbor in heightMap &&
                    heightMap[neighbor]!! <= (currHeight + 1)
            }.toSet()
        }
        return output.size
    }

    private fun part2(input: List<String>): Int {
        val (heightMap, _, endLoc) = parseInput(input)
        val startingCandidates = heightMap.filterValues { it == 0 }.keys
        val output = AStarSearch.shortestPath(
            start = endLoc,
            spaces = heightMap.keys,
            isGoal = { it in startingCandidates },
            heuristicCostEstimate = { -it.manhattanDistance(endLoc) },
            neighbors = {
                it.orthoNeighbors.filter { neighbor ->
                    neighbor in heightMap && (heightMap[neighbor]!! >= (heightMap[it]!! - 1))
                }.toSet()
            }
        )
        return output.size
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
