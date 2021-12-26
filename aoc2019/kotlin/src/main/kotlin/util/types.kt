package util

import aoc.Point
import aoc.PointL

interface Glyphable {
    fun Glyphable?.toGlyph(): String
}

typealias PointGrid<V> = MutableMap<Point, V>
typealias PointGridL<V> = MutableMap<PointL, V>

interface TwoD<T> {
    val x: T
    val y: T
}
