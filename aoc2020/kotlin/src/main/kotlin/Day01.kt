import util.AdventOfCode

object Day01 : AdventOfCode() {
    private fun find2020(lines: List<Int>): Int {
        val sorted = lines.sorted()

        tailrec fun recurse(minVal: Int, remaining: List<Int>): Int {
            val answer = minVal + remaining.last()
            return when {
                answer > 2020 -> recurse(minVal, remaining.dropLast(1))
                answer == 2020 -> minVal * remaining.last()
                else -> recurse(remaining.first(), remaining.drop(1))
            }
        }

        return recurse(sorted.first(), sorted.drop(1))
    }

    private fun find2020From3(lines: List<Int>): Int {
        val tuple = (lines.indices).asSequence().flatMap { i ->
            (i until lines.size).asSequence().flatMap { j ->
                (j until lines.size).asSequence().map { k ->
                    listOf(i, j, k)
                }.filter { ls ->
                    ls.map { lines[it] }.sum() == 2020
                }
            }
        }.first()

        val (x, y, z) = tuple

        return lines[x] * lines[y] * lines[z]
    }

    fun part1(input: List<Int>) = find2020(input)

    fun part2(input: List<Int>) = find2020From3(input)
}

fun main() {
    println("Part 1: ${Day01.part1(Day01.inputFileInts)}")
    println("Part 2: ${Day01.part2(Day01.inputFileInts)}")
}
