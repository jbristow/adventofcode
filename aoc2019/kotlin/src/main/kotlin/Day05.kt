import arrow.core.*
import java.nio.file.Files
import java.nio.file.Paths

sealed class Mode {
    fun assignable(first: Long, code: Array<Long>, offset: Long): Int {
        return when (this) {
            is Immediate -> throw Error("Cannot assign to immediate value")
            is Position -> first
            is Relative -> (offset + first)
        }.toInt()
    }

    fun valueOf(first: Long, code: Array<Long>, relativeBase: Long): Long {
        return when (this) {
            is Immediate -> first
            is Position -> code[first.toInt()]
            is Relative -> code[(relativeBase + first).toInt()]
        }
    }

    object Immediate : Mode()
    object Position : Mode()
    object Relative : Mode()
}

data class CurrentState(
    val pointer: Option<Int> = 0.some(),
    val inputs: MutableList<Long>,
    val output: Option<Long> = Option.empty(),
    val relativeBase: Long = 0
)

operator fun CurrentState.plus(n: Int) = CurrentState(pointer.map { it + n }, inputs, output)

fun Pair<Long, Mode>.index(code: Array<Long>, state: CurrentState): Int =
    second.assignable(first, code, state.relativeBase)

fun Pair<Long, Mode>.value(code: Array<Long>, state: CurrentState) =
    second.valueOf(first, code, state.relativeBase)

sealed class Instruction {
    abstract val opcodes: Int
    abstract fun execute(
        code: Array<Long>,
        params: List<Pair<Long, Mode>>,
        state: CurrentState
    ): Either<String, CurrentState>

    open fun findInputs(code: Array<Long>, state: CurrentState): List<Pair<Long, Mode>> {
        return state.pointer.fold(
            { emptyList() },
            { pointer ->
                code.drop(pointer + 1)
                    .take(3)
                    .zip(
                        "%010d".format(code[pointer] / 100).takeLast(opcodes).reversed()
                            .map {
                                when (it) {
                                    '0' -> Mode.Position
                                    '1' -> Mode.Immediate
                                    '2' -> Mode.Relative
                                    else -> throw Error("Bad mode $it")
                                }
                            })
            })
    }


    sealed class ThreeParameterInstruction : Instruction() {
        override val opcodes: Int = 3

        class Add : ThreeParameterInstruction() {
            override fun execute(
                code: Array<Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(code, state)] = params[0].value(code, state) + params[1].value(code, state)
                return (state + 4).right()
            }
        }

        class Multiply : ThreeParameterInstruction() {
            override fun execute(
                code: Array<Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(code, state)] = params[0].value(code, state) * params[1].value(code, state)
                return (state + 4).right()
            }
        }

        class LessThan : ThreeParameterInstruction() {
            override fun execute(
                code: Array<Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                println("LessThan")
                println(state)
                println(params)
                println(params[0].value(code,state))
                println(params[1].value(code,state))
                println(params[2].index(code,state))
                code[params[2].index(code, state)] = when {
                    params[0].value(code, state) < params[1].value(code, state) -> 1
                    else -> 0
                }
                return (state + 4).right()
            }
        }

        class Equal : ThreeParameterInstruction() {
            override fun execute(
                code: Array<Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(code, state)] = when {
                    params[0].value(code, state) == params[1].value(code, state) -> 1
                    else -> 0
                }
                return (state + 4).right()
            }
        }
    }

    sealed class TwoParameterInstruction : Instruction() {
        override val opcodes: Int = 2

        class JumpIfTrue : TwoParameterInstruction() {
            override fun execute(
                code: Array<Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                return when (params[0].value(code, state)) {
                    0L -> state + 3
                    else -> CurrentState(params[1].value(code, state).toInt().some(), state.inputs, state.output)
                }.right()
            }
        }

        class JumpIfFalse : TwoParameterInstruction() {
            override fun execute(
                code: Array<Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> =
                when (params[0].value(code, state)) {
                    0L -> CurrentState(params[1].value(code, state).toInt().some(), state.inputs, state.output)
                    else -> state + 3
                }.right()
        }
    }

    class SetFromInput(private val inputOption: Option<Long>) : Instruction() {
        override val opcodes: Int = 1
        override fun execute(
            code: Array<Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ) = inputOption.fold(
            { state },
            { input ->
                code[params[0].index(code, state)] = input
                CurrentState(state.pointer.map { it + 2 }, state.inputs, state.output)
            }).right()
    }

    class Output : Instruction() {
        override val opcodes: Int = 1
        override fun execute(
            code: Array<Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ): Either<String, CurrentState> {
            println("OUTPUT: ${params[0].value(code, state)}")
            return CurrentState(state.pointer.map { it + 2 }, state.inputs, params[0].value(code, state).some()).right()
        }
    }

    class ModifyRelativeBase : Instruction() {
        override val opcodes: Int = 1
        override fun execute(
            code: Array<Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ): Either<String, CurrentState> {
            return CurrentState(
                pointer = state.pointer.map { it + 2 },
                inputs = state.inputs,
                relativeBase = state.relativeBase + params[0].value(code, state)
            ).right()
        }
    }

    object End : Instruction() {
        override val opcodes: Int = 0
        override fun findInputs(code: Array<Long>, state: CurrentState): List<Pair<Long, Mode>> = emptyList()
        override fun execute(
            code: Array<Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ): Either<String, CurrentState> =
            CurrentState(
                Option.empty(),
                state.inputs,
                state.output
            ).right()
    }
}


object Day05 {

    fun parseInstruction(
        instruction: String,
        input: MutableList<Long>
    ): Either<String, Pair<Instruction, MutableList<Long>>> =
        when (instruction.takeLast(2).toLong()) {
            1L -> (Instruction.ThreeParameterInstruction.Add() to input).right()
            2L -> (Instruction.ThreeParameterInstruction.Multiply() to input).right()
            3L -> (Instruction.SetFromInput(input.firstOrNone()) to input.drop(1).toMutableList()).right()
            4L -> (Instruction.Output() to input).right()
            5L -> (Instruction.TwoParameterInstruction.JumpIfTrue() to input).right()
            6L -> (Instruction.TwoParameterInstruction.JumpIfFalse() to input).right()
            7L -> (Instruction.ThreeParameterInstruction.LessThan() to input).right()
            8L -> (Instruction.ThreeParameterInstruction.Equal() to input).right()
            9L -> (Instruction.ModifyRelativeBase() to input).right()
            99L -> (Instruction.End to input).right()
            else -> "Problem parsing instruction $instruction".left()
        }


    fun handleCodePoint(
        code: Array<Long>,
        state: CurrentState
    ): Either<String, CurrentState> {
        return state.pointer.fold(
            { Either.left("Problem handling codePoint: $state") },
            { pointer ->
                parseInstruction(code[pointer].toString(), state.inputs)
                    .flatMap { (instr, inp) ->
                        val finputs = instr.findInputs(code, state)
                        instr.execute(
                            code,
                            finputs,
                            CurrentState(pointer.some(), inp, state.output, state.relativeBase)
                        )
                    }
            })
    }

    tailrec fun step(
        code: Array<Long>,
        state: Either<String, CurrentState>
    ): String = when (state) {
        is Either.Left<String> -> state.a
        is Either.Right<CurrentState> -> {
            when (state.b.pointer) {
                is None -> state.b.output.fold({ "No output" }, { it.toString() })
                is Some<Int> -> step(code, handleCodePoint(code, state.b))
            }
        }
    }

    const
    val FILENAME = "src/main/resources/day05.txt"
}

fun main() {
    val problemInput = Files.readAllLines(Paths.get(Day05.FILENAME))
        .first()
        .split(",")
        .map { it.toLong() }

    // Part 01
    println("Part 01")
    println(
        Day05.step(
            code = problemInput.toTypedArray(),
            state = CurrentState(inputs = mutableListOf(1)).right()
        )
    )

    // Part 02
    println("\nPart 02")
    println(
        Day05.step(
            code = problemInput.toTypedArray(),
            state = CurrentState(inputs = mutableListOf(5)).right()
        )
    )

}
