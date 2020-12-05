import Day03.FILENAME
import Day03.part1
import Day03.part2
import util.Point2d
import util.Point2d.Companion.modX
import util.Point2d.Companion.plus
import java.nio.file.Files
import java.nio.file.Paths


object Day03 {

    const val FILENAME = "src/main/resources/day03.txt"
    private fun processData(input: List<String>): Set<Point2d> {
        return input.mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                Pair(Point2d(x, y), c)
            }.filter { (_, c) -> c == '#' }
        }
            .flatten()
            .map { (p, _) -> p as Point2d }.toSet()
    }

    private tailrec fun treeHits(
        slope: Point2d,
        width: Int,
        height: Int,
        trees: Set<Point2d>,
        curr: Point2d = Point2d(0, 0),
        treesHit: Int = 0
    ): Int {
        val nextTreesHit = when {
            curr modX width in trees -> treesHit + 1
            else -> treesHit
        }
        return when {
            (curr.y + slope.y) > height -> nextTreesHit
            else -> treeHits(slope, width, height, trees, curr + slope, nextTreesHit)
        }
    }

    fun part1(lines: List<String>): Int {
        return treeHits(
            Point2d(3, 1),
            lines.first().length,
            lines.size,
            processData(lines),
        )
    }

    fun part2(lines: List<String>): Int {
        val data = processData(lines)
        return listOf(
            Point2d(1, 1),
            Point2d(3, 1),
            Point2d(5, 1),
            Point2d(7, 1),
            Point2d(1, 2)
        ).parallelStream().map {
            treeHits(it, lines.first().length, lines.size, data)
        }.reduce(1) { acc, curr -> acc * curr }
    }
}

fun main() {
    println("Part 1: ${part1(Files.readAllLines(Paths.get(FILENAME)))}")
    println("Part 2: ${part2(Files.readAllLines(Paths.get(FILENAME)))}")
}