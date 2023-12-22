import util.AdventOfCode
import util.Point3d
import util.Point3dRange

object Day22 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 22")
        val input = inputFileLines
        val bricks = input.toBricks()

        val settledBricks = settle(bricks.toMutableList())
        println("\tPart 1: ${part1(settledBricks)}")
        println("\tPart 2: ${part2(settledBricks)}")
    }

    private fun List<String>.toBricks(): List<Brick> {
        return this.mapIndexed { i, line ->
            val (a, b) = line.split("~")
                .map { range -> range.split(",") }
                .map { (x, y, z) -> Point3d(x.toInt(), y.toInt(), z.toInt()) }
            Brick("$i", Point3dRange(a, b).toSet())
        }
    }

    private fun List<Brick>.supporting(): Map<String, List<String>> {
        return this.associate {
            it.label to
                it.cubes.map { c ->
                    c + Point3d(0, 0, 1)
                }.mapNotNull { c -> this.find { b -> b.label!=it.label && c in b } }.map { b -> b.label }
        }
    }

    private fun List<Brick>.supportedBy(): Map<String, List<String>> {
        return this.associate {
            it.label to
                it.cubes
                    .map { cube -> cube + Point3d(0, 0, -1) }
                    .mapNotNull { c -> this.find { b -> b.label!=it.label && c in b } }.map { b -> b.label }
        }
    }

    private fun part2(bricks: List<Brick>): Int {
        val supportMap = bricks.supporting()
        val supportedByMap = bricks.supportedBy()

        tailrec fun chainReaction(removed: Set<String>): Set<String> {
            val isSupporting =
                removed.map { brick -> supportMap.getValue(brick).filter { it !in removed } }
                    .filter { it.isNotEmpty() }.flatten().toSet()

            if (isSupporting.isEmpty()) {
                return removed
            }
            val nowUnsupported = isSupporting.filter { supportedByMap.getValue(it).none { label -> label !in removed } }

            if (nowUnsupported.isEmpty()) {
                return removed
            }

            return chainReaction(removed + nowUnsupported)
        }

        return bricks.sumOf {
            chainReaction(setOf(it.label)).filter { label -> label!=it.label }.size
        }
    }

    data class Brick(val label: String, val cubes: Set<Point3d>) {
        operator fun plus(p: Point3d): Brick {
            return copy(cubes = cubes.map { it + p }.toSet())
        }

        operator fun contains(p: Point3d): Boolean {
            return p in cubes
        }

        fun map(transform: (Point3d) -> Point3d): Brick {
            return copy(cubes = cubes.map(transform).toSet())
        }
    }

    private fun part1(bricks: List<Brick>): Int {
        return safeToRemove(bricks).size
    }

    private tailrec fun settle(bricks: MutableList<Brick>): List<Brick> {
        val filtered =
            bricks.filter { brick -> brick.cubes.none { it.z==0 } }
                .filter { brick ->
                    brick.cubes.map { it + Point3d(0, 0, -1) }.all {
                        bricks.none { other ->
                            other.label!=brick.label && it in other
                        }
                    } // no bricks underneath
                }

        if (filtered.isEmpty()) {
            return bricks
        }

        bricks.removeAll(filtered)
        bricks.addAll(filtered.map { brick -> brick + Point3d(0, 0, -1) })

        return settle(bricks)
    }

    private fun safeToRemove(bricks: List<Brick>): List<Brick> {
        val supports = bricks.supporting()
        val supportedBy = bricks.supportedBy()

        return supports.filter { (supportBrick, supportedBricks) ->
            supportedBricks.isEmpty() ||
                supportedBricks.all {
                    supportedBy.getValue(it).any { label -> supportBrick!=label }
                }
        }.keys.mapNotNull { bricks.find { b -> b.label==it } }
    }
}
