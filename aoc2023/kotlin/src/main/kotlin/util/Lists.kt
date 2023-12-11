package util

fun <T> List<T>.frequency(): Map<T, Int> = this.groupBy { it }.mapValues { (_, v) -> v.count() }
