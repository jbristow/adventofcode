import util.AdventOfCode

object Day13 : AdventOfCode() {
    private fun List<String>.toRows() = this.map { pattern -> pattern.lines() }

    private fun List<String>.toCols() =
        this.map { pattern -> pattern.lines() }
            .map { pattern ->
                (0..<(pattern.maxOf { line -> line.length }))
                    .map { pattern.joinToString("") { line -> "${line[it]}" } }
            }

    private fun List<Int>.summarizeHorizontal() = this.sumOf { 100 * (it + 1) }

    private fun List<Int>.summarizeVertical() = this.sumOf { it + 1 }

    private fun findReflectionCandidateIndices(pattern: List<String>) =
        pattern.windowed(2).withIndex().filter { iv -> iv.value[0] == iv.value[1] }.map { iv -> iv.index }

    private tailrec fun checkMirrorness(
        pattern: List<String>,
        i: Int,
        j: Int,
    ): Boolean {
        if (i == 0 || j + 1 == pattern.size) {
            return pattern[i] == pattern[j]
        }

        if (pattern[i] == pattern[j]) {
            return checkMirrorness(pattern, i - 1, j + 1)
        }
        return false
    }

    private fun part1(input: String): Int {
        val chunks = input.split("\n\n")

        val hValue =
            chunks.toRows().mapNotNull { pattern: List<String> ->
                findReflectionCandidateIndices(
                    pattern,
                ).firstOrNull { i -> checkMirrorness(pattern, i, i + 1) }
            }.summarizeHorizontal()
        val vValue =
            chunks.toCols().mapNotNull { pattern: List<String> ->
                findReflectionCandidateIndices(pattern).firstOrNull { i ->
                    checkMirrorness(pattern, i, i + 1)
                }
            }.summarizeVertical()

        return hValue + vValue
    }

    private tailrec fun findBlemish(
        pattern: List<String>,
        i: Int,
        j: Int,
        blemishFound: Boolean = false,
    ): Boolean {
        val differences = pattern[i].zip(pattern[j]).count { (a, b) -> a != b }

        if (i == 0 || j + 1 == pattern.size) {
            return (differences == 0 && blemishFound) || (differences == 1 && !blemishFound)
        }

        return when {
            differences == 1 && !blemishFound -> findBlemish(pattern, i - 1, j + 1, true)
            blemishFound && differences != 0 -> false
            differences == 0 -> findBlemish(pattern, i - 1, j + 1, blemishFound)
            else -> false
        }
    }

    private fun part2(input: String): Int {
        val chunks = input.split("\n\n")

        val hValue =
            chunks.toRows()
                .flatMap { pattern: List<String> ->
                    (0..<pattern.size - 1).filter { i -> findBlemish(pattern, i, i + 1) }
                }.summarizeHorizontal()
        val vValue =
            chunks.toCols()
                .flatMap { pattern: List<String> ->
                    (0..<pattern.size - 1).filter { i -> findBlemish(pattern, i, i + 1) }
                }.summarizeVertical()

        return hValue + vValue
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 1")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
