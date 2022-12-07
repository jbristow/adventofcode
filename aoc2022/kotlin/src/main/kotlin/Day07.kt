import util.AdventOfCode

object Day07 : AdventOfCode() {

    private tailrec fun processLine(
        input: List<String>,
        path: List<String> = listOf("root"),
        files: Map<String, Long> = emptyMap()
    ): Map<String, Long> {
        val head = input.firstOrNull() ?: return files
        val splitLine = head.split(" ")
        return when (splitLine[1]) {
            "cd" -> {
                when (val dir = splitLine[2]) {
                    "/" -> processLine(input.drop(1), listOf("root"), files)
                    ".." -> processLine(input.drop(1), path.dropLast(1), files)
                    else -> processLine(input.drop(1), path + dir, files)
                }
            }
            else -> {
                val lsLines = input.drop(1).takeWhile { !it.startsWith('$') }.toList()
                val fileSizes = lsLines.filter { it.matches("""\d+ .*""".toRegex()) }
                    .sumOf { it.split(" ")[0].toLong() }

                val newSizes = path.drop(1).runningFold(path.first()) { acc, curr -> "$acc/$curr" }
                    .associateWith { p -> ((files[p] ?: 0) + fileSizes) }


                processLine(input.drop(1 + lsLines.size), path, files + newSizes)
            }
        }
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
