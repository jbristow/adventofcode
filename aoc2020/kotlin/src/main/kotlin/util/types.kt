package util

data class Point2d(val x: Int, val y: Int) {
    companion object {
        operator fun Point2d.plus(other: Point2d) = Point2d(this.x + other.x, this.y + other.y)

        infix fun Point2d.modX(other: Int) = Point2d(x % other, y)
    }
}
