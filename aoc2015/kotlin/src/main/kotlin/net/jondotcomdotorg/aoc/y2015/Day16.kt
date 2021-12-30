package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day16 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 16")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    private val tickerTape = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )

    data class Sue(val id: Int, val traits: Map<String, Int>)

    private val sueRe = Regex("""Sue (\d+): """)
    private fun String.toSue(): Sue {
        val idMatch = sueRe.find(this, 0)!!
        val id = idMatch.groupValues[1].toInt()
        val traits = this.substring(idMatch.range.last + 1).split(", ").map {
            it.split(": ").let { (a, b) -> a to b.toInt() }
        }.toMap()
        return Sue(id, traits)
    }

    private fun part1(input: Sequence<String>): Int {
        return applyClues(tickerTape.toList(), input.map { it.toSue() }).first().id
    }

    private tailrec fun applyClues(clues: List<Pair<String, Int>>, sues: Sequence<Day16.Sue>): Sequence<Sue> {
        if (clues.isEmpty()) {
            return sues
        }
        val (trait, count) = clues.first()

        return applyClues(
            clues.drop(1),
            sues.filter { trait !in it.traits || it.traits[trait] == count }
        )
    }

    private val traitRangeGreater = setOf("cats", "trees")
    private val traitRangeLesser = setOf("pomeranians", "goldfish")

    private tailrec fun applyCluesCurrent(clues: List<Pair<String, Int>>, sues: Sequence<Day16.Sue>): Sequence<Sue> {
        if (clues.isEmpty()) {
            return sues
        }
        val (trait, count) = clues.first()

        val remaining = clues.drop(1)
        val newSues = when (trait) {
            in traitRangeGreater -> sues.filter { trait !in it.traits || (it.traits[trait] ?: 0) > count }
            in traitRangeLesser -> sues.filter { trait !in it.traits || (it.traits[trait] ?: 0) < count }
            else -> sues.filter { trait !in it.traits || it.traits[trait] == count }
        }
        return applyCluesCurrent(remaining, newSues)
    }

    private fun part2(input: Sequence<String>): Int {
        return applyCluesCurrent(tickerTape.toList(), input.map { it.toSue() }).first().id
    }
}
