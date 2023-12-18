import Day10.PipeTile
import Day10.determineStartTileType
import Day10.inside
import util.AdventOfCode
import util.Direction
import util.Down
import util.Left
import util.Point2dL
import util.Point2dLRange
import util.Right
import util.Up

object Day18 : AdventOfCode() {
    data class Instruction(val direction: Direction, val distance: Long)

    private fun part1(input: List<String>): Int {
        val instrs =
            input.map {
                it.split(" ").let { (d, length, _) ->
                    Instruction(
                        when (d) {
                            "R" -> Right
                            "D" -> Down
                            "L" -> Left
                            "U" -> Up
                            else -> throw Exception("Unknown direction $d")
                        },
                        length.toLong(),
                    )
                }
            }

        val dugSpaces = dig(instrs).toMutableMap()
        dugSpaces[Point2dL(0, 0)] = determineStartTileType(dugSpaces, Point2dL(0, 0))
        val range = Point2dLRange(dugSpaces)

        val inside = range.filter { it !in dugSpaces && it.inside(dugSpaces, range.yRange.first) }
        return dugSpaces.size + inside.size
    }

    private tailrec fun dig(
        instrs: List<Instruction>,
        corner: Point2dL = Point2dL(0, 0),
        map: MutableMap<Point2dL, PipeTile> = mutableMapOf(Point2dL(0, 0) to PipeTile.Start),
    ): Map<Point2dL, PipeTile> {
        val current = instrs.first()
        val next = instrs.drop(1).firstOrNull()

        val nextTiles =
            (1L..<current.distance).map { d ->
                (current.direction.offset * d + corner) to
                    when (current.direction) {
                        Left, Right -> PipeTile.Horizontal
                        Up, Down -> PipeTile.Vertical
                        else -> throw Exception("Illegal direction ${current.direction}")
                    }
            }
        map.putAll(nextTiles)
        if (next == null) {
            return map
        }
        val nextCorner = current.direction.offset * current.distance + corner
        map[nextCorner] =
            when {
                current.direction is Up && next.direction is Right -> PipeTile.BendSE
                current.direction is Up && next.direction is Left -> PipeTile.BendSW

                current.direction is Down && next.direction is Right -> PipeTile.BendNE
                current.direction is Down && next.direction is Left -> PipeTile.BendNW

                current.direction is Right && next.direction is Up -> PipeTile.BendNW
                current.direction is Right && next.direction is Down -> PipeTile.BendSW

                current.direction is Left && next.direction is Up -> PipeTile.BendNE
                current.direction is Left && next.direction is Down -> PipeTile.BendSE
                else -> throw Exception("Unknown combo: curr=${current.direction} to next=${next.direction}")
            }

        return dig(instrs.drop(1), nextCorner, map)
    }

    private fun findCorners(instrs: List<Instruction>): List<Point2dL> {
        return instrs.runningFold(Point2dL(0, 0)) { acc, curr ->
            acc + curr.direction.offset * curr.distance
        }
    }

    private fun part2(input: List<String>): Long {
        val instrs =
            input.map {
                it.split(" ").let { (_, _, color) ->
                    Instruction(
                        when (val d = color.drop(7).first()) {
                            '0' -> Right
                            '1' -> Down
                            '2' -> Left
                            '3' -> Up
                            else -> throw Exception("Unknown direction $d")
                        },
                        color.drop(2).take(5).toLong(16),
                    )
                }
            }

        // stolen from ruke47, who got it from https://www.mathopenref.com/coordpolygonarea.html
        val area = findCorners(instrs).windowed(2).sumOf { (a, b) -> a.x * b.y - a.y * b.x } / 2
        // since the above formula assumes ranges with exclusive maximums, we have to add the missing pieces along the x and y axes
        // All of these missing pieces will be on one side, but since our instructions don't include the start position, you have to add 1
        val outline = instrs.filter { it.direction == Down || it.direction == Right }.sumOf { it.distance } + 1
        return area + outline
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 18")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
