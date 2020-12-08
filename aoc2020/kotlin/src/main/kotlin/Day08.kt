import Day08.FILENAME
import Day08.part1
import Day08.part2
import java.nio.file.Files
import java.nio.file.Paths

object Day08 {
    const val FILENAME = "src/main/resources/day08.txt"

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
        data class Terminated(val acc: Int) : Outcome()
        data class LoopDetected(val lineNumber: Int, val acc: Int, val log: List<Int>) : Outcome()

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
            current >= instrs.size -> Outcome.Terminated(accumulator)
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
        val condition = loopDetector(0, 0, originalInstrs)
        val changeable =
            (condition as Outcome.LoopDetected).log
                .filter { instr -> originalInstrs[instr] !is Operation.Acc }.toSet()

        val answer =
            changeable.reversed()
                .asSequence()
                .map {
                    loopDetector(0, 0, originalInstrs.swapInstructionAt(it))
                }.find { it is Outcome.Terminated }

        return answer?.accumulator
    }

    private fun List<Operation>.swapInstructionAt(it: Int): MutableList<Operation> {
        val instrs = toMutableList()
        instrs[it] = this[it].switch()
        return instrs
    }
}

fun main() {
    val data = Files.readAllLines(Paths.get(FILENAME))
    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}
