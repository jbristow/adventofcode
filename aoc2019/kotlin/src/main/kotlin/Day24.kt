import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths

data class RecursivePoint(val level: Int, val point: Point) {
    override fun toString(): String {
        return "$level:(${point.x},${point.y})"
    }
}

private val middle = Point(2, 2)

object Day24 {
    private const val FILENAME = "src/main/resources/day24.txt"
    val fileData: List<String> get() = Files.readAllLines(Paths.get(FILENAME)).filter { it.isNotEmpty() }

    private fun parseMap(input: List<String>): Map<Point, Boolean> {
        return (input.indices).flatMap { y ->
            (input[0].indices).map { x ->
                Point(x, y) to (input[y][x] == '#')
            }
        }.toMap()
    }

    private fun shouldLive(self: Boolean, neighbors: List<Boolean>) =
        neighbors.count { it }.let { (!self && it == 2) || (it == 1) }

    private fun neighbors(rp: RecursivePoint) = when {
        rp.point == Point(0, 0) ->
            listOf(
                RecursivePoint(rp.level - 1, Point(2, 1)),
                RecursivePoint(rp.level - 1, Point(1, 2))
            ) + rp.right() + rp.below()
        rp.point == Point(4, 4) ->
            listOf(
                RecursivePoint(rp.level - 1, Point(2, 3)),
                RecursivePoint(rp.level - 1, Point(3, 2))
            ) + rp.left() + rp.above()
        rp.point == Point(0, 4) ->
            listOf(
                RecursivePoint(rp.level - 1, Point(2, 3)),
                RecursivePoint(rp.level - 1, Point(1, 2))
            ) + rp.right() + rp.above()
        rp.point == Point(4, 0) ->
            listOf(
                RecursivePoint(rp.level - 1, Point(2, 1)),
                RecursivePoint(rp.level - 1, Point(3, 2))
            ) + rp.left() + rp.below()

        rp.point == Point(1, 2) ->
            rp.aboveAndBelow() + rp.left() + (0..4).map {
                rp.copy(level = rp.level + 1, point = Point(x = 0, y = it))
            }
        rp.point == Point(2, 1) ->
            rp.leftAndRight() + rp.above() + (0..4).map {
                rp.copy(level = rp.level + 1, point = Point(x = it, y = 0))
            }
        rp.point == Point(2, 3) ->
            rp.leftAndRight() + rp.below() + (0..4).map {
                rp.copy(level = rp.level + 1, point = Point(x = it, y = 4))
            }
        rp.point == Point(3, 2) ->
            rp.aboveAndBelow() + rp.right() + (0..4).map {
                rp.copy(level = rp.level + 1, point = Point(x = 4, y = it))
            }            // top
        rp.point.y == 0 && rp.point.x > 0 && rp.point.x < 4 ->
            rp.leftAndRight() + rp.below() + RecursivePoint(rp.level - 1, Point(2, 1))
        // bottom
        rp.point.y == 4 && rp.point.x > 0 && rp.point.x < 4 ->
            rp.leftAndRight() + rp.above() + RecursivePoint(rp.level - 1, Point(2, 3))
        //left
        rp.point.x == 0 && rp.point.y > 0 && rp.point.y < 4 ->
            rp.aboveAndBelow() + rp.right() + RecursivePoint(rp.level - 1, Point(1, 2))
        //right
        rp.point.x == 4 && rp.point.y > 0 && rp.point.y < 4 ->
            rp.aboveAndBelow() + rp.left() + RecursivePoint(rp.level - 1, Point(3, 2))
        else -> rp.allDirections()
    }

    fun part1(input: List<String>) {

        val bugMap = parseMap(input)
        val neighborMap = bugMap.mapValues { (k, _) ->
            (allDirections().map(k::inDirection).filter(bugMap::containsKey))
        }

        fun step(bm: Map<Point, Boolean>): Map<Point, Boolean> {
            return bm.mapValues { (k, v) -> shouldLive(v, neighborMap[k]!!.mapNotNull { bm[it] }) }
        }

        tailrec fun stepUntilRepeat(bm: Map<Point, Boolean>, seen: Set<String>): Map<Point, Boolean> {
            return when (val prev = bm.printOut()) {
                in seen -> bm
                else -> stepUntilRepeat(step(bm), seen + prev)
            }
        }

        println(stepUntilRepeat(bugMap, emptySet()).calculateDiversity())
    }

    private fun Map<Point, Boolean>.calculateDiversity(): BigInteger {
        val inline = (0 until 5).flatMap { y ->
            (0 until 5).map { x ->
                this[Point(x, y)] ?: false
            }
        }
        return inline.withIndex().fold(BigInteger.ZERO) { acc, curr ->
            if (curr.value) {
                acc + BigInteger.TWO.pow(curr.index)
            } else {
                acc
            }
        }

    }


    fun part2(input: List<String>) {
        val bugMap = (parseMap(input) - middle).mapKeys { RecursivePoint(0, it.key) }

        fun step(bm: Map<RecursivePoint, Boolean>): Map<RecursivePoint, Boolean> {
            return bm.mapValues { (k, v) ->
                val ns = neighbors(k)
                shouldLive(v, ns.map { bm[it] ?: false })
            }
        }

        fun addLevelBelow(minLevel: Map.Entry<Int, Int>) = when {
            minLevel.value > 0 -> addEmptyLevel(minLevel.key - 1)
            else -> emptyMap()
        }

        fun addLevelAbove(maxLevel: Map.Entry<Int, Int>) = when {
            maxLevel.value > 0 -> addEmptyLevel(maxLevel.key + 1)
            else -> emptyMap()
        }

        tailrec fun stepN(count: Int, bm: Map<RecursivePoint, Boolean>): Map<RecursivePoint, Boolean> = when (count) {
            0 -> bm
            else -> {
                val (minLevel, maxLevel) =
                    bm.asSequence()
                        .groupBy { it.key.level }
                        .mapValues { it.value.count(Map.Entry<RecursivePoint, Boolean>::value) }.asSequence()
                        .sortedBy { it.key }
                        .let { it.first() to it.last() }
                val expanded = bm + addLevelBelow(minLevel) + addLevelAbove(maxLevel)
                stepN(count - 1, step(expanded))
            }
        }

        println(stepN(200, bugMap).count(Map.Entry<RecursivePoint, Boolean>::value))
    }

    private fun addEmptyLevel(i: Int) =
        (0 until 5).flatMap { y ->
            (0 until 5).map { x -> RecursivePoint(i, Point(x, y)) to false }
        }.toMap() - RecursivePoint(i, middle)
}

private fun RecursivePoint.above() = copy(point = point.copy(y = point.y - 1))
private fun RecursivePoint.below() = copy(point = point.copy(y = point.y + 1))
private fun RecursivePoint.aboveAndBelow() = listOf(above(), below())
private fun RecursivePoint.left() = copy(point = point.copy(x = point.x - 1))
private fun RecursivePoint.right() = copy(point = point.copy(x = point.x + 1))
private fun RecursivePoint.leftAndRight() = listOf(left(), right())
private fun RecursivePoint.allDirections() = aboveAndBelow() + leftAndRight()


fun Map<Point, Boolean>.printOut(): String {
    return (0 until 5).joinToString("\n") { y ->
        (0 until 5).joinToString("") { x ->
            when (this[Point(x, y)] ?: false) {
                true -> '#'
                false -> '.'
            }.toString()
        }
    }
}

fun main() {
    Day24.part1(Day24.fileData)
    Day24.part2(Day24.fileData)
}

