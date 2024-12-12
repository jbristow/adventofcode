import util.AdventOfCode
import util.Left
import util.Point2d
import util.Point2dRange
import util.Up
import kotlin.math.min

object Day12 : AdventOfCode() {

    val sample = """AAAA
BBCD
BBCC
EEEC""".lines()

    val sample2 = """OOOOO
OXOXO
OOOOO
OXOXO
OOOOO""".lines()

    val sample3 = """RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""".lines()

    fun parse(input: List<String>): Map<Point2d, String> {
        return input.flatMapIndexed { y, line -> line.mapIndexed { x, c -> Point2d(x, y) to c.toString() } }.toMap()
    }

    data class Fence(val a: Point2d, val b: Point2d) {
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

    fun connectivity(grid: Map<Point2d, String>, initial: Map<Point2d, Int>? = null): Map<Point2d, Int> {

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
                if ((grid[p + Up.offset] == v && grid[p + Left.offset] == v) && (
                        labelMap[p + Up.offset] != labelMap[p] || labelMap[p + Left.offset] != labelMap[p]
                        )
                ) {
                    println("$p ${v} (2) ${labelMap[p + Left.offset]} ${labelMap[p + Up.offset]}")
                    val u = labelMap[p + Up.offset]
                    val l = labelMap[p + Left.offset]
                    when {
                        u == null && l == null -> {
                            throw Error("oops")
                        }

                        l == null && u != null -> {
                            labelMap[p] = u
                        }

                        u == null && l != null -> {
                            labelMap[p] = l
                        }

                        u != null && l != null && u < l -> {

                            labelMap[p] = u
                            println("=>$l - $u")
                            val a = equivalence.getOrDefault(l, setOf())
                            val b = equivalence.getOrDefault(u, setOf())

                            val eq = a + b + setOf(l,u)
                            eq.forEach { e -> equivalence[e] = eq }
                        }

                        u != null && l != null && u >= l -> {
                            labelMap[p] = l
                            println("=>$u - $l")
                            val a = equivalence.getOrDefault(l, setOf())
                            val b = equivalence.getOrDefault(u, setOf())

                            val eq = a + b + setOf(l,u)
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

        println(equivalence)
        var after = labelMap.mapValues { (_, v) -> equivalence[v]?.min() ?: v }

        return after
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)

        val fences = grid.keys.flatMap {
            it.orthoNeighbors.map { neighbor -> Fence(it, neighbor) }
        }.filter { fence -> grid[fence.a] != grid[fence.b] }.toSet()

        val cgrid = connectivity(grid)
        val chunks = cgrid.toList().groupBy({ it.second }, { it.first })

        println(
            Point2dRange(grid).joinToString { p ->
                if (cgrid.getValue(p) < 26) {
                    ('a'.code + cgrid.getValue(p)).toChar().toString()
                } else if (cgrid.getValue(p) - 26 < 26) {
                    ('A'.code + cgrid.getValue(p) - 26).toChar().toString()
                } else if (cgrid.getValue(p) - 26 - 26 < 10) {
                    ('0'.code + cgrid.getValue(p) - 26 - 26).toChar().toString()
                } else {

                    (cgrid.getValue(p)).toChar().toString()
                }
            },
        )
        println(
            chunks.toList().map { (_, v) ->
                v.size.toString() + "x" + v.flatMap { p -> p.orthoNeighbors.map { neighbor -> Fence(p, neighbor) } }.count { it in fences }
            },
        )
        println(
            chunks.toList().sumOf { (_, v) ->
                v.size.toLong() * v.flatMap { p -> p.orthoNeighbors.map { neighbor -> Fence(p, neighbor) } }.count { it in fences }
            },
        )

        return -1
    }

    fun part2(input: List<String>): Int {
        return -1
    }


    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
