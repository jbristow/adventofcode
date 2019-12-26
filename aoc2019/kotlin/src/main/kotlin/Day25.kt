import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import arrow.core.right
import arrow.optics.optics
import intcode.CurrentState
import intcode.IntCode
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import java.util.LinkedList

@optics
data class Cryostasis(
    val code: IntCode,
    val state: Either<String, CurrentState> = CurrentState().right(),
    val inputs: LinkedList<Long> = LinkedList(),
    val saveState: Map<Long, Long> = code,
    val initalCommands: LinkedList<String>
) {
    companion object
}

private tailrec fun Cryostasis.runCode(): Cryostasis {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when (state.b.pointer) {
            is None -> this
            is Some<Long> -> {
                val nCryostasis = processOutput().sendInput()
                nCryostasis.copy(state = handleCodePoint(code, nCryostasis.state)).runCode()
            }
        }
    }
}

private fun Cryostasis.sendInput(): Cryostasis {
    return state.fold(
        ifLeft = { this },
        ifRight = {
            when {
                it.waitingForInput && initalCommands.isNotEmpty() -> it.inputs.addAll(initalCommands.pop().map(Char::toLong))
                it.waitingForInput -> {
                    val enteredString = readLine()
                    it.inputs.addAll(enteredString!!.map(Char::toLong))
                    it.inputs.add(10L)
                }
            }
            this
        }
    )
}

private tailrec fun Cryostasis.processOutput(): Cryostasis {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when {
            state.b.output.isEmpty() -> this
            else -> {
                val o = state.b.output.pop()
                when {
                    o > 127 -> println("NONASCII:$o")
                    else -> print(o.toChar())
                }
                processOutput()
            }
        }
    }
}

object Day25 {
    fun part1() {
        val initialCommandText = """
            |south
            |west
            |south
            |south
            |take pointer
            |north
            |take space law space brochure
            |north
            |take wreath
            |east
            |south
            |east
            |take loom
            |west
            |north
            |take space heater
            |north
            |north
            |north
            |take sand
            |south
            |south
            |west
            |south
            |take planetoid
            |north
            |west
            |take festive hat
            |south
            |west""".trimMargin().lines().map { "$it\n" }
        Cryostasis(code = fileData.toMutableMap(), initalCommands = LinkedList(initialCommandText)).runCode()
    }

    private const val FILENAME = "src/main/resources/day25.txt"
    val fileData = FILENAME.toIntCodeProgram()


}

fun main() {
    Day25.part1()
}