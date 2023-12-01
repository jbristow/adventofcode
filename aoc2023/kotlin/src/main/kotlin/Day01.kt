import util.AdventOfCode

object Day01 : AdventOfCode() {
    private fun part1(input: List<String>) =
        input.map { it.filter(Char::isDigit) }
            .sumOf { "${it.first()}${it.last()}".toInt() }

    private val numberRegex = """\d|(one|two|three|four|five|six|seven|eight|nine)""".toRegex()

    private fun part2(input: List<String>) =
        input.map { word ->
            word.indices.mapNotNull { numberRegex.find(word, startIndex = it)?.value }
        }.sumOf {
            "${it.first().asNumber()}${it.last().asNumber()}".toInt()
        }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun String.asNumber(): Int {
        return when (this) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            else -> this.toInt()
        }
    }
}



