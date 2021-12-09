import util.AdventOfCode
import util.Point2d

object Day09 : AdventOfCode() {

    fun part1(input: List<String>): Int {
        val data = input.asSequence()
            .flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point2d(x, y) to c.digitToInt() } }
            .toMap()

        val lows = data.filter { (k, v) -> k.orthoNeighbors.mapNotNull { np -> data[np] }.all { h -> h > v } }
        return lows.values.sumOf { it + 1 }
    }

    private tailrec fun basinFinder(
        data: MutableMap<Point2d, LowPoint>,
        points: List<Point2d>,
        basins: MutableMap<Int, MutableList<Point2d>> = mutableMapOf(),
        lastBasinId: Int = -1
    ): Map<Point2d, LowPoint> {
        if (points.isEmpty()) {
            return data
        }

        val currentPoint = points.first()
        val remainingPoints = points.drop(1)
        val neighborBasins =
            currentPoint.orthoNeighbors.mapNotNull { data[it]?.basinId }.sorted().distinct()
        when {
            neighborBasins.isEmpty() -> {
                data[currentPoint]!!.basinId = lastBasinId + 1
                basins[lastBasinId + 1] = mutableListOf(currentPoint)
                return basinFinder(data, remainingPoints, basins, lastBasinId + 1)
            }
            else -> {
                val currentBasinId = neighborBasins.first()
                data[currentPoint]!!.basinId = currentBasinId

                val currentBasinPoints = basins[currentBasinId]!!
                currentBasinPoints.add(currentPoint)

                neighborBasins.drop(1).forEach {
                    val basinPoints = basins.remove(it)!!
                    currentBasinPoints.addAll(basinPoints)
                    basinPoints.forEach { p -> data[p]!!.basinId = currentBasinId }
                }
                return basinFinder(data, remainingPoints, basins, lastBasinId)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val data = input.asSequence()
            .flatMapIndexed { y, s ->
                s.mapIndexed { x, c -> Point2d(x, y) to c.digitToInt() }
                    .filter { it.second < 9 }
                    .map { it.first to LowPoint() }
            }
            .toMap()

        return basinFinder(data.toMutableMap(), data.keys.toList()).values.groupBy { it.basinId }
            .map { (_, v) -> v.count() }
            .sorted()
            .takeLast(3)
            .reduce { a, b -> a * b }
    }

    data class LowPoint(var basinId: Int? = null)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 9")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
