import util.AdventOfCode

object Day03 : AdventOfCode() {

    fun part1(input: List<String>): Int {
        val sum =
            input.map { it.mapIndexed { i, c -> i to c.digitToInt() }.toMap() }
                .fold(mutableMapOf<Int, Int>()) { acc, map ->
                    map.forEach { (k, v) ->
                        val nv = v + (acc[k] ?: 0)
                        acc[k] = nv
                    }
                    acc
                }

        val gamma = (0 until sum.size).map {
            if ((sum[it] ?: 0) >= input.size / 2) {
                1
            } else {
                0
            }
        }.joinToString("")

        val epsilon = (0 until sum.size).map {
            if ((sum[it] ?: 0) < input.size / 2) {
                1
            } else {
                0
            }
        }.joinToString("")

        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun computeMost(input: List<String>): String {
        return compute("", input) {
            if (it.count { s -> s.first() == '1' } >= (it.size / 2.0)) {
                "1"
            } else {
                "0"
            }
        }
    }

    fun computeLeast(input: List<String>): String {
        return compute("", input) {
            if (it.count { s -> s.first() == '1' } >= (it.size / 2.0)) {
                "0"
            } else {
                "1"
            }
        }
    }

    tailrec fun compute(soFar: String, input: List<String>, extractor: (List<String>) -> String): String {
        if (input.size == 1) {
            return soFar + input.first()
        }
        val next: String = extractor(input);
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
