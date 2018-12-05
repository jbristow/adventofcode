val String.tail get() = drop(1)
val String.head get() = first()

val CharSequence.tail get() = drop(1)
val CharSequence.head get() = first()

operator fun CharSequence.component1(): Char = head
operator fun CharSequence.component2(): CharSequence = tail
operator fun String.component2(): String = tail

fun String.frequencies(): Map<Char, Int> =
    groupBy { it }.mapValues { (_, v) -> v.count() }

fun <B> CharSequence.cartesian(transform: (Char, Char) -> B): Sequence<B> {
    return dropLast(1).asSequence().mapIndexed { i, a ->
        drop(i + 1).asSequence().map { b ->
            transform(a, b)
        }
    }.flatten()
}

fun <B> String.cartesian(transform: (Char, Char) -> B): Sequence<B> {
    return dropLast(1).asSequence().mapIndexed { i, a ->
        drop(i + 1).asSequence().map { b ->
            transform(a, b)
        }
    }.flatten()
}

val String.init get() = dropLast(1)
