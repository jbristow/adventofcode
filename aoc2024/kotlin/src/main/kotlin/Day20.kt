import util.AdventOfCode
import util.Point2d

object Day20 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 20")
        val cheatEngine = CheatEngine(inputFileLines)
        println("\tPart 1: ${part1(cheatEngine)}")
        println("\tPart 2: ${part2(cheatEngine)}")
    }

    class CheatEngine(
        input: List<String>,
    ) {
        val track = parse(input)
        val start = track.asSequence().find { it.value is Track.Start }!!.key
        val end = track.asSequence().find { it.value is Track.End }!!.key
        val outbound = Day16.distMap(start, track.keys, { it.orthoNeighbors.filter { n -> n in track } }).first
        val inbound = Day16.distMap(end, track.keys, { it.orthoNeighbors.filter { n -> n in track } }).first
        val noCheat = outbound.getValue(end)

        fun cheat(noclipPicoseconds: Int): Int =
            track.keys.asSequence().flatMap { p1 ->
                track.keys.asSequence().filter { p2 -> p1.manhattanDistance(p2) <= noclipPicoseconds }
                    .map { p2 -> outbound.getValue(p1) + inbound.getValue(p2) + p1.manhattanDistance(p2) }
            }.map { noCheat - it }.count { it >= 100 }
    }

    private fun part1(cheatEngine: CheatEngine): Int = cheatEngine.cheat(2)

    private fun part2(cheatEngine: CheatEngine): Int = cheatEngine.cheat(20)

    sealed interface Track {
        data object Start : Track
        data object End : Track
        data object Empty : Track
    }

    private fun parse(input: List<String>): Map<Point2d, Track> =
        input.flatMapIndexed { y: Int, line: String ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    'S' -> Point2d(x, y) to Track.Start
                    'E' -> Point2d(x, y) to Track.End
                    '.' -> Point2d(x, y) to Track.Empty
                    else -> null
                }
            }
        }.toMap()
}
