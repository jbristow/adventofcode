package net.jondotcomdotorg.aoc.y2015

import arrow.core.filterOption
import arrow.core.none
import arrow.core.some
import util.AdventOfCode
import java.util.PriorityQueue

object Day09 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 08:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    private fun Sequence<String>.toEdges(): Map<String, Map<String, Int>> {
        return map { it.split(" ") }.flatMap {
            listOf(
                it[0] to (it[2] to it[4].toInt()),
                it[2] to (it[0] to it[4].toInt())
            )
        }.groupBy { it.first }.map {
            it.key to it.value.map { v -> v.second }.toMap()
        }.toMap()
    }

    private fun part1(input: Sequence<String>): Int {
        val edges = input.toEdges()
        return shortestPath(edges, PriorityQueue(edges.keys.toInitialFlights())).distance
    }

    private fun part2(input: Sequence<String>): Int {
        val edges = input.toEdges()
        return longestPath(edges, PriorityQueue(edges.keys.toInitialFlights()))!!.distance
    }

    data class Flight(val stops: List<String>, val distance: Int) : Comparable<Flight> {
        override fun compareTo(other: Flight): Int {
            if (distance == other.distance) {
                return stops.size.compareTo(other.stops.size)
            }
            return distance.compareTo(other.distance)
        }
    }

    private fun Flight.addStop(stop: String, distance: Int) =
        when (stop) {
            in this.stops -> none()
            else -> copy(stops = stops + stop, distance = this.distance + distance).some()
        }

    private fun Iterable<String>.toInitialFlights() = this.map { Flight(listOf(it), 0) }

    private tailrec fun shortestPath(
        edges: Map<String, Map<String, Int>>,
        pq: PriorityQueue<Flight>
    ): Flight {
        if (pq.isEmpty()) {
            throw Exception("")
        }

        val current = pq.remove()
        if (current.stops.size == edges.keys.size) {
            return current
        }

        val lastStop = current.stops.last()

        pq.addAll(edges.getValue(lastStop).map { (k, v) -> current.addStop(k, v) }.filterOption())
        return shortestPath(edges, pq)
    }

    private tailrec fun longestPath(
        edges: Map<String, Map<String, Int>>,
        pq: PriorityQueue<Flight>,
        last: Flight? = null
    ): Flight? {
        if (pq.isEmpty()) {
            return last
        }

        val current = pq.remove()
        if (current.stops.size == edges.keys.size) {
            return longestPath(edges, pq, current)
        }

        val lastStop = current.stops.last()

        pq.addAll(edges.getValue(lastStop).map { (k, v) -> current.addStop(k, v) }.filterOption())
        return longestPath(edges, pq, last)
    }
}
