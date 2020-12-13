package util

import kotlin.math.abs

data class Point2d(val x: Int, val y: Int) {
    companion object {
        operator fun Point2d.plus(other: Point2d) = Point2d(this.x + other.x, this.y + other.y)

        infix fun Point2d.modX(other: Int) = Point2d(x % other, y)

        fun Point2d.manhattanDistance(from: Point2d = Point2d(0, 0)): Long {
            return abs(x.toLong() - from.x) + abs(y.toLong() - from.y)
        }

        operator fun Point2d.times(value: Int): Point2d = Point2d(x * value, y * value)
    }
}
