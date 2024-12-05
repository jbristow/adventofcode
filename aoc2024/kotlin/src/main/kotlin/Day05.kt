import util.*

object Day05 : AdventOfCode() {
    internal val <T> List<T>.middle
        get() = this[this.size / 2]

    fun parse(input: String): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val split = input.split("\n\n")
        val before =
            split[0]
                .lines()
                .map { line -> line.split("|").map { it.toInt() }.let { (a, b) -> a to b } }
                .groupBy({ it.first }, { it.second })
        val updates =
            split[1]
                .lines()
                .map { line -> line.split(""",\s*""".toRegex()).map { line -> line.toInt() } }
        return before to updates
    }

    private fun isOrdered(
        before: Map<Int, List<Int>>,
        input: List<Int>,
    ): Boolean {
        val s = input.joinToString(",", prefix = ",", postfix = ",")
        return input.all { instr ->
            (before[instr] ?: listOf()).all { b ->
                b !in input || """,$instr(,\d+)*,$b,""".toRegex().find(s) != null
            }
        }
    }

    private fun part1(input: String): Int {
        val (before, updates) = parse(input)
        return updates
            .filter { isOrdered(before, it) }
            .sumOf { it.middle }
    }

    private fun part2(input: String): Int {
        val (before, updates) = parse(input)
        return updates
            .filterNot { isOrdered(before, it) }
            .map { correct(before, it) }
            .sumOf { it.middle }
    }

    private tailrec fun correct(
        before: Map<Int, List<Int>>,
        input: List<Int>,
        output: MutableList<Int> = mutableListOf(),
    ): List<Int> {
        if (input.isEmpty()) {
            return output
        }

        val x = input.first()
        val bs = (before[x] ?: listOf()).map { output.indexOf(it) }.filter { it >= 0 }

        if (bs.isEmpty() || output.isEmpty()) {
            output.add(x)
        } else {
            output.add(bs.min(), x)
        }

        return correct(before, input.drop(1), output)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
