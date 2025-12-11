import util.AdventOfCode

object Day11 : AdventOfCode() {
    private fun List<String>.toReversePathMap(): Map<String, List<String>> {
        return flatMap { line ->
            line.split(": ")
                .let { splitLine ->
                    splitLine[1].split(" ").map { it to splitLine[0] }
                }
        }.groupBy({ it.first }, { it.second })
    }

    tailrec fun countBackwards(
        current: Map<String, Long>,
        paths: Map<String, List<String>>,
        pathCounts: MutableMap<String, Long> = current.toMap().toMutableMap(),
        seen: MutableSet<String> = mutableSetOf(),
    ): Map<String, Long> {
        val next = current.toList().fold(mutableMapOf<String, Long>()) { acc, (k, v) ->
            paths[k].orEmpty().forEach {
                pathCounts.merge(it, v) { old, v -> old + v }
                acc.merge(it, v) { old, v -> old + v }
            }
            acc
        }
        val label = next.keys.joinToString("-")
        return if (current.isEmpty()) {
            pathCounts
        } else if (label in seen) {
            println("LOOP DETECTED: `$label`")
            pathCounts
        } else {
            seen.add(label)
            countBackwards(next, paths, pathCounts, seen)
        }
    }

    private fun part1(lines: List<String>): Long {
        val paths = lines.flatMap { line ->
            line.split(": ")
                .let { splitLine ->
                    splitLine[1].split(" ").map { it to splitLine[0] }
                }
        }.groupBy({ it.first }, { it.second })
        val pathCounts = countBackwards(mapOf("out" to 1), paths)
        return pathCounts["you"] ?: -1L
    }

    fun countBackwardsMulti(
        multiPath: List<String>,
        paths: Map<String, List<String>>,
    ): Long =
        multiPath.windowed(2).map { (start, end) ->
            (countBackwards(mapOf(end to 1), paths)[start]) ?: 0.toLong()
        }.reduce { a, b -> a * b }

    private fun part2(lines: List<String>): Long {
        val paths = lines.toReversePathMap()
        val possibilities = listOf(
            listOf("svr", "fft", "dac", "out"),
            listOf("svr", "dac", "fft", "out"),
        )
        return possibilities.sumOf { countBackwardsMulti(it, paths) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 11")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
