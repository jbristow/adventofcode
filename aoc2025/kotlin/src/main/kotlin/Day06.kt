import util.AdventOfCode

object Day06 : AdventOfCode() {
    val spacesRegex = """\s+""".toRegex()
    val commaSpaceRegex = """,\s*""".toRegex()

    private fun part1(lines: List<String>): Long {
        val operations = lines.last().split(spacesRegex)
        val numbers = lines.dropLast(1)
            .map { line -> line.split(spacesRegex).map(String::toLong) }

        return operations.mapIndexed { i, operation ->
            when (operation) {
                "+" -> numbers.fold(0L) { acc, curr -> acc + curr[i] }
                "*" -> numbers.fold(1L) { acc, curr -> acc * curr[i] }
                else -> throw IllegalArgumentException("Unrecognized operation: $operation")
            }
        }.sum()
    }

    private fun part2(lines: List<String>): Long {
        val operations = lines.last().split(spacesRegex)

        val width = lines.first().length
        val numberInput = lines.dropLast(1)
        val numberGroups = (0 until width)
            .joinToString(",") { col -> numberInput.joinToString("") { line -> line[col].toString() } }
            .split(""", {${numberInput.size}},""".toRegex())
            .map { line -> line.split(commaSpaceRegex).map { it.trim().toLong() } }

        return operations.zip(numberGroups).sumOf { (operation, numbers) ->
            when (operation) {
                "+" -> numbers.reduce(Long::plus)
                "*" -> numbers.reduce(Long::times)
                else -> throw IllegalArgumentException("Unrecognized operation: $operation")
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
