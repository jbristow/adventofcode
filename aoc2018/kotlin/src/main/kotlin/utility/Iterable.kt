package utility

val <T> Iterable<T>.head: T get() = first()
val <T, I : Iterable<T>> I.tail: List<T> get() = drop(1)
val <T> Iterable<T>.last: T get() = last()

operator fun <T> Iterable<T>.component1(): T = head
operator fun <T> Iterable<T>.component2(): List<T> = tail