package utility

fun <K, V : Comparable<V>> Map<K, V>.maxByValue() =
    this.maxBy { (_, v) -> v }

fun <K, V : Comparable<V>, R : Comparable<R>> Map<K, V>.maxByValue(selector: (V) -> R) =
    this.maxBy { (_, v) -> selector(v) }

fun <K : Comparable<K>, V> Map<K, V>.minByKey() = this.minBy { (k, _) -> k }

fun <K, V : Comparable<V>> Map<K, V>.sortedByValue(): List<Pair<K, V>> =
    toList().sortedBy { (_, v) -> v }

fun <K, V : Comparable<V>> Map<K, V>.sortedByValueDescending(): List<Pair<K, V>> =
    toList().sortedByDescending { (_, v) -> v }