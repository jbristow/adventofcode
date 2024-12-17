import util.AdventOfCode
import java.lang.Math.pow
import kotlin.streams.asStream

object Day17 : AdventOfCode() {
    val sample = """Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0""".lines()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 17")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    sealed class Opcode(val id: Int) {

        abstract fun op(combo: Long, program: Program): Program

        // The adv instruction (opcode 0) performs division.
        // The numerator is the value in the A register.
        // The denominator is found by raising 2 to the power of the instruction's combo operand.
        // (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
        // The result of the division operation is truncated to an integer and then written to the A register.
        data object Adv : Opcode(0) {
            override fun op(combo: Long, program: Program): Program {
                val numerator = program.a
                val denominator = pow(2.0, calculate(combo,program).toDouble())
                return program.copy(a = (numerator / denominator).toLong(), pointer = program.pointer + 2)
            }
        }

        // The bxl instruction (opcode 1) calculates the bitwise XOR of
        // register B and the instruction's literal operand, then stores
        // the result in register B.
        data object Bxl : Opcode(1) {
            override fun op(combo: Long, program: Program): Program {
                return program.copy(b = program.b xor combo, pointer = program.pointer + 2)
            }
        }

        // The bst instruction (opcode 2) calculates the value of its combo operand modulo 8
        // (thereby keeping only its lowest 3 bits), then writes that value to the B register.
        data object Bst : Opcode(2) {
            override fun op(combo: Long, program: Program): Program {
                return program.copy(b = calculate(combo, program) % 8, pointer = program.pointer + 2)
            }
        }

        //  The jnz instruction (opcode 3) does nothing if the A register is 0.
//  However, if the A register is not zero, it jumps by setting the instruction
//  pointer to the value of its literal operand; if this instruction jumps, the
//  instruction pointer is not increased by 2 after this instruction.
        data object Jnz : Opcode(3) {
            override fun op(combo: Long, program: Program): Program {
                if (program.a == 0L) {
                    return program.copy(pointer = program.pointer + 2)
                } else {
                    return program.copy(pointer = combo)
                }
            }
        }

        //  The bxc instruction (opcode 4) calculates the
        //  bitwise XOR of register B and register C,
        //  then stores the result in register B.
        //  (For legacy reasons, this instruction reads an operand but ignores it.)
        data object Bxc : Opcode(4) {
            override fun op(combo: Long, program: Program): Program {
                return program.copy(b = program.b xor program.c, pointer = program.pointer + 2)
            }
        }

        //  The out instruction (opcode 5) calculates the value of its combo operand modulo 8,
        //  then outputs that value.
        //  (If a program outputs multiple values, they are separated by commas.)
        data object Out : Opcode(5) {
            override fun op(combo: Long, program: Program): Program {
                val output = calculate(combo, program)
                return program.copy(output = program.output + (output % 8), pointer = program.pointer + 2)
            }
        }

        //  The bdv instruction (opcode 6) works exactly like the adv instruction
        //  except that the result is stored in the B register.
        //  (The numerator is still read from the A register.)
        data object Bdv : Opcode(6) {
            override fun op(combo: Long, program: Program): Program {
                val numerator = program.a
                val denominator = pow(2.0, calculate(combo, program).toDouble())
                return program.copy(b = (numerator / denominator).toLong(), pointer = program.pointer + 2)
            }
        }

        //  The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register. (The numerator is still read from the A register.)
        data object Cdv : Opcode(7) {
            override fun op(combo: Long, program: Program): Program {
                val numerator = program.a
                val denominator = pow(2.0, calculate(combo, program).toDouble())
                return program.copy(c = (numerator / denominator).toLong(), pointer = program.pointer + 2)
            }
        }

        companion object {
            fun toOp(code: Long): Opcode {
                return when (code.toInt()) {
                    Adv.id -> Adv
                    Bxl.id -> Bxl
                    Bst.id -> Bst
                    Jnz.id -> Jnz
                    Bxc.id -> Bxc
                    Out.id -> Out
                    Bdv.id -> Bdv
                    Cdv.id -> Cdv
                    else -> throw Exception("Baad code ${code}")
                }
            }
            fun calculate(combo:Long, program:Program): Long {
                return when (combo) {
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
    }

    data class Program(
        val a: Long,
        val b: Long,
        val c: Long,
        val instructions: List<Long>,
        val pointer: Long = 0,
        val output: List<Long> = emptyList()
    ) {

    }

    private fun part1(inputFileLines: List<String>): Int {
        val program = parse(inputFileLines)
        println(program)

        val finished = execute(program)
        println(finished.output.joinToString(","))
        return -1
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

    private fun part2(input: List<String>): Int {
        val program = parse(input)
        println(program)

        val finished = generateSequence(0L) { it +1  }.asStream().parallel().filter {
            if (it % 1000L == 0L) {
                println(it)
            }
            executeShortCircuiting(program.copy(a=it)) != null
        }.findFirst()

        println(finished)
        return -1    }

    private tailrec fun findInital(program: Day17.Program, init:Long=0): Long {
        val after = executeShortCircuiting(program.copy(a=init))
        if (init % 1000L == 0L) {
            println(init)
        }
        if (after != null) {
            return init
        }
        return findInital(program, init+1)
    }

    private tailrec fun executeShortCircuiting(program: Day17.Program): Program? {
        if (program.pointer >= program.instructions.size && program.instructions == program.output) {
            return program
        }
        if (program.pointer >= program.instructions.size ) {
            return null
        }
        val (id, combo) = program.instructions.drop(program.pointer.toInt()).take(2)
        val op = Opcode.toOp(id)
        val next = execute(op.op(combo, program))

        if (op is Opcode.Out && next.output != next.instructions.take(next.output.size)) {
            return null
        }
        return executeShortCircuiting(next)
    }
}
