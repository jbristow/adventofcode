import util.AdventOfCode
import util.Djikstra.djikstra
import util.Point2d
import java.util.PriorityQueue
import javax.sound.midi.Track

object Day20 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 20")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun part1(input: List<String>): Int {
        val track = parse(input)
        val start = track.asSequence().filter { it.value is Track.Start }.first().key
        val end = track.asSequence().filter { it.value is Track.End }.first().key
        println("$start -> $end")
        val noCheats = djikstra(
            start = start,
            isEnd = { it == end },
            q = track.keys,
            neighborFn = { it.orthoNeighbors.filter { n -> n in track } }
        )!!.first
        println(noCheats)
        val cheater = Cheats(track, start, end, noCheats)
        println(cheater.race().count { noCheats - it.steps >= 100 })
        return -1
    }

    private fun part2(input: List<String>): Int {
        return -1
    }

    sealed interface Track {
        data object Start : Track
        data object End : Track
        data object Empty : Track
    }

    private fun parse(input: List<String>): Map<Point2d, Track> {

        return input.flatMapIndexed { y: Int, line: String ->
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

    data class State(
        val steps: Int,
        val position: Point2d,
        val hasCheated:Boolean = false,
        val seen: Set<Point2d> = setOf(position)
    ) : Comparable<State> {
        override fun compareTo(other: State): Int {
            return this.steps.compareTo(other.steps)
        }

    }

    private class Cheats(val track: Map<Point2d, Track>, val start: Point2d, val end: Point2d, val maxSteps: Int) {

        tailrec fun race(
            queue: PriorityQueue<State> = PriorityQueue(setOf(State(0, start))),
            finished: Set<State> = emptySet(),
        ): Set<State> {
            if (queue.isEmpty()) {
                return finished
            }
            val currState = queue.remove()
            if (queue.size % 1000 == 0) {
                println("${queue.size}/${finished.size} = $currState")
            }
            if (currState.position == end) {
                return race(queue, finished + currState)
            }
            if (currState.steps >= maxSteps) {
                return race(queue, finished)
            }

            if (!currState.hasCheated) {
                val nextCheat = currState.position.orthoNeighbors.flatMap { n -> n.orthoNeighbors }.filter {
                    it.manhattanDistance(currState.position) == 2L &&
                            it != currState.position
                    it in track
                    it !in currState.seen
                }.map {
                    currState.copy(
                        steps = currState.steps + 2,
                        position = it,
                        hasCheated = true,
                        seen = currState.seen + it
                    )
                }
                queue.addAll(nextCheat)
            }
            val nextLegal = currState.position.orthoNeighbors.filter { it in track && it !in currState.seen }
                .map { currState.copy(steps = currState.steps + 1, position = it, seen = currState.seen + it) }
            queue.addAll(nextLegal)
            return race(queue, finished)
        }
    }
}
