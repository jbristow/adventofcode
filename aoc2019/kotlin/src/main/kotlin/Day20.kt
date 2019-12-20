import arrow.core.None
import arrow.core.Option
import arrow.core.extensions.list.functorFilter.filter
import arrow.core.extensions.sequence.monadFilter.filterMap
import arrow.core.some
import arrow.core.toOption
import java.nio.file.Files
import java.nio.file.Paths

object Day20 {
    private const val FILENAME = "src/main/resources/day20.txt"
    val fileData get() = Files.readAllLines(Paths.get(FILENAME))

    fun part1(input: List<String>) {
        val width = input.map {
            it.split(regex = """[^.#]+""".toRegex()).filterNot(String::isEmpty).map(String::length)
        }.filter { it.size > 1 }.flatten().toSet().first()

        val bottomRight = Point(
            input.map { it.drop(2).dropLast(2).length }.max()!!,
            input.drop(2).dropLast(2).size
        )
        val raw = input.drop(2).dropLast(2).map { it.drop(2) }.pointSet()
        val rawMap = raw.map { p ->
            p to allDirections().map(p::inDirection).filter { it in raw }
        }.toMap()
        val horizontalLeftWarps =
            input.drop(2)
                .map { it.take(2).trim() }.withIndex()
                .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
                .map { it.value to Point(0, it.index) } +
                    input.drop(2).dropLast(2)
                        .map { it.drop(bottomRight.x - width).take(2).trim() }.withIndex()
                        .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
                        .map { it.value to Point(bottomRight.x - width, it.index) }

        val horizontalRightWarps =
            input.drop(2).dropLast(2)
                .map { it.take(width + 4).takeLast(2).trim() }.withIndex()
                .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
                .map { it.value to Point(width - 1, it.index) } +
                    input.drop(2).dropLast(2)
                        .map { it.takeLast(2).trim() }.withIndex()
                        .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
                        .map { it.value to Point(bottomRight.x - 1, it.index) }

        val verticalTopWarps =
            input[0].drop(2).withIndex()
                .filter { it.value != ' ' }
                .map { IndexedValue(index = it.index, value = "${it.value}${input[1].drop(2)[it.index]}") }
                .map { it.value to Point(it.index, 0) } +
                    input[input.lastIndex - 3 - width].drop(2).withIndex()
                        .filter { it.value !in " .#" }
                        .map {
                            IndexedValue(
                                index = it.index,
                                value = "${it.value}${input[input.lastIndex - width - 2].drop(2)[it.index]}"
                            )
                        }
                        .map { it.value to Point(it.index, bottomRight.y - width) }

        val verticalBottomWarps =
            input[width + 2].drop(2).withIndex()
                .filter { it.value !in " #." }
                .map { IndexedValue(index = it.index, value = "${it.value}${input[width + 3].drop(2)[it.index]}") }
                .map { it.value to Point(it.index, width - 1) } +
                    input[input.lastIndex - 1].drop(2).withIndex()
                        .filter { it.value !in " #." }
                        .map {
                            IndexedValue(
                                index = it.index,
                                value = "${it.value}${input[input.lastIndex].drop(2)[it.index]}"
                            )
                        }.map { it.value to Point(it.index, bottomRight.y - 1) }

        val (endpoints, allWarpsRaw) =
            (verticalTopWarps + verticalBottomWarps + horizontalLeftWarps + horizontalRightWarps)
                .partition { it.first in listOf("AA", "ZZ") }
        val allWarps = allWarpsRaw.filter { ' ' !in it.first }.groupBy { it.first }
            .mapValues { it.value.map { v -> v.second } }

        val warpedMap = rawMap + allWarps.flatMap { (k, v) ->
            val a = v[0]
            val b = v[1]
            listOf(
                a to ((rawMap[a] ?: error("Couldn't find $a in rawMap")) + b),
                b to ((rawMap[b] ?: error("Couldn't find $b in rawMap")) + a)
            )
        }
        val shortest = djikstra(warpedMap, endpoints[0].second, endpoints[1].second)
        println("dist")
        println(shortest)

    }
}

private fun List<String>.pointSet(yOffset: Int = 0, xOffset: Int = 0): Set<Point> {
    return this.asSequence().withIndex().flatMap { (y, line) ->
        line.withIndex().asSequence().filterMap { (x, c) ->
            when (c) {
                '.' -> Point(x + xOffset, y + yOffset).some()
                else -> None
            }
        }
    }.toSet()
}

fun main() {
    Day20.part1(Day20.fileData)
}

tailrec fun djikstra(
    map: Map<Point, List<Point>>,
    start: Point,
    end: Point,
    q: Set<Point> = map.keys,
    dist: Map<Point, Int> = mapOf(start to 0),
    prev: Map<Point, Point> = emptyMap()
): Option<Int> = when {
    q.isEmpty() -> None
    else -> when (val u = q.filter { it in dist }.minBy { dist[it]!! }!!) {
        end ->
            dist[u].toOption()
        else -> {
            val updates =
                q.filter { it in (map[u] ?: emptyList()) }
                    .filter { v -> v !in dist || ((dist[u] ?: 0) + 1) < dist[v]!! }
            djikstra(
                map,
                start,
                end,
                q - u,
                dist + updates.map { it to dist[u]!! + 1 },
                prev + updates.map { it to u })
        }
    }
}
