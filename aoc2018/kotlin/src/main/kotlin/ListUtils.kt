fun <A, B> List<A>.scan(initial: B, operation: (B, A) -> B) =
        fold(listOf(initial)) { scanList, curr ->
            scanList + operation(scanList.last(), curr)
        }

fun <A> List<A>.scan(operation: (A, A) -> A) = tail.scan(head, operation)

val <T> List<T>.init get() = dropLast(1)
fun <T> List<T>.inits(): List<List<T>> = scan(emptyList()) { list, element -> list + element }
fun <T> List<T>.tails(): List<List<T>> = when {
    isEmpty() -> emptyList()
    else -> listOf(this) + tail.tails()
}

fun <A> List<A>.frequencies(): Map<A, Int> = groupBy { it }.mapValues { (_, v) -> v.count() }

fun <A, B> List<A>.cartesian(transform: (A, A) -> B): Sequence<B> {
    return init.asSequence().mapIndexed { i, a ->
        drop(i + 1).asSequence().map { b ->
            transform(a, b)
        }
    }.flatten()
}

fun <A, B : Any> List<A>.cartesianNotNull(transform: ((A, A) -> B?)): Sequence<B> {
    return init.asSequence().mapIndexedNotNull { i, a ->
        drop(i + 1).asSequence().mapNotNull { b ->
            transform(a, b)
        }
    }.flatten()
}
