import util.AdventOfCode

object Day02 : AdventOfCode() {

    data class GameResult(val id: Long, val blue: Long, val red: Long, val green: Long) {
        fun isValidFor(blue: Long, red: Long, green: Long): Boolean =
            this.blue <= blue && this.red <= red && this.green <= green

        val power: Long
            get() = blue * red * green

    }

    private fun String.toGameResult(): GameResult {
        val (label, results) = this.split(": ")
        val (_, id) = label.split(" ")
        val pulls = results.split("[;,] ".toRegex())
            .map { it.toCubePull() }
            .groupBy({ (k, _) -> k }, { (_, v) -> v })
            .mapValues { (_, v) -> v.max() }

        return GameResult(
            id = id.toLong(),
            blue = pulls.getValue("blue"),
            red = pulls.getValue("red"),
            green = pulls.getValue("green")
        )
    }

    private fun String.toCubePull() = this.split(" ").let { (n, color) -> color to n.toLong() }

    private fun part1(input: List<String>) =
        input.map { it.toGameResult() }
            .filter { it.isValidFor(red = 12, green = 13, blue = 14) }
            .sumOf(GameResult::id)

    private fun part2(input: List<String>) = input.map { it.toGameResult() }.sumOf(GameResult::power)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}