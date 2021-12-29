package util

data class Point2d(val x: Int, val y: Int)

operator fun Point2d.plus(other: Point2d) =
    Point2d(x = this.x + other.x, y = this.y + other.y)
