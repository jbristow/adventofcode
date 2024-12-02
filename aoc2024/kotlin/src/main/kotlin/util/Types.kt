package util

import java.math.BigDecimal
import kotlin.math.abs

interface Point<F : Number, M : Number, T : Point<F, M, T>> {
    val neighbors: Set<Point<F, M, T>>
    val orthoNeighbors: Set<T>

    fun manhattanDistance(other: T): M

    operator fun plus(other: T): T

    operator fun minus(other: T): T

    operator fun times(value: F): T
}

data class Point2dRange(
    val xRange: IntRange,
    val yRange: IntRange,
) : Iterable<Point2d> {
    constructor(minX: Int, maxX: Int, minY: Int, maxY: Int) : this((minX..maxX), (minY..maxY))
    constructor(topLeft: Point2d, bottomRight: Point2d) : this((topLeft.x..bottomRight.x), (topLeft.y..bottomRight.y))

    constructor(points: Iterable<Point2d>) : this(
        points.minOf(Point2d::x)..points.maxOf(Point2d::x),
        points.minOf(Point2d::y)..points.maxOf(Point2d::y),
    )

    constructor(points: Map<Point2d, Any?>) : this(points.keys)

    override fun iterator(): Iterator<Point2d> =
        yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point2d(x, y) } }.iterator()

    fun joinToString(transform: ((Point2d) -> CharSequence)? = null): String =
        yRange.joinToString("\n") { y ->
            xRange.joinToString("") { x -> transform?.let { it(Point2d(x, y)) } ?: "." }
        }
}

data class Point3dRange(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange,
) : Iterable<Point3d> {
    constructor(
        topLeft: Point3d,
        bottomRight: Point3d,
    ) : this((topLeft.x..bottomRight.x), (topLeft.y..bottomRight.y), (topLeft.z..bottomRight.z))

    constructor(points: Iterable<Point3d>) : this(
        points.minOf(Point3d::x)..points.maxOf(Point3d::x),
        points.minOf(Point3d::y)..points.maxOf(Point3d::y),
        points.minOf(Point3d::z)..points.maxOf(Point3d::z),
    )

    constructor(points: Map<Point3d, Any?>) : this(points.keys)

    override fun iterator(): Iterator<Point3d> =
        zRange
            .asSequence()
            .flatMap { z ->
                yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point3d(x, y, z) } }
            }.iterator()
}

data class Point3dLRange(
    val xRange: LongRange,
    val yRange: LongRange,
    val zRange: LongRange,
) : Iterable<Point3dL> {
    constructor(
        topLeft: Point3dL,
        bottomRight: Point3dL,
    ) : this((topLeft.x..bottomRight.x), (topLeft.y..bottomRight.y), (topLeft.z..bottomRight.z))

    constructor(points: Iterable<Point3dL>) : this(
        points.minOf(Point3dL::x)..points.maxOf(Point3dL::x),
        points.minOf(Point3dL::y)..points.maxOf(Point3dL::y),
        points.minOf(Point3dL::z)..points.maxOf(Point3dL::z),
    )

    constructor(points: Map<Point3dL, Any?>) : this(points.keys)

    override fun iterator(): Iterator<Point3dL> =
        zRange
            .asSequence()
            .flatMap { z ->
                yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point3dL(x, y, z) } }
            }.iterator()
}

data class Point2dLRange(
    val xRange: LongRange,
    val yRange: LongRange,
) : Iterable<Point2dL> {
    constructor(minX: Long, maxX: Long, minY: Long, maxY: Long) : this((minX..maxX), (minY..maxY))

    constructor(points: Iterable<Point2dL>) : this(
        points.minOf(Point2dL::x)..points.maxOf(Point2dL::x),
        points.minOf(Point2dL::y)..points.maxOf(Point2dL::y),
    )

    constructor(points: Map<Point2dL, Any?>) : this(points.keys)
    constructor(topLeft: Point2dL, bottomRight: Point2dL) : this((topLeft.x..bottomRight.x), (topLeft.y..bottomRight.y))

    override fun iterator(): Iterator<Point2dL> =
        yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point2dL(x, y) } }.iterator()

    fun joinToString(transform: ((Point2dL) -> CharSequence)? = null): String =
        yRange.joinToString("\n") { y ->
            xRange.joinToString("") { x -> transform?.let { it(Point2dL(x, y)) } ?: "." }
        }
}

data class Point2d(
    val x: Int = 0,
    val y: Int = 0,
) : Point<Int, Long, Point2d> {
    override val orthoNeighbors: Set<Point2d>
        get() = listOf(-1, 1).flatMap { listOf(Point2d(x = x + it, y), Point2d(x, y = y + it)) }.toSet()
    val diagonalNeighbors: Set<Point2d>
        get() =
            setOf(
                Point2d(x + 1, y + 1),
                Point2d(x - 1, y + 1),
                Point2d(x - 1, y - 1),
                Point2d(x + 1, y - 1),
            )

    override val neighbors: Set<Point2d>
        get() = (-1..1).flatMap { dx -> (-1..1).map { dy -> Point2d(x + dx, y + dy) } }.toSet()

    override fun manhattanDistance(other: Point2d): Long = abs(x.toLong() - other.x) + abs(y.toLong() - other.y)

    override operator fun plus(other: Point2d): Point2d = Point2d(this.x + other.x, this.y + other.y)

    override operator fun minus(other: Point2d): Point2d = Point2d(this.x - other.x, this.y - other.y)

    override operator fun times(value: Int): Point2d = Point2d(x * value, y * value)

    operator fun times(value: Long): Point2dL = Point2dL(x * value, y * value)

    override fun toString(): String = "($x,$y)"
}

data class Point2dL(
    val x: Long = 0,
    val y: Long = 0,
) : Point<Long, Long, Point2dL> {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    override val orthoNeighbors: Set<Point2dL>
        get() = listOf(-1, 1).flatMap { listOf(Point2dL(x = x + it, y), Point2dL(x, y = y + it)) }.toSet()

    override val neighbors: Set<Point2dL>
        get() = (-1..1).flatMap { dx -> (-1..1).map { dy -> Point2dL(x + dx, y + dy) } }.toSet()

    override fun manhattanDistance(other: Point2dL): Long = abs(x - other.x) + abs(y - other.y)

    override operator fun plus(other: Point2dL): Point2dL = Point2dL(this.x + other.x, this.y + other.y)

    override operator fun minus(other: Point2dL): Point2dL = Point2dL(this.x - other.x, this.y - other.y)

    override operator fun times(value: Long): Point2dL = Point2dL(x * value, y * value)
}

data class Point3d(
    val x: Int,
    val y: Int,
    val z: Int,
) : Point<Int, Long, Point3d> {
    override val neighbors: Set<Point3d>
        get() =
            (-1..1)
                .flatMap { dx ->
                    (-1..1).flatMap { dy ->
                        (-1..1).mapNotNull { dz -> Point3d(x + dx, y + dy, z + dz) }
                    }
                }.toSet()

    override val orthoNeighbors: Set<Point3d>
        get() = listOf(-1, 1).flatMap { listOf(copy(x = x + it), copy(y = y + it), copy(z = z + it)) }.toSet()

    override operator fun plus(other: Point3d) = Point3d(this.x + other.x, this.y + other.y, this.z + other.z)

    override operator fun minus(other: Point3d) = Point3d(this.x - other.x, this.y - other.y, this.z - other.z)

    override fun manhattanDistance(other: Point3d): Long = abs(x.toLong() - other.x) + abs(y.toLong() - other.y) + abs(z.toLong() - other.z)

    override operator fun times(value: Int): Point3d = Point3d(x * value, y * value, z * value)

    override fun toString(): String = "($x,$y,$z)"
}

data class Point3dL(
    val x: Long,
    val y: Long,
    val z: Long,
) : Point<Long, Long, Point3dL> {
    override val neighbors: Set<Point3dL>
        get() =
            (-1..1)
                .flatMap { dx ->
                    (-1..1).flatMap { dy ->
                        (-1..1).mapNotNull { dz -> Point3dL(x + dx, y + dy, z + dz) }
                    }
                }.toSet()

    override val orthoNeighbors: Set<Point3dL>
        get() = listOf(-1, 1).flatMap { listOf(copy(x = x + it), copy(y = y + it), copy(z = z + it)) }.toSet()

    override operator fun plus(other: Point3dL) = Point3dL(this.x + other.x, this.y + other.y, this.z + other.z)

    override operator fun minus(other: Point3dL) = Point3dL(this.x - other.x, this.y - other.y, this.z - other.z)

    override fun manhattanDistance(other: Point3dL): Long = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    override operator fun times(value: Long): Point3dL = Point3dL(x * value, y * value, z * value)

    override fun toString(): String = "($x,$y,$z)"

    fun toArray(): LongArray = longArrayOf(x, y, z)

    operator fun get(i: Int): Long =
        when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw Exception("Illegal access, index $i")
        }
}

data class Point3dBigDecimal(
    val x: BigDecimal,
    val y: BigDecimal,
    val z: BigDecimal,
) : Point<BigDecimal, BigDecimal, Point3dBigDecimal> {
    constructor(x: Int, y: Int, z: Int) : this(x.toBigDecimal(), y.toBigDecimal(), z.toBigDecimal())

    override val neighbors: Set<Point3dBigDecimal>
        get() = TODO("Non integer points cannot easily define what neighbors are")

    override val orthoNeighbors: Set<Point3dBigDecimal>
        get() = TODO("Non integer points cannot easily define what neighbors are")

    override operator fun plus(other: Point3dBigDecimal) =
        Point3dBigDecimal(
            this.x.add(other.x),
            this.y.add(other.y),
            this.z.add(other.z),
        )

    override operator fun minus(other: Point3dBigDecimal) =
        Point3dBigDecimal(
            this.x.subtract(other.x),
            this.y.subtract(other.y),
            this.z.subtract(other.z),
        )

    override fun manhattanDistance(other: Point3dBigDecimal): BigDecimal = (x - other.x).abs() + (y - other.y).abs() + (z - other.z).abs()

    fun manhattanDistance(): BigDecimal = this.manhattanDistance(Point3dBigDecimal(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO))

    override operator fun times(value: BigDecimal): Point3dBigDecimal = Point3dBigDecimal(x * value, y * value, z * value)

    override fun toString(): String = "($x,$y,$z)"

    operator fun get(i: Int): BigDecimal =
        when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw Exception("Illegal access, index $i")
        }
}

sealed class Direction(
    val offset: Point2d,
) {
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
