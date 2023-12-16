import Day16.Direction.East
import Day16.Direction.North
import Day16.Direction.South
import Day16.Direction.West
import util.AdventOfCode
import util.Point2d
import util.Point2dRange
import java.util.LinkedList

object Day16 : AdventOfCode() {
    sealed class Direction(val offset: Point2d) {
        data object North : Direction(Point2d(0, -1))

        data object East : Direction(Point2d(1, 0))

        data object South : Direction(Point2d(0, 1))

        data object West : Direction(Point2d(-1, 0))
    }

    sealed interface MapTile {
        val label: String

        fun output(incoming: Direction): List<Direction>
    }

    data object EmptyTile : MapTile {
        override val label: String = "."

        override fun output(incoming: Direction) = listOf(incoming)
    }

    sealed class Mirror(override val label: String, private val reflectMap: Map<Direction, Direction>) : MapTile {
        data object SWtoNE : Mirror(
            "/",
            mapOf(
                East to North,
                West to South,
                North to East,
                South to West,
            ),
        )

        data object NWtoSE : Mirror(
            "\\",
            mapOf(
                East to South,
                West to North,
                North to West,
                South to East,
            ),
        )

        override fun output(incoming: Direction): List<Direction> = listOf(reflectMap.getValue(incoming))
    }

    sealed class Splitter(override val label: String, private val splitMap: Map<Direction, List<Direction>>) : MapTile {
        data object Vertical : Splitter(
            "|",
            mapOf(
                East to listOf(North, South),
                West to listOf(North, South),
                North to listOf(North),
                South to listOf(South),
            ),
        )

        data object Horizontal : Splitter(
            "-",
            mapOf(
                East to listOf(East),
                West to listOf(West),
                North to listOf(East, West),
                South to listOf(East, West),
            ),
        )

        override fun output(incoming: Direction): List<Direction> = splitMap.getValue(incoming)
    }

    data class Beam(val position: Point2d = Point2d(0, 0), val direction: Direction = East)

    private fun List<String>.toTiles(): Map<Point2d, MapTile> =
        flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                Point2d(x, y) to
                    when (c) {
                        '|' -> Splitter.Vertical
                        '-' -> Splitter.Horizontal
                        '/' -> Mirror.SWtoNE
                        '\\' -> Mirror.NWtoSE
                        else -> EmptyTile
                    }
            }
        }.toMap()

    private fun part1(input: List<String>) = energize(input.toTiles(), LinkedList(listOf(Beam()))).size

    private tailrec fun energize(
        map: Map<Point2d, MapTile>,
        beams: LinkedList<Beam>,
        energized: MutableSet<Point2d> = mutableSetOf(),
        seen: MutableSet<Beam> = mutableSetOf(),
    ): MutableSet<Point2d> {
        if (beams.isEmpty()) {
            return energized
        }

        val current = beams.removeFirst()
        map[current.position]?.let { tile ->
            energized.add(current.position)
            seen.add(current)
            val newBeams =
                tile.output(
                    current.direction,
                ).map { Beam(position = current.position + it.offset, direction = it) }.filter { it !in seen }
            beams.addAll(newBeams)
        }
        return energize(map, beams, energized, seen)
    }

    private fun part2(input: List<String>): Int {
        val map = input.toTiles()
        val pRange = Point2dRange(map)

        return sequenceOf(
            pRange.xRange.map { Point2d(it, 0) }.map { Beam(it, South) },
            pRange.xRange.map { Point2d(it, pRange.xRange.last) }.map { Beam(it, North) },
            pRange.yRange.map { Point2d(0, it) }.map { Beam(it, East) },
            pRange.yRange.map { Point2d(pRange.xRange.last, it) }.map { Beam(it, West) },
        ).flatten().map { energize(map, LinkedList(listOf(it))) }.map { it.size }.maxOf { it }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 16")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
