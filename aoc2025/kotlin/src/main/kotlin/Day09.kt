import util.AdventOfCode
import util.Point2dL
import util.Point2dLRange

object Day09 : AdventOfCode() {

    enum class AreaType {
        NONE,
        ABOVE,
        BELOW,
        AT_LEFT,
        AT_RIGHT,
    }

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
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun part2(lines: List<String>): Long {
        val points = lines.map { line ->
            line.split(",")
                .map { it.toLong() }
                .let { Point2dL(it[0], it[1]) }
        }

        val perimeter = (points + points.first())
            .windowed(2)
            .mapIndexed { i, (a, b) -> Segment(a, b, i) }

        improvePerimeterInfo(perimeter, points.minOf { it.y })

        return searchRectangles(points, perimeter)
    }

    fun searchRectangles(allRedTiles: List<Point2dL>, perimeter: List<Segment>): Long {
        val len = allRedTiles.size
        return (0..<len).flatMap { indexA ->
            (indexA + 1..<len).map { indexB ->
                val tileA = allRedTiles[indexA]
                val tileB = allRedTiles[indexB]
                tileA to tileB
            }
        }.map { (tileA, tileB) ->
            Segment(tileA, tileB, 0)
        }.fold(0L) { largestArea, rect ->

            val width = 1 + (rect.right - rect.left)
            val height = 1 + (rect.bottom - rect.top)

            val area = width * height

            if (area > largestArea &&
                perimeter.none { segment -> segment.intrudesOn(rect) } &&
                isGoodUpperLeftCorner(perimeter, rect.top, rect.left)
            ) {
                area
            } else {
                largestArea
            }
        }
    }

    fun isGoodUpperLeftCorner(
        perimeter: List<Segment>,
        cornerRow: Long,
        cornerCol: Long
    ): Boolean { // corner coordinates

        return perimeter.filter { segment ->
            (segment.top == segment.bottom && segment.top == cornerRow)
        }.any { segment ->
            // vertical segment
            if (segment.left == cornerCol) {
                segment.greenAreaIs == AreaType.BELOW
            } else {
                val columnAtRight = findColumnAtRight(perimeter, segment)

                // column goes up
                if (columnAtRight.bottom == segment.top) {
                    segment.greenAreaIs == AreaType.BELOW
                } else {
                    columnAtRight.greenAreaIs == AreaType.AT_RIGHT
                }
            }
        }
    }

    fun findColumnAtRight(perimeter: List<Segment>, segment: Segment): Segment {
        return listOf(-1, +1)
            .map { delta ->
                var index = segment.index + delta

                if (index < 0) {
                    index = perimeter.size - 1
                }

                if (index == perimeter.size) {
                    index = 0
                }
                index
            }
            .map { index -> perimeter[index] }
            .find { candidate -> candidate.left == segment.right }
            ?: error("No candidates")
    }

    data class Segment(
        val top: Long,
        val bottom: Long,
        val left: Long,
        val right: Long,
        val index: Int,
        var greenAreaIs: AreaType = AreaType.NONE
    ) {
        fun intrudesOn(rect: Segment): Boolean {
            return top < rect.bottom &&
                bottom > rect.top &&
                left < rect.right &&
                right > rect.left
        }

        constructor(tileA: Point2dL, tileB: Point2dL, index: Int) : this(
            top = when {
                tileA.y < tileB.y -> tileA.y
                else -> tileB.y
            },
            bottom = when {
                tileB.y > tileA.y -> tileB.y
                else -> tileA.y
            },
            left = when {
                tileA.x < tileB.x -> tileA.x
                else -> tileB.x
            },
            right = when {
                tileA.x > tileB.x -> tileA.x
                else -> tileB.x
            },
            index = index,
        )

        fun markHorizontalSegment(previousSegment: Segment) {
            greenAreaIs =
                when {
                    left == previousSegment.left &&
                        previousSegment.greenAreaIs == AreaType.AT_LEFT -> AreaType.ABOVE

                    left == previousSegment.left -> AreaType.BELOW
                    previousSegment.greenAreaIs == AreaType.AT_LEFT -> AreaType.BELOW
                    else -> AreaType.ABOVE
                }
        }

        fun markVerticalSegment(previousSegment: Segment) {
            greenAreaIs =
                when {
                    top == previousSegment.top && previousSegment.greenAreaIs == AreaType.BELOW -> AreaType.AT_LEFT
                    top == previousSegment.top -> AreaType.AT_RIGHT
                    previousSegment.greenAreaIs == AreaType.BELOW -> AreaType.AT_RIGHT
                    else -> AreaType.AT_LEFT
                }
        }
    }

    fun improvePerimeterInfo(perimeter: List<Segment>, highestRow: Long) {
        var index = highestHorizontalSegmentIndex(perimeter, highestRow) // no problem if there ara two or more

        val highestSegment = perimeter[index]

        highestSegment.greenAreaIs = AreaType.BELOW

        var previousSegment = highestSegment

        while (true) {
            index += 1

            if (index == perimeter.size) {
                index = 0
            }

            val segment = perimeter[index]

            if (segment.greenAreaIs != AreaType.NONE) {
                return
            } // already done

            if (segment.top == segment.bottom) {
                segment.markHorizontalSegment(previousSegment)
            } else {
                segment.markVerticalSegment(previousSegment)
            }

            previousSegment = segment
        }
    }

    fun highestHorizontalSegmentIndex(perimeter: List<Segment>, highestRow: Long): Int {
        return perimeter.indexOfFirst { segment ->
            segment.top <= highestRow && segment.top == segment.bottom
        }
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
