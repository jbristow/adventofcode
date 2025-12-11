import util.AdventOfCode
import util.Direction
import util.Down
import util.Left
import util.Point2dL
import util.Point2dLRange
import util.Right
import util.Up

object Day09 : AdventOfCode() {
    val temp1 = """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3""".lines()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(temp1)}")
    }

    sealed class Edge {
        abstract val start: Point2dL
        abstract val end: Point2dL
        abstract val direction: Direction

        class Vertical(
            a: Point2dL,
            b: Point2dL,
        ) : Edge() {
            override val start = listOf(a, b).minBy { it.y }
            override val end = listOf(b).maxBy { it.y }
            override val direction: Direction = when {
                a.y < b.y -> Up
                else -> Down
            }

            fun sameY(p: Point2dL): Boolean =
                when (direction) {
                    Up -> p.y in start.y..<end.y
                    else -> p.y in (start.y + 1)..end.y
                }

            override fun isAbove(p: Point2dL): Boolean = false

            override fun isBelow(p: Point2dL): Boolean = false

            override fun isLeftOf(p: Point2dL): Boolean = p.x > start.x && sameY(p)

            override fun isRightOf(p: Point2dL): Boolean = p.x < start.x && sameY(p)

            override fun contains(p: Point2dL): Boolean = p.x == start.x && sameY(p)
        }

        class Horizontal(
            a: Point2dL,
            b: Point2dL,
        ) : Edge() {

            override val start = listOf(a, b).minBy { it.x }
            override val end = listOf(b).maxBy { it.x }
            override val direction: Direction = when {
                a.x < b.x -> Right
                else -> Left
            }

            fun sameX(p: Point2dL): Boolean =
                when (direction) {
                    Right -> p.x in start.x..<end.x
                    else -> p.x in (start.x + 1)..end.x
                }

            override fun isAbove(p: Point2dL): Boolean = p.y < start.y && sameX(p)

            override fun isBelow(p: Point2dL): Boolean = p.y > start.y && sameX(p)

            override fun isLeftOf(p: Point2dL): Boolean = false

            override fun isRightOf(p: Point2dL): Boolean = false

            override fun contains(p: Point2dL): Boolean = p.y == start.y && sameX(p)
        }

        companion object {
            fun of(
                a: Point2dL,
                b: Point2dL,
            ): Edge =
                if (a.x == b.x) {
                    Vertical(a, b)
                } else {
                    Horizontal(a, b)
                }
        }

        /*

        X-a-X
        |cdOb
        |OX-X
        |O|..
        |O|..
        |OX-X
        |OOe|
        X---X

        trbl
        a = 1131
        b = 1131
        c = 1111
        d = 1131
        e =





         above   rightOf


         */

        abstract fun isAbove(p: Point2dL): Boolean
        abstract fun isBelow(p: Point2dL): Boolean
        abstract fun isLeftOf(p: Point2dL): Boolean
        abstract fun isRightOf(p: Point2dL): Boolean

        override fun toString(): String = "$start<=$direction=>$end"

        abstract operator fun contains(p: Point2dL): Boolean
    }

    private fun part2(lines: List<String>): String {
        val corners = lines.map {
            it.split(",")
                .map(String::toLong)
                .let { (x, y) -> Point2dL(x, y) }
        }

        val edges = (corners + corners.first()).windowed(2).map { (a, b) -> Edge.of(a, b) }

        println(
            (edges + edges.take(1)).windowed(3)
                .filterNot { (a, _, c) -> a.direction == c.direction }
                .map { (a, b, c) ->
                    if (a.start.manhattanDistance(b.end) < b.start.manhattanDistance(c.end)) {
                        Point2dLRange(listOf(a.start, b.end))
                    } else {
                        Point2dLRange(listOf(b.start, c.end))
                    }
                }
                .sortedBy { -it.size }
                .take(5)
                .joinToString("\n") { "$it - ${it.size}" },
        )

        return ""
    }

    fun inside(
        p: Point2dL,
        edges: List<Edge>,
    ): Boolean {
        val above = edges.count { it.isAbove(p) }
        val below = edges.count { it.isBelow(p) }
        val left = edges.count { it.isLeftOf(p) }
        val right = edges.count { it.isRightOf(p) }
        return (above + below) % 2 == 1 &&
            (left + right) % 2 == 1
    }

    fun lookOut(
        p: Point2dL,
        edges: List<Edge>,
    ): String {
        val above = edges.count { it.isAbove(p) }
        val below = edges.count { it.isBelow(p) }
        val left = edges.count { it.isLeftOf(p) }
        val right = edges.count { it.isRightOf(p) }
        return "$above-$right-$below-$left"
    }

    private fun part1(lines: List<String>): Long {
        val corners = lines.map {
            it.split(",")
                .map(String::toLong)
                .let { (x, y) -> Point2dL(x, y) }
        }
        var largestSquare: Long? = null
        for (i in 0 until (corners.size - 1)) {
            for (j in corners.indices.drop(i)) {
                val curr = Point2dLRange(corners[i], corners[j])
                if (largestSquare == null || largestSquare < curr.size) {
                    largestSquare = curr.size
                }
            }
        }
        return (largestSquare ?: error("oops"))
    }
}
