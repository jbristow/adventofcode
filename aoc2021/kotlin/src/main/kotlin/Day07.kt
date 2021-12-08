import util.AdventOfCode
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

object Day07 : AdventOfCode() {
    // First pass brute forced all values. It was reasonably fast.
    // Apparently the minimum x for Σ(abs(v_j-x)) is provably equal to the median of set v.
    // https://math.stackexchange.com/questions/85448/why-does-the-median-minimize-ex-c?rq=1
    fun part1(input: String): Int {
        val crabs = input.split(",").map { it.toInt() }
        val median = crabs.median()
        return crabs.sumOf { abs(it - median) }
    }

    fun Int.triangleNumber() = this * (this + 1) / 2

    // Found after brute forcing for the answer that the true answer is always close to average
    // But my brain hurts trying to prove it for arbitrary sets & functions.
    // Experimentation seems to show that floor/ceil of the average is a good guess for quadratic distance equations.
    fun part2(input: String): Int {
        val crabs = input.split(",").map { it.toInt() }
        val mean = crabs.average()

        return (floor(mean).toInt()..ceil(mean).toInt()).minOf { jitter ->
            crabs.sumOf { abs(it - jitter).triangleNumber() }
        }
    }

    fun List<Int>.median() = sorted().let {
        when {
            it.size % 2 == 0 -> (it[it.size / 2] + it[(it.size - 1) / 2]) / 2
            else -> it[it.size / 2]
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 7")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }

    private fun List<Int>.triCrabSum(it: Int): Int {
        return sumOf { crab -> abs(crab - it).triangleNumber() }
    }
}

