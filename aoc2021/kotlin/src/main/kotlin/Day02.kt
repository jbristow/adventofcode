import util.AdventOfCode

object Day02 : AdventOfCode() {

    fun part1(input: Sequence<String>): Int {
        val (horiz, depth) =
            input.map { it.split(" ") }
                .fold(0 to 0) { (h, d), (instr, value) ->
                    val amount = value.toInt()
                    when (instr) {
                        "forward" -> (h + amount) to d
                        "down" -> h to (d + amount)
                        "up" -> h to (d - amount)
                        else -> throw Exception("Illegal instruction $this")
                    }
                }
        return horiz * depth
    }

    fun part2(input: Sequence<String>): Int {
        val (horiz, depth, _) =
            input.map { it.split(" ") }
                .fold(listOf(0, 0, 0)) { (h, d, a), (instr, value) ->
                    val amount = value.toInt()
                    when (instr) {
                        "forward" -> listOf(h + amount, d + a * amount, a)
                        "down" -> listOf(h, d, a + amount)
                        "up" -> listOf(h, d, a - amount)
                        else -> throw Exception("Illegal instruction $this")
                    }
                }
        return horiz * depth
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
