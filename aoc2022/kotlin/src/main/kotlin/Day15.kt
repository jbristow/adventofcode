import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.manhattanDistance
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day15 : AdventOfCode() {
    private val lineRegex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    data class Sensor(val location: Point2d, val closestBeacon: Point2d) {
        private val radius = location.manhattanDistance(closestBeacon)
        fun noBeaconsAllowed(y: Long): LongRange? {
            val width = this.radius - abs(this.location.y - y)
            return when {
                width > 0 -> this.location.x - width..this.location.x + width
                else -> null
            }
        }
    }

    private fun part1(input: List<String>): Long {
        val sensors = toSensors(input)
        return candidateXRanges(sensors, 2000000).sumOf { it.last - it.first }
    }

    private fun part2(input: List<String>): Long? {
        val sensors = toSensors(input)
        val range = 0..4000000L
        return range.asSequence()
            .map { y -> candidateXRanges(sensors, y) }
            .withIndex()
            .find { (_, ranges) -> ranges.size > 1 }
            ?.let { 4000000L * (it.value.first().last + 1L) + it.index }
    }

    private fun toSensors(input: List<String>) = input.map {
        lineRegex.matchEntire(it)!!.groupValues.let { (_, sx, sy, bx, by) ->
            Sensor(
                Point2d(sx.toInt(), sy.toInt()),
                Point2d(bx.toInt(), by.toInt())
            )
        }
    }

    private fun candidateXRanges(sensors: List<Sensor>, y: Long): LinkedList<LongRange> {
        return sensors
            .mapNotNull { it.noBeaconsAllowed(y) }
            .sortedBy { it.first }
            .fold(LinkedList<LongRange>()) { acc, r ->
                val last = acc.lastOrNull()
                if (last != null &&
                    (
                        last.last in r && r.first in last ||
                            last.first in r && last.last in r ||
                            r.first in last && r.last in last
                        )
                ) {
                    acc.removeLast()
                    acc.add(min(last.first, r.first)..max(last.last, r.last))
                } else {
                    acc.add(r)
                }
                acc
            }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 15")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
