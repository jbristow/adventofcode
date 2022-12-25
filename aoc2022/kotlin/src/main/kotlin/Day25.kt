import util.AdventOfCode
import kotlin.math.pow

object Day25 : AdventOfCode() {
    private val digitMap = mapOf('2' to 2L, '1' to 1L, '0' to 0L, '-' to -1L, '=' to -2L)
    private val digits = listOf("=", "-", "0", "1", "2")

    private fun String.snafu() =
        map { c -> digitMap[c]!! }.reversed().mapIndexed { i, n -> (5.0.pow(i) * n).toLong() }.sum()

    private tailrec fun reverseSnafu(number: Long, output: String = ""): String {
        if (number == 0L) {
            return output
        }
        val remainder = (number + 2) % 5
        return reverseSnafu((number + 2) / 5L, digits[remainder.toInt()] + output)
    }

    private fun part1(input: List<String>) = reverseSnafu(input.sumOf { it.snafu() })

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 25")
        println("\tPart 1: ${part1(inputFileLines)}")
    }
}
