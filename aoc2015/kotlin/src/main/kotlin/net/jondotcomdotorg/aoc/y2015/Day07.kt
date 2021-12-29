package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day07 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day07:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    private val assignRe = Regex("""(\d+) -> ([a-z]+)""")
    private val operateRe = Regex("""(\d+) ([A-Z]+) (\d+) -> ([a-z]+)""")
    private val negateRe = Regex("""NOT (\d+) -> ([a-z]+)""")

    private fun part1(input: Sequence<String>): String? {
        val x = process(input)
        return x["a"]
    }

    private fun part2(input: Sequence<String>): String? {
        val x = process(
            input.map {
                if (it.endsWith("-> b")) {
                    "3176 -> b"
                } else it
            }
        )
        return x["a"]
    }

    private tailrec fun process(input: Sequence<String>, assignments: List<String> = listOf()): Map<String, String> {
        val (assignT, assignF) = input.partition { assignRe.matches(it) }

        val newAssignments = assignT.fold(assignF) { amended, text ->
            val match = assignRe.matchEntire(text)!!
            amended.map { Regex("""\b${match.groupValues[2]}\b""").replace(it, match.groupValues[1]) }
        }.asSequence()

        if (assignT.isEmpty()) {
            return assignments.associate { it.split(" -> ").let { (v, k) -> k to v } }
        }

        val operations = newAssignments.map { text ->
            val match = operateRe.matchEntire(text)
            if (match == null) {
                text
            } else {
                val a = match.groupValues[1].toUShort()
                val b = match.groupValues[3].toUShort()
                val c = match.groupValues[4]
                val result = when (match.groupValues[2]) {
                    "AND" -> a and b
                    "OR" -> a or b
                    "LSHIFT" -> a shl b
                    "RSHIFT" -> a shr b
                    else -> throw IllegalArgumentException("Unknown operator in $text")
                }
                "$result -> $c"
            }
        }.asSequence()

        val nots = operations.map { text ->
            val match = negateRe.matchEntire(text)
            if (match == null) {
                text
            } else {
                val a = match.groupValues[1].toUShort()
                val b = match.groupValues[2]
                "${a.inv()} -> $b"
            }
        }.asSequence()

        return process(nots, assignments + assignT)
    }

    private infix fun UShort.shl(b: UShort): UShort {
        return this.toInt().shl(b.toInt()).toUShort()
    }

    private infix fun UShort.shr(b: UShort): UShort {
        return this.toInt().shr(b.toInt()).toUShort()
    }
}
