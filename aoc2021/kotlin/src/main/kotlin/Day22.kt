import util.AdventOfCode
import util.Point3d
import kotlin.math.max
import kotlin.math.min

object Day22 : AdventOfCode() {
    sealed class Flip {
        object On : Flip()
        object Off : Flip()

        override fun toString(): String {
            return when (this) {
                On -> "on"
                Off -> "off"
            }
        }
    }

    private fun String.toFlip(): Flip {
        return when (this) {
            "on" -> Flip.On
            "off" -> Flip.Off
            else -> throw IllegalArgumentException("Unknown flip type $this")
        }
    }

    data class CubeStep(val status: Flip, val cubeoid: Cuboid)

    private val stepRegex = Regex("""(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)""")

    private fun String.parseLine(): CubeStep {
        val match = stepRegex.matchEntire(this) ?: throw IllegalArgumentException("Couldn't parse '$this'")
        return CubeStep(
            match.groupValues[1].toFlip(), Cuboid(
                max(-50, match.groupValues[2].toInt()), min(50, match.groupValues[3].toInt()),
                max(-50, match.groupValues[4].toInt()), min(50, match.groupValues[5].toInt()),
                max(-50, match.groupValues[6].toInt()), min(50, match.groupValues[7].toInt()),
            ))
    }

    private fun String.parseLineUnbounded(): CubeStep {
        val match = stepRegex.matchEntire(this) ?: throw IllegalArgumentException("Couldn't parse '$this'")
        return CubeStep(
            match.groupValues[1].toFlip(),
            Cuboid(match.groupValues[2].toInt(), match.groupValues[3].toInt(),
                match.groupValues[4].toInt(), match.groupValues[5].toInt(),
                match.groupValues[6].toInt(), match.groupValues[7].toInt()),
        )
    }

    fun part1(input: List<String>) =
        input.map { it.parseLine() }.fold(mutableSetOf<Point3d>()) { pointsOn, step ->
            when (step.status) {
                Flip.On -> pointsOn.addAll(step.cubeoid.points)
                Flip.Off -> pointsOn.removeAll(step.cubeoid.points)
            }
            pointsOn
        }.size

    data class Cuboid(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int, val zMin: Int, val zMax: Int) {
        val volume: Long
            get() = (xMin..xMax).count().toLong() * (yMin..yMax).count().toLong() * (zMin..zMax).count().toLong()
        val points: Set<Point3d>
            get() = (xMin..xMax).flatMap { x ->
                (yMin..yMax).flatMap { y ->
                    (zMin..zMax).map { z ->
                        Point3d(x,
                            y,
                            z)
                    }
                }
            }.toSet()
    }

    private fun Cuboid.nonIntersecting(other: Cuboid): Set<Cuboid> {
        val b = copy(
            xMin = if (other.xMin < xMin) xMin else other.xMin,
            yMin = if (other.yMin < yMin) yMin else other.yMin,
            zMin = if (other.zMin < zMin) zMin else other.zMin,
            xMax = if (other.xMax > xMax) xMax else other.xMax,
            yMax = if (other.yMax > yMax) yMax else other.yMax,
            zMax = if (other.zMax > zMax) zMax else other.zMax,
        )

        if (b.volume <= 0) {
            return setOf(this)
        }

        if (xMin >= b.xMin && xMax <= b.xMax
            && yMin >= b.yMin && yMax <= b.yMax
            && zMin >= b.zMin && zMax <= b.zMax
        ) {
            return setOf()
        }

        return sequenceOf(
            Cuboid(xMin, b.xMin - 1, yMin, yMax, zMin, zMax),
            Cuboid(b.xMin, b.xMax, yMin, b.yMin - 1, zMin, zMax),
            Cuboid(b.xMin, b.xMax, b.yMin, b.yMax, zMin, b.zMin - 1),
            Cuboid(b.xMin, b.xMax, b.yMin, b.yMax, b.zMax + 1, zMax),
            Cuboid(b.xMin, b.xMax, b.yMax + 1, yMax, zMin, zMax),
            Cuboid(b.xMax + 1, xMax, yMin, yMax, zMin, zMax),
        ).filter { it.volume > 0 }.toSet()
    }

    fun part2(input: List<String>) =
        processSteps(input.asSequence().map { it.parseLineUnbounded() }).sumOf { it.volume }

    private tailrec fun processSteps(steps: Sequence<CubeStep>, cuboids: MutableSet<Cuboid> = mutableSetOf()): Set<Cuboid> {
        println("saw ${cuboids.size} cuboids!")
        if (steps.firstOrNull() == null) {
            return cuboids
        }
        val current = steps.first()
        val remaining = steps.drop(1)

        return when (current.status) {
            Flip.Off -> processSteps(remaining, cuboids.flatMap { it.nonIntersecting(current.cubeoid) }.toMutableSet())
            Flip.On -> {
                cuboids.addAll(cuboids.fold(setOf(current.cubeoid)) { acc, seen ->
                    acc.flatMap { it.nonIntersecting(seen) }.toSet()
                })
                processSteps(remaining, cuboids)
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 22")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}

