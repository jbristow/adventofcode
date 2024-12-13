import util.AdventOfCode
import util.Maths.lcm
import util.Point2dL
import kotlin.math.floor

object Day13 : AdventOfCode() {

    data class Machine(
        val buttonA: Point2dL,
        val buttonB: Point2dL,
        val prize: Point2dL,
    ) {
        fun cost(): Long? {
            val m = lcm(buttonA.x.toLong(), buttonA.y.toLong())
            val xm = m / buttonA.x
            val ym = m / buttonA.y
            val b = (prize.x * xm - prize.y * ym).toDouble() / (buttonB.x * xm - buttonB.y * ym)
            val a = (prize.x - b * buttonB.x) / buttonA.x

            return if (a == floor(a) && b == floor(b)) {
                (3 * a + b).toLong()
            } else {
                null
            }
        }

        companion object {
            fun parse(input: String): Machine {
                val (a, b, c) = input.lines()
                val buttonA = """Button A: X\+(\d+), Y\+(\d+)""".toRegex().matchEntire(a)?.let { result ->
                    Point2dL(result.groupValues[1].toLong(), result.groupValues[2].toLong())
                }!!
                val buttonB = """Button B: X\+(\d+), Y\+(\d+)""".toRegex().matchEntire(b)?.let { result ->
                    Point2dL(result.groupValues[1].toLong(), result.groupValues[2].toLong())
                }!!
                val prize = """Prize: X=(\d+), Y=(\d+)""".toRegex().matchEntire(c)?.let { result ->
                    Point2dL(result.groupValues[1].toLong(), result.groupValues[2].toLong())
                }!!
                return Machine(buttonA, buttonB, prize)
            }

            fun parseBig(input: String): Machine {
                val initial = parse(input)
                return initial.copy(prize = initial.prize + Point2dL(10000000000000, 10000000000000))
            }
        }
    }

    fun part1(input: String): Long = input.split("\n\n").map { Machine.parse(it) }.sumOf { it.cost() ?: 0 }

    fun part2(input: String): Long = input.split("\n\n").map { Machine.parseBig(it) }.sumOf { it.cost() ?: 0L }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 13")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
