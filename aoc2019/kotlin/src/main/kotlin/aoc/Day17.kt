package aoc

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import arrow.core.left
import arrow.core.right
import arrow.optics.optics
import intcode.CurrentState
import intcode.IntCode
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import java.util.*

@optics
data class Ascii(
    val code: IntCode,
    val output: LinkedList<Long> = LinkedList(),
    val state: Either<String, CurrentState> = CurrentState().right(),
    val inputs: LinkedList<Long> = LinkedList()
) {
    companion object
}

private tailrec fun Ascii.runCode(): Ascii {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when (state.value.pointer) {
            is None -> this
            is Some<Long> -> {
                val nAscii = processOutput().sendInput()
                nAscii.copy(state = handleCodePoint(code, nAscii.state)).runCode()
            }
        }
    }
}

private fun Ascii.sendInput(): Ascii {
    return state.fold(
        ifLeft = { this },
        ifRight = {
            when {
                it.waitingForInput && inputs.isEmpty() -> copy(state = "Error: Attempted to get input with no inputs left.".left())
                it.waitingForInput -> {
                    val nInstr = inputs.pop()
                    it.inputs.add(nInstr.toLong())
                    copy(state = it.right())
                }
                else -> {
                    this
                }
            }
        }
    )
}

private tailrec fun Ascii.processOutput(): Ascii {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when {
            state.value.output.isEmpty() -> this
            else -> {
                val o = state.value.output.pop()
                when {
                    o > 127 -> println("NONASCII:$o")
                    else -> print(o.toInt().toChar())
                }
                output.add(o)
                if (o.toInt().toChar() == '\n') {
                    output.clear()
                }

                processOutput()
            }
        }
    }
}

object Day17 {
    private const val FILENAME = "src/main/resources/day17.txt"
    val fileData = FILENAME.toIntCodeProgram()

    fun part1() {
        val ascii = Ascii(code = fileData).runCode()
        println(ascii.output.joinToString("") { it.toInt().toChar().toString() })
        val points = ascii.output.joinToString("") { l -> "${l.toInt().toChar()}" }
            .lines().mapIndexed { y, line ->
                line.withIndex().filter { (_, c) -> c == '#' }
                    .map { (x, c) ->
                        Point(x, y) to c
                    }
            }.flatten().toMap()
        val intersections =
            points.filterKeys { p -> allDirections().map { d -> p.inDirection(d) }.all(points::contains) }.keys

        println(intersections.sumOf { (x, y) -> x * y })
    }

    val prog = "A,B,A,C,B,C,B,C,A,B\n" +
        "L,6,L,4,R,8\n" +
        "R,8,L,6,L,4,L,10,R,8\n" +
        "L,4,R,4,L,4,R,8\n" +
        "y\n"

    fun part2() {
        println(prog.lines().map { it.length })
        println(prog.map { b -> b.code.toLong() })
        val newCode = FILENAME.toIntCodeProgram().apply { this[0] = 2 }
        val ascii = Ascii(
            code = newCode,
            inputs = LinkedList(prog.map { it.code.toLong() }),
            state = CurrentState().right()
        ).runCode()

        println(ascii)
    }
}

fun main() {
    Day17.part1()
    Day17.part2()
}
