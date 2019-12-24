package util

import Point
import PointL

interface Glyphable {
    fun Glyphable?.toGlyph(): String
}

typealias PointGrid<V> = MutableMap<Point, V>
typealias PointGridL<V> = MutableMap<PointL, V>

interface TwoD<T> {
    val x: T
    val y: T
}
