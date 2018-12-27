package utility

/**
 * scan is similar to fold, but returns a list of successive reduced values from the left:
 *   [1,2,3].scan(0) {a,i->a + i} ==> [0,1,3,6]
 *   ["a","b","c"].scan {"$a:$i"} ==> ["a","a:b","a:b:c"]
 * Note that `seq.scan(x,operation).last()` is equal to seq.fold(x,operation)
 */
fun <A, B> Sequence<A>.scan(initial: B, operation: (B, A) -> B) =
    fold(sequenceOf(initial)) { scanSequence, curr ->
        scanSequence + operation(scanSequence.last(), curr)
    }

val <T> Sequence<T>.head get() = first()
val <T> Sequence<T>.tail get() = drop(1)

operator fun <T> Sequence<T>.component1() = head
operator fun <T> Sequence<T>.component2() = tail


