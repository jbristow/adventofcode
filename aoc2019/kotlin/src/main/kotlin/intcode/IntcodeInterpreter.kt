package intcode

import arrow.core.*

sealed class Mode {
    fun assignable(first: Long, offset: Long) =
        when (this) {
            is Immediate -> throw Error("Cannot assign to immediate value")
            is Position -> first
            is Relative -> (offset + first)
        }

    fun valueOf(first: Long, code: Map<Long, Long>, relativeBase: Long) =
        when (this) {
            is Immediate -> first.some()
            is Position -> code[first].toOption()
            is Relative -> code[relativeBase + first].toOption()
        }.getOrElse { 0L }

    object Immediate : Mode()
    object Position : Mode()
    object Relative : Mode()
}

data class CurrentState(
    val pointer: Option<Long> = 0L.some(),
    val inputs: MutableList<Long> = mutableListOf(),
    val output: Option<Long> = Option.empty(),
    val relativeBase: Long = 0
)

operator fun CurrentState.plus(n: Int) = copy(pointer = pointer.map { it + n })
operator fun CurrentState.plus(n: Long) = copy(pointer = pointer.map { it + n })

fun Pair<Long, Mode>.index(state: CurrentState) = second.assignable(first, state.relativeBase)

fun Pair<Long, Mode>.value(code: Map<Long, Long>, state: CurrentState) =
    second.valueOf(first, code, state.relativeBase)

sealed class Instruction {
    abstract val opcodes: Int
    abstract fun execute(
        code: MutableMap<Long, Long>,
        params: List<Pair<Long, Mode>>,
        state: CurrentState
    ): Either<String, CurrentState>

    open fun findInputs(code: MutableMap<Long, Long>, state: CurrentState): Either<String, List<Pair<Long, Mode>>> =
        state.pointer.fold(
            { emptyList<Pair<Long, Mode>>().right() },
            { pointer ->
                (pointer + 1..pointer + 3)
                    .map { code[it] ?: 0 }
                    .zip(extractParameterType(pointer, code))
                    .fold(listOf<Pair<Long, Mode>>().right()) { a: Either<String, List<Pair<Long, Mode>>>, b ->
                        a.flatMap { acc ->
                            b.second.fold({
                                "Pointer $pointer: Problem parsing input ${b.first}: $it".left()
                            }, {
                                (acc + (b.first to it)).right()
                            })
                        }
                    }
            })

    private fun extractParameterType(pointer: Long, code: MutableMap<Long, Long>) =
        "%010d".format((code[pointer] ?: 0) / 100).takeLast(opcodes).reversed()
            .map {
                when (it) {
                    '0' -> Mode.Position.right()
                    '1' -> Mode.Immediate.right()
                    '2' -> Mode.Relative.right()
                    else -> "Bad mode $it".left()
                }
            }


    sealed class ThreeParameterInstruction : Instruction() {
        override val opcodes: Int = 3

        class Add : ThreeParameterInstruction() {
            override fun execute(
                code: MutableMap<Long, Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(state)] = params[0].value(code, state) + params[1].value(code, state)
                return (state + 4).right()
            }
        }

        class Multiply : ThreeParameterInstruction() {
            override fun execute(
                code: MutableMap<Long, Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(state)] = params[0].value(code, state) * params[1].value(code, state)
                return (state + 4).right()
            }
        }

        class LessThan : ThreeParameterInstruction() {
            override fun execute(
                code: MutableMap<Long, Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(state)] = when {
                    params[0].value(code, state) < params[1].value(code, state) -> 1L
                    else -> 0L
                }
                return (state + 4).right()
            }
        }

        class Equal : ThreeParameterInstruction() {
            override fun execute(
                code: MutableMap<Long, Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                code[params[2].index(state)] = when {
                    params[0].value(code, state) == params[1].value(code, state) -> 1
                    else -> 0L
                }
                return (state + 4).right()
            }
        }
    }

    sealed class TwoParameterInstruction : Instruction() {
        override val opcodes: Int = 2

        class JumpIfTrue : TwoParameterInstruction() {
            override fun execute(
                code: MutableMap<Long, Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> {
                return when (params[0].value(code, state)) {
                    0L -> state + 3
                    else -> state.copy(pointer = params[1].value(code, state).some())
                }.right()
            }
        }

        class JumpIfFalse : TwoParameterInstruction() {
            override fun execute(
                code: MutableMap<Long, Long>,
                params: List<Pair<Long, Mode>>,
                state: CurrentState
            ): Either<String, CurrentState> =
                when (params[0].value(code, state)) {
                    0L -> state.copy(pointer = params[1].value(code, state).some())
                    else -> state + 3
                }.right()
        }
    }

    class SetFromInput(private val inputOption: Option<Long>) : Instruction() {
        override val opcodes: Int = 1
        override fun execute(
            code: MutableMap<Long, Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ) = inputOption.fold(
            { state },
            { input ->
                code[params[0].index(state)] = input
                state.copy(pointer = state.pointer.map { it + 2 })
            }).right()
    }

    class Output : Instruction() {
        override val opcodes: Int = 1
        override fun execute(
            code: MutableMap<Long, Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ): Either<String, CurrentState> {
            println("OUTPUT: ${params[0].value(code, state)}")
            return state.copy(
                pointer = state.pointer.map { it + 2 },
                output = params[0].value(code, state).some()
            ).right()
        }
    }

    class ModifyRelativeBase : Instruction() {
        override val opcodes: Int = 1
        override fun execute(
            code: MutableMap<Long, Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ): Either<String, CurrentState> {
            return state.copy(
                pointer = state.pointer.map { it + 2 },
                relativeBase = state.relativeBase + params[0].value(code, state)
            ).right()
        }
    }

    object End : Instruction() {
        override val opcodes: Int = 0
        override fun execute(
            code: MutableMap<Long, Long>,
            params: List<Pair<Long, Mode>>,
            state: CurrentState
        ): Either<String, CurrentState> = state.copy(pointer = Option.empty()).right()
    }
}


fun parseInstruction(
    instruction: Long,
    input: MutableList<Long>
): Either<String, Pair<Instruction, MutableList<Long>>> =
    when (instruction % 100) {
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
    code: MutableMap<Long, Long>,
    eitherState: Either<String, CurrentState>
): Either<String, CurrentState> =
    eitherState.flatMap { state ->
        state.pointer.fold(
            { "Program has stopped.".left() },
            { pointer ->
                code[pointer].rightIfNotNull { "Code point $pointer undefined." }.flatMap { codeValue ->
                    parseInstruction(codeValue, state.inputs).flatMap { (instr, inp) ->
                        instr.findInputs(code, state).flatMap { params ->
                            instr.execute(code, params, state.copy(pointer = pointer.some(), inputs = inp))
                        }
                    }
                }
            }
        )
    }

fun step(code: MutableMap<Long, Long>, state: CurrentState): Either<String, Long> {
    return step(code, state.right())
}

tailrec fun step(
    code: MutableMap<Long, Long>,
    state: Either<String, CurrentState>
): Either<String, Long> = when (state) {
    is Either.Left<String> -> "Error: ${state.a}".left()
    is Either.Right<CurrentState> -> {
        when (state.b.pointer) {
            is None -> state.b.output.fold({ "No output".left() }, { it.right() })
            is Some<Long> -> step(code, handleCodePoint(code, state))
        }
    }
}

