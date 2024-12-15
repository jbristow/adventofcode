import util.AdventOfCode
import util.Direction
import util.Left
import util.Point2d
import util.Point2dRange
import util.Up
import java.util.LinkedList

object Day12 : AdventOfCode() {

    fun parse(input: List<String>): Map<Point2d, String> =
        input.flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Point2d(x, y) to c.toString() }
        }.toMap()

    data class Fence(
        val a: Point2d,
        val b: Point2d,
    ) {
        override fun equals(other: Any?): Boolean {
            if (other !is Fence) {
                return false
            }
            return (a == other.a && b == other.b) || (b == other.a && a == other.b)
        }

        override fun hashCode(): Int {
            var result = a.hashCode()
            result = 31 * result + 31 * b.hashCode()
            return result
        }
    }

    fun connectivity(
        grid: Map<Point2d, String>,
        initial: Map<Point2d, Int>? = null,
    ): Map<Point2d, Int> {
        val labelMap = initial?.toMutableMap() ?: mutableMapOf<Point2d, Int>()
        var labelMax = 0
        val range = Point2dRange(grid)
        val equivalence = mutableMapOf<Int, Set<Int>>()
        range.yRange.forEach { y ->
            range.xRange.forEach { x ->
                val p = Point2d(x, y)
                val v = grid[p]
                if (grid[p + Left.offset] == v) {
                    labelMap[p] = labelMap[p + Left.offset]!!
                }
                if ((grid[p + Up.offset] == v && grid[p + Left.offset] == v) &&
                    (
                        labelMap[p + Up.offset] != labelMap[p] || labelMap[p + Left.offset] != labelMap[p]
                        )
                ) {
                    val u = labelMap[p + Up.offset]
                    val l = labelMap[p + Left.offset]
                    when {
                        u == null && l == null -> {
                            throw Error("oops")
                        }

                        l == null -> {
                            labelMap[p] = u!!
                        }

                        u == null -> {
                            labelMap[p] = l
                        }

                        u < l -> {
                            labelMap[p] = u
                            val a = equivalence.getOrDefault(l, setOf())
                            val b = equivalence.getOrDefault(u, setOf())

                            val eq = a + b + setOf(l, u)
                            eq.forEach { e -> equivalence[e] = eq }
                        }

                        u >= l -> {
                            labelMap[p] = l
                            val a = equivalence.getOrDefault(l, setOf())
                            val b = equivalence.getOrDefault(u, setOf())

                            val eq = a + b + setOf(l, u)
                            eq.forEach { e -> equivalence[e] = eq }
                        }
                    }
                }
                if (grid[p + Left.offset] != v && grid[p + Up.offset] == v) {
                    labelMap[p] = labelMap[p + Up.offset]!!
                }
                if (grid[p + Left.offset] != v && grid[p + Up.offset] != v) {
                    labelMap[p] = labelMax
                    labelMax += 1
                }
                if (labelMap[p] == null) {
                    println("oops, $p")
                }
            }
        }
        return labelMap.mapValues { (_, v) -> equivalence[v]?.min() ?: v }
    }

    fun part1(input: List<String>): Long {
        val grid = parse(input)

        val fences = grid.keys.flatMap {
            it.orthoNeighbors.map { neighbor -> Fence(it, neighbor) }
        }.filter { fence -> grid[fence.a] != grid[fence.b] }.toSet()

        val cgrid = connectivity(grid)
        val chunks = cgrid.toList().groupBy({ it.second }, { it.first })

        return chunks.toList().sumOf { (_, v) ->
            v.size.toLong() * v.flatMap { p -> p.orthoNeighbors.map { neighbor -> Fence(p, neighbor) } }
                .count { it in fences }
        }
    }

    fun formRegion(
        grid: Map<Point2d, String>,
        point: Point2d,
        seen: MutableSet<Point2d>,
    ): Int {
        if (point in seen) {
            return 0
        }

        val label = grid.getValue(point)
        val queue = LinkedList<Point2d>(listOf(point))

        var area = 1
        var perimiter = 4
        var corners = countCorners(grid, point)
        seen.add(point)
        while (queue.isNotEmpty()) {
            val curr = queue.remove()
            curr.orthoNeighbors.filter { it in grid && grid.getValue(it) == label }
                .forEach {
                    perimiter -= 1
                    if (it !in seen) {
                        area += 1
                        perimiter += 4
                        corners += countCorners(grid, it)
                        queue.add(it)
                        seen.add(it)
                    }
                }
        }
        return area * corners
    }

    fun countCorners(
        grid: Map<Point2d, String>,
        point: Point2d,
    ): Int {
        val label = grid.getValue(point)
        return listOf(
            Direction.North to Direction.East,
            Direction.East to Direction.South,
            Direction.South to Direction.West,
            Direction.West to Direction.North,
        ).map { (a, b) ->
            listOf(
                grid[point + a.offset],
                grid[point + b.offset],
                grid[point + a.offset + b.offset],
            )
        }.count { (left, right, mid) ->
            (left != label && right != label) || (left == label && right == label && mid != label)
        }
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        val seen = mutableSetOf<Point2d>()
        return Point2dRange(grid).fold(0) { acc, curr -> acc + formRegion(grid, curr, seen) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
