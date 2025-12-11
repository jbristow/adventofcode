import util.AdventOfCode
import util.Point3dL
import java.util.PriorityQueue

object Day08 : AdventOfCode() {

    const val MAX_CONNECTIONS = 1000
    const val N_BIGGEST = 3

    data class Connection(
        val a: Point3dL,
        val b: Point3dL,
        val distance: Double = a.distance(b),
    ) : Comparable<Connection> {
        override fun compareTo(other: Connection): Int = distance.compareTo(other.distance)
        override fun hashCode(): Int = 17 * (a.hashCode() + b.hashCode())

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            val o = other as Connection
            return (a == o.a && b == o.b) || (a == o.b && b == o.a)
        }
    }

    data class LightString(
        val junctions: Set<Point3dL>,
    ) {
        constructor (initial: Connection) : this(setOf(initial.a, initial.b))

        val size = junctions.size

        fun add(other: Connection): LightString = copy(junctions = junctions + setOf(other.a, other.b))

        fun addAll(other: LightString): LightString = copy(junctions = junctions + other.junctions)

        operator fun contains(point: Point3dL): Boolean = point in junctions

        fun containsBoth(conn: Connection): Boolean = conn.a in this && conn.b in this
    }

    fun List<String>.toPoints(): List<Point3dL> =
        map { line ->
            line.split(",")
                .let { (x, y, z) -> Point3dL(x.toLong(), y.toLong(), z.toLong()) }
        }

    tailrec fun List<Point3dL>.findConnections(connections: List<Connection> = emptyList()): List<Connection> =
        if (isEmpty()) {
            connections
        } else {
            drop(1).findConnections(
                (drop(1).map { Connection(this.first(), it) } + connections)
                    .sorted()
                    .take(MAX_CONNECTIONS),
            )
        }

    private fun Connection.mergeWith(matching: List<LightString>) =
        if (matching.isEmpty()) {
            listOf(LightString(this))
        } else {
            listOf(matching.fold(LightString(this)) { acc, curr -> acc.addAll(curr) })
        }

    private tailrec fun List<Connection>.join(lightStrings: List<LightString> = emptyList()): List<LightString> {
        if (isEmpty()) {
            return lightStrings
        }
        val curr = this.first()
        val (matching, disjoint) = lightStrings.partition { curr.a in it || curr.b in it }
        return drop(1).join(disjoint + curr.mergeWith(matching))
    }

    fun part1(input: List<String>) =
        input.toPoints()
            .findConnections()
            .join()
            .map { it.size }
            .sortedDescending()
            .take(N_BIGGEST)
            .reduce { a, b -> a * b }

    fun List<Point3dL>.sortConnections(): PriorityQueue<Connection> {
        val pq = PriorityQueue<Connection>()
        for (i in indices) {
            for (j in (i + 1) until size) {
                pq.add(Connection(this[i], this[j]))
            }
        }
        return pq
    }

    private tailrec fun PriorityQueue<Connection>.connectAll(
        lightStrings: List<LightString> = emptyList(),
        lastConnection: Connection? = null,
        goal: Int,
        count: Int = 0,
    ): Connection {
        if (lightStrings.size == 1 && lightStrings.first().size == goal) {
            return lastConnection!!
        }
        val closestConnection = remove()
        val (matching, disjoint) = lightStrings.partition { closestConnection.a in it || closestConnection.b in it }
        return this.connectAll(closestConnection.mergeWith(matching) + disjoint, closestConnection, goal, count + 1)
    }

    private fun part2(input: List<String>) =
        input.toPoints()
            .sortConnections()
            .connectAll(goal = input.toPoints().size)
            .let { (a, b) -> (a.x * b.x) }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
