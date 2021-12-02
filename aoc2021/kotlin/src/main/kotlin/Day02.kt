import util.AdventOfCode

object Day02 : AdventOfCode() {

    sealed class InstructionA(val horizontal: Int = 0, val depth: Int = 0) {
        class Forward(amount: Int = 1) : InstructionA(horizontal = 1 * amount)
        class Down(amount: Int = 1) : InstructionA(depth = 1 * amount)
        class Up(amount: Int = 1) : InstructionA(depth = -1 * amount)
    }

    fun String.toInstructionA(amount: Int) =
        when (this) {
            "forward" -> InstructionA.Forward(amount)
            "down" -> InstructionA.Down(amount)
            "up" -> InstructionA.Up(amount)
            else -> throw Exception("Illegal instruction $this")
        }

    fun part1(input: Sequence<String>): Int {
        val (horiz, depth) =
            input.map { it.split(" ") }
                .map { (a, b) -> a.toInstructionA(b.toInt()) }
                .fold(0 to 0) { (a, b), instr -> (a + instr.horizontal) to (b + instr.depth) }
        return horiz * depth
    }

    sealed class InstructionB(val value: Int = 0) {
        class Forward(amount: Int) : InstructionB(amount)
        class Down(amount: Int) : InstructionB(amount)
        class Up(amount: Int) : InstructionB(amount)

        fun handleMovement(currHoriz: Int, currDepth: Int, currAim: Int): List<Int> {
            return when (this) {
                is Forward -> listOf(currHoriz + value, currDepth + currAim * value, currAim)
                is Down -> listOf(currHoriz, currDepth, currAim + value)
                is Up -> listOf(currHoriz, currDepth, currAim - value)
            }
        }
    }

    fun String.toInstructionB(amount: Int): InstructionB {
        return when (this) {
            "forward" -> InstructionB.Forward(amount)
            "down" -> InstructionB.Down(amount)
            "up" -> InstructionB.Up(amount)
            else -> throw Exception("Illegal instruction $this")
        }
    }

    fun part2(input: Sequence<String>): Int {
        val (horiz, depth, _) = input.map { it.split(" ") }
            .map { (a, b) -> a.toInstructionB(b.toInt()) }
            .fold(listOf(0, 0, 0)) { (a, b, c), instr -> instr.handleMovement(a, b, c) }
        return horiz * depth
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
