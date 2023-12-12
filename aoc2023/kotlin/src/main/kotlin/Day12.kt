import arrow.core.memoize
import util.AdventOfCode

object Day12 : AdventOfCode() {
    private fun toPossibilities(start: String): Sequence<String> {
        val possibilties = mutableSetOf(start)
        return sequence {
            while (possibilties.isNotEmpty()) {
                val current = possibilties.first()
                possibilties.remove(current)
                if ('?' !in current) {
                    yield(current)
                } else {
                    val (a, b) = current.split("?", limit = 2)

                    val dot = "$a.$b"
                    val hash = "$a#$b"
                    possibilties.add(dot)
                    possibilties.add(hash)
                }
            }
        }
    }

    private fun part1(input: List<String>): Int {
        return input.sumOf {
            val (record, lens) = it.split(" ").let { (r, l) -> r to l.split(",").map { c -> c.toInt() } }
            val finalRe = lens.joinToString("""\.+""", prefix = """\.*""", postfix = """\.*""") { l -> """#{$l}""" }.toRegex()
            toPossibilities(record).count { p -> p.matches(finalRe) }
        }
    }

    data class SpringDiagram(val diagram: String, val chunkLengths: List<Int>) {
        private fun stepSpring(
            currentChunkSize: Int,
            diagramIndex: Int,
            chunkLengthIndex: Int,
        ) = when {
            // No chunks left to contain this spring, reject
            chunkLengthIndex >= chunkLengths.size -> 0
            // Adding this spring makes the current chunk too big, reject
            currentChunkSize >= chunkLengths[chunkLengthIndex] -> 0
            // Add this spring to the current chunk and move to the next char
            else -> countPossibilities(currentChunkSize + 1, diagramIndex + 1, chunkLengthIndex)
        }

        private fun stepEmpty(
            currentChunkSize: Int,
            diagramIndex: Int,
            chunkLengthIndex: Int,
        ) = when (currentChunkSize) {
            // we haven't started a chunk yet, move onto the next character
            0 -> countPossibilities(0, diagramIndex + 1, chunkLengthIndex)
            // we completed the current chunk, reset the size counter, increment which chunk we're looking at and move to the next char
            chunkLengths[chunkLengthIndex] -> countPossibilities(0, diagramIndex + 1, chunkLengthIndex + 1)
            // chunk is too small, reject
            else -> 0
        }

        private fun countPossibilitiesRaw(
            currentChunkSize: Int,
            diagramIndex: Int,
            chunkLengthIndex: Int,
        ): Long {
            return if (diagramIndex == diagram.length) {
                if (currentChunkSize == 0 && chunkLengthIndex == chunkLengths.size) {
                    1
                } else {
                    0
                }
            } else {
                when (diagram[diagramIndex]) {
                    '.' -> stepEmpty(currentChunkSize, diagramIndex, chunkLengthIndex)
                    '#' -> stepSpring(currentChunkSize, diagramIndex, chunkLengthIndex)
                    '?' ->
                        stepSpring(currentChunkSize, diagramIndex, chunkLengthIndex) +
                            stepEmpty(currentChunkSize, diagramIndex, chunkLengthIndex)

                    else -> throw Exception("unknown char: ${diagram[diagramIndex]}")
                }
            }
        }

        // where the magic happens, thanks arrow-kt
        val countPossibilities = ::countPossibilitiesRaw.memoize()
    }

    private fun part2(input: List<String>): Long {
        return input.map { line ->
            val (record, chunkLens) = line.split(" ").let { (r, l) -> r to l.split(",").map { c -> c.toInt() } }

            // add a '.' to the end to force any open chunks closed
            val fiveXRecord = (0..<5).joinToString("?", postfix = ".") { record }
            val fiveXChunks = (0..<5).flatMap { chunkLens }

            SpringDiagram(fiveXRecord, fiveXChunks)
        }.sumOf {
            it.countPossibilities(0, 0, 0)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
