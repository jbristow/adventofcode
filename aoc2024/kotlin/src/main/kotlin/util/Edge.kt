package util

data class Edge<T, W : Number>(
    val a: T,
    val b: T,
    val dist: W,
) {
    operator fun contains(node: T): Boolean = node == a || node == b

    override fun hashCode(): Int = a.hashCode() + 3 * b.hashCode() + 5 * dist.hashCode() + 17

    override fun equals(other: Any?): Boolean =
        when {
            other !is Edge<*, *> -> false
            other.dist == this.dist ->
                when {
                    other.a == this.a && other.b == this.b -> true
                    other.b == this.a && other.a == this.b -> true
                    else -> false
                }

            else -> false
        }

    fun toList(): List<T> = listOf(a, b)

    fun asSequence(): Sequence<T> = sequenceOf(a, b)
}
