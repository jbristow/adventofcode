package util

object Djikstra : PathfindingAlgorithm() {
    fun <P> run(
        start: P,
        isEnd: (P?) -> Boolean,
        q: Set<P>,
        neighborFn: (P) -> List<P>,
        distanceFn: (P, P) -> Int = { _, _ -> 1 },
    ): Pair<Int, List<P>>? {
        tailrec fun djikstraPrime(
            q: Set<P>,
            dist: Map<P, Int>,
            prev: Map<P, P> = emptyMap(),
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
                            u in q && (dist[v] == null || dist[v]!! > ((dist[u] ?: 0) + distanceFn(u, v)))
                        }
                    djikstraPrime(
                        q - u,
                        dist + updates.map { it to (dist[u] ?: 0) + distanceFn(u, it) },
                        prev + updates.map { it to u },
                    )
                }
            }
        }
        return djikstraPrime(q, mapOf(start to 0), emptyMap())
    }
}

object DjikstraUnbounded : PathfindingAlgorithm() {
    fun <P> run(
        start: P,
        isEnd: (P?) -> Boolean,
        neighborFn: (P) -> List<P>,
        distanceFn: (P, P) -> Int = { _, _ -> 1 },
    ): Pair<Int, List<P>>? {
        tailrec fun djikstraPrime(
            q: Set<P>,
            dist: Map<P, Int>,
            prev: Map<P, P> = emptyMap(),
            seen: Set<P> = emptySet(),
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
                            u in q && (dist[v] == null || dist[v]!! > ((dist[u] ?: 0) + distanceFn(u, v)))
                        }
                    val newQ = (q + neighborFn(u)) - u - seen
                    val newest = newQ.filterNot { it in dist }.map { it to 0 }
                    djikstraPrime(
                        newQ,
                        dist + newest + updates.map { it to (dist[u] ?: 0) + distanceFn(u, it) },
                        prev + updates.map { it to u },
                        seen + u,
                    )
                }
            }
        }

        val q = setOf(start)
        return djikstraPrime(q, mapOf(start to 0) + q.map { it to 0 }, emptyMap())
    }
}
