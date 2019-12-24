import arrow.core.firstOrNone
import arrow.core.getOrElse
import java.nio.file.Files
import java.nio.file.Paths

object Day08 {

    private const val FILENAME = "src/main/resources/day08.txt"
    val fileData: String = Files.readAllLines(Paths.get(FILENAME)).first()

    fun part1(input: String) =
        input.chunked(25 * 6)
            .asSequence()
            .map { layer ->
                layer.groupBy { it }
                    .mapValues { (_, v) -> v.size }
            }.minBy { it.getOrDefault('0', 0) }?.let {
                it.getOrDefault('1', 0) * it.getOrDefault('2', 0)
            } ?: 0

    private fun List<String>.findPixelColors() =
        (this[0].indices).map { i -> map { it[i] } }
            .map(this@Day08::findPixelColor)

    private fun findPixelColor(it: List<Char>) =
        it.dropWhile { c -> c == '2' }
            .firstOrNone()
            .getOrElse { '2' }

    private fun List<List<Char>>.renderImage() =
        joinToString("\n") { row ->
            row.joinToString("") { pixel ->
                when (pixel) {
                    '0' -> ' '
                    '1' -> '#'
                    '2' -> '-'
                    else -> '?'
                }.toString()
            }
        }

    fun part2(input: String) =
        input.chunked(25 * 6)
            .findPixelColors()
            .chunked(25)
            .renderImage()
}

fun main() {
    println(Day08.part1(Day08.fileData))
    println(Day08.part2(Day08.fileData))
}
