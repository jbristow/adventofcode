package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import kotlin.math.max

object Day15 : AdventOfCode() {
    /*
        Frosting:     capacity  4, durability -2, flavor  0, texture 0, calories 5
        Candy:        capacity  0, durability  5, flavor -1, texture 0, calories 8
        Butterscotch: capacity -1, durability  0, flavor  5, texture 0, calories 6
        Sugar:        capacity  0, durability  0, flavor -2, texture 2, calories 1
      */
    private val ingredientPossibilities: Sequence<List<Int>>
        get() = (1..100).asSequence().flatMap { frosting ->
            (1..(100 - frosting)).asSequence().flatMap { candy ->
                (1..(100 - frosting - candy)).asSequence().flatMap { butterscotch ->
                    (1..(100 - frosting - candy - butterscotch)).asSequence().map { sugar ->
                        listOf(frosting, candy, butterscotch, sugar)
                    }
                }
            }
        }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 15")
        println("\tPart 1: ${part1()}")
        println("\tPart 2: ${part2()}")
    }

    private fun cookieScore(ingredients: List<Int>): Long {
        val (frosting, candy, butterscotch, sugar) = ingredients
        return max(0, (4L * frosting - butterscotch)) *
            max(0, (5L * candy - 2L * frosting)) *
            max(0, (5L * butterscotch - 2L * sugar - candy)) *
            max(0, 2L * sugar)
    }

    private fun part1() = ingredientPossibilities.map { cookieScore(it) }.maxOf { it }

    private fun part2() =
        ingredientPossibilities
            .filter { (frosting, candy, butterscotch, sugar) -> 5 * frosting + 8 * candy + 6 * butterscotch + sugar == 500 }
            .map { cookieScore(it) }.maxOf { it }
}
