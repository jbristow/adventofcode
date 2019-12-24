import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import arrow.core.identity
import arrow.core.right
import arrow.optics.optics
import arrow.syntax.function.compose
import arrow.syntax.function.partially2
import intcode.CurrentState
import intcode.IntCode
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import java.util.*

@optics
data class DroneController(
    val code: IntCode,
    val state: Either<String, CurrentState> = CurrentState().right()
) {
    companion object
}

private tailrec fun DroneController.runCode(): DroneController {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when (state.b.pointer) {
            is None -> this
            is Some<Long> -> {
                copy(state = handleCodePoint(code, state)).runCode()
            }
        }
    }
}

object Day19 {
    private const val FILENAME = "src/main/resources/day19.txt"
    val fileData get() = FILENAME.toIntCodeProgram()

    val cache: MutableMap<PointL, Long> = mutableMapOf()

    fun findPowerAt(p: PointL): Long {
        return cache.getOrPut(p, {
            DroneController(
                code = fileData.toMutableMap(),
                state = CurrentState(
                    inputs = LinkedList(listOf(p.x, p.y))
                ).right()
            ).runCode().state.fold(
                { error("Unknonwn Problem at $p") },
                { state -> state.output.pop() })
        })
    }

    fun fourCorners(point: PointL, squareSize: Long) = listOf(
        ::identity,
        PointL.x::modify::partially2 { it + squareSize },
        PointL.x::modify::partially2 { it + squareSize } compose PointL.y::modify::partially2 { it - squareSize },
        PointL.y::modify::partially2 { it - squareSize }
    ).all { findPowerAt(it(point)) == 1L }

    fun part1() {
        cache.clear()
        println((0 until 50).flatMap { y ->
            (0 until 50).map { x ->
                PointL(x.toLong(), y.toLong())
            }
        }.map(::findPowerAt).count { it == 1L })
    }

    fun part2() {
        val squareSize = 99L
        cache.clear()
        val n = generateSequence(squareSize) { it + 1 }.map { y ->
            generateSequence(y * 0.7) { it + 1 }.takeWhile { it <= y }.map { x ->
                PointL(x.toLong(), y.toLong())
            }.dropWhile { findPowerAt(it) == 0L }.first()
        }.dropWhile { !fourCorners(it, squareSize) }.first()
        println(n.x * 10000 + n.y - squareSize)
    }
}

fun main() {
    Day19.part1()
    Day19.part2()
}
