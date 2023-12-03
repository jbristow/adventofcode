import util.AdventOfCode
import util.Point2d

object Day03 : AdventOfCode() {

    data class Part(val n: Long, val points: List<Point2d>) {
        val neighbors = points.flatMap { it.neighbors }.toSet()
    }

    private fun List<Pair<Char, Point2d>>.toPart(): Part {
        val n = this.joinToString("") { it.first.toString() }.toLong()
        val points = this.map { it.second }
        return Part(n, points)
    }

    private fun List<String>.toSymbolPoints(): Set<Point2d> {
        return this.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when {
                    c.isDigit() -> null
                    c == '.' -> null
                    else -> Point2d(x, y)
                }
            }
        }.toSet()
    }

    private tailrec fun String.toPartList(
        y: Int,
        x: Int = 0,
        seen: MutableList<Part> = mutableListOf(),
        soFar: MutableList<Pair<Char, Point2d>> = mutableListOf()
    ): List<Part> {
        if (this.isEmpty()) {
            if (soFar.isNotEmpty()) {
                seen.add(soFar.toPart())
            }
            return seen
        }

        val c = this[0]
        val next = this.drop(1)

        return when {
            c.isDigit() -> {
                soFar.add((c to Point2d(x, y)))
                next.toPartList(y, x + 1, seen, soFar)
            }

            soFar.isNotEmpty() -> {
                seen.add(soFar.toPart())
                next.toPartList(y, x + 1, seen)
            }

            else -> {
                next.toPartList(y, x + 1, seen)
            }
        }
    }

    private fun part1(input: List<String>): Long {
        val symbols = input.toSymbolPoints()
        val parts = input.flatMapIndexed { y, line -> line.toPartList(y) }

        return parts.filter { part -> part.neighbors.any { it in symbols } }
            .sumOf { part -> part.n }
    }


    private fun part2(input: List<String>): Long {
        val symbols = input.toSymbolPoints()
        val parts = input.flatMapIndexed { y, line -> line.toPartList(y) }

        return symbols.map { symbol -> parts.filter { part -> symbol in part.neighbors } }
            .filter { it.size == 2 }
            .sumOf { (a, b) -> a.n * b.n }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 3")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}