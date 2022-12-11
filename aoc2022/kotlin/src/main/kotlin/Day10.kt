import util.AdventOfCode

object Day10 : AdventOfCode() {

    sealed class Instruction {
        abstract fun execute(): State
        data class Addx(val x: Int, private val cycles: Int = 2) : Instruction() {
            override fun execute(): State {
                return if (cycles > 1) {
                    State.Running(this.copy(cycles = this.cycles - 1))
                } else {
                    State.Finished(this.x)
                }
            }
        }

        object Noop : Instruction() {
            override fun execute(): State {
                return State.Finished(0)
            }
        }
    }

    sealed interface State {
        data class Running(val instr: Instruction) : State
        data class Finished(val x: Int) : State
    }

    private fun generateCycleXValues(input: List<String>): List<Int> {
        val instrs = input.map {
            it.split(" ").let { line ->
                when (line[0]) {
                    "addx" -> Instruction.Addx(line[1].toInt())
                    "noop" -> Instruction.Noop
                    else -> throw IllegalArgumentException("bad line: $line")
                }
            }
        }
        return runProgram(instrs)
    }

    private tailrec fun runProgram(
        input: List<Instruction>,
        executing: Instruction? = null,
        x: Int = 1,
        seenX: List<Int> = listOf(1)
    ): List<Int> {
        if (executing == null && input.isEmpty()) {
            return seenX
        }

        val (currExecuting, newInput) = when (executing) {
            null -> input.first() to input.drop(1)
            else -> executing to input
        }

        val (newX, nextExecuting) = when (val result = currExecuting.execute()) {
            is State.Finished -> x + result.x to null
            is State.Running -> x to result.instr
        }

        return runProgram(newInput, nextExecuting, newX, seenX + newX)
    }

    private fun part1(input: List<String>): Int {
        val xVals = generateCycleXValues(input)
        return xVals.asSequence().withIndex()
            .drop(19)
            .chunked(40) { it.first() }
            .sumOf { (i, it) -> (i + 1) * it }
    }

    private fun part2(input: List<String>): String {
        val xVals = generateCycleXValues(input)
        return xVals.windowed(40, 40)
            .joinToString("\n") { row ->
                row.map { it -> it - 1..it + 1 }
                    .mapIndexed { pixel, sprite -> if (pixel in sprite) "\uD83C\uDF84" else "◼️" }
                    .joinToString("")
            }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: \n${part2(inputFileLines)}")
    }
}
