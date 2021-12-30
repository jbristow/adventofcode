package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import kotlin.math.min

object Day14 : AdventOfCode() {
    val sample = """Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.""".lineSequence()

    val raceTime = 2503L
    val reindeerRe = Regex("""(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""")

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 14")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    private fun part1(input: Sequence<String>): Long {
        return input.map { reindeerRe.matchEntire(it)!! }.maxOf {
            val speed = it.groupValues[2].toLong()
            val moveTime = it.groupValues[3].toLong()
            val restTime = it.groupValues[4].toLong()
            val fullCycles = raceTime / (moveTime + restTime)
            val partialCycle = min(moveTime, raceTime % (moveTime + restTime))
            speed * (fullCycles * moveTime + partialCycle)
        }
    }

    data class Reindeer(
        val name: String,
        val moveTime: Long,
        val cycleTime: Long,
        val speed: Long
    )

    fun Reindeer.distanceAt(sec: Long): Long {
        val fullCycles = sec / cycleTime
        val partialCycle = min(moveTime, sec % cycleTime)
        return speed * (fullCycles * moveTime + partialCycle)
    }

    private fun part2(input: Sequence<String>): Int {
        val reindeer = input.map { reindeerRe.matchEntire(it)!! }.map {
            val name = it.groupValues[1]
            val speed = it.groupValues[2].toLong()
            val moveTime = it.groupValues[3].toLong()
            val restTime = it.groupValues[4].toLong()
            Reindeer(name, moveTime, moveTime + restTime, speed)
        }.toList()

        val output = (1..raceTime).fold(listOf<String>()) { acc, i ->
            val runmap = reindeer.map { it.name to it.distanceAt(i) }.sortedBy { it.second }
            acc + runmap.filter { it.second == runmap.last().second }.map { it.first }
        }.groupBy { it }.mapValues { it.value.count() }
        return output.values.maxOf { it }
    }
}
