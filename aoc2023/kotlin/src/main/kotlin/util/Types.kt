package util

import kotlin.math.abs

interface Point<F : Number, M : Number, T : Point<F, M, T>> {
    val neighbors: Set<Point<F, M, T>>
    val orthoNeighbors: Set<Point<F, M, T>>

    fun manhattanDistance(other: T): M

    operator fun plus(other: T): T

    operator fun minus(other: T): T

    operator fun times(value: F): T
}

data class Point2dRange(val xRange: IntRange, val yRange: IntRange) : Iterable<Point2d> {
    constructor(minX: Int, maxX: Int, minY: Int, maxY: Int) : this((minX..maxX), (minY..maxY))
    constructor(topLeft: Point2d, bottomRight: Point2d) : this((topLeft.x..bottomRight.x), (topLeft.y..bottomRight.y))

    constructor(points: Iterable<Point2d>) : this(
        points.minOf(Point2d::x)..points.maxOf(Point2d::x),
        points.minOf(Point2d::y)..points.maxOf(Point2d::y),
    )

    constructor(points: Map<Point2d, Any?>) : this(points.keys)

    override fun iterator(): Iterator<Point2d> {
        return yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point2d(x, y) } }.iterator()
    }

    fun joinToString(transform: ((Point2d) -> CharSequence)? = null): String {
        return yRange.joinToString("\n") { y ->
            xRange.joinToString("") { x -> transform?.let { it(Point2d(x, y)) } ?: "." }
        }
    }
}

data class Point2dLRange(val xRange: LongRange, val yRange: LongRange) : Iterable<Point2dL> {
    constructor(minX: Long, maxX: Long, minY: Long, maxY: Long) : this((minX..maxX), (minY..maxY))

    constructor(points: Iterable<Point2dL>) : this(
        points.minOf(Point2dL::x)..points.maxOf(Point2dL::x),
        points.minOf(Point2dL::y)..points.maxOf(Point2dL::y),
    )

    constructor(points: Map<Point2dL, Any?>) : this(points.keys)
    constructor(topLeft: Point2dL, bottomRight: Point2dL) : this((topLeft.x..bottomRight.x), (topLeft.y..bottomRight.y))

    override fun iterator(): Iterator<Point2dL> {
        return yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point2dL(x, y) } }.iterator()
    }

    fun joinToString(transform: ((Point2dL) -> CharSequence)? = null): String {
        return yRange.joinToString("\n") { y ->
            xRange.joinToString("") { x -> transform?.let { it(Point2dL(x, y)) } ?: "." }
        }
    }
}

data class Point2d(val x: Int = 0, val y: Int = 0) : Point<Int, Long, Point2d> {
    override val orthoNeighbors: Set<Point2d>
        get() = listOf(-1, 1).flatMap { listOf(Point2d(x = x + it, y), Point2d(x, y = y + it)) }.toSet()

    override val neighbors: Set<Point2d>
        get() = (-1..1).flatMap { dx -> (-1..1).map { dy -> Point2d(x + dx, y + dy) } }.toSet()

    override fun manhattanDistance(other: Point2d): Long = abs(x.toLong() - other.x) + abs(y.toLong() - other.y)

    override operator fun plus(other: Point2d): Point2d = Point2d(this.x + other.x, this.y + other.y)

    override operator fun minus(other: Point2d): Point2d = Point2d(this.x - other.x, this.y - other.y)

    override operator fun times(value: Int): Point2d = Point2d(x * value, y * value)

    operator fun times(value: Long): Point2dL = Point2dL(x * value, y * value)
}

data class Point2dL(val x: Long = 0, val y: Long = 0) : Point<Long, Long, Point2dL> {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    override val orthoNeighbors: Set<Point2dL>
        get() = listOf(-1, 1).flatMap { listOf(Point2dL(x = x + it, y), Point2dL(x, y = y + it)) }.toSet()

    override val neighbors: Set<Point2dL>
        get() = (-1..1).flatMap { dx -> (-1..1).map { dy -> Point2dL(x + dx, y + dy) } }.toSet()

    override fun manhattanDistance(other: Point2dL): Long = abs(x.toLong() - other.x) + abs(y.toLong() - other.y)

    override operator fun plus(other: Point2dL): Point2dL = Point2dL(this.x + other.x, this.y + other.y)

    override operator fun minus(other: Point2dL): Point2dL = Point2dL(this.x - other.x, this.y - other.y)

    override operator fun times(value: Long): Point2dL = Point2dL(x * value, y * value)
}

data class Point3d(val x: Int, val y: Int, val z: Int) : Point<Int, Long, Point3d> {
    override val neighbors: Set<Point3d>
        get() =
            (-1..1).flatMap { dx ->
                (-1..1).flatMap { dy ->
                    (-1..1).mapNotNull { dz -> Point3d(x + dx, y + dy, z + dz) }
                }
            }.toSet()

    override val orthoNeighbors: Set<Point3d>
        get() = listOf(-1, 1).flatMap { listOf(copy(x = x + it), copy(y = y + it), copy(z = z + it)) }.toSet()

    override operator fun plus(other: Point3d) = Point3d(this.x + other.x, this.y + other.y, this.z + other.z)

    override operator fun minus(other: Point3d) = Point3d(this.x - other.x, this.y - other.y, this.z - other.z)

    override fun manhattanDistance(other: Point3d): Long {
        return abs(x.toLong() - other.x) + abs(y.toLong() - other.y) + abs(z.toLong() - other.z)
    }

    override operator fun times(value: Int): Point3d = Point3d(x * value, y * value, z * value)
}

data class Point4d(val x: Int, val y: Int, val z: Int, val w: Int) : Point<Int, Long, Point4d> {
    override val neighbors: Set<Point4d>
        get() =
            (-1..1).flatMap { dx ->
                (-1..1).flatMap { dy ->
                    (-1..1).flatMap { dz ->
                        (-1..1).map { dw -> Point4d(x + dx, y + dy, z + dz, w + dw) }
                    }
                }
            }.toSet()

    override val orthoNeighbors: Set<Point4d>
        get() =
            listOf(-1, 1).flatMap {
                listOf(
                    copy(x = x + it),
                    copy(y = y + it),
                    copy(z = z + it),
                    copy(w = w + it),
                )
            }.toSet()

    override operator fun plus(other: Point4d) = Point4d(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w)

    override operator fun times(value: Int): Point4d = Point4d(x * value, y * value, z * value, w * value)

    override operator fun minus(other: Point4d): Point4d = this + (other * -1)

    override fun manhattanDistance(other: Point4d): Long =
        abs(x.toLong() - other.x) + abs(y.toLong() - other.y) + abs(z.toLong() - other.z) + abs(w.toLong() - other.w)
}

sealed class Direction(val offset: Point2d) {
    data object North : Direction(Point2d(0, -1))

    data object East : Direction(Point2d(1, 0))

    data object South : Direction(Point2d(0, 1))

    data object West : Direction(Point2d(-1, 0))

    companion object {
        val all: Sequence<Direction>
            get() = sequenceOf(North, East, South, West)
    }
}

typealias Up = Direction.North
typealias Down = Direction.South
typealias Left = Direction.West
typealias Right = Direction.East
