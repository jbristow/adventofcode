import util.AdventOfCode

object Day07 : AdventOfCode() {

    data class ValuesForRecursion(
        val input: List<String>,
        val path: List<String>,
        val files: Map<String, Long>
    )

    private tailrec fun processLine(
        input: List<String>,
        path: List<String> = listOf(),
        files: Map<String, Long> = emptyMap()
    ): Map<String, Long> {
        val head = input.firstOrNull() ?: return files
        val splitLine = head.split(" ")

        val nextValues = when (splitLine[1]) {
            "cd" -> handleCd(input, path, files, splitLine[2])
            else -> handleLs(input, path, files)
        }
        return processLine(nextValues.input, nextValues.path, nextValues.files)
    }

    private fun handleLs(
        input: List<String>,
        path: List<String>,
        files: Map<String, Long>
    ): ValuesForRecursion {
        val lsLines = input.drop(1)
            .takeWhile { !it.startsWith('$') }
            .toList()

        val fileSizes = lsLines.filter { it.matches("""\d+ .*""".toRegex()) }
            .sumOf { it.split(" ")[0].toLong() }

        val newSizes = path.drop(1)
            .runningFold(path.first()) { acc, curr -> "$acc/$curr" }
            .associateWith { p -> ((files[p] ?: 0) + fileSizes) }

        val nextInput = input.drop(1 + lsLines.size)
        val nextFiles = files + newSizes
        return ValuesForRecursion(nextInput, path, nextFiles)
    }

    private fun handleCd(
        input: List<String>,
        path: List<String>,
        files: Map<String, Long>,
        dir: String
    ): ValuesForRecursion {
        val nextPath = when (dir) {
            "/" -> listOf("root")
            ".." -> path.dropLast(1)
            else -> path + dir
        }
        return ValuesForRecursion(input.drop(1), nextPath, files)
    }

    fun part1(input: List<String>): Long {
        val files = processLine(input)
        return files.values.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Long? {
        val files = processLine(input)
        val usedSpace = files["root"]!!
        val neededSpace = 30000000 - (70000000 - usedSpace)
        return files.values.filter { it >= neededSpace }.minOrNull()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 7")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
