import arrow.core.Either
import intcode.CurrentState
import intcode.step
import java.nio.file.Files
import java.nio.file.Paths

object Day09 {

    private const val FILENAME = "src/main/resources/day09.txt"
    val fileData: String = Files.readAllLines(Paths.get(FILENAME)).first()

    private val initialCode = fileData.splitToSequence(",")
        .mapIndexed { i, it -> i.toLong() to it.toLong() }.toMap()

    fun part1(): Either<String, Long> {
        return step(
            initialCode.toMutableMap(),
            CurrentState(inputs = mutableListOf(1))
        )
    }

    fun part2() =
        step(
            initialCode.toMutableMap(),
            CurrentState(inputs = mutableListOf(2))
        )
}

fun main() {
    println(Day09.part1())
    println(Day09.part2())
}