package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import java.util.PriorityQueue

object Day13 : AdventOfCode() {
    val sample = """Alice would gain 54 happiness units by sitting next to Bob.
Alice would lose 79 happiness units by sitting next to Carol.
Alice would lose 2 happiness units by sitting next to David.
Bob would gain 83 happiness units by sitting next to Alice.
Bob would lose 7 happiness units by sitting next to Carol.
Bob would lose 63 happiness units by sitting next to David.
Carol would lose 62 happiness units by sitting next to Alice.
Carol would gain 60 happiness units by sitting next to Bob.
Carol would gain 55 happiness units by sitting next to David.
David would gain 46 happiness units by sitting next to Alice.
David would lose 7 happiness units by sitting next to Bob.
David would gain 41 happiness units by sitting next to Carol.""".lineSequence()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    val seatingRe = Regex("""(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+)\.""")

    private fun part1(inputFile: Sequence<String>): Int? {
        val happiness = inputFile.toHappinessChart()

        return maximizeHappiness(happiness, PriorityQueue(happiness.keys.map { Table(listOf(it)) }))?.happiness
    }

    private tailrec fun maximizeHappiness(
        chart: Map<String, Map<String, Int>>,
        pq: PriorityQueue<Table>,
        answer: Table = Table()
    ): Table? {
        if (pq.isEmpty()) {
            return answer
        }

        val current = pq.remove()
        if (current.seats.size == chart.keys.size && current.happiness > answer.happiness) {
            return maximizeHappiness(chart, pq, current)
        } else if (current.seats.size == chart.keys.size) {
            return maximizeHappiness(chart, pq, answer)
        }
        pq.addAll(
            chart.getValue(current.seats.last())
                .filter { it.key !in current.seats }
                .map {
                    current.copy(
                        seats = current.seats + it.key,
                        happiness = (current.seats + it.key).calculateHappiness(chart)
                    )
                }
        )
        return maximizeHappiness(chart, pq, answer)
    }

    data class Table(val seats: List<String> = listOf(), val happiness: Int = 0) : Comparable<Table> {
        override fun compareTo(other: Table): Int {
            return if (this.happiness == other.happiness) {
                this.seats.size.compareTo(other.seats.size)
            } else {
                this.happiness.compareTo(other.happiness)
            }
        }
    }

    fun List<String>.calculateHappiness(chart: Map<String, Map<String, Int>>): Int {
        if (this.size < 2) {
            return 0
        }

        return (this.takeLast(1) + this + this.first()).windowed(3, 1).sumOf { (l, p, r) ->
            chart.getValue(p).getValue(l) +
                chart.getValue(p).getValue(r)
        }
    }

    private fun part2(inputFile: Sequence<String>): Int? {
        val happiness = inputFile.toHappinessChart()
        val withMe =
            happiness.mapValues { (_, v) -> v + ("Me" to 0) } + ("Me" to (happiness.keys.map { it to 0 }).toMap())

        return maximizeHappiness(withMe, PriorityQueue(withMe.keys.map { Table(listOf(it)) }))?.happiness
    }

    private fun Sequence<String>.toHappinessChart() =
        mapNotNull { seatingRe.matchEntire(it) }
            .map {
                when (it.groupValues[2]) {
                    "gain" -> it.groupValues[1] to (it.groupValues[4] to it.groupValues[3].toInt())
                    else -> it.groupValues[1] to (it.groupValues[4] to -it.groupValues[3].toInt())
                }
            }
            .groupBy { it.first }
            .mapValues { (_, v) -> v.map { it.second }.toMap() }.toMap()
}
