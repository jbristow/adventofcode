package net.jondotcomdotorg.aoc.y2015

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import util.AdventOfCode
import java.io.StringReader

object Day12 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12:")
        println("\tPart 1: ${part1(inputFile)}")
        println("\tPart 2: ${part2(inputFile)}")
    }

    private fun part1(input: String) =
        Regex("""-?\d+""")
            .findAll(input)
            .sumOf { it.groupValues[0].toLong() }

    private fun part2(input: String) =
        nonRedSum(Parser.default().parse(StringReader(input)))

    private fun nonRedSum(json: Any?): Long =
        when (json) {
            is JsonObject -> when {
                "red" in json.values -> 0L
                else -> json.values.sumOf { nonRedSum(it) }
            }
            is JsonArray<*> -> json.sumOf { nonRedSum(it) }
            is String -> 0
            is Int -> json.toLong()
            is Long -> json
            else -> 0L
        }
}
