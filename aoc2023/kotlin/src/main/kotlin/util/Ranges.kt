package util

@Suppress("DuplicatedCode")
fun IntRange.splitOnIntersect(other: IntRange): List<IntRange> {

    val (a, b) = if (this.first < other.first) {
        this to other
    } else if (this.first == other.first && this.last > other.last) {
        this to other
    } else {
        other to this
    }

    return when {
        a == b -> listOf(a)
        a.last < b.first || a.first > b.last -> listOf(a, b)
        b.first == a.first && b.last in a -> listOf(b.first..b.last, b.last + 1..a.last)
        b.last == a.last && b.last in a -> listOf(a.first until b.first, b.first..b.last)
        b.first in a && b.last in a -> listOf(a.first until b.first, b.first..b.last, b.last + 1..a.last)
        a.last in b -> listOf(a.first until b.first, b.first..a.last, a.last + 1..b.last)
        else -> listOf(b.first until a.first, a.first..b.last, b.last + 1..a.last)
    }
}

@Suppress("DuplicatedCode")
fun LongRange.splitOnIntersect(other: LongRange): List<LongRange> {

    val (a, b) = if (this.first < other.first) {
        this to other
    } else if (this.first == other.first && this.last > other.last) {
        this to other
    } else {
        other to this
    }

    return when {
        a == b -> listOf(a)
        a.last < b.first || a.first > b.last -> listOf(a, b)
        b.first == a.first && b.last in a -> listOf(b.first..b.last, b.last + 1..a.last)
        b.last == a.last && b.last in a -> listOf(a.first until b.first, b.first..b.last)
        b.first in a && b.last in a -> listOf(a.first until b.first, b.first..b.last, b.last + 1..a.last)
        a.last in b -> listOf(a.first until b.first, b.first..a.last, a.last + 1..b.last)
        else -> listOf(b.first until a.first, a.first..b.last, b.last + 1..a.last)
    }
}

@Suppress("DuplicatedCode")
fun IntRange.merge(other: IntRange) = when {
    this == other -> listOf(this)
    this.first in other && this.last in other -> listOf(other)
    other.first in this && other.last in this -> listOf(this)
    this.first < other.first && this.last in other -> listOf(this.first..other.last)
    this.first in other && this.last > other.last -> listOf(other.first..this.last)
    else -> listOf(this, other)
}

@Suppress("DuplicatedCode")
fun LongRange.merge(other: LongRange) = when {
    this == other -> listOf(this)
    this.first in other && this.last in other -> listOf(other)
    other.first in this && other.last in this -> listOf(this)
    this.first < other.first && this.last in other -> listOf(this.first..other.last)
    this.first in other && this.last > other.last -> listOf(other.first..this.last)
    else -> listOf(this, other)
}
