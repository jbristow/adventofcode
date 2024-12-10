import util.AdventOfCode
import util.Point2d

object Day10 : AdventOfCode() {
    fun parseTerrain(lines: List<String>): Map<Point2d, Int> =
        lines.flatMapIndexed { y, line -> line.mapIndexed { x, c -> Point2d(x, y) to c.digitToInt() } }.toMap()

    private fun scoreTrail(
        terrain: Map<Point2d, Int>,
        position: Point2d,
    ): Set<Point2d> {
        val currHeight = terrain.getValue(position)
        if (currHeight == 9) {
            return setOf(position)
        }

        val neighbors = position.orthoNeighbors.filter { terrain[it] == currHeight + 1 }
        if (neighbors.isEmpty()) {
            return setOf()
        }

        return neighbors.flatMap { scoreTrail(terrain, it) }.toSet()
    }

    fun part1(input: List<String>): Int {
        val terrain = parseTerrain(input)
        val trailheads = terrain.filterValues { v -> v == 0 }.keys
        return trailheads.sumOf { scoreTrail(terrain, it).count() }
    }

    private fun rateTrail(
        terrain: Map<Point2d, Int>,
        position: Point2d,
    ): Int {
        val currHeight = terrain.getValue(position)
        if (currHeight == 9) {
            return 1
        }

        val neighbors = position.orthoNeighbors.filter { terrain[it] == currHeight + 1 }
        if (neighbors.isEmpty()) {
            return 0
        }

        return neighbors.sumOf { rateTrail(terrain, it) }
    }

    fun part2(input: List<String>): Int {
        val terrain = parseTerrain(input)
        val trailheads = terrain.filterValues { v -> v == 0 }.keys
        return trailheads.sumOf { rateTrail(terrain, it) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
