import util.AdventOfCode
import java.util.*

object Day05 : AdventOfCode() {
    data class CargoArea(
        val stacks: Map<Int, LinkedList<Char>>,
        val instructions: Sequence<Instruction>,
        val crane: Crane
    ) {
        fun processInstructions() = instructions.forEach { crane.work(stacks, it) }
        fun topBoxLabels() = stacks.toSortedMap().values.map { it.first() }.joinToString("")
    }

    interface Crane {
        fun work(stacks: Map<Int, LinkedList<Char>>, instruction: Instruction)
    }

    object CrateMover9000 : Crane {
        override fun work(stacks: Map<Int, LinkedList<Char>>, instruction: Instruction) {
            transfer(instruction.numToMove, stacks[instruction.toCol]!!, stacks[instruction.fromCol]!!)
        }
    }

    object CrateMover9001 : Crane {
        private val tempStack = LinkedList<Char>()
        override fun work(stacks: Map<Int, LinkedList<Char>>, instruction: Instruction) {
            transfer(instruction.numToMove, tempStack, stacks[instruction.fromCol]!!)
            transfer(instruction.numToMove, stacks[instruction.toCol]!!, tempStack)
        }
    }

    data class Instruction(val numToMove: Int, val fromCol: Int, val toCol: Int) {
        constructor(n: String, f: String, t: String) : this(n.toInt(), f.toInt(), t.toInt())
    }

    private fun String.toInstruction() =
        """move (\d+) from (\d+) to (\d+)""".toRegex()
            .matchEntire(this)!!.groupValues
            .let { Instruction(it[1], it[2], it[3]) }

    private fun parse(input: String, crane: Crane): CargoArea {
        val (diagram, instrInput) = input.split("\n\n").map { it.lines() }

        val cols = diagram.last().trim().split("   ").map {
            it.toInt() to (it.toInt() - 1) * 4 + 1
        }

        val stacks = cols.associate { it.first to LinkedList<Char>() }
        diagram.dropLast(1).reversed().forEach { line ->
            cols.filter { (_, col) -> line[col] != ' ' }.forEach { (id, col) -> stacks[id]?.push(line[col]) }
        }

        return CargoArea(stacks, instrInput.asSequence().map { it.toInstruction() }, crane)
    }

    private fun transfer(n: Int, a: LinkedList<Char>, b: LinkedList<Char>) = repeat(n) { a.push(b.pop()) }

    fun part1(input: String): String {
        val cargoArea = parse(input, CrateMover9000)
        cargoArea.processInstructions()
        return cargoArea.topBoxLabels()
    }

    fun part2(input: String): String {
        val cargoArea = parse(input, CrateMover9001)
        cargoArea.processInstructions()
        return cargoArea.topBoxLabels()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
