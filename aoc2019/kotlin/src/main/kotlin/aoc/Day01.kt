package aoc

import aoc.Day01.part1
import aoc.Day01.part2
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.max

object Day01 {
    private const val FILENAME = "src/main/resources/day01.txt"

    fun fuelNeeded(mass: Int) = max((mass / 3) - 2, 0)

    tailrec fun totalFuelNeeded(mass: Int, fuel: Option<Int> = none(), totalFuel: Int = 0): Int =
        when (val nextFuel = fuel.fold({ fuelNeeded(mass) }, ::fuelNeeded)) {
            0 -> totalFuel
            else -> totalFuelNeeded(mass, nextFuel.some(), totalFuel + nextFuel)
        }

    fun part1() = Files.readAllLines(Paths.get(FILENAME))
        .sumOf { fuelNeeded(it.toInt()) }
        .toString()

    fun part2() = Files.readAllLines(Paths.get(FILENAME))
        .sumOf { totalFuelNeeded(it.toInt()) }
        .toString()
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}
