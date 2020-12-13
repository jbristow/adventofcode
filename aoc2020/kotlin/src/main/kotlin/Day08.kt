import util.AdventOfCode

object Day08 : AdventOfCode() {

    sealed class Operation {
        data class Acc(val value: Int) : Operation()
        data class Jmp(val value: Int) : Operation()
        data class Nop(val value: Int) : Operation()

        fun operate(lineNumber: Int, accumulator: Int) =
            when (this) {
                is Acc -> lineNumber + 1 to accumulator + value
                is Jmp -> lineNumber + value to accumulator
                is Nop -> lineNumber + 1 to accumulator
            }

        fun switch() =
            when (this) {
                is Acc -> throw IllegalArgumentException("Can't switch an Acc to anything.")
                is Jmp -> Nop(this.value)
                is Nop -> Jmp(this.value)
            }
    }

    fun processLine(line: String): Operation {
        val match = """(acc|jmp|nop) ([+-]\d+)""".toRegex().matchEntire(line)!!

        return when (match.groupValues[1]) {
            "acc" -> Operation.Acc(match.groupValues[2].toInt())
            "jmp" -> Operation.Jmp(match.groupValues[2].toInt())
            "nop" -> Operation.Nop(match.groupValues[2].toInt())
            else -> throw Exception("Bad instruction: ${match.groupValues[0]}")
        }
    }

    sealed class Outcome {
        data class Terminated(val line: Int, val acc: Int, val seen: List<Int>) : Outcome()
        data class LoopDetected(val line: Int, val acc: Int, val seen: List<Int>) : Outcome()

        val log: List<Int>
            get() = when (this) {
                is Terminated -> this.seen
                is LoopDetected -> this.seen
            }
        val accumulator: Int
            get() = when (this) {
                is Terminated -> this.acc
                is LoopDetected -> this.acc
            }
    }

    tailrec fun loopDetector(
        current: Int,
        accumulator: Int,
        instrs: List<Operation>,
        seenLines: List<Int> = emptyList()
    ): Outcome =
        when {
            current >= instrs.size -> Outcome.Terminated(current, accumulator, seenLines)
            current in seenLines -> Outcome.LoopDetected(current, accumulator, seenLines)
            else -> {
                val (nextCurrent, nextAccumulator) = instrs[current].operate(current, accumulator)
                loopDetector(nextCurrent, nextAccumulator, instrs, seenLines + current)
            }
        }

    fun part1(input: List<String>) =
        loopDetector(0, 0, input.map(Day08::processLine)).accumulator

    fun part2(input: List<String>): Int? {
        val originalInstrs = input.map(::processLine)

        return loopDetector(0, 0, originalInstrs).log
            .filter { instr -> originalInstrs[instr] !is Operation.Acc }
            .reversed()
            .asSequence()
            .map {
                loopDetector(0, 0, originalInstrs.swapInstructionAt(it))
            }.find { it is Outcome.Terminated }?.accumulator
    }

    private fun List<Operation>.swapInstructionAt(it: Int): MutableList<Operation> {
        val instrs = toMutableList()
        instrs[it] = this[it].switch()
        return instrs
    }
}

fun main() {
    println("Part 1: ${Day08.part1(Day08.inputFileLines)}")
    println("Part 2: ${Day08.part2(Day08.inputFileLines)}")
}
