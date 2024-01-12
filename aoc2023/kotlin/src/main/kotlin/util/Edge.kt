package util

data class Edge<T, W : Number>(val a: T, val b: T, val dist: W) {
    operator fun contains(node: T): Boolean {
        return node == a || node == b
    }

    override fun hashCode(): Int {
        return a.hashCode() + 3 * b.hashCode() + 5 * dist.hashCode() + 17
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other !is Edge<*, *> -> false
            other.dist == this.dist ->
                when {
                    other.a == this.a && other.b == this.b -> true
                    other.b == this.a && other.a == this.b -> true
                    else -> false
                }

            else -> false
        }
    }

    fun toList(): List<T> {
        return listOf(a, b)
    }

    fun asSequence(): Sequence<T> {
        return sequenceOf(a, b)
    }
}
