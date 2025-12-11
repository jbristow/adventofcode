import util.AdventOfCode

object Day01 : AdventOfCode() {
    private const val DIAL_SIZE = 100
    private const val DIAL_START_POSITION = 50

    private fun part1(input: List<String>) =
        input.map { line -> line.first() to line.substring(1).toInt() }
            .map { (direction, amount) ->
                when (direction) {
                    'L' -> -1
                    else -> 1
                } * amount % DIAL_SIZE
            }
            .runningFold(DIAL_START_POSITION) { current, amount ->
                val nextAmt = current + amount
                when {
                    nextAmt < 0 -> nextAmt + DIAL_SIZE
                    else -> nextAmt
                } % DIAL_SIZE
            }.count { current -> current == 0 }

    private fun part2(input: List<String>) =
        input.map { line -> line.first() to line.substring(1).toInt() }
            .fold(0 to DIAL_START_POSITION) { (zeroCount, current), (direction, amount) ->
                val fullRotations = amount / DIAL_SIZE
                val netTurn = amount - DIAL_SIZE * fullRotations
                when (direction) {
                    'L' -> {
                        val next = current - netTurn
                        when {
                            next > 0 -> zeroCount + fullRotations to next
                            next == 0 -> zeroCount + fullRotations + 1 to 0
                            current == 0 -> zeroCount + fullRotations to next + DIAL_SIZE
                            else -> zeroCount + fullRotations + 1 to next + DIAL_SIZE
                        }
                    }

                    else -> {
                        val next = current + netTurn
                        if (next < DIAL_SIZE) {
                            zeroCount + fullRotations to next
                        } else {
                            zeroCount + fullRotations + 1 to next % DIAL_SIZE
                        }
                    }
                }
            }.first

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
