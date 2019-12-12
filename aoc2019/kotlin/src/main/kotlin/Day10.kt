import Day10.destruction
import Day10.mostVisible
import arrow.core.andThen
import arrow.optics.optics
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

@optics
data class Point(val x: Int, val y: Int) {
    companion object
}

private fun Point.slope(b: Point) = (y - b.y).toDouble() / (x - b.x).toDouble()
private fun Point.distance(b: Point) =
    sqrt(abs(x.toDouble() - b.x.toDouble()).pow(2.0) + abs(y.toDouble() - b.y.toDouble()).pow(2.0))

private fun Point.anglePair(entry: Map.Entry<EuclideanGrouping, List<Point>>) = entry.key.anglePair(this, entry.value)

private fun Point.countByGroup(entry: Map.Entry<EuclideanGrouping, List<Point>>) = entry.key.count(this, entry.value)

sealed class EuclideanGrouping
object Down : EuclideanGrouping()
object DownLeft : EuclideanGrouping()
object DownRight : EuclideanGrouping()
object Left : EuclideanGrouping()
object Right : EuclideanGrouping()
object Up : EuclideanGrouping()
object UpLeft : EuclideanGrouping()
object UpRight : EuclideanGrouping()

fun Point.mapEuclideanGroup(other: Point): EuclideanGrouping {
    return when {
        other.x > x && other.y > y -> DownRight
        other.x > x && other.y < y -> UpRight
        other.x > x && other.y == y -> Right
        other.x < x && other.y > y -> DownLeft
        other.x < x && other.y < y -> UpLeft
        other.x < x && other.y == y -> Left
        other.x == x && other.y > y -> Down
        other.x == x && other.y < y -> Up
        else -> throw Error("Invalid point comparison: $this to $other")
    }
}

fun Point.sortByDistanceFromKeyValue(entry: Map.Entry<Double, List<Point>>) =
    entry.key to entry.value.sortedBy(this::distance)

fun Point.radiansConverter(offset: Double) = { p: Point -> (atan(p.slope(this)) + offset) }

private fun EuclideanGrouping.anglePair(station: Point, list: List<Point>) =
    when (this) {
        Up -> listOf(0.0 to list)
        Down -> listOf(PI to list)
        DownLeft, UpLeft -> list.groupBy(station.radiansConverter(3 * PI / 2))
            .map(station::sortByDistanceFromKeyValue)
            .toList()
        DownRight, UpRight -> list.groupBy(station.radiansConverter(PI / 2))
            .map(station::sortByDistanceFromKeyValue)
            .toList()
        Left -> listOf(3 * PI / 2 to list)
        Right -> listOf(PI / 2 to list)
    }

fun EuclideanGrouping.count(station: Point, list: List<Point>) = when (this) {
    Up, Down, Left, Right -> list.take(1).count()
    DownLeft, DownRight, UpLeft, UpRight -> list.groupBy { station.slope(it) }.count { (_, v) -> v.any() }
}

object Day10 {

    private const val FILENAME = "src/main/resources/day10.txt"
    val fileData = Files.readAllLines(Paths.get(FILENAME)).onlyAsteroids()

    private fun List<String>.onlyAsteroids() =
        mapIndexed { y, row -> row.mapIndexed { x, c -> Point(x, y) to c } }.flatten()
            .filter(Pair<Point, Char>::second andThen '#'::equals)
            .map(Pair<Point, Char>::first)

    fun mostVisible(data: List<Point>) =
        data.map { station ->
            station to data.asSequence().filterNot(station::equals).groupBy(station::mapEuclideanGroup).asSequence()
                .sumBy(station::countByGroup)
        }.maxBy { it.second }

    fun destruction(data: List<Point>): Int {
        val station = Point(20, 20)
        val groupedAsteroids =
            data.asSequence().filterNot(station::equals)
                .groupBy(station::mapEuclideanGroup)
                .flatMap(station::anglePair)

        val initial = emptyList<Point>() to groupedAsteroids.sortedBy { it.first }
        return (0..(groupedAsteroids.maxBy { it.second.size }?.second?.size ?: 0))
            .fold(initial) { (destroyed, remaining), _ ->
                val nextDestroyed = destroyed + remaining.flatMap { (_, list) -> list.take(1) }
                val nextRemaining = remaining.map { (angle, list) -> angle to list.drop(1) }
                nextDestroyed to nextRemaining
            }.first[199]
            .let { it.x * 100 + it.y }
    }
}

fun main() {
    println(mostVisible(Day10.fileData))
    println(destruction(Day10.fileData))
}
