import util.AdventOfCode
import util.Point2d

object Day11 : AdventOfCode() {

    private fun mapOctopi(input: List<String>) =
        input.flatMapIndexed { y, row -> row.mapIndexed { x, c -> Point2d(x, y) to c.digitToInt() } }.toMap()

    private tailrec fun takeSteps(octopi: Map<Point2d, Int>, stepsLeft: Int, flashes: Int = 0): Int {
        if (stepsLeft == 0) {
            return flashes
        }

        val nextOctopi = octopi.mapValues { it.value + 1 }.toMutableMap()

        val flashPoints = flash(nextOctopi, nextOctopi.filter { it.value > 9 }.keys)

        flashPoints.forEach { p -> nextOctopi[p] = 0 }

        return takeSteps(nextOctopi, stepsLeft - 1, flashes + flashPoints.count())
    }

    private tailrec fun flash(
        nextOctopi: MutableMap<Point2d, Int>,
        flashing: Set<Point2d>,
        flashed: Set<Point2d> = setOf()
    ): Set<Point2d> {
        if (flashing.isEmpty()) {
            return flashed
        }

        val nextFlashed = flashed + flashing

        flashing.flatMap { it.neighbors - nextFlashed }
            .filter { neighbor -> neighbor in nextOctopi }
            .forEach { neighbor ->
                nextOctopi[neighbor] = nextOctopi[neighbor]!! + 1
            }

        return flash(nextOctopi, nextOctopi.filterValues { it > 9 }.keys - nextFlashed, nextFlashed)
    }

    fun part1(input: List<String>) = takeSteps(mapOctopi(input), 100)

    private tailrec fun waitForAllFlash(octopi: Map<Point2d, Int>, steps: Int = 0, flashes: Int = 0): Int {
        if (flashes == octopi.size) {
            return steps
        }

        val nextOctopi = octopi.mapValues { it.value + 1 }.toMutableMap()

        val flashPoints = flash(nextOctopi, nextOctopi.filter { it.value > 9 }.keys)

        flashPoints.forEach { nextOctopi[it] = 0 }

        return waitForAllFlash(nextOctopi, steps + 1, flashPoints.count())
    }

    fun part2(input: List<String>) = waitForAllFlash(mapOctopi(input))

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 11")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
