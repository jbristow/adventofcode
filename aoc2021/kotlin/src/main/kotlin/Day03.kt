import Day03.Extractor.Least
import Day03.Extractor.Most
import util.AdventOfCode

object Day03 : AdventOfCode() {

    fun part1(input: List<String>): Int {
        val digitCounts =
            input.map { it.mapIndexed { i, c -> i to c.digitToInt() }.toMap() }
                .fold(mapOf<Int, Int>()) { acc, map ->
                    map.mapValues { (k, v) -> v + (acc[k] ?: 0) }
                }

        val gamma = Most.compareDigits(digitCounts, input.size)
        val epsilon = Least.compareDigits(digitCounts, input.size)

        return gamma * epsilon
    }

    private enum class Extractor(val a: String, val b: String) {
        Most("1", "0"),
        Least("0", "1");

        fun extract(list: List<String>): String {
            return if (list.count { s -> s.first() == '1' } >= (list.size / 2.0)) {
                a
            } else {
                b
            }
        }

        fun compareDigits(
            counts: Map<Int, Int>,
            inputSize: Int,
        ) = (0 until counts.size).map {
            if ((counts[it] ?: 0) < inputSize / 2) {
                a
            } else {
                b
            }
        }.joinToString("").toInt(2)
    }

    private fun computeMost(input: List<String>): String {
        return compute("", input, Most)
    }

    private fun computeLeast(input: List<String>): String {
        return compute("", input, Least)
    }

    private tailrec fun compute(soFar: String, input: List<String>, extractor: Extractor): String {
        if (input.size == 1) {
            return soFar + input.first()
        }

        val next: String = extractor.extract(input)
        return compute(soFar + next, input.filter { it.startsWith(next) }.map { it.drop(1) }, extractor)
    }

    fun part2(input: List<String>): Int {
        return computeMost(input).toInt(2) * computeLeast(input).toInt(2)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 3")
        println("\tPart 1: ${part1(inputFileLines.toList())}")
        println("\tPart 2: ${part2(inputFileLines.toList())}")
    }
}
