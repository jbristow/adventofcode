import util.AdventOfCode
import kotlin.math.pow

object Day17 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 17")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    sealed class Opcode(
        val id: Int,
    ) {

        abstract fun op(
            combo: Long,
            program: Program,
        ): Program

        data object Adv : Opcode(0) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program {
                val numerator = program.a
                val denominator = 2.0.pow(calculate(combo, program).toDouble())
                return program.copy(a = (numerator / denominator).toLong(), pointer = program.pointer + 2)
            }
        }

        data object Bxl : Opcode(1) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program = program.copy(b = program.b xor combo, pointer = program.pointer + 2)
        }

        data object Bst : Opcode(2) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program = program.copy(b = calculate(combo, program) % 8, pointer = program.pointer + 2)
        }

        data object Jnz : Opcode(3) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program =
                if (program.a == 0L) {
                    program.copy(pointer = program.pointer + 2)
                } else {
                    program.copy(pointer = combo)
                }
        }

        data object Bxc : Opcode(4) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program = program.copy(b = program.b xor program.c, pointer = program.pointer + 2)
        }

        data object Out : Opcode(5) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program {
                val output = calculate(combo, program)
                return program.copy(output = program.output + (output % 8), pointer = program.pointer + 2)
            }
        }

        data object Bdv : Opcode(6) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program {
                val numerator = program.a
                val denominator = 2.0.pow(calculate(combo, program).toDouble())
                return program.copy(b = (numerator / denominator).toLong(), pointer = program.pointer + 2)
            }
        }

        //  The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register. (The numerator is still read from the A register.)
        data object Cdv : Opcode(7) {
            override fun op(
                combo: Long,
                program: Program,
            ): Program {
                val numerator = program.a
                val denominator = 2.0.pow(calculate(combo, program).toDouble())
                return program.copy(c = (numerator / denominator).toLong(), pointer = program.pointer + 2)
            }
        }

        companion object {
            fun toOp(code: Long): Opcode =
                when (code.toInt()) {
                    Adv.id -> Adv
                    Bxl.id -> Bxl
                    Bst.id -> Bst
                    Jnz.id -> Jnz
                    Bxc.id -> Bxc
                    Out.id -> Out
                    Bdv.id -> Bdv
                    Cdv.id -> Cdv
                    else -> throw Exception("Bad code $code")
                }

            fun calculate(
                combo: Long,
                program: Program,
            ): Long =
                when (combo) {
                    0L -> 0L
                    1L -> 1L
                    2L -> 2L
                    3L -> 3L
                    4L -> program.a
                    5L -> program.b
                    6L -> program.c
                    else -> throw Exception("illegal output %combo")
                }
        }
    }

    data class Program(
        val a: Long,
        val b: Long,
        val c: Long,
        val instructions: List<Long>,
        val pointer: Long = 0,
        val output: List<Long> = emptyList(),
    )

    private fun part1(inputFileLines: List<String>): String {
        val program = parse(inputFileLines)

        return execute(program).output.joinToString(",")
    }

    tailrec fun execute(program: Program): Program {
        if (program.pointer >= program.instructions.size) {
            return program
        }
        val (id, combo) = program.instructions.drop(program.pointer.toInt()).take(2)
        return execute(Opcode.toOp(id).op(combo, program))
    }

    private fun parse(input: List<String>): Program {
        val a = input[0].split(": ")[1].toLong()
        val b = input[1].split(": ")[1].toLong()
        val c = input[2].split(": ")[1].toLong()
        val instructions = input[4].split(": ")[1].split(",").map { it.toLong() }
        return Program(a, b, c, instructions)
    }

    private fun part2(input: List<String>): Long {
        val program = parse(input)
        return (0..7).flatMap { findA(program, it.toLong()) }.minOf { it }
    }

    private fun findA(
        program: Program,
        x: Long,
    ): List<Long> {
        val output = execute(program.copy(a = x)).output

        return if (output == program.instructions) {
            listOf(x)
        } else if (output == program.instructions.takeLast(output.size)) {
            (0..7).mapNotNull { findA(program, x * 8 + it) }.flatten()
        } else {
            listOf()
        }
    }
}
