package aoc

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import arrow.core.right
import arrow.optics.optics
import intcode.CurrentState
import intcode.IntCode
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import java.util.*

object Day21 {

    private const val FILENAME = "src/main/resources/day21.txt"
    val fileData get() = FILENAME.toIntCodeProgram()

    @optics
    data class CodeRunner(val state: Either<String, CurrentState>, val code: IntCode) {
        companion object
    }

    private tailrec fun CodeRunner.runCode(): CodeRunner {
        return when (state) {
            is Either.Left<String> -> this
            is Either.Right<CurrentState> -> when (state.value.pointer) {
                is None -> this
                is Some<Long> -> {
                    copy(state = handleCodePoint(code, state)).runCode()
                }
            }
        }
    }

    private val springCodePart1 = """
        |NOT A J
        |NOT B T
        |OR T J
        |NOT C T
        |OR T J
        |AND D J
        |WALK
        |""".trimMargin()

    private val springCodePart2 = """
        |NOT C J
        |AND D J
        |NOT H T
        |NOT T T
        |OR E T
        |AND T J
        |NOT A T
        |OR T J
        |NOT B T
        |NOT T T
        |OR E T
        |NOT T T
        |OR T J
        |RUN
        |""".trimMargin()

    fun part1() {
        println("\tPart 1: ${runSpringDroid(springCodePart1)}")
    }

    fun part2() {
        println("\tPart 2: ${runSpringDroid(springCodePart2)}")
    }

    private fun runSpringDroid(code: String): Long {
        return CodeRunner(
            code = fileData.toMutableMap(),
            state = CurrentState(
                inputs = LinkedList(code.map { it.code.toLong() })
            ).right()
        ).runCode()
            .let { runner ->
                when (runner.state) {
                    is Either.Right -> runner.state.value.output.last
                    is Either.Left -> throw Exception(runner.state.value)
                }
            }
    }
}

fun main() {
    println("Day 21:")
    Day21.part1()
    Day21.part2()
}
