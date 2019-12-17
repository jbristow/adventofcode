import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

object Day16r {
    private const val FILENAME = "src/main/resources/day16r.txt"
    val fileData = Files.readAllLines(Paths.get(FILENAME)).first().trim().map { it.toString().toLong() }
    private val basePattern = arrayOf(0, 1, 0, -1)

    fun part1(input: List<Long>) = nFft1(100, input).take(8).joinToString("").toInt()
    fun part2(input: List<Long>): Int =
        nFft2(
            n = 100,
            input = generateSequence { input }
                .take(10_000)
                .flatten()
                .drop(input.take(7).joinToString("").toInt())
                .toList()
        ).take(8).joinToString("").toInt()


    private fun fft(input: List<Long>): List<Long> {
        val half = input.size / 2
        val next = MutableList(input.size) { 0L }

        input.take(half).forEachIndexed { i, _ ->
            next[i] = input.mapIndexed { j, it -> j to it }
                .drop(i).filter { (j, _) ->
                    ((j + 1) / (i + 1)) % 2 != 0
                }
                .fold(0L) { acc, (j, c) ->
                    acc + c * basePattern[((j + 1) / (i + 1)) % 4]
                }
        }
        (1..half).asSequence().forEach {
            next[half + it - 1] = input.slice(half + it - 1 until input.size).sum()
        }
        return next.map { abs(it) % 10 }

    }

    private tailrec fun fft2(
        input: List<Long>,
        next: MutableList<Long>,
        n: Int,
        value: Int
    ): List<Long> {
        return when {
            (n < 0) -> next.map { abs(it) % 10 }
            else -> {
                val newValue = value + input[input.lastIndex - n]
                next[next.lastIndex - n] = newValue
                fft2(input, next, n, value)
            }
        }
    }

    tailrec fun nFft1(n: Int, input: List<Long>): List<Long> {
        return when {
            n < 1 -> input
            else -> nFft1(n - 1, fft(input))
        }
    }

    private tailrec fun nFft2(n: Int, input: List<Long>): List<Long> {
        return when {
            n < 1 -> input
            else -> nFft2(n - 1, fft2(input, MutableList(size = input.size, init = { 0L }), input.lastIndex, 0))
        }
    }
}

fun main() {
    println("part1: ${Day16r.part1(Day16r.fileData)}")
    println("part2: ${Day16r.part2(Day16r.fileData)}")
}
