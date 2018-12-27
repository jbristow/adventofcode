package utility

import kotlin.math.absoluteValue

data class Point3d(val x: Long, val y: Long, val z: Long)
data class Point4d(val a1: Long, val a2: Long, val a3: Long, val a4: Long)

fun Point3d.manhattanDistance(b: Point3d): Long =
    (x - b.x).absoluteValue + (y - b.y).absoluteValue + (z - b.z).absoluteValue

fun Point4d.manhattanDistance(b: Point4d): Long =
    (a1 - b.a1).absoluteValue + (a2 - b.a2).absoluteValue + (a3 - b.a3).absoluteValue + (a4 - b.a4).absoluteValue

operator fun Point3d.minus(b: Point3d): Point3d =
    Point3d(x - b.x, y - b.y, z - b.z)

operator fun Point3d.plus(b: Point3d): Point3d =
    Point3d(x + b.x, y + b.y, z + b.z)

operator fun Point3d.times(b: Long) = Point3d(x * b, y * b, z * b)

operator fun Point3d.div(div: Int) = Point3d(x / div, y / div, z / div)

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    val down: Point get() = Point(x, y + 1)
    val up: Point get() = Point(x, y - 1)
    val left: Point get() = Point(x - 1, y)
    val right: Point get() = Point(x + 1, y)

    override fun compareTo(other: Point) = when (y) {
        other.y -> x.compareTo(other.x)
        else -> y.compareTo(other.y)
    }

    override fun toString(): String {
        return "($x,$y)"
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


operator fun <T> Map<Point, T>.get(x: Int, y: Int) = this[Point(x, y)]
