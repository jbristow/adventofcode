import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.core.some
import java.nio.file.Files
import java.nio.file.Paths

sealed class Mode {
    object Immediate : Mode()
    object Position : Mode()
}

sealed class Instruction {
    abstract val opcodeFormat: String
    abstract fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int>

    open fun findInputs(code: Array<Int>, pointer: Int) =
        code.drop(pointer + 1)
            .take(3)
            .zip(opcodeFormat.format(code[pointer] / 100).map {
                when (it) {
                    '0' -> Mode.Position
                    '1' -> Mode.Immediate
                    else -> throw Error("Bad mode $it")
                }
            }.reversed())
            .map { (it, mode) ->
                when (mode) {
                    Mode.Position -> code[it]
                    Mode.Immediate -> it
                }
            }

    sealed class ThreeParameterInstruction : Instruction() {
        override val opcodeFormat = "1%02d"

        class Add : ThreeParameterInstruction() {
            override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> {
                code[params[2]] = params[0] + params[1]
                return (pointer + 4).right()
            }
        }

        class Multiply : ThreeParameterInstruction() {
            override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> {
                code[params[2]] = params[0] * params[1]
                return (pointer + 4).right()
            }
        }

        class LessThan : ThreeParameterInstruction() {
            override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> {
                code[params[2]] = when {
                    params[0] < params[1] -> 1
                    else -> 0
                }
                return (pointer + 4).right()
            }
        }

        class Equal : ThreeParameterInstruction() {
            override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> {
                code[params[2]] = when {
                    params[0] == params[1] -> 1
                    else -> 0
                }
                return (pointer + 4).right()
            }
        }
    }

    sealed class TwoParameterInstruction : Instruction() {
        override val opcodeFormat = "%02d"

        class JumpIfTrue : TwoParameterInstruction() {
            override fun execute(pointer: Int, code: Array<Int>, params: List<Int>) =
                when (params[0]) {
                    0 -> pointer + 3
                    else -> params[1]
                }.right()
        }

        class JumpIfFalse : TwoParameterInstruction() {
            override fun execute(pointer: Int, code: Array<Int>, params: List<Int>) =
                when (params[0]) {
                    0 -> params[1]
                    else -> pointer + 3
                }.right()
        }
    }

    class SetFromInput(private val input: Int) : Instruction() {
        override val opcodeFormat = "1"
        override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> {
            code[params[0]] = input
            return (pointer + 2).right()
        }
    }

    class Output : Instruction() {
        override val opcodeFormat = "0"
        override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> {
            println("output: ${params[0]}")
            return (pointer + 2).right()
        }
    }

    object End : Instruction() {
        override val opcodeFormat: String
            get() = throw Error("No opcode format for End instructions.")

        override fun findInputs(code: Array<Int>, pointer: Int) = emptyList<Int>()
        override fun execute(pointer: Int, code: Array<Int>, params: List<Int>): Either<Option<String>, Int> =
            Option.empty<String>().left()
    }
}

object Day05 {

    private fun parseInstruction(instruction: String, input: Int) =
        when (instruction.takeLast(2).toInt()) {
            1 -> Instruction.ThreeParameterInstruction.Add().right()
            2 -> Instruction.ThreeParameterInstruction.Multiply().right()
            3 -> Instruction.SetFromInput(input).right()
            4 -> Instruction.Output().right()
            5 -> Instruction.TwoParameterInstruction.JumpIfTrue().right()
            6 -> Instruction.TwoParameterInstruction.JumpIfFalse().right()
            7 -> Instruction.ThreeParameterInstruction.LessThan().right()
            8 -> Instruction.ThreeParameterInstruction.Equal().right()
            99 -> Instruction.End.right()
            else -> "Problem parsing instruction $instruction".some().left()
        }

    private fun handleCodePoint(pointer: Int, input: Int, code: Array<Int>) =
        parseInstruction(code[pointer].toString(), input).flatMap { instr ->
            instr.execute(pointer, code, instr.findInputs(code, pointer))
        }

    tailrec fun step(
        code: Array<Int>,
        input: Int,
        instructionPointer: Either<Option<String>, Int> = Either.right(0)
    ): String = when (instructionPointer) {
        is Either.Left<Option<String>> -> instructionPointer.a.getOrElse { "Program terminated successfully." }
        is Either.Right<Int> -> {
            val nextInstruction = handleCodePoint(instructionPointer.b, input, code)
            step(code, input, nextInstruction)
        }
    }

    const val FILENAME = "src/main/resources/day05.txt"
}

fun main() {
    val problemInput = Files.readAllLines(Paths.get(Day05.FILENAME))
        .first()
        .split(",")
        .map { it.toInt() }

    // Part 01
    println("Part 01")
    Day05.step(code = problemInput.toTypedArray(), input = 1)

    // Part 02
    println("\nPart 02")
    Day05.step(code = problemInput.toTypedArray(), input = 5)

    println("\nDay 02")
    val day02Code = Files.readAllLines(Paths.get("src/main/resources/day02.txt")).first()
        .split(",")
        .map { it.toInt() }.toTypedArray()
    Day05.step(code = day02Code, input = 5)
    println(day02Code.take(10))
}
