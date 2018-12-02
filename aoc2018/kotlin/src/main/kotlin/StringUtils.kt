val String.tail get() = drop(1)
val String.head get() = first()

val CharSequence.tail get() = drop(1)
val CharSequence.head get() = first()

operator fun CharSequence.component1(): Char = head
operator fun CharSequence.component2(): CharSequence = tail
operator fun String.component2(): String = tail

fun String.frequencies(): Map<Char, Int> =
        groupBy { it }.mapValues { (_, v) -> v.count() }
