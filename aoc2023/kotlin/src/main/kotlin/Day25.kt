import util.AdventOfCode
import util.Edge
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path

object Day25 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 25")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 1 (alt): ${part1Alt(inputFileLines)}")
    }

    private fun part1(input: List<String>): Int {
        val connections =
            input.flatMap { line ->
                line.split(""":\s+""".toRegex()).let {
                        (component, connections) ->
                    connections.split("""\s+""".toRegex()).map { Edge(component, it, 1L) }
                }
            }

        Files.newOutputStream(Path.of("output.dot")).use { fos ->
            PrintWriter(fos).use { pw ->
                pw.println("graph D {")
                pw.println(connections.joinToString("\n") { "\t${it.a} -- ${it.b}" })
                pw.println("}")
            }
        }

        // I cheated and used Gephi to efficiently visualize this
        // htb--bbg
        // pjj--dlk
        // pcc--htj
        // I'll come back and implement a graph eccentricity calculator... these six had the lowest eccentricity

        val filtered =
            connections.filterNot { connection ->
                ("htb" in connection && "bbg" in connection) ||
                    ("pjj" in connection && "dlk" in connection) ||
                    ("pcc" in connection && "htj" in connection)
            }

        val subsets = disjoint(filtered)

        return subsets.entries.map {
            it.value to it.key
        }.groupBy({ it.first }, { it.second }).entries.map { it.value.size }.reduce { a, b -> a * b }
    }

    private fun part1Alt(input: List<String>): Int {
        val connections =
            input.flatMap { line ->
                line.split(""":\s+""".toRegex()).let {
                        (component, connections) ->
                    connections.split("""\s+""".toRegex()).map { Edge(component, it, 1L) }
                }
            }

        val eccentricity = eccentricity(connections)

        val radius = eccentricity.minOf { it.value }
        val centralComponents = eccentricity.filterValues { it == radius }.keys

        val filtered = connections.filterNot { it.a in centralComponents && it.b in centralComponents }

        return disjoint(filtered).entries.map {
            it.value to it.key
        }.groupBy({ it.first }, { it.second }).entries.map { it.value.size }.reduce { a, b -> a * b }
    }

    private fun eccentricity(connections: List<Edge<String, Long>>): Map<String, Long> {
        val components: Map<String, Int> = connections.flatMap { it.asSequence() }.toSet().withIndex().associate { it.value to it.index }
        println(components)
        val componentsRev: Map<Int, String> = components.asSequence().associate { it.value to it.key }

        // Floyd Warshall, the most efficient min-dist calculator known is still O(V^3).
        val dist =
            components.map {
                arrayOfNulls<Long?>(components.size)
            }.toTypedArray()

        connections.forEach {
            dist[components.getValue(it.a)][components.getValue(it.b)] = 1L
            dist[components.getValue(it.b)][components.getValue(it.a)] = 1L
        }

        val range = dist.indices
        range.forEach { dist[it][it] = 0 }

        range.forEach { k ->
            range.forEach { i ->
                range.forEach { j ->
                    if (dist[k][j] != null && dist[i][k] != null && (dist[i][j] == null || dist[i][j]!! > (dist[i][k]!! + dist[k][j]!!))) {
                        dist[i][j] = dist[i][k]!! + dist[k][j]!!
                    }
                }
            }
        }
        return dist.withIndex().associate { componentsRev.getValue(it.index) to it.value.filterNotNull().max() }
    }

    private tailrec fun disjoint(
        filtered: List<Edge<String, Long>>,
        map: MutableMap<String, Long> = mutableMapOf(),
    ): Map<String, Long> {
        if (filtered.isEmpty()) {
            return map
        }

        val current = filtered.first()

        when {
            current.a in map && current.b in map ->
                map.filterValues {
                    it == map.getValue(current.b)
                }.keys.forEach { map[it] = map.getValue(current.a) }

            current.a in map -> map[current.b] = map.getValue(current.a)
            current.b in map -> map[current.a] = map.getValue(current.b)

            else -> {
                val currMax = map.values.maxOrNull() ?: 0
                map[current.a] = currMax + 1
                map[current.b] = currMax + 1
            }
        }
        return disjoint(filtered.drop(1), map)
    }
}
