import util.AdventOfCode

object Day19 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 18")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun part1(lines: List<String>): Int {
        val (towels, patterns) = parse(lines)
        val solver = Solver(towels)
        return patterns.count { solver.possible(it) }
    }

    private fun part2(lines: List<String>): Long {
        val (towels, patterns) = parse(lines)
        val solver = Solver(towels)
        return patterns.sumOf { solver.possibilities(it) }
    }

    private fun parse(strings: List<String>): Pair<List<String>, List<String>> {
        val towels = strings[0].split(""",\s+""".toRegex())
        val patterns = strings.drop(2)
        return towels to patterns
    }

    class Solver(
        private val towels: List<String>,
    ) {
        val cachePossible = mutableMapOf<String, Boolean>()
        val cacheTotal = mutableMapOf<String, Long>()
        fun possible(pattern: String): Boolean {
            if (pattern.isEmpty()) {
                return true
            }
            if (pattern in cachePossible) {
                return cachePossible.getValue(pattern)
            }
            val validTowels = towels.filter { pattern.startsWith(it) }
            if (validTowels.isEmpty()) {
                return false
            }
            return validTowels.any {
                val nextPattern = pattern.substring(it.length)
                val isPossible = possible(nextPattern)
                cachePossible[nextPattern] = isPossible
                isPossible
            }
        }

        fun possibilities(pattern: String): Long {
            if (pattern.isEmpty()) {
                return 1
            }
            if (pattern in cacheTotal) {
                return cacheTotal.getValue(pattern)
            }
            val validTowels = towels.filter { pattern.startsWith(it) }
            if (validTowels.isEmpty()) {
                return 0
            }
            return validTowels.sumOf {
                val nextPattern = pattern.substring(it.length)
                val subtotal = possibilities(nextPattern)
                cacheTotal[nextPattern] = subtotal
                subtotal
            }
        }
    }
}
