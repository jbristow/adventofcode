package utility

import kotlin.math.absoluteValue

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    override fun compareTo(other: Point) = when (x) {
        other.x -> y.compareTo(other.y)
        else -> x.compareTo(other.x)
    }
}

fun Point.manhattanDistance(b: Point): Int =
    (x - b.x).absoluteValue + (y - b.y).absoluteValue

operator fun Point.minus(b: Point): Point = Point(x - b.x, y - b.y)
operator fun Point.plus(b: Point): Point = Point(x + b.x, y + b.y)
operator fun Point.times(b: Int): Point = Point(x * b, y * b)

fun Pair<Point, Point>.allPoints(): List<List<Point>> {
    return this.let { (tl, br) ->
        (tl.x..br.x).map { x ->
            (tl.y..br.y).map { y ->
                Point(x, y)
            }
        }
    }
}

fun Iterable<Point>.minX() =
    minBy { (x, _) -> x }?.x

fun Iterable<Point>.minY() =
    minBy { (_, y) -> y }?.y

fun Iterable<Point>.maxX() =
    maxBy { (x, _) -> x }?.x

fun Iterable<Point>.maxY() =
    maxBy { (_, y) -> y }?.y



