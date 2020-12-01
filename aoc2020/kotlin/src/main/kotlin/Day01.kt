import Day01.part1
import Day01.part2
import java.nio.file.Files
import java.nio.file.Paths

object Day01 {
    private const val FILENAME = "src/main/resources/day01.txt"

    private fun find2020(lines: List<String>) =
            (lines.indices).asSequence().mapNotNull { i ->
                val b = (i until lines.size).find { j ->
                    lines[j].toInt() + lines[i].toInt() == 2020
                }
                when (b) {
                    null -> null
                    else -> lines[b].toInt().times(lines[i].toInt())
                }
            }.first()

    private fun find2020From3(lines: List<String>): Int? {
        val x = (lines.indices).asSequence().flatMap { i ->
            (i until lines.size).asSequence().flatMap { j ->
                (j until lines.size).asSequence().map { k ->
                    listOf(i, j, k)
                }
            }
        }.filter { ls ->
            ls.map { lines[it].toInt() }.sum() == 2020
        }.first()

        return lines[x[0]].toInt() * lines[x[1]].toInt() * lines[x[2]].toInt()
    }

    fun part1(): Int? = find2020(Files.readAllLines(Paths.get(FILENAME)))

    fun part2(): Int? = find2020From3(Files.readAllLines(Paths.get(FILENAME)))
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

