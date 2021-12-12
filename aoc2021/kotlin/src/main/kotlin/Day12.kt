import util.AdventOfCode

object Day12 : AdventOfCode() {

    internal class Graph(edges: List<List<String>>) {
        private val adjacencyMap =
            edges.flatMap { (a, b) -> listOf(a to b, b to a) }
                .groupBy { it.first }
                .mapValues { (_, v) -> v.map { it.second } }

        fun countPaths(root: String, target: String, explored: Set<String> = setOf()): Int =
            when {
                root == target -> 1
                root in explored && root.isSmallCave() -> 0
                else -> adjacencyMap[root]!!.sumOf { countPaths(it, target, explored + root) }
            }

        fun countPathsComplex(
            root: String,
            target: String,
            explored: Set<String> = setOf(),
            hasExploredTwice: Boolean = false
        ): Int {
            if (root == target) {
                return 1
            }

            if (root.isSmallCave() && root in explored) {
                when {
                    root == "start" -> return 0
                    hasExploredTwice -> return 0
                }
            }

            val nextExplored = when {
                root.isSmallCave() && root !in explored -> (explored + root)
                else -> explored
            }
            val nextExploredTwice = when {
                root.isSmallCave() && root in explored -> true
                else -> hasExploredTwice
            }

            return adjacencyMap[root]!!.sumOf { countPathsComplex(it, target, nextExplored, nextExploredTwice) }
        }

        private fun String.isSmallCave() = all(Char::isLowerCase)
    }

    fun part1(input: Sequence<String>) =
        Graph(input.map { it.split("-") }.toList()).countPaths("start", "end")

    fun part2(input: Sequence<String>) =
        Graph(input.map { it.split("-") }.toList()).countPathsComplex("start", "end")

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
