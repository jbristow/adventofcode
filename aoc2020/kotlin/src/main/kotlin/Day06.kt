import Day06.FILENAME
import Day06.part1
import Day06.part2
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

private fun String.onlyUniqueLetters() =
    replace("""[^a-z]""".toRegex(), "").toSet()

private fun String.chunkify(): Stream<String> =
    split("\n\n").parallelStream()

object Day06 {
    const val FILENAME = "src/main/resources/day06.txt"

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
    val data: String = Files.readString(Paths.get(FILENAME))
    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}