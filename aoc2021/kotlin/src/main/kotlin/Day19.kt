import arrow.core.compose
import util.AdventOfCode
import util.Point3d
import util.Point3d.Companion.manhattanDistance
import util.Point3d.Companion.minus
import util.Point3d.Companion.plus

object Day19 : AdventOfCode() {

    private tailrec fun Point3d.rotateZ(times: Int = 0): Point3d {
        return when {
            times < 1 -> this
            else -> Point3d(-y, x, z).rotateZ(times - 1)
        }
    }

    private tailrec fun Point3d.rotateY(times: Int = 0): Point3d {
        return when {
            times < 1 -> this
            else -> Point3d(z, y, -x).rotateY(times - 1)
        }
    }

    private tailrec fun Point3d.rotateX(times: Int = 0): Point3d {
        return when {
            times < 1 -> this
            else -> Point3d(x, -z, y).rotateX(times - 1)
        }
    }

    private fun Point3d.flipX(): Point3d {
        return copy(x = -x)
    }

    private fun Point3d.flipY(): Point3d {
        return copy(x = -y)
    }

    private fun Point3d.flipZ(): Point3d {
        return copy(x = -z)
    }

    data class Scanner(val id: Int, val beacons: List<Point3d>) {
        val allBeaconRots: List<List<Point3d>>
            get() = allRotations().map { beacons.map(it) }
    }

    data class PlacedScanner(val id: Int, val beacons: List<Point3d>, val position: Point3d = Point3d(0, 0, 0))

    fun allRotations(): Set<(Point3d) -> Point3d> {
        val initial = (0..3).flatMap { z ->
            (0..3).flatMap { y ->
                (0..3).map { x -> { p: Point3d -> p.rotateX(x).rotateY(y).rotateZ(z) } }
            }
        }

        val flipped = initial.flatMap {
            listOf(
                it.compose { p: Point3d -> p.flipX() },
                it.compose { p: Point3d -> p.flipY() },
                it.compose { p: Point3d -> p.flipZ() }
            )
        }
        return (initial + flipped).toSet()
    }

    // !wa [-8,-7,0] * [[cos 90, -sin 90, 0],[sin 90, cos 90, 0],[0, 0, 1]]
    private fun String.toScanner(): Scanner {
        val lines = lines()
        val id = Regex("""--- scanner (\d+) ---""").matchEntire(lines.first())?.groupValues?.get(1)?.toInt()!!
        val beacons =
            lines.drop(1).map { line -> line.split(",").let { (x, y, z) -> Point3d(x.toInt(), y.toInt(), z.toInt()) } }

        return Scanner(id, beacons)
    }

    private tailrec fun deduceLayout(unmatched: List<Scanner>, matched: List<PlacedScanner>): List<PlacedScanner> {
        if (unmatched.isEmpty()) {
            return matched
        }

        val current = unmatched.first()
        val remaining = unmatched.drop(1)

        val match = matched.mapNotNull { s0 ->
            current.allBeaconRots.associateWith { br ->
                s0.beacons.flatMap { b -> br.map { b - it } }.groupBy { it }
                    .filterValues { it.count() >= 12 }.keys
            }.filter { it.value.isNotEmpty() }.toList().firstOrNull()
        }.firstOrNull()
            ?: return deduceLayout(remaining + current, matched)

        val (rotatedBeacons, translation) = match
        val translated = rotatedBeacons.map { it + translation.first() }
        return deduceLayout(remaining, matched + PlacedScanner(current.id, translated, translation.first()))
    }

    fun part1(input: String): List<PlacedScanner> {
        val scanners = input.split("\n\n").map { it.toScanner() }
        return deduceLayout(scanners.drop(1), scanners.take(1).map { PlacedScanner(it.id, it.beacons) })
    }

    fun part2(input: List<PlacedScanner>): Long? {
        return input.indices.asSequence().flatMap { a ->
            (a + 1 until input.size).asSequence().map { b ->
                input[a].position.manhattanDistance(input[b].position)
            }
        }.maxOrNull()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 19")
        val placedScanners = part1(inputFileString)
        println("\tPart 1: ${placedScanners.flatMap { it.beacons }.toSet().size}")
        println("\tPart 2: ${part2(placedScanners)}")
    }
}
