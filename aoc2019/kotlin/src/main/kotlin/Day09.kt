import arrow.core.Either
import intcode.CurrentState
import intcode.step
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object Day09 {

    private const val FILENAME = "src/main/resources/day09.txt"
    val fileData: String = Files.readAllLines(Paths.get(FILENAME)).first()

    private val initialCode = fileData.splitToSequence(",")
        .mapIndexed { i, it -> i.toLong() to it.toLong() }.toMap()

    fun part1(): Either<String, LinkedList<Long>> {
        return step(
            initialCode.toMutableMap(),
            CurrentState(inputs = LinkedList(listOf(1L)))
        )
    }

    fun part2() =
        step(
            initialCode.toMutableMap(),
            CurrentState(inputs = LinkedList(listOf(2L)))
        )
}

fun main() {
    println(Day09.part1())
    println(Day09.part2())
}
