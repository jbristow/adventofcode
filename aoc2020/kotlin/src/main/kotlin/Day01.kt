import Day01.part1
import Day01.part2
import java.nio.file.Files
import java.nio.file.Paths

object Day01 {
    const val FILENAME = "src/main/resources/day01.txt"

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
    val data = Files.readAllLines(Paths.get(Day01.FILENAME)).map(String::toInt)
    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}

