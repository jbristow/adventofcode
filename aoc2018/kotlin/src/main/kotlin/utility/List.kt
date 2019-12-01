package utility

fun <A, B> List<A>.scan(initial: B, operation: (B, A) -> B) =
    fold(listOf(initial)) { scanList, curr ->
        scanList + operation(scanList.last(), curr)
    }

fun <A> List<A>.scan(operation: (A, A) -> A) = tail.scan(head, operation)

fun <A> List<A>.frequencies(): Map<A, Int> =
    groupBy { it }.mapValues { (_, v) -> v.count() }

fun <A, B> List<A>.cartesian(transform: (A, A) -> B): Sequence<B> {
    return dropLast(1).asSequence().mapIndexed { i, a ->
        drop(i + 1).asSequence().map { b ->
            transform(a, b)
        }
    }.flatten()
}

fun <A, B> List<A>.cartesianForEach(transform: (A, A) -> B) {
    cartesian().forEach { (a, b) -> transform(a, b) }
}

fun <A> List<A>.cartesian(): Sequence<Pair<A, A>> {
    return this.cartesian { a, b -> a to b }
}


