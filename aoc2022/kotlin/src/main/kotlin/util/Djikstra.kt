package util

object Djikstra : PathfindingAlgorithm() {

    fun <P> djikstra(
        start: P,
        isEnd: (P?) -> Boolean,
        q: Set<P>,
        neighborFn: (P) -> List<P>
    ): Pair<Int, List<P>>? {
        tailrec fun djikstraPrime(
            q: Set<P>,
            dist: Map<P, Int>,
            prev: Map<P, P> = emptyMap()
        ): Pair<Int, List<P>>? {
            return when (val u = q.filter { it in dist }.minByOrNull { dist[it]!! }) {
                null -> null
                else -> {
                    if (isEnd(u)) {
                        val distance = dist[u]!!
                        val path = reconstructPath(prev, u)
                        return distance to path
                    }
                    val updates =
                        neighborFn(u).filter { v ->
                            u in q && (dist[v] == null || dist[v]!! > ((dist[u] ?: 0) + 1))
                        }
                    djikstraPrime(
                        q - u,
                        dist + updates.map { it to (dist[u] ?: 0) + 1 },
                        prev + updates.map { it to u }
                    )
                }
            }
        }
        return djikstraPrime(q, mapOf(start to 0), emptyMap())
    }
}
