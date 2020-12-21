import util.AdventOfCode
import util.Point
import util.Point3d
import util.Point4d

object Day17 : AdventOfCode() {

    tailrec fun Set<Point3d>.cycle3d(
        maxCount: Int,
        xBound: Pair<Int, Int>,
        yBound: Pair<Int, Int>,
        zBound: Pair<Int, Int> = (0 to 0),
        count: Int = 0,
    ): Set<Point3d> {
        if (count == maxCount) {
            return this
        }

        val nextMap = xBound.range.flatMap { x ->
            yBound.range.flatMap { y ->
                zBound.range.map { z -> Point3d(x, y, z).nextStatePair(this) }
            }
        }.filter { it.second }.map { it.first }.toSet()

        return nextMap.cycle3d(
            maxCount = maxCount,
            xBound = xBound.expand(),
            yBound = yBound.expand(),
            zBound = zBound.expand(),
            count = count + 1
        )
    }

    private fun <T : Point> T.nextStatePair(active: Set<T>) =
        this to nextState(this in active, neighbors.count { it in active })

    tailrec fun Set<Point4d>.cycle4d(
        maxCount: Int,
        xBound: Pair<Int, Int>,
        yBound: Pair<Int, Int>,
        zBound: Pair<Int, Int> = (0 to 0),
        wBound: Pair<Int, Int> = (0 to 0),
        count: Int = 0,
    ): Set<Point4d> {
        if (count == maxCount) {
            return this
        }
        val nextMap = xBound.range.flatMap { x ->
            yBound.range.flatMap { y ->
                zBound.range.flatMap { z ->
                    wBound.range.map { w -> Point4d(x, y, z, w).nextStatePair(this) }
                }
            }
        }.filter { it.second }.map { it.first }.toSet()

        return nextMap.cycle4d(
            maxCount = maxCount,
            xBound = xBound.expand(),
            yBound = yBound.expand(),
            zBound = zBound.expand(),
            wBound = wBound.expand(),
            count = count + 1
        )
    }

    private fun nextState(v: Boolean, neighborCount: Int) = when {
        neighborCount == 3 -> true
        neighborCount == 2 && v -> true
        else -> false
    }

    fun Pair<Int, Int>.expand() = (first - 1) to (second + 1)
    val Pair<Int, Int>.range: Sequence<Int>
        get() = ((first - 1)..(second + 1)).asSequence()

    private fun part1(data: List<String>): Int {
        val active = data.asSequence().flatMapIndexed { y, row ->
            row.asSequence().mapIndexed { x, c -> Point3d(x, y, 0) to c.inputToBool() }
        }.filter { it.second }
            .map { it.first }.toSet()

        return active.cycle3d(
            maxCount = 6,
            xBound = 0 to data.first().length,
            yBound = 0 to data.size,
        ).size
    }

    private fun part2(data: List<String>): Int {
        val active = data.flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                Point4d(x, y, 0, 0) to c.inputToBool()
            }
        }.filter { it.second }
            .map { it.first }.toSet()

        return active.cycle4d(
            maxCount = 6,
            xBound = 0 to data.first().length,
            yBound = 0 to data.size,
        ).size
    }

    private fun Char.inputToBool() = when (this) {
        '#' -> true
        '.' -> false
        else -> throw Exception("Illegal map char $this")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Part 1: ${part1(inputFileLines)}")
        println("Part 2: ${part2(inputFileLines)}")
    }
}
