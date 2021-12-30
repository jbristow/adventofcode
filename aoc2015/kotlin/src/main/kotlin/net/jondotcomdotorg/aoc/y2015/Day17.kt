package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day17 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 17")
        println("\tPart 1: ${part1()}")
        println("\tPart 2: ${part2()}")
    }

    data class Container(val id: Int, val size: Int)

    private val containers = inputFileLineSequence.mapIndexed { i, it -> Container(i, it.toInt()) }.toList()

    data class Containment(val eggnogLeft: Int = 0, val containers: Set<Container> = emptySet())

    private fun part1(): Int {
        return fillContainers().count()
    }

    private tailrec fun fillContainers(
        possible: List<Containment> = listOf(Containment(150)),
        ways: List<Containment> = emptyList()
    ): List<Containment> {
        if (possible.isEmpty()) {
            return ways
        }

        val current = possible.first()
        val remaining = possible.drop(1)

        return when (current.eggnogLeft) {
            0 -> when (current) {
                !in ways -> fillContainers(remaining, ways + current)
                else -> fillContainers(remaining, ways)
            }
            else -> {
                val newContainers =
                    containers.filter { it !in current.containers && it.size <= current.eggnogLeft }.map {
                        current.copy(
                            eggnogLeft = current.eggnogLeft - it.size,
                            containers = current.containers + it
                        )
                    }.filter { it !in remaining }
                when (newContainers.isEmpty()) {
                    true -> fillContainers(remaining, ways)
                    else -> fillContainers(remaining + newContainers, ways)
                }
            }
        }
    }

    private fun part2(): Int {
        val containers = fillContainers()
        val fewest = containers.minOf { it.containers.size }
        return containers.filter { it.containers.size == fewest }.count()
    }
}
