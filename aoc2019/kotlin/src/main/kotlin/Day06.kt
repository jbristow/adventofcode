import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object Day06 {

    private const val FILENAME = "src/main/resources/day06.txt"

    private fun parseOrbit(s: String): Pair<String, String> {
        return """(.*)\)(.*)""".toRegex().matchEntire(s)?.let {
            it.groupValues[1] to it.groupValues[2]
        } ?: throw Error("Bad orbit: $s")
    }

    private fun List<String>.countPaths() =
        map(::parseOrbit).fold(mapOf<String, Set<String>>()) { m, (a, b) ->
            val nset = (m[a] ?: emptySet()) + b
            m + (a to nset)
        }.let { orbits ->
            orbits.countChildren(orbits["COM"].orEmpty().map { it to 1 }, 0)
        }

    private tailrec fun Map<String, Set<String>>.countChildren(
        paths: List<Pair<String, Int>>,
        size: Int
    ): Int {

        val nextPaths = paths.flatMap {
            this[it.first].orEmpty().map { c -> c to it.second + 1 }
        }
        return when {
            nextPaths.isEmpty() -> size + paths.sumBy(Pair<String, Int>::second)
            else -> countChildren(nextPaths, size + paths.sumBy(Pair<String, Int>::second))
        }
    }

    private fun List<String>.shortestDistanceToSanta(): Int {
        val paths = map(::parseOrbit).fold(mutableMapOf<String, Set<String>>()) { m, (a, b) ->
            m[a] = (m[a] ?: emptySet()) + b
            m
        }

        val youParents = paths.filterValues { "YOU" in it }.keys.map { it to 0 }

        return paths.findDistanceToSan(queue = LinkedList(youParents), seen = setOf("YOU"))
    }

    private tailrec fun MutableMap<String, Set<String>>.findDistanceToSan(
        queue: Deque<Pair<String, Int>>,
        seen: Set<String>
    ): Int {
        val (node, dist) = queue.pop()
        return when (node) {
            "SAN" -> dist - 1
            in seen -> findDistanceToSan(queue, seen)
            else -> {
                queue.addAll(filterValues { node in it }.keys.map { it to (dist + 1) })
                queue.addAll(filterKeys { node == it }.flatMap { (_, v) -> v.map { it to (dist + 1) } })
                findDistanceToSan(LinkedList(queue.filterNot { (n, _) -> n in seen }), seen + node)
            }
        }
    }

    fun part1() = Files.readAllLines(Paths.get(FILENAME)).countPaths()

    fun part2(): Int = Files.readAllLines(Paths.get(FILENAME)).shortestDistanceToSanta()
}

fun main() {
    println(Day06.part1())
    println(Day06.part2())
}
