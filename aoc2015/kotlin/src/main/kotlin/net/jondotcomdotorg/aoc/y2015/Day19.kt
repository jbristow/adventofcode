package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import java.util.PriorityQueue

object Day19 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day19:")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private val elementRe = Regex("""[A-Z][a-z]?""")
    private fun part1(input: List<String>): Int {
        val recipes =
            input.takeWhile { it.isNotEmpty() }
                .fold(mapOf()) { m: Map<String, List<String>>, line ->
                    val pieces = line.split(" => ")
                    m + (pieces[0] to ((m[pieces[0]] ?: listOf()).plusElement(pieces[1])))
                }

        val molecule = input.last()

        val afterReplacement = elementRe.findAll(molecule).flatMap { match ->
            val begin = molecule.take(match.range.first)
            val end = molecule.drop(match.range.last + 1)
            (recipes[match.value] ?: listOf()).map { "$begin$it$end" }
        }.distinct().count()

        return afterReplacement
    }

    data class Step(val step: Int = 0, val molecule: String) : Comparable<Step> {
        override fun compareTo(other: Step): Int {
            return if (other.molecule.length == this.molecule.length) {
                this.step.compareTo(other.step)
            } else {
                this.molecule.length.compareTo(other.molecule.length)
            }
        }
    }

    private fun part2(input: List<String>): Int? {
        val recipes =
            input.takeWhile { it.isNotEmpty() }
                .associate { it.split(" => ").let { (a, b) -> b to a } }

        val molecule = input.last()

        return reverseEngineer(recipes, PriorityQueue(listOf(Step(molecule = molecule))))
    }

    private fun reverseEngineer(
        recipes: Map<String, String>,
        pq: PriorityQueue<Step>
    ): Int? {
        if (pq.isEmpty()) {
            throw Exception("No way to make molecule.")
        }
        val current = pq.remove()
        if (current.molecule == "e") {
            return current.step
        }
        pq.addAll(
            recipes.filterKeys { it in current.molecule }
                .map { (k, v) ->
                    Step(step = current.step + 1, molecule = current.molecule.replaceFirst(k, v))
                }
        )
        return reverseEngineer(recipes, pq)
    }
}
