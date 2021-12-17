import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.plus
import kotlin.math.max
import kotlin.math.min

object Day17 : AdventOfCode() {

    private val targetRegex = Regex("""target area: x=(\d+)..(\d+), y=(-\d+)..(-\d+)""")

    private fun extractBoundaries(input: String) =
        targetRegex.matchEntire(input)!!
            .groupValues.drop(1)
            .map { it.toInt() }
            .let { groups ->
                listOf(
                    min(groups[0], groups[1]),
                    max(groups[0], groups[1]),
                    min(groups[2], groups[3]),
                    max(groups[2], groups[3])
                )
            }

    data class Probe(
        val velocity: Point2d,
        val position: Point2d = Point2d(0, 0),
        val maxHeight: Int = position.y
    )

    private fun Probe.move(): Probe {
        val newPosition = this.position + velocity
        val newXv = applyDrag(velocity.x)
        val newYv = velocity.y - 1
        val newMaxHeight = when {
            maxHeight < newPosition.y -> newPosition.y
            else -> maxHeight
        }
        return Probe(velocity = Point2d(newXv, newYv), position = newPosition, maxHeight = newMaxHeight)
    }

    private fun applyDrag(x: Int): Int {
        return when {
            x > 0 -> x - 1
            x < 0 -> x + 1
            else -> x
        }
    }

    private fun Probe.inTargetArea(topLeft: Point2d, bottomRight: Point2d) =
        position.x in (topLeft.x..bottomRight.x) && position.y in (bottomRight.y..topLeft.y)

    private fun Probe.beyondTargetArea(topLeft: Point2d, bottomRight: Point2d) =
        position.x > bottomRight.x || (position.x < topLeft.x && velocity.x == 0) || position.y < bottomRight.y

    private tailrec fun Probe.fire(topLeft: Point2d, bottomRight: Point2d, steps: Int = 0): Probe? =
        when {
            inTargetArea(topLeft, bottomRight) -> this
            beyondTargetArea(topLeft, bottomRight) -> null
            else -> move().fire(topLeft, bottomRight, steps + 1)
        }

    private tailrec fun xThrow(
        minVal: Int,
        maxVal: Int,
        startVelocity: Int,
        velocity: Int = startVelocity,
        currPos: Int = 0
    ): Int? =
        when {
            currPos in (minVal..maxVal) -> startVelocity
            currPos > maxVal || currPos < minVal && velocity < 1 -> null
            else -> xThrow(minVal, maxVal, startVelocity, applyDrag(velocity), currPos + velocity)
        }

    private tailrec fun yThrow(
        minVal: Int,
        maxVal: Int,
        startVelocity: Int,
        velocity: Int = startVelocity,
        currPos: Int = 0
    ): Int? {
        return when {
            currPos in (minVal..maxVal) -> startVelocity
            currPos < minVal -> null
            else -> yThrow(minVal, maxVal, startVelocity, velocity - 1, currPos + velocity)
        }
    }

    fun part1(input: String): Int {
        val (xMin, xMax, yMin, yMax) = extractBoundaries(input)

        val xseq = generateSequence(0) { it + 1 }.takeWhile { it <= xMax }.mapNotNull { xThrow(xMin, xMax, it) }
        val yseq = generateSequence(0) { it + 1 }.takeWhile { it <= -yMin }.mapNotNull { yThrow(yMin, yMax, it) }

        return yseq.flatMap { y -> xseq.map { x -> Point2d(x, y) } }
            .mapNotNull { Probe(it).fire(Point2d(xMin, yMax), Point2d(xMax, yMin)) }
            .maxByOrNull { it.maxHeight }!!.maxHeight
    }

    fun part2(input: String): Int {
        val (xMin, xMax, yMin, yMax) = extractBoundaries(input)

        val xseq = generateSequence(0) { it + 1 }
            .takeWhile { it <= xMax }
            .mapNotNull { xThrow(xMin, xMax, it) }
        val yseq = generateSequence(yMin) { it + 1 }
            .takeWhile { it <= -(2 * yMin) }
            .mapNotNull { yThrow(yMin, yMax, it) }

        return yseq
            .flatMap { y -> xseq.map { x -> Point2d(x, y) } }
            .mapNotNull { Probe(it).fire(Point2d(xMin, yMax), Point2d(xMax, yMin)) }.count()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 16")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
