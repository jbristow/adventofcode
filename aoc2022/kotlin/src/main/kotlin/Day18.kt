import util.AdventOfCode
import util.Point3d
import kotlin.math.abs

object Day18 : AdventOfCode() {

    private fun surfaceAreaOf(cubes: Collection<Point3d>) = cubes.sumOf { a -> 6 - cubes.count { b -> a connectsTo b } }

    private fun Point3d.sharesYZWith(b: Point3d) = y == b.y && z == b.z && abs(x - b.x) == 1
    private fun Point3d.sharesXZWith(b: Point3d) = x == b.x && z == b.z && abs(y - b.y) == 1
    private fun Point3d.sharesXYWith(b: Point3d) = x == b.x && y == b.y && abs(z - b.z) == 1
    private infix fun Point3d.connectsTo(it: Point3d) = sharesXYWith(it) || sharesXZWith(it) || sharesYZWith(it)

    private fun Set<Point3d>.noneOnOuterEdge(xRange: IntRange, yRange: IntRange, zRange: IntRange) =
        none {
            it.x == xRange.first ||
                it.x == xRange.last ||
                it.y == yRange.first ||
                it.y == yRange.last ||
                it.z == zRange.first ||
                it.z == zRange.last
        }

    private tailrec fun disjointSubsets(data: Sequence<Point3d>, subsets: Set<Set<Point3d>>): Set<Set<Point3d>> {
        val head = data.firstOrNull() ?: return subsets
        val rest = data.drop(1)

        val (headIn, headNotIn) = subsets.partition { subset -> subset.any { head connectsTo it } }

        val newSubsets = if (headIn.isEmpty()) {
            subsets.plus<Set<Point3d>>(setOf(head))
        } else {
            headNotIn.toSet().plus<Set<Point3d>>(headIn.reduce(Set<Point3d>::union) + head)
        }

        return disjointSubsets(rest, newSubsets)
    }

    private fun part1(input: List<String>) =
        surfaceAreaOf(input.map { it.split(",").map(String::toInt).let { (x, y, z) -> Point3d(x, y, z) } })

    private fun part2(input: List<String>): Int {
        val cubes = input.map { it.split(",").map(String::toInt).let { (x, y, z) -> Point3d(x, y, z) } }.toSet()

        val xRange = cubes.minOf { it.x }..cubes.maxOf { it.x }
        val yRange = cubes.minOf { it.y }..cubes.maxOf { it.y }
        val zRange = cubes.minOf { it.z }..cubes.maxOf { it.z }

        val spaces = xRange.asSequence().flatMap { x ->
            yRange.asSequence().flatMap { y ->
                zRange.asSequence().map { z ->
                    Point3d(x, y, z)
                }
            }
        }.filter { it !in cubes }

        val djs = disjointSubsets(spaces, setOf())

        return surfaceAreaOf(cubes) - djs.filter { dj -> dj.noneOnOuterEdge(xRange, yRange, zRange) }
            .sumOf { surfaceAreaOf(it) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 18")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
