package aoc

import arrow.core.None
import arrow.core.filterOption
import arrow.core.firstOrNone
import arrow.core.getOrElse
import arrow.core.some
import arrow.core.toOption
import java.nio.file.Files
import java.nio.file.Paths

object Day18 {

    private const val FILENAME = "src/main/resources/day18.txt"
    val fileData: List<String> get() = Files.readAllLines(Paths.get(FILENAME))

    sealed class DungeonTile {
        object Open : DungeonTile()
        object StartLoc : DungeonTile()
        data class Door(val label: Char) : DungeonTile()
        data class Key(val label: Char) : DungeonTile()
    }

    fun part1(fileData: List<String>): Int {
        val dungeon = fileData.mapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when {
                    c == '#' -> null
                    c == '@' -> Point(x, y) to DungeonTile.StartLoc
                    c == '.' -> Point(x, y) to DungeonTile.Open
                    c.isLetter() && c.isUpperCase() -> Point(x, y) to DungeonTile.Door(c.lowercaseChar())
                    else -> Point(x, y) to DungeonTile.Key(c)
                }
            }
        }.flatten().toMap().toMutableMap()
        val startingLoc = dungeon.filterValues { it is DungeonTile.StartLoc }.keys.first()
        val keylocs = dungeon.asSequence().map {
            when (it.value) {
                is DungeonTile.Key -> (it.key to (it.value as DungeonTile.Key).some()).some()
                else -> None
            }
        }.filterOption().toMap()

        var deadEnds = dungeon.keys.filterNot { k ->
            dungeon[k].toOption().exists { o -> o is DungeonTile.Key || o is DungeonTile.Door } ||
                allDirections().map { k.inDirection(it) }.count { it in dungeon } > 1
        }

        while (deadEnds.isNotEmpty()) {
            deadEnds.forEach { dungeon.remove(it) }
            deadEnds = dungeon.keys.filterNot { k ->
                dungeon[k].toOption().exists { o -> o is DungeonTile.Key } ||
                    allDirections().map { k.inDirection(it) }.count { it in dungeon } > 1
            }
        }

        (0..fileData.size).forEach { y ->
            (0..fileData[0].length).forEach { x ->
                print(
                    when (val tile = dungeon[Point(x, y)]) {
                        null -> '#'
                        is DungeonTile.StartLoc -> '@'
                        is DungeonTile.Key -> tile.label
                        is DungeonTile.Door -> tile.label.uppercaseChar()
                        else -> '.'
                    }
                )
            }
            println()
        }

        val setOfAllTiles =
            (dungeon.keys).map { it to if (dungeon[it] is DungeonTile.Key) listOf(dungeon[it]!! as DungeonTile.Key) else emptyList() }
                .toSet()
        val setOfAllPoints = setOfAllTiles.map { it.first }

        val neighborFn: (
            Set<Pair<Point, List<DungeonTile.Key>>>,
            Map<Pair<Point, List<DungeonTile.Key>>, Int>,
            Pair<Point, List<DungeonTile.Key>>
        ) -> List<Pair<Point, List<DungeonTile.Key>>> =
            { q, dist, it ->
                allDirections().asSequence().map(it.first::inDirection).filter { p ->
                    p in setOfAllPoints
                }.filter { p ->
                    when (val tile = dungeon[p]) {
                        is DungeonTile.Key -> tile !in it.second
                        is DungeonTile.Door -> {
                            tile.label in it.second.map(DungeonTile.Key::label)
                        }
                        null -> false
                        else -> true
                    }
                }.filter { v ->
                    it in q &&
                        dist.filterKeys { k ->
                            k.first == v
                        }.values.firstOrNone().all { vdist ->
                            dist[it]!! + 1 < vdist
                        }
                }.map { p ->

                    p to (it.second + if (dungeon[p] is DungeonTile.Key) listOf(dungeon[p]!! as DungeonTile.Key) else emptyList())
                }.toList()
            }
        val minFn = { q: Set<Pair<Point, List<DungeonTile.Key>>>, dist: Map<Pair<Point, List<DungeonTile.Key>>, Int> ->
            val x = q.flatMap {
                dist.filterKeys { dk ->
                    dk.first == it.first
                }.toList().ifEmpty { listOf(it to Int.MAX_VALUE) }
            }.asSequence()
            x.minByOrNull {
                it.second
            }?.first
        }

        val (dist, prev) = djikstra18<Pair<Point, List<DungeonTile.Key>>>(
            startingLoc to emptyList(),
            setOfAllTiles,
            minFn,
            neighborFn
        )
        println("***\nFirst Step Dists\n$dist\n${dist.filter { it.key.first in keylocs }}")

        dist.filter { it.key.first in keylocs }.forEach { a ->

            val (d2, p2) =
                djikstra18(a.key, setOfAllTiles, minFn, neighborFn)
            println(
                "a: " + d2.filter { d2e ->
                    d2e.key.first in keylocs && d2e.value > 0
                }
            )

            d2.filter { it.key.first in keylocs }.forEach { b ->
                val steps = b.value
                val next = b.key.first to (a.key.second + b.key.second)
                val nextAllTiles = (setOfAllTiles - b.key) + next
                val (d3, p3) =
                    djikstra18(next, nextAllTiles, minFn, neighborFn)
                println(
                    "b: " + d3.filter { d3e ->
                        d3e.key.first in keylocs && d3e.value > 0
                    }
                )
            }
        }

        return -1
    }
}

fun main() {
    println(Day18.part1(Day18.fileData))
}

fun <P> djikstra18(
    start: P,
    q: Set<P>,
    minFn: (Set<P>, Map<P, Int>) -> P?,
    neighborFn: (Set<P>, Map<P, Int>, P) -> List<P>
): Pair<Map<P, Int>, Map<P, P>> {
    tailrec fun djikstraPrime18(
        q: Set<P>,
        dist: Map<P, Int>,
        prev: Map<P, P> = emptyMap()
    ): Pair<Map<P, Int>, Map<P, P>> {
        return when (val u = minFn(q, dist)) {
            null -> {
                dist to prev
            }
            else -> {
                val updates = neighborFn(q, dist, u)
                djikstraPrime18(
                    q - u,
                    dist + updates.map { it to dist[u].toOption().getOrElse { 0 } + 1 },
                    prev + updates.map { it to u }
                )
            }
        }
    }
    return djikstraPrime18(q, mapOf(start to 0), emptyMap())
}
