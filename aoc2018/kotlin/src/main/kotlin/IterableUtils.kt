val <T> Iterable<T>.head get() = first()
val <T> Iterable<T>.tail get() = drop(1)
val <T> Iterable<T>.last get() = last()


operator fun <T> Iterable<T>.component1() = head
operator fun <T> Iterable<T>.component2() = tail