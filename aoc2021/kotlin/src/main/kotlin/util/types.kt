package util

import kotlin.math.abs

interface Point {
    val neighbors: Set<Point>
    val orthoNeighbors: Set<Point>
}

class Point2dRange(val xRange: IntRange, val yRange: IntRange) : Iterable<Point2d> {
    constructor(minX: Int, maxX: Int, minY: Int, maxY: Int) : this((minX..maxX), (minY..maxY))

    constructor(points: Iterable<Point2d>) :
        this(
            points.minOf(Point2d::x)..points.maxOf(Point2d::x),
            points.minOf(Point2d::y)..points.maxOf(Point2d::y)
        )

    constructor(points: Map<Point2d, Any?>) : this(points.keys)

    override fun iterator(): Iterator<Point2d> {
        return yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point2d(x, y) } }.iterator()
    }

    fun joinToString(transform: ((Point2d) -> CharSequence)? = null): String {
        return yRange.joinToString("\n") { y ->
            xRange.asSequence().joinToString("") { x -> transform?.let { it(Point2d(x, y)) } ?: "." }
        }
    }
}

data class Point2d(val x: Int, val y: Int) : Point {
    companion object {
        operator fun Point2d.plus(other: Point2d) = Point2d(this.x + other.x, this.y + other.y)

        infix fun Point2d.modX(other: Int) = Point2d(x % other, y)

        fun Point2d.manhattanDistance(from: Point2d = Point2d(0, 0)): Long {
            return abs(x.toLong() - from.x) + abs(y.toLong() - from.y)
        }

        operator fun Point2d.times(value: Int): Point2d = Point2d(x * value, y * value)
    }

    override val orthoNeighbors: Set<Point2d>
        get() = listOf(-1, 1).flatMap { listOf(Point2d(x = x + it, y), Point2d(x, y = y + it)) }.toSet()

    override val neighbors: Set<Point2d>
        get() = (-1..1).flatMap { dx -> (-1..1).map { dy -> Point2d(x + dx, y + dy) } }.toSet()
}

data class Point3d(val x: Int, val y: Int, val z: Int) : Point {
    override val neighbors: Set<Point3d>
        get() = (-1..1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).mapNotNull { dz -> Point3d(x + dx, y + dy, z + dz) }
            }
        }.toSet()

    override val orthoNeighbors: Set<Point3d>
        get() = listOf(-1, 1).flatMap { listOf(copy(x = x + it), copy(y = y + it), copy(z = z + it)) }.toSet()

    companion object {
        operator fun Point3d.plus(other: Point3d) = Point3d(this.x + other.x, this.y + other.y, this.z + other.z)
        operator fun Point3d.minus(other: Point3d) = Point3d(this.x - other.x, this.y - other.y, this.z - other.z)

        infix fun Point3d.modX(other: Int) = copy(x = x % other)

        fun Point3d.manhattanDistance(from: Point3d = Point3d(0, 0, 0)): Long {
            return abs(x.toLong() - from.x) +
                abs(y.toLong() - from.y) +
                abs(z.toLong() - from.z)
        }

        operator fun Point3d.times(value: Int): Point3d = Point3d(x * value, y * value, z * value)
    }
}

data class Point4d(val x: Int, val y: Int, val z: Int, val w: Int) : Point {
    override val neighbors: Set<Point4d>
        get() = (-1..1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).flatMap { dz ->
                    (-1..1).map { dw -> Point4d(x + dx, y + dy, z + dz, w + dw) }
                }
            }
        }.toSet()

    override val orthoNeighbors: Set<Point4d>
        get() = listOf(-1, 1).flatMap {
            listOf(
                copy(x = x + it),
                copy(y = y + it),
                copy(z = z + it),
                copy(w = w + it)
            )
        }.toSet()

    companion object {
        operator fun Point4d.plus(other: Point4d) =
            Point4d(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w)

        infix fun Point4d.modX(other: Int) = copy(x = x % other)

        fun Point4d.manhattanDistance(from: Point4d = Point4d(0, 0, 0, 0)): Long {
            return abs(x.toLong() - from.x) +
                abs(y.toLong() - from.y) +
                abs(z.toLong() - from.z) +
                abs(w.toLong() - from.w)
        }

        operator fun Point4d.times(value: Int): Point4d = Point4d(x * value, y * value, z * value, w * value)
    }
}
