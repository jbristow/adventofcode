import util.AdventOfCode
import util.Point2d

object Day12 : AdventOfCode() {

    data class Topomap(val heightMap: Map<Point2d, Int>, val startLoc: Point2d, val endLoc: Point2d)

    private fun parseInput(input: List<String>): Topomap {
        val heightCharMap = input.flatMapIndexed { y, row ->
            row.mapIndexed { x, height -> Point2d(x, y) to height }
        }.toMap()
        val startLoc = heightCharMap.filterValues { it == 'S' }.keys.first()
        val endLoc = heightCharMap.filterValues { it == 'E' }.keys.first()
        val heightMap = heightCharMap.mapValues { (k, v) ->
            when (v) {
                'S' -> 0
                'E' -> 'z'.code - 'a'.code
                else -> v.code - 'a'.code
            }
        }
        return Topomap(heightMap, startLoc, endLoc)
    }

    fun <P> djikstra(
        start: P,
        isEnd: (P?) -> Boolean,
        q: Set<P>,
        neighborFn: (P) -> List<P>
    ): Pair<Int?, List<P>> {
        tailrec fun djikstraPrime(
            q: Set<P>,
            dist: Map<P, Int>,
            prev: Map<P, P> = emptyMap()
        ): Pair<Int?, List<P>> {
            tailrec fun reconstructPath(
                end: P?,
                output: List<P>
            ): List<P> = when (end) {
                null -> output + start
                else -> reconstructPath(prev[end], output + end)
            }

            val u = q.filter { it in dist }.minByM { dist[it] }

            return when (u) {
                null -> null to emptyList()
                else -> {
                    if (isEnd(u)) {
                        return dist[u] to reconstructPath(u, emptyList())
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

    /**
     * Returns the first element yielding the smallest value of the given function or `null` if there are no elements.
     *
     */
    inline fun <T, R : Comparable<R>> Iterable<T>.minByM(selector: (T) -> R?): T? {
        val iterator = iterator()
        if (!iterator.hasNext()) return null
        var minElem = iterator.next()
        if (!iterator.hasNext()) return minElem
        var minValue = selector(minElem)
        do {
            val e = iterator.next()
            val v = selector(e)
            if (minValue != null && v != null && minValue > v) {
                minElem = e
                minValue = v
            }
        } while (iterator.hasNext())
        return minElem
    }

    private fun part1(input: List<String>): Int {
        val (heightMap, startLoc, endLoc) = parseInput(input)
        val output = djikstra(startLoc, { it == endLoc }, heightMap.keys) {
            it.orthoNeighbors.filter { neighbor ->
                neighbor in heightMap && (heightMap[neighbor]!! <= (heightMap[it]!! + 1))
            }
        }
        return output.first!!
    }

    private fun part2(input: List<String>): Int {
        val (heightMap, _, endLoc) = parseInput(input)

        val startingCandidates = heightMap.filterValues { it == 0 }.keys

        val output = djikstra(endLoc, { it in startingCandidates }, heightMap.keys) {
            it.orthoNeighbors.filter { neighbor ->
                neighbor in heightMap && (heightMap[neighbor]!! >= (heightMap[it]!! - 1))
            }
        }
        return output.first!!
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
