import Day10.destruction
import Day10.mostVisible
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.*

object Day10 {

    private fun slope(a: Point, b: Point) = (a.y - b.y).toDouble() / (a.x - b.x).toDouble()

    private fun distance(a: Point, b: Point) = sqrt(
        abs(a.x.toDouble() - b.x.toDouble()).pow(2.0)
                + abs(a.y.toDouble() - b.y.toDouble()).pow(2.0)
    )

    private fun List<String>.allAsteroids() =
        mapIndexed { y, row -> row.mapIndexed { x, c -> (x to y) to c } }
            .flatten()
            .filter { (_, v) -> v == '#' }
            .map { it.first }

    fun mostVisible(data: List<Point>) =
        data.map { station ->
            val others = data.filter { it != station }
            val right = others.filter { it.y == station.y && it.x > station.x }
            val left = others.filter { it.y == station.y && it.x < station.x }
            val down = others.filter { it.x == station.x && it.y > station.y }
            val up = others.filter { it.x == station.x && it.y < station.y }
            val upRight = others.filter { it.x > station.x && it.y < station.y }.groupBySlope(station)
            val downRight = others.filter { it.x > station.x && it.y > station.y }.groupBySlope(station)
            val upLeft = others.filter { it.x < station.x && it.y < station.y }.groupBySlope(station)
            val downLeft = others.filter { it.x < station.x && it.y > station.y }.groupBySlope(station)

            station to (
                    min(1, right.size) +
                            min(1, left.size) +
                            min(1, down.size) +
                            min(1, up.size) +
                            upRight.sumBy { min(1, it.second.size) } +
                            upLeft.sumBy { min(1, it.second.size) } +
                            downRight.sumBy { min(1, it.second.size) } +
                            downLeft.sumBy { min(1, it.second.size) }

                    )
        }.maxBy { it.second }

    private fun List<Point>.groupBySlope(station: Point) =
        map { it to slope(station, it) }
            .groupBy { it.second }.toList()

    private const val FILENAME = "src/main/resources/day10.txt"
    val fileData = Files.readAllLines(Paths.get(FILENAME)).allAsteroids()

    fun destruction(data: List<Point>): Int {
        val station = 20 to 20
        val others = data.filter { it != station }
        val right = others.filter { it.y == station.y && it.x > station.x }
            .sortedBy { distance(it, station) }
        val left = others.filter { it.y == station.y && it.x < station.x }
            .sortedBy { distance(it, station) }
        val down = others.filter { it.x == station.x && it.y > station.y }
            .sortedBy { distance(it, station) }
        val up = others.filter { it.x == station.x && it.y < station.y }
            .sortedBy { distance(it, station) }
        val upRight = others.filter { it.x > station.x && it.y < station.y }
            .map { p -> (atan(slope(p, station)) + PI / 2) to p }
            .groupBy { it.first }
            .map { (k, v) -> k to v.map { it.second }.sortedBy { distance(it, station) } }

        val downRight = others.filter { it.x > station.x && it.y > station.y }
            .map { p -> (atan(slope(p, station)) + PI / 2) to p }
            .groupBy { it.first }
            .map { (k, v) -> k to v.map { it.second }.sortedBy { distance(it, station) } }
        val upLeft = others.filter { it.x < station.x && it.y < station.y }
            .map { p -> (atan(slope(p, station)) + 3 * PI / 2) to p }
            .groupBy { it.first }
            .map { (k, v) -> k to v.map { it.second }.sortedBy { distance(it, station) } }

        val downLeft = others.filter { it.x < station.x && it.y > station.y }
            .map { p -> (atan(slope(p, station)) + 3 * PI / 2) to p }
            .groupBy { it.first }
            .map { (k, v) -> k to v.map { it.second }.sortedBy { distance(it, station) } }

        val groupedAsteroids = listOf(
            0.0 to up,
            PI to down,
            PI / 2 to right,
            3 * PI / 2 to left
        ) + upLeft + upRight + downLeft + downRight

        val biggestList = groupedAsteroids.maxBy { it.second.size }?.second?.size ?: 0
        return (0..biggestList).fold(emptyList<Point>() to groupedAsteroids.sortedBy { it.first }) { (destroyed, remaining), i ->
            (destroyed + remaining.flatMap { it.second.take(1) }) to (remaining.map { it.first to it.second.drop(1) })
        }.first.let {

            it[199].x * 100 + it[199].y
        }
    }
}


fun main() {
    println(mostVisible(Day10.fileData))
    println(destruction(Day10.fileData))
}