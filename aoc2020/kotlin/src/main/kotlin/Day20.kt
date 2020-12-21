import arrow.core.extensions.sequence.foldable.isEmpty
import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.plus
import kotlin.math.sqrt

object Day20 : AdventOfCode() {

    data class Tile(
        val label: Long,
        val picture: List<String>,
    ) {
        fun sidesAsSet(): Set<String> {
            return setOf(
                this,
                this.rotate(),
                this.rotate().rotate(),
                this.rotate().rotate().rotate(),
                this.flipHoriz(),
                this.flipVert()
            ).flatMap {
                listOf(it[Edge.Up], it[Edge.Right], it[Edge.Down], it[Edge.Left])
            }.toSet()
        }

        fun matchesAnySideOf(head: Tile): Boolean {
            return head.sidesAsSet().intersect(sidesAsSet()).isNotEmpty()
        }

        fun flipHoriz(): Tile {
            return copy(picture = picture.map { it.reversed() })
        }

        fun flipVert(): Tile {
            return copy(picture = picture.reversed())
        }

        fun rotate(): Tile {
            val n = picture.size - 1
            return copy(
                picture = picture.indices.map { y ->
                    picture.indices.joinToString("") { x ->
                        picture[x][n - y].toString()
                    }
                }
            )
        }

        operator fun get(gridEdge: Day20.Edge): String {
            return when (gridEdge) {
                Edge.Up -> picture.first()
                Edge.Down -> picture.last()
                Edge.Right -> picture.joinToString("") { it.last().toString() }
                Edge.Left -> picture.joinToString("") { it.first().toString() }
            }
        }

        fun pp(): String {
            return picture.joinToString("\n")
        }
    }

    val seaMonsterShape = """                  # 
                            |#    ##    ##    ###
                            | #  #  #  #  #  #""".trimMargin()

    tailrec fun groupedByMatchCount(
        tiles: Sequence<Tile>,
        allTiles: List<Tile> = tiles.toList(),
        answers: Map<Int, Set<Long>> = mapOf(),
    ): Map<Int, Set<Long>> {
        if (tiles.isEmpty()) {
            return answers
        }
        val head = tiles.first()
        val countMatches = allTiles.count {
            it.label != head.label && it.matchesAnySideOf(head)
        }
        val pair = when (val value = answers[countMatches]) {
            null -> countMatches to setOf(head.label)
            else -> countMatches to (value + head.label)
        }
        return groupedByMatchCount(tiles.drop(1), allTiles, answers + pair)
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val tiles = inputFileString.splitToSequence("\n\n").map { it.lines() }.map {
            val label = it.first().substring(5).dropLast(1).toLong()
            val tile = it.drop(1).toList()

            Tile(
                label = label,
                picture = tile
            )
        }

        val neighborMap = tiles.map { t ->
            t.label to (tiles.filter { it.label != t.label && it.matchesAnySideOf(t) }.toSet())
        }.toMap()

        val x = groupedByMatchCount(tiles)

        println(tiles.count())
        println(x)

        // println(listOf(Edge.Up, Edge.Right, Edge.Down, Edge.Left).map{tiles.first()[it]})
        // println(listOf(Edge.Up, Edge.Right, Edge.Down, Edge.Left).map{tiles.first().rotate()[it]})
        // println(listOf(Edge.Up, Edge.Right, Edge.Down, Edge.Left).map{tiles.first().rotate().rotate()[it]})
        // println(listOf(Edge.Up, Edge.Right, Edge.Down, Edge.Left).map{tiles.first().rotate().rotate().rotate()[it]})

        val t = tiles.first()
        println(t[Edge.Up])
        println(t == t.flipVert().rotate().rotate())
        println()
        val s = solve(tiles.toSet())
        println(s.size)
        val n = t.picture.size
        val chunks = s.chunked(sqrt(s.size.toDouble()).toInt())

        val mega = chunks.flatMap { chunkRow ->
            (1 until (n - 1)).map { x ->
                chunkRow.joinToString("") { tile ->
                    tile.picture[x].drop(1).dropLast(1)
                }
            }
        }
        val megaTile = Tile(-1, mega)

        println(megaTile.pp())

        val seamonsterOffsets: List<Point2d> = seaMonsterShape.lines().flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> Point2d(x, y)
                    else -> null
                }
            }
        }

        val seamonsterMaxX = seamonsterOffsets.map { it.x }.maxOrNull()!!
        val seamonsterMaxY = seamonsterOffsets.map { it.y }.maxOrNull()!!

        val nsm = generateSequence(megaTile) { it.rotate() }.take(4).map {
            seamonsterLocations(it, seamonsterMaxY, seamonsterMaxX, seamonsterOffsets).count()
        }
        val total = megaTile.picture.sumBy {
            it.count { c -> c == '#' }
        }
        println(
            nsm.map {
                total - seamonsterOffsets.size * it
            }.toList()
        )

        val megaMap = megaTile.picture.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> Point2d(x, y) to '#'
                    else -> null
                }
            }
        }.toMap().toMutableMap()
        val locs = seamonsterLocations(megaTile, seamonsterMaxY, seamonsterMaxX, seamonsterOffsets)
        locs.forEach { loc ->
            seamonsterOffsets.forEach { off ->
                megaMap[off + loc] = 'O'
            }
        }

        println()
        println()
        println(
            (megaTile.picture.indices).joinToString("\n") { y ->
                (megaTile.picture.first().indices).joinToString("") { x ->
                    when (val c = megaMap[Point2d(x, y)]) {
                        null -> "."
                        else -> c.toString()
                    }
                }
            }
        )
    }

    private fun seamonsterLocations(
        megaTile: Tile,
        seamonsterMaxY: Int,
        seamonsterMaxX: Int,
        seamonsterOffsets: List<Point2d>,
    ) = (0 until (megaTile.picture.size - seamonsterMaxY)).asSequence().flatMap { y ->
        (0 until (megaTile.picture.first().length - seamonsterMaxX)).asSequence().map { x ->
            Point2d(x, y)
        }
    }.filter {
        seamonsterOffsets.map { o -> o + it }.all { megaTile.picture[it.y][it.x] == '#' }
    }

    sealed class Edge {
        object Up : Edge()
        object Down : Edge()
        object Right : Edge()
        object Left : Edge()
    }

    fun edgeMatches(
        grid: List<Tile>,
        gridEdge: Edge,
        gridX: Int,
        gridY: Int,
        newEdge: Edge,
        newTile: Tile,
        w: Int,
        h: Int,
    ): Boolean {
        val n = gridX + gridY * w
        val boundsCheck = (
            gridX < 0 ||
                (gridY < 0) ||
                (gridX >= w) ||
                (gridY >= h)
            )
        return boundsCheck ||
            (n >= grid.size) ||
            (grid[n][gridEdge] == newTile[newEdge])
    }

    fun isOk(grid: List<Tile>, newTile: Tile, x: Int, y: Int, w: Int, h: Int): Boolean {
        return edgeMatches(grid, Edge.Right, x - 1, y, Edge.Left, newTile, w, h) &&
            edgeMatches(grid, Edge.Left, x + 1, y, Edge.Right, newTile, w, h) &&
            edgeMatches(grid, Edge.Up, x, y + 1, Edge.Down, newTile, w, h) &&
            edgeMatches(grid, Edge.Down, x, y - 1, Edge.Up, newTile, w, h)
    }

    fun tryPlace(
        grid: List<Tile>,
        candidate: Tile,
        candidates: Set<Tile>,
        nextpos: Int,
        w: Int,
        h: Int,
    ): List<Tile> {
        return if (isOk(grid, candidate, nextpos % w, (nextpos / w), w, h)) {
            solve1(grid + candidate, candidates, nextpos + 1, w, h)
        } else {
            emptyList()
        }
    }

    fun solve1(
        grid: List<Tile>,
        candidates: Set<Tile>,
        nextpos: Int,
        w: Int,
        h: Int,
    ): List<Tile> {
        if (candidates.isEmpty()) {
            return grid
        }

        return candidates.asSequence().map {
            val nextCand = candidates - it
            setOf(
                it,
                it.flipVert(),
                it.rotate(),
                it.flipVert().rotate(),
                it.rotate().rotate(),
                it.flipVert().rotate().rotate(),
                it.rotate().rotate().rotate(),
                it.flipVert().rotate().rotate().rotate(),
            ).asSequence().shuffled().map { rot -> tryPlace(grid, rot, nextCand, nextpos, w, h) }

                .filter { l -> l.isNotEmpty() }.firstOrNull()
        }.filterNotNull().flatten().take(w * h).toList()
    }

    fun solve(tiles: Set<Tile>): List<Tile> {
        return solve1(emptyList(), tiles, 0, 12, 12)
    }
}
