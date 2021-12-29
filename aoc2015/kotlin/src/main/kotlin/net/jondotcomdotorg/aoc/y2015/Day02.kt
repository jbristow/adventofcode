package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import kotlin.math.min

object Day02 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day02:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    data class Package(val l: Int, val w: Int, val h: Int)

    private val Package.paper: Long
        get() = 2L * l * w + 2L * w * h + 2 * h * l + min(min(l * w, w * h), h * l)

    private val Package.ribbon: Long
        get() = listOf(l + w, w + h, h + l).minOrNull()!! * 2L + l * w * h

    private fun String.toPackage(): Package {
        return split("x")
            .let { (a, b, c) -> Package(a.toInt(), b.toInt(), c.toInt()) }
    }

    private fun part1(input: Sequence<String>) =
        input.sumOf { it.toPackage().paper }

    private fun part2(input: Sequence<String>) =
        input.sumOf { it.toPackage().ribbon }
}
