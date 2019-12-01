package utility

fun <K : Comparable<K>, V> Map<K, V>.minByKey() = this.minBy { (k, _) -> k }

fun <K, V : Comparable<V>> Map<K, V>.sortedByValueDescending(): List<Pair<K, V>> =
    toList().sortedByDescending { (_, v) -> v }