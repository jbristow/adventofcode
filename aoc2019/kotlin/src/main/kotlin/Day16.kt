import arrow.core.extensions.sequence.applicative.replicate
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.streams.asStream

object Day16 {
    private const val FILENAME = "src/main/resources/day16.txt"
    val fileData = Files.readAllLines(Paths.get(FILENAME)).first()

    val basePattern = sequenceOf(0, 1, 0, -1)
    fun generatePattern(n: Int): Sequence<Int> {
        return generateSequence { basePattern.flatMap { sequenceOf(it).replicate(n).flatten() } }.flatten().drop(1)
    }

    fun fft(input: Sequence<Int>, size: Int): Sequence<Int> {
        return (0 until size).asSequence().asStream().parallel().map { i ->
            abs(generatePattern(i + 1).zip(input.asSequence())
                .filterNot { (a, _) -> a == 0 }.asStream().parallel()
                .map { it.first * it.second }
                .reduce { t: Int, u: Int -> t + u }.orElseGet { 0 }) % 10
        }.collect(Collectors.toUnmodifiableList()).asSequence()
    }

    tailrec fun fftTimes(input: Sequence<Int>, size: Int, n: Int): Sequence<Int> {
        println("$n) sum(${input.sum()})" + input.joinToString(""))
        return when {
            n < 1 -> input
            else -> fftTimes(fft(input, size), size, n - 1)
        }
    }

    fun part1(): String {
        val list = fileData.map { it.toString().toInt() }
        return fftTimes(list.asSequence(), list.size, 100).take(8).joinToString("")
    }
}

fun main() {
    println(Day16.fftTimes("12345678".asSequence().map { it.toString().toInt() }, 8, 3).joinToString(""))
    println(Day16.part1())
}
