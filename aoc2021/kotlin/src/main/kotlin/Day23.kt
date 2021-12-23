import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.manhattanDistance
import java.util.PriorityQueue
import java.util.UUID

object Day23 : AdventOfCode() {

    sealed class Amphipod(val type: String, val energy: Int, val home: Int, val id: UUID = UUID.randomUUID()) {
        class Amber : Amphipod("A", 1, 3)
        class Bronze : Amphipod("B", 10, 5)
        class Copper : Amphipod("C", 100, 7)
        class Desert : Amphipod("D", 1000, 9)

        override fun toString(): String {
            return type
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Amphipod) return false
            if (!this::class.isInstance(other::class)) return false
            return this.id == other.id
        }
    }

    private val Amphipod.home2: Point2d get() = Point2d(home, 2)
    private val Amphipod.home3: Point2d get() = Point2d(home, 3)
    private val Amphipod.home4: Point2d get() = Point2d(home, 4)
    private val Amphipod.home5: Point2d get() = Point2d(home, 5)

    data class HallState(val positions: Map<Amphipod, Point2d>, val energySpent: Long = 0) : Comparable<HallState> {
        override fun compareTo(other: HallState) = energySpent.compareTo(other.energySpent)
    }

    private val roomSpots = setOf(
        Point2d(3, 2),
        Point2d(3, 3),
        Point2d(5, 2),
        Point2d(5, 3),
        Point2d(7, 2),
        Point2d(7, 3),
        Point2d(9, 2),
        Point2d(9, 3)
    )
    private val roomSpots2 = setOf(
        Point2d(3, 2), Point2d(3, 3), Point2d(3, 4), Point2d(3, 5),
        Point2d(5, 2), Point2d(5, 3), Point2d(5, 4), Point2d(5, 5),
        Point2d(7, 2), Point2d(7, 3), Point2d(7, 4), Point2d(7, 5),
        Point2d(9, 2), Point2d(9, 3), Point2d(9, 4), Point2d(9, 5)
    )
    private val hallwaySpots = setOf(
        Point2d(1, 1),
        Point2d(2, 1),
        Point2d(4, 1),
        Point2d(6, 1),
        Point2d(8, 1),
        Point2d(10, 1),
        Point2d(11, 1),
        Point2d(1, 1),
        Point2d(2, 1),
        Point2d(4, 1),
        Point2d(6, 1),
        Point2d(8, 1),
        Point2d(10, 1),
        Point2d(11, 1)
    )

    private val possibleMovesOut = roomSpots.associateWith { hallwaySpots }
    private val possibleMovesOut2 = roomSpots2.associateWith { hallwaySpots }

    fun part1(input: List<String>): Long {
        return shufflePod(PriorityQueue(listOf(HallState(input.findAmphipods())))).energySpent
    }

    private fun Set<Point2d>.filterCollisions(
        amph: Amphipod,
        pos: Point2d,
        occHallway: Map<Point2d, Amphipod>
    ): Set<Point2d> {
        return filter { newPos ->
            val och = occHallway.filter { (p, a) -> pos != p && a != amph }.keys
            if (newPos.x < pos.x) {
                och.none { it.x in newPos.x..pos.x }
            } else if (newPos.x > pos.x) {
                och.none { it.x in pos.x..newPos.x }
            } else {
                true
            }
        }.toSet()
    }

    private tailrec fun shufflePod(
        priorityQueue: PriorityQueue<HallState>,
        seen: MutableSet<Map<Amphipod, Point2d>> = mutableSetOf()
    ): HallState {
        if (priorityQueue.isEmpty()) {
            throw Exception("No solutions.")
        }

        val current = priorityQueue.remove()
        if (current.positions.all { (k, v) -> k.home == v.x }) {
            return current
        }

        if (current.positions in seen) {
            return shufflePod(priorityQueue, seen)
        }
        seen.add(current.positions)
        val invmap = current.positions.map { (k, v) -> v to k }.toMap()
        val occHallway = invmap.filterKeys { k -> k in hallwaySpots }

        current.positions.forEach { (amph, pos) ->
            val moves = when {
                pos in roomSpots && pos == amph.home2 && invmap[amph.home3]?.type == amph.type -> setOf()
                pos in roomSpots && pos == amph.home3 -> setOf()
                pos in roomSpots && pos.y == 3 && pos.copy(y = 2) in invmap -> setOf()
                pos in roomSpots -> possibleMovesOut[pos]!!
                pos in hallwaySpots &&
                    amph.home2 !in invmap &&
                    amph.home3 !in invmap -> setOf(amph.home3)
                pos in hallwaySpots &&
                    amph.home2 !in invmap &&
                    invmap[amph.home3]?.type == amph.type -> setOf(amph.home2)
                else -> setOf()
            }.filterCollisions(amph, pos, occHallway)

            addAllNewMoves(moves, current, amph, pos, seen, priorityQueue)
        }
        return shufflePod(priorityQueue, seen)
    }

    private tailrec fun shufflePod2(
        priorityQueue: PriorityQueue<HallState>,
        seen: MutableSet<Map<Amphipod, Point2d>> = mutableSetOf()
    ): HallState {
        if (priorityQueue.isEmpty()) {
            throw Exception("No solutions.")
        }

        val current = priorityQueue.remove()
        if (current.positions.all { (k, v) -> k.home == v.x }) {
            return current
        }

        if (current.positions in seen) {
            return shufflePod2(priorityQueue, seen)
        }
        seen.add(current.positions)

        val invmap = current.positions.map { (k, v) -> v to k }.toMap()
        val occHallway = invmap.filterKeys { k -> k in hallwaySpots }

        current.positions.forEach { (amph, pos) ->
            val moves = when (pos) {
                in roomSpots2 -> handleRoomSpot2(pos, amph, invmap)
                in hallwaySpots -> handleHallwaySpot2(amph, invmap, pos)
                else -> setOf()
            }.filterCollisions(amph, pos, occHallway)
            addAllNewMoves(moves, current, amph, pos, seen, priorityQueue)
        }
        return shufflePod2(priorityQueue, seen)
    }

    private fun addAllNewMoves(
        moves: Set<Point2d>,
        current: HallState,
        amph: Amphipod,
        pos: Point2d,
        seen: MutableSet<Map<Amphipod, Point2d>>,
        priorityQueue: PriorityQueue<HallState>
    ) {
        moves.forEach { move ->
            val newMap = current.positions.toMutableMap()
            newMap[amph] = move

            val newHallState = HallState(
                newMap,
                current.energySpent + pos.manhattanDistance(move) * amph.energy
            )
            if (newHallState.positions !in seen) {
                priorityQueue.add(newHallState)
            }
        }
    }

    private fun handleHallwaySpot2(
        amph: Amphipod,
        invmap: Map<Point2d, Amphipod>,
        pos: Point2d
    ) = when {
        amph.home2 !in invmap &&
            amph.home3 !in invmap &&
            amph.home4 !in invmap &&
            amph.home5 !in invmap -> setOf(amph.home5)
        pos in hallwaySpots &&
            amph.home2 !in invmap &&
            amph.home3 !in invmap &&
            amph.home4 !in invmap &&
            invmap[amph.home5]?.type == amph.type -> setOf(amph.home4)
        pos in hallwaySpots &&
            amph.home2 !in invmap &&
            amph.home3 !in invmap &&
            invmap[amph.home4]?.type == amph.type &&
            invmap[amph.home5]?.type == amph.type -> setOf(amph.home3)
        pos in hallwaySpots &&
            amph.home2 !in invmap &&
            invmap[amph.home3]?.type == amph.type &&
            invmap[amph.home4]?.type == amph.type &&
            invmap[amph.home5]?.type == amph.type -> setOf(amph.home2)
        else -> setOf()
    }

    private fun handleRoomSpot2(
        pos: Point2d,
        amph: Amphipod,
        invmap: Map<Point2d, Amphipod>
    ) = when {
        pos == amph.home5 -> setOf()
        pos == amph.home4 &&
            invmap[amph.home5]?.type == amph.type -> setOf()
        pos == amph.home3 &&
            invmap[amph.home4]?.type == amph.type &&
            invmap[amph.home5]?.type == amph.type -> setOf()
        pos == amph.home2 &&
            invmap[amph.home3]?.type == amph.type &&
            invmap[amph.home4]?.type == amph.type &&
            invmap[amph.home5]?.type == amph.type -> setOf()
        pos.y == 3 && pos.copy(y = 2) in invmap -> setOf()
        pos.y == 4 && pos.copy(y = 3) in invmap -> setOf()
        pos.y == 5 && pos.copy(y = 4) in invmap -> setOf()
        else -> possibleMovesOut2[pos]!!
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 22")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun part2(input: List<String>): Long {
        val unfolded = input.take(3) + "  #D#C#B#A#\n  #D#B#A#C#".lines() + input.drop(3)
        val amphipods = unfolded.findAmphipods()
        return shufflePod2(PriorityQueue(listOf(HallState(amphipods)))).energySpent
    }

    private fun List<String>.findAmphipods(): Map<Amphipod, Point2d> {
        return flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    'A' -> Amphipod.Amber() to Point2d(x, y)
                    'B' -> Amphipod.Bronze() to Point2d(x, y)
                    'C' -> Amphipod.Copper() to Point2d(x, y)
                    'D' -> Amphipod.Desert() to Point2d(x, y)
                    else -> null
                }
            }
        }.toMap()
    }
}
