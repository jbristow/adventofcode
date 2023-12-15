import util.AdventOfCode

object Day15 : AdventOfCode() {
    private fun String.toHash() = fold(0) { currentValue, c -> ((currentValue + c.code) * 17) % 256 }

    private fun part1(input: String) = input.trim().split(",").sumOf { it.toHash() }

    private val insertRe = """(?<label>.*)=(?<lens>\d+)""".toRegex()
    private val removeRe = """(?<label>.*)-""".toRegex()

    sealed class Instruction(val box: Int) {
        class Remove(val label: String) : Instruction(label.toHash())

        class Insert(val lens: Lens) : Instruction(lens.label.toHash())

        override fun toString(): String {
            return when (this) {
                is Remove -> "$label-"
                is Insert -> "${lens.label}=${lens.focalLength}"
            }
        }
    }

    private fun String.toInstruction(): Instruction {
        return when (val mrInsert = insertRe.find(this)) {
            null ->
                when (val mrRemove = removeRe.find(this)) {
                    null -> throw Exception("Unknown instruction $this")
                    else -> Instruction.Remove(mrRemove.groups["label"]!!.value)
                }

            else -> Instruction.Insert(Lens(mrInsert.groups["label"]!!.value, mrInsert.groups["lens"]!!.value.toInt()))
        }
    }

    data class Lens(val label: String, var focalLength: Int) {
        override fun hashCode(): Int {
            return label.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return when (other) {
                null -> false
                !is Lens -> false
                else -> label == other.label
            }
        }

        override fun toString(): String {
            return "[$label $focalLength]"
        }
    }

    private fun slotPower(
        box: Int,
        slot: Int,
        length: Int,
    ) = (slot + 1L) * length * (box + 1L)

    private fun IndexedValue<List<Lens>>.boxPower() =
        this.value.withIndex().sumOf { (slot, lens) ->
            slotPower(this.index, slot, lens.focalLength)
        }

    private fun part2(input: String): Long {
        val instructions = input.split(",").map { it.toInstruction() }

        val boxes = Array(256) { mutableListOf<Lens>() }

        instructions.forEach { instruction ->
            val currBox = boxes[instruction.box]
            when (instruction) {
                is Instruction.Insert -> {
                    when (val match = currBox.indexOf(instruction.lens)) {
                        -1 -> currBox.add(instruction.lens)
                        else -> currBox[match].focalLength = instruction.lens.focalLength
                    }
                }

                is Instruction.Remove -> currBox.remove(Lens(instruction.label, 0))
            }
        }

        return boxes.withIndex().sumOf { it.boxPower() }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 15")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
