import util.AdventOfCode

object Day03 : AdventOfCode() {

    val mul_re = """mul\((\d+),\s*(\d+)\)""".toRegex()

    val all_re = """(mul|do|don't)\((?:\d+,\s*\d+)?\)""".toRegex()

    fun part1(input: String): Long {
        val ops = mul_re.findAll(input)
        return ops.sumOf { multiply(it) }
    }

    private fun multiply(it: MatchResult): Long {
        return it.groupValues[1].toLong() * it.groupValues[2].toLong()
    }

    fun part2(input: String): Long {
        val ops = all_re.findAll(input)
        return ops.map { it.groupValues }.fold(0L to true) { (sum, shouldI), match ->
                when (match[1]) {
                    "do" -> sum to true
                    "don't" -> sum to false
                    else -> {
                        if (shouldI) {
                            val parsed = mul_re.find(match[0])
                            (sum + multiply(parsed!!)) to true
                        } else {
                            sum to false
                        }
                    }
                }
            }.first

    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 3")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }

}
