package util

interface Glyphable {
    fun Glyphable?.toGlyph(): String
}

interface TwoD<T> {
    val x: T
    val y: T
}