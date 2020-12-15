import util.AdventOfCode

object Day15 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Part 1: ${part1(inputFileString.split(",").map(String::toInt))}")
        println("Part 2: ${part2(inputFileString.split(",").map(String::toInt))}")
    }

    tailrec fun nextTurn(map: MutableMap<Int, Int>, lastSpoken: Int, turn: Int, maxTurns: Int): Int {
        if (turn == maxTurns) {
            return lastSpoken
        }

        val spoken = when (val last = map[lastSpoken]) {
            null -> 0
            else -> turn - last - 1
        }
        map[lastSpoken] = (turn - 1)
        return nextTurn(map, spoken, turn + 1, maxTurns)
    }

    fun part1(input: List<Int>) =
        nextTurn(
            input.dropLast(1).mapIndexed { i, it -> it to i }.toMap().toMutableMap(),
            input.last(),
            input.size,
            2020
        )

    fun part2(input: List<Int>) =
        nextTurn(
            input.dropLast(1).mapIndexed { i, it -> it to i }.toMap().toMutableMap(),
            input.last(),
            input.size,
            30000000
        )
}