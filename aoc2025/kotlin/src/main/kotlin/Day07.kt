import util.AdventOfCode
import util.Down
import util.Left
import util.Point2d
import util.Point2dRange
import util.Right

object Day07 : AdventOfCode() {

    private fun List<String>.toSplitterSet(): List<Point2d> =
        flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                if (c == '^') {
                    Point2d(x, y)
                } else {
                    null
                }
            }
        }

    private fun List<Point2d>.toYXMap(): Map<Int, Set<Int>> =
        groupBy({ it.y }, { it.x })
            .mapValues { (_, v) -> v.toSet() }

    private tailrec fun traceBeam(
        positions: Set<Point2d>,
        splitters: Map<Int, Set<Int>>,
        last: Int,
        splits: Long = 0,
    ): Long {
        if (positions.first().y > last) {
            return splits
        }
        val (newSplits, nextPositions) = positions.map { it + Down.offset }
            .fold(0L to mutableSetOf<Point2d>()) { (splitCount, acc), curr ->
                if (splitters[curr.y]?.contains(curr.x) ?: false) {
                    acc.add(curr + Left.offset)
                    acc.add(curr + Right.offset)
                    (splitCount + 1) to acc
                } else {
                    acc.add(curr)
                    splitCount to acc
                }
            }
        return traceBeam(nextPositions, splitters, last, splits + newSplits)
    }

    private fun part1(lines: List<String>): Long {
        val splitters = lines.toSplitterSet()
        val splitterRange = Point2dRange(splitters)
        val start = Point2d(lines.first().indexOf("S"), 0)

        return traceBeam(
            positions = setOf(start),
            splitters = splitters.toYXMap(),
            last = splitterRange.yRange.last,
        )
    }

    private tailrec fun possibleTimelines(
        currY: Int,
        currTimelines: Array<Long>,
        splitters: Map<Int, Set<Int>>,
        maxY: Int,
    ): Long =
        when {
            currY > maxY -> currTimelines.sum()

            !splitters.contains(currY) -> {
                possibleTimelines(currY + 1, currTimelines, splitters, maxY)
            }

            else -> {
                val nextTimelines = Array(currTimelines.size) { 0L }
                currTimelines.forEachIndexed { x, strength ->
                    if (splitters[currY]?.contains(x) ?: false) {
                        nextTimelines[x - 1] += strength
                        nextTimelines[x + 1] += strength
                    } else {
                        nextTimelines[x] += strength
                    }
                }
                possibleTimelines(currY + 1, nextTimelines, splitters, maxY)
            }
        }

    private fun part2(lines: List<String>): Long {
        val splitters = lines.toSplitterSet()
        val splitterRange = Point2dRange(splitters)
        val start = Point2d(lines.first().indexOf("S"), 0)

        val startArr = Array(splitterRange.xRange.last + 2) { 0L }
        startArr[start.x] = 1

        return possibleTimelines(
            currY = start.y,
            currTimelines = startArr,
            splitters = splitters.toYXMap(),
            maxY = splitterRange.yRange.last,
        )
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 7")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
