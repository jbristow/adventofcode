import util.AdventOfCode
import java.util.stream.Collectors
import java.util.stream.Stream

private fun String.onlyUniqueLetters() =
    replace("""[^a-z]""".toRegex(), "").toSet()

private fun String.chunkify(): Stream<String> =
    split("\n\n").parallelStream()

object Day06 : AdventOfCode() {

    fun part1(input: String): Int =
        input.chunkify()
            .map(String::onlyUniqueLetters)
            .collect(Collectors.summingInt(Set<Char>::size))

    fun part2(input: String): Int =
        input.chunkify()
            .map {
                it.lines()
                    .map(String::onlyUniqueLetters)
                    .reduce(Set<Char>::intersect)
                    .size
            }.collect(Collectors.summingInt { it })
}

fun main() {
    println("Part 1: ${Day06.part1(Day06.inputFileString)}")
    println("Part 2: ${Day06.part2(Day06.inputFileString)}")
}
