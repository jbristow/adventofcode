import util.AdventOfCode

object Day10Alt : AdventOfCode("day10.txt") {
    @JvmStatic
    fun main(args: Array<String>) {
        val code = inputFileLineSequence.flatMap {
            val split = it.split(" ")
            when (split[0]) {
                "noop" -> sequenceOf(0)
                else -> sequenceOf(0, split[1].toInt())
            }
        }.runningFold(1, Int::plus).toList()
        println(
            code.asSequence().withIndex()
                .drop(19)
                .chunked(40)
                .map { it.first() }
                .sumOf { (it.index + 1) * it.value }
        )

        println(
            code.windowed(40, 40, partialWindows = false)
                .joinToString("\n") { row ->
                    row.withIndex().joinToString("") {
                        if (it.value >= it.index - 1 && it.value <= it.index + 1) "#" else "."
                    }
                }
        )
    }
}
