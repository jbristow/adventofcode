import util.AdventOfCode
import util.Point2d
import util.Point2dL

object Day21 : AdventOfCode() {
    sealed interface Tile {
        data object Rock : Tile

        data object Garden : Tile

        data object Start : Tile
    }

    private fun part1(input: List<String>): Int {
        val map: Map<Point2d, Tile> =
            input.flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, c ->
                    when (c) {
                        '#' -> Point2d(x, y) to Tile.Rock
                        '.' -> Point2d(x, y) to Tile.Garden

                        'S' -> Point2d(x, y) to Tile.Start
                        else -> null
                    }
                }
            }.toMap()
        val start = map.entries.find { (_, v) -> v is Tile.Start }?.key!!
        val reachable = map.navigate(setOf(start), 64)

        println(reachable.size)
        return -1
    }

    private tailrec fun Map<Point2d, Tile>.navigate(
        distances: Set<Point2d>,
        maxDist: Int,
        currDist: Int = 0,
    ): Set<Point2d> {
        val map = this
        if (currDist >= maxDist) {
            return distances
        }

        val nextDist = currDist + 1
        val next = distances.flatMap { it.orthoNeighbors }.toSet()
        val filtered = next.filter { map[it] is Tile.Garden || map[it] is Tile.Start }.toSet()
        return this.navigate(filtered, maxDist, nextDist)
    }

    private tailrec fun distanceMap(
        gardens: Set<Point2dL>,
        start: Point2dL,
        shortestPaths: Map<Point2dL, Long> = mapOf(start to 0L),
        q: List<Pair<Long, Point2dL>> = listOf(0L to start),
    ): Map<Point2dL, Long> {
        if (q.isEmpty()) {
            return shortestPaths
        }

        val (depth, curr) = q.first()
        val neighbors = curr.orthoNeighbors.filter { it in gardens && it !in shortestPaths }
        return distanceMap(
            gardens,
            start,
            neighbors.fold(shortestPaths) { prevs, n -> prevs + (n to depth + 1) },
            q.drop(1) + neighbors.map { depth + 1 to it },
        )
    }

    private fun Long.squared() = this * this

    private fun part2(input: List<String>): Long {
        val start =
            input.withIndex().firstNotNullOf { (y, line) ->
                when (val x = line.indexOf('S')) {
                    -1 -> null
                    else -> Point2dL(x, y)
                }
            }

        val steps = 26501365
        val dists =
            distanceMap(
                input.flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, c ->
                        when (c) {
                            '.' -> Point2dL(x, y)
                            else -> null
                        }
                    }
                }.toSet(),
                start,
            )

        val width = input.size.toLong()

        val halfWidth = start.x
        val repeatRadius = ((steps - halfWidth) / width)

        // each filled original map will be filled by alternating parity (odd/even) from the origin
        val allParityPoints = dists.filterValues { it % 2 != repeatRadius % 2 }
        val allNonParityPoints = dists.filterValues { it % 2 == repeatRadius % 2 }

        // in our 3x3 example the repeat radius would have to be 1, so there are 4 partially filled squares
        val partiallyFilledSquares = (repeatRadius + 1).squared()
        // and 1 internally filled square
        val fullyFilledSquares = repeatRadius.squared()

        val totalFilledParity = allParityPoints.size * partiallyFilledSquares
        val totalFilledNonParity = allNonParityPoints.size * fullyFilledSquares

        // clear out the middle of the original map to show "walking in from a corner"
        // this will only work because our input is an odd-number-length square, our start point is in the exact middle,
        // and for some reason the distance to each corner is identical and can be reached in (width-1) steps.
        val totalOutside = allParityPoints.count { it.value > halfWidth } * (repeatRadius + 1)
        val totalPartial = allNonParityPoints.count { it.value > halfWidth } * repeatRadius

        return totalFilledParity + totalFilledNonParity - totalOutside + totalPartial
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 21")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
