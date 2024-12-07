import util.AdventOfCode

object Day07 : AdventOfCode() {
    private fun parse(input: Sequence<String>): Sequence<Pair<Long, List<Long>>> =
        input.map { line ->
            line
                .split(""":\s+""".toRegex())
                .let { (a, b) -> a.toLong() to (b.split("""\s+""".toRegex()).map { it.toLong() }) }
        }

    private fun isPossible(
        testValue: Long,
        components: List<Long>,
        currTotal: Long = 0,
    ): Boolean {
        if (components.isEmpty()) {
            return currTotal == testValue
        }
        if (currTotal > testValue) {
            return false
        }
        val nextNumber = components.first()
        val remaining = components.drop(1)
        if (currTotal == 0L) {
            return isPossible(testValue, remaining, nextNumber)
        }
        return isPossible(testValue, remaining, nextNumber + currTotal) ||
            isPossible(testValue, remaining, nextNumber * currTotal)
    }

    private fun isPossible2(
        testValue: Long,
        components: List<Long>,
        currTotal: Long = 0,
    ): Boolean {
        if (components.isEmpty()) {
            return currTotal == testValue
        }
        if (currTotal > testValue) {
            return false
        }
        val nextNumber = components.first()
        val remaining = components.drop(1)

        if (currTotal == 0L) {
            return isPossible2(testValue, remaining, nextNumber)
        }
        return isPossible2(testValue, components.drop(1), currTotal + components.first()) ||
            isPossible2(testValue, components.drop(1), currTotal * components.first()) ||
            isPossible2(testValue, components.drop(1), "$currTotal${components.first()}".toLong())
    }

    private fun part1(input: Sequence<String>): Long =
        parse(input).filter { (total, values) -> isPossible(total, values) }.sumOf { it.first }

    private fun part2(input: Sequence<String>): Long =
        parse(input).filter { (total, values) -> isPossible2(total, values) }.sumOf { it.first }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 7")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
