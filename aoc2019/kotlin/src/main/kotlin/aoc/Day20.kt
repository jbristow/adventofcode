package aoc

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.filterOption
import arrow.core.getOrElse
import arrow.core.some
import arrow.core.toOption
import java.nio.file.Files
import java.nio.file.Paths

data class LevelPoint(val point: Point, val level: Int = 0)

object Day20 {
    private const val FILENAME = "src/main/resources/day20.txt"
    val fileData: List<String> get() = Files.readAllLines(Paths.get(FILENAME))

    fun part1(input: List<String>) {
        val width = parseWidth(input)
        val bottomRight = parseBottomRight(input)
        val rawMap = parseUnwarpedPoints(input)
        val (endpoints, allWarpsRaw) =
            sequenceOf(
                leftInnerWarps(input, bottomRight, width),
                leftOuterWarps(input),
                rightInnerWarps(input, width),
                rightOuterWarps(input, bottomRight),
                topInnerWarps(input, width, bottomRight),
                topOuterWarps(input),
                bottomInnerWarps(input, width),
                bottomOuterWarps(input, bottomRight)
            ).flatten().partition { it.first in listOf("AA", "ZZ") }
        val allWarps = allWarpsRaw.filter { ' ' !in it.first }.groupBy { it.first }
            .mapValues { it.value.map { v -> v.second } }

        val warpedMap = rawMap + allWarps.flatMap { (_, v) ->
            listOf(
                v[0] to (rawMap[v[0]] ?: error { "Couldn't find ${v[0]} in rawMap" }) + v[1],
                v[1] to (rawMap[v[1]] ?: error { "Couldn't find ${v[1]} in rawMap" }) + v[0]
            )
        }
        val shortest = djikstra(
            start = endpoints[0].second,
            end = endpoints[1].second.some(),
            q = warpedMap.keys
        ) { warpedMap[it] ?: emptyList() }
        println("dist")
        println(shortest.first)
    }

    fun part2(input: List<String>) {
        val width = parseWidth(input)
        val bottomRight = parseBottomRight(input)
        val rawMap = parseUnwarpedPoints(input)
        val innerWarps = sequenceOf(
            leftInnerWarps(input, bottomRight, width),
            rightInnerWarps(input, width),
            topInnerWarps(input, width, bottomRight),
            bottomInnerWarps(input, width)
        ).flatten().filter { ' ' !in it.first }.groupBy { it.first }.mapValues { it.value.map { v -> v.second } }

        val (endpoints, outerWarpsRaw) =
            sequenceOf(
                leftOuterWarps(input),
                rightOuterWarps(input, bottomRight),
                topOuterWarps(input),
                bottomOuterWarps(input, bottomRight)
            ).flatten().partition { it.first in listOf("AA", "ZZ") }
        val outerWarps = outerWarpsRaw.groupBy { it.first }.toMap().mapValues { it.value.map { v -> v.second } }

        val innerWarpMap = innerWarps
            .flatMap {
                when (val ow = outerWarps[it.key].toOption()) {
                    is None -> listOf(it.value[0] to it.value[1], it.value[1] to it.value[0])
                    is Some<List<Point>> -> listOf(it.value[0] to ow.value[0])
                }
            }.toMap()
        val outerWarpMap = outerWarps
            .flatMap {
                when (val iw = innerWarps[it.key].toOption()) {
                    is None -> listOf(it.value[0] to it.value[1], it.value[1] to it.value[0])
                    is Some<List<Point>> -> listOf(it.value[0] to iw.value[0])
                }
            }.toMap()

        val shortest = djikstra2(
            start = LevelPoint(endpoints[0].second),
            end = LevelPoint(endpoints[1].second),
            qStart = rawMap.keys.map { LevelPoint(it) }.toSet(),
            massageQ = { level: Int -> rawMap.keys.map { LevelPoint(it, level) }.toSet() }
        ) {
            (rawMap[it.point] ?: emptyList()).map { p -> LevelPoint(p, it.level) } +
                listOf(
                    when (it.level) {
                        0 -> None
                        else -> outerWarpMap[it.point]?.let { p -> LevelPoint(p, level = it.level - 1) }.toOption()
                    }
                ).filterOption() +
                listOf(
                    innerWarpMap[it.point]?.let { p -> LevelPoint(p, level = it.level + 1) }.toOption()
                ).filterOption()
        }
        println("dist")
        println(shortest)
    }

    private fun parseWidth(input: List<String>): Int {
        return input.asSequence().map {
            it.split(regex = """[^.#]+""".toRegex()).filterNot(String::isEmpty).map(String::length)
        }.filter { it.size > 1 }.flatten().toSet().first()
    }

    private fun parseBottomRight(input: List<String>): Point {
        return Point(
            input.maxOf { it.drop(2).dropLast(2).length },
            input.drop(2).dropLast(2).size
        )
    }

    private fun parseUnwarpedPoints(input: List<String>): Map<Point, List<Point>> {
        val raw = input.drop(2).dropLast(2).map { it.drop(2) }.pointSet()
        return raw.associateWith { p ->
            allDirections().map(p::inDirection).filter { it in raw }
        }
    }

    private fun bottomOuterWarps(
        input: List<String>,
        bottomRight: Point
    ): List<Pair<String, Point>> {
        return input[input.lastIndex - 1].drop(2).withIndex()
            .filter { it.value !in " #." }
            .map {
                IndexedValue(
                    index = it.index,
                    value = "${it.value}${input[input.lastIndex].drop(2)[it.index]}"
                )
            }.map { it.value to Point(it.index, bottomRight.y - 1) }
    }

    private fun bottomInnerWarps(
        input: List<String>,
        width: Int
    ): List<Pair<String, Point>> {
        return input[width + 2].drop(2).withIndex()
            .filter { it.value !in " #." }
            .map { IndexedValue(index = it.index, value = "${it.value}${input[width + 3].drop(2)[it.index]}") }
            .map { it.value to Point(it.index, width - 1) }
    }

    private fun topInnerWarps(
        input: List<String>,
        width: Int,
        bottomRight: Point
    ): List<Pair<String, Point>> {
        return input[input.lastIndex - 3 - width].drop(2).withIndex()
            .filter { it.value !in " .#" }
            .map {
                IndexedValue(
                    index = it.index,
                    value = "${it.value}${input[input.lastIndex - width - 2].drop(2)[it.index]}"
                )
            }
            .map { it.value to Point(it.index, bottomRight.y - width) }
    }

    private fun topOuterWarps(input: List<String>): List<Pair<String, Point>> {
        return input[0].drop(2).withIndex()
            .filter { it.value != ' ' }
            .map { IndexedValue(index = it.index, value = "${it.value}${input[1].drop(2)[it.index]}") }
            .map { it.value to Point(it.index, 0) }
    }

    private fun rightOuterWarps(
        input: List<String>,
        bottomRight: Point
    ): List<Pair<String, Point>> {
        return input.drop(2).dropLast(2)
            .map { it.takeLast(2).trim() }.withIndex()
            .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
            .map { it.value to Point(bottomRight.x - 1, it.index) }
    }

    private fun rightInnerWarps(
        input: List<String>,
        width: Int
    ): List<Pair<String, Point>> {
        return input.drop(2).dropLast(2)
            .map { it.take(width + 4).takeLast(2).trim() }.withIndex()
            .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
            .map { it.value to Point(width - 1, it.index) }
    }

    private fun leftInnerWarps(
        input: List<String>,
        bottomRight: Point,
        width: Int
    ): List<Pair<String, Point>> {
        return input.drop(2).dropLast(2)
            .map { it.drop(bottomRight.x - width).take(2).trim() }.withIndex()
            .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
            .map { it.value to Point(bottomRight.x - width, it.index) }
    }

    private fun leftOuterWarps(input: List<String>): List<Pair<String, Point>> {
        return input.asSequence()
            .drop(2)
            .map { it.take(2).trim() }.withIndex()
            .filter { it.value.isNotEmpty() && it.value.none { c -> c in " .#" } }
            .map { it.value to Point(0, it.index) }.toList()
    }
}

private fun List<String>.pointSet(yOffset: Int = 0, xOffset: Int = 0): Set<Point> {
    return this.asSequence().withIndex().flatMap { (y, line) ->
        line.withIndex().asSequence().map { (x, c) ->
            when (c) {
                '.' -> Point(x + xOffset, y + yOffset).some()
                else -> None
            }
        }
    }.filterOption().toSet()
}

fun main() {
    Day20.part1(Day20.fileData)
    val sample1 = """         A           
         A           
  #######.#########  
  #######.........#  
  #######.#######.#  
  #######.#######.#  
  #######.#######.#  
  #####  B    ###.#  
BC...##  C    ###.#  
  ##.##       ###.#  
  ##...DE  F  ###.#  
  #####    G  ###.#  
  #########.#####.#  
DE..#######...###.#  
  #.#########.###.#  
FG..#########.....#  
  ###########.#####  
             Z       
             Z       """.lines()
    Day20.part2(sample1)
    Day20.part2(Day20.fileData)
}

fun <P> djikstra(
    start: P,
    end: Option<P>,
    q: Set<P>,
    neighborFn: (P) -> List<P>
): Pair<Option<Int>, List<P>> {
    tailrec fun djikstraPrime(
        q: Set<P>,
        dist: Map<P, Int>,
        prev: Map<P, P> = emptyMap()
    ): Pair<Option<Int>, List<P>> {
        tailrec fun reconstructPath(
            end: Option<P>,
            output: List<P>
        ): List<P> = when (end) {
            is None -> output + start
            is Some<P> -> reconstructPath(prev[end.value].toOption(), output + end.value)
        }
        return when (val u = q.filter { it in dist }.minByM { dist[it].toOption() }) {
            None -> None to emptyList()
            end -> u.fold(
                { None to emptyList() },
                { uo -> dist[uo].toOption() to reconstructPath(uo.some(), emptyList()) }
            )
            is Some<P> -> {
                val updates =
                    neighborFn(u.value).filter { v ->
                        u.exists { uo ->
                            uo in q &&
                                dist[v].toOption().all { vdist ->
                                    ((dist[uo] ?: 0) + 1) < vdist
                                }
                        }
                    }
                djikstraPrime(
                    q - u.value,
                    dist + updates.map { it to dist[u.value].toOption().getOrElse { 0 } + 1 },
                    prev + updates.map { it to u.value }
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
inline fun <T, R : Comparable<R>> Iterable<T>.minByM(selector: (T) -> Option<R>): Option<T> {
    val iterator = iterator()
    if (!iterator.hasNext()) return None
    var minElem = iterator.next()
    if (!iterator.hasNext()) return minElem.some()
    var minValue = selector(minElem)
    do {
        val e = iterator.next()
        val v = selector(e)
        if (minValue.exists { mv -> v.exists { mv > it } }) {
            minElem = e
            minValue = v
        }
    } while (iterator.hasNext())
    return minElem.some()
}

fun djikstra2(
    start: LevelPoint,
    end: LevelPoint,
    qStart: Set<LevelPoint>,
    massageQ: (Int) -> Set<LevelPoint>,
    neighborFn: (LevelPoint) -> List<LevelPoint>
): Option<Int> {
    tailrec fun djikstra2Prime(
        count: Int,
        q: Set<LevelPoint>,
        dist: Map<LevelPoint, Int>,
        prev: Map<LevelPoint, LevelPoint>,
        levelsSeen: Set<Int>
    ): Option<Int> {
        return when (val u = q.filter { it in dist }.minByM { dist[it].toOption() }) {
            is None -> None
            end.some() -> {
                tailrec fun reconstructPath(
                    start: LevelPoint,
                    end: Option<LevelPoint>,
                    output: List<LevelPoint>
                ): List<LevelPoint> = when (end) {
                    is None -> output + start
                    is Some<LevelPoint> -> reconstructPath(start, prev[end.value].toOption(), output + end.value)
                }
                println(
                    reconstructPath(start, u, emptyList()).chunked(100)
                        .joinToString { s ->
                            s.joinToString { sp ->
                                "(${sp.point.x},${sp.point.y})-${sp.level}"
                            }
                        }
                )
                u.flatMap { dist[it].toOption() }
            }
            is Some<LevelPoint> -> {
                if (count % 2500 == 0) {
                    println("djikstra2($count): $u")
                    println((0..5).map { n -> q.count { it.level == n } })
                }
                val neighbors = neighborFn(u.value)
                val nextLevelsSeen = levelsSeen + neighbors.map { it.level }.toSet()
                val nextQ = q + (nextLevelsSeen - levelsSeen).flatMap { massageQ(it) }.toSet()
                val updates = nextQ.filter { v ->
                    v in neighbors && dist[v].toOption().all { dv -> dv > ((dist[u.value] ?: 0) + 1) }
                }
                djikstra2Prime(
                    count + 1,
                    nextQ - u.value,
                    dist + updates.map { it to dist[u.value].toOption().getOrElse { 0 } + 1 },
                    prev + updates.map { it to u.value },
                    nextLevelsSeen
                )
            }
        }
    }

    return djikstra2Prime(0, qStart, mapOf(start to 0), emptyMap(), setOf(0))
}

fun Option<Point>.plus(element: Option<Point>) = this.fold(
    ifEmpty = { element.map(::listOf) },
    ifSome = { a ->
        element.fold(
            ifEmpty = { listOf(a) },
            ifSome = { b -> listOf(a, b) }
        ).some()
    }
)
