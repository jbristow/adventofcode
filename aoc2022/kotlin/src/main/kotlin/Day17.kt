import Day17.RockShape.Box
import Day17.RockShape.Cross
import Day17.RockShape.Dash
import Day17.RockShape.Ell
import Day17.RockShape.Pipe
import util.AdventOfCode
import util.Point2dL
import util.Point2dLRange

object Day17 : AdventOfCode() {

    sealed interface RockShape {
        fun moveDown(): RockShape
        fun moveRight(): RockShape
        fun moveLeft(): RockShape

        fun getPushed(wind: Char, rocks: Set<Point2dL>, minX: Long = 0, maxX: Long = 6): RockShape {
            val movedShape = when (wind) {
                '>' -> this.moveRight()
                '<' -> this.moveLeft()
                else -> throw IllegalArgumentException("Bad wind: $wind")
            }
            return if (movedShape.rocks.any { it.x !in minX..maxX || it in rocks }) {
                this
            } else {
                movedShape
            }
        }

        val rocks: Set<Point2dL>
        val bottomLine: Long
        val leftCol: Long

        data class Dash(override val bottomLine: Long = 0, override val leftCol: Long = 0) : RockShape {
            override fun moveDown() = this.copy(bottomLine = bottomLine + 1)
            override fun moveRight() = this.copy(leftCol = leftCol + 1)
            override fun moveLeft() = this.copy(leftCol = leftCol - 1)

            override val rocks
                get() = (0..3).map { x -> Point2dL(x + leftCol, bottomLine) }.toSet()
        }

        data class Cross(override val bottomLine: Long = 0, override val leftCol: Long = 0) : RockShape {
            override fun moveDown() = this.copy(bottomLine = bottomLine + 1)
            override fun moveRight() = this.copy(leftCol = leftCol + 1)
            override fun moveLeft() = this.copy(leftCol = leftCol - 1)

            override val rocks
                get() = (0..2).map { x -> Point2dL(x + leftCol, bottomLine - 1) }.toSet() + setOf(
                    Point2dL(1 + leftCol, bottomLine),
                    Point2dL(1 + leftCol, bottomLine - 2)
                )
        }

        data class Ell(override val bottomLine: Long = 0, override val leftCol: Long = 0) : RockShape {
            override fun moveDown() = this.copy(bottomLine = bottomLine + 1)
            override fun moveRight() = this.copy(leftCol = leftCol + 1)
            override fun moveLeft() = this.copy(leftCol = leftCol - 1)

            override val rocks
                get() = (0..2).map { x -> Point2dL(x + leftCol, bottomLine) }.toSet() + setOf(
                    Point2dL(2 + leftCol, bottomLine - 1),
                    Point2dL(2 + leftCol, bottomLine - 2)
                )
        }

        data class Pipe(override val bottomLine: Long = 0, override val leftCol: Long = 0) : RockShape {
            override fun moveDown() = this.copy(bottomLine = bottomLine + 1)
            override fun moveRight() = this.copy(leftCol = leftCol + 1)
            override fun moveLeft() = this.copy(leftCol = leftCol - 1)

            override val rocks
                get() = (bottomLine - 3..bottomLine).map { y -> Point2dL(leftCol, y) }.toSet()
        }

        data class Box(override val bottomLine: Long = 0, override val leftCol: Long = 0) : RockShape {
            override fun moveDown() = this.copy(bottomLine = bottomLine + 1)
            override fun moveRight() = this.copy(leftCol = leftCol + 1)
            override fun moveLeft() = this.copy(leftCol = leftCol - 1)

            override val rocks
                get() = (-1..0).flatMap { y -> (0..1).map { x -> Point2dL(x + leftCol, y + bottomLine) } }.toSet()
        }
    }

    private val rockOrder = listOf<(Long) -> RockShape>(
        { Dash(it - 3L, 2) },
        { Cross(it - 3, 2) },
        { Ell(it - 3, 2) },
        { Pipe(it - 3, 2) },
        { Box(it - 3, 2) }
    )

    private fun <T> List<T>.cycle() = generateSequence(0) { (it + 1) % this.size }.map { IndexedValue(it, this[it]) }
    private val xCoords = (0..6L)

    private fun part1(input: String): Long {
        val rocks = xCoords.map { Point2dL(it, 0) }.toMutableSet()
        return allRocksFall(rockOrder.cycle(), input.toList().cycle(), rocks)
    }

    private fun part2(input: String): Long {
        val rocks = xCoords.map { Point2dL(it, 0) }.toMutableSet()
        return allRocksFall(rockOrder.cycle(), input.toList().cycle(), rocks, maxRocks = 1_000_000_000_000L)
    }

    data class RockFallState(
        val windSequence: Sequence<IndexedValue<Char>>,
        val rocks: Set<Point2dL>,
        val rockShape: RockShape
    )

    data class Snapshot(
        val rocks: String,
        val minY: Long,
        val finishedRocks: Long,
        val previous: Snapshot? = null
    ) {
        fun matches(snapshot: Snapshot?): Boolean {
            if (snapshot == null) {
                return false
            }
            return this.rocks == snapshot.rocks
        }
    }

    private tailrec fun allRocksFall(
        rockSequence: Sequence<IndexedValue<(Long) -> RockShape>>,
        windSequence: Sequence<IndexedValue<Char>>,
        rocks: MutableSet<Point2dL>,
        minY: Long = -1,
        finishedRocks: Long = 0,
        maxRocks: Long = 2022,
        snapshots: MutableMap<Pair<Int, Int>, Snapshot> = mutableMapOf()
    ): Long {
        if (finishedRocks == maxRocks) {
            return (-minY) - 1
        }

        val snapshotKey = (rockSequence.first().index) to (windSequence.first().index)
        val snapshotValue = takeSnapshot(rocks, minY, finishedRocks)
        val existingSnapshot = snapshots[snapshotKey]

        if (snapshotValue.matches(existingSnapshot)) {
            val rocksRemaining = maxRocks - existingSnapshot!!.finishedRocks
            val rockDelta = snapshotValue.finishedRocks - existingSnapshot.finishedRocks
            if (rocksRemaining % rockDelta == 0L) {
                val ydiff = snapshotValue.minY - existingSnapshot.minY
                val quotient = rocksRemaining / rockDelta
                return -(quotient * ydiff + existingSnapshot.minY) - 1
            }
        }

        snapshots[snapshotKey] = snapshotValue

        val landed = singleRockFalls(RockFallState(windSequence, rocks, rockSequence.first().value(minY)))
        rocks.addAll(landed.rockShape.rocks)

        return allRocksFall(
            rockSequence.drop(1),
            landed.windSequence,
            rocks,
            rocks.minOf { it.y } - 1,
            finishedRocks + 1,
            maxRocks,
            snapshots
        )
    }

    private fun takeSnapshot(
        rocks: MutableSet<Point2dL>,
        minY: Long,
        finishedRocks: Long
    ) = Snapshot(
        Point2dLRange(rocks.filter { it.y == minY + 1 }).joinToString { if (it in rocks) "#" else "." },
        minY,
        finishedRocks
    )

    private tailrec fun singleRockFalls(state: RockFallState): RockFallState {
        val wind = state.windSequence.first()
        val rockShapePushed = state.rockShape.getPushed(wind.value, state.rocks)

        val rockShapeDown = rockShapePushed.moveDown()
        return if (rockShapeDown.rocks.any { it in state.rocks }) {
            return RockFallState(state.windSequence.drop(1), state.rocks, rockShapePushed)
        } else {
            singleRockFalls(
                RockFallState(
                    state.windSequence.drop(1),
                    state.rocks,
                    rockShapeDown
                )
            )
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 17:")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
