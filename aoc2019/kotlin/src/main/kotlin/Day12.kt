import arrow.optics.optics
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs

val ZERO_3D = Point3d(0, 0, 0)

@optics
data class Point3d(val x: Int, val y: Int, val z: Int) {
    override fun toString() = "<x=$x,y=$y,z=$z>"

    companion object
}
typealias Velocity3d = Point3d

private fun Point3d.asMoonLocation() = Moon(location = this)

typealias PointVel = Array<Int>

fun pv(p: Int): PointVel = arrayOf(p, 0)
var PointVel.point
    get() = this[0]
    set(value) {
        this[0] = value
    }
var PointVel.velocity
    get() = this[1]
    set(value) {
        this[1] = value
    }

fun Point3d.sum() = abs(x) + abs(y) + abs(z)

@optics
data class Moon(
    val location: Point3d,
    val velocity: Velocity3d = ZERO_3D.copy()
) {
    override fun toString() = "$location, $velocity"

    companion object
}

val Moon.potential: Int get() = location.sum()
val Moon.kinetic: Int get() = velocity.sum()
val Moon.total: Int get() = potential * kinetic

object Day12 {
    private const val FILENAME = "src/main/resources/day12.txt"
    private val fileData = Files.readAllLines(Paths.get(FILENAME))

    fun part1(): Int {
        return moons.step(1000).sumBy(Moon::total)
    }

    fun part2(): BigInteger {
        return lcm(
            moons.map { pv(it.location.x) }.findLoop(),
            lcm(
                moons.map { pv(it.location.y) }.findLoop(),
                moons.map { pv(it.location.z) }.findLoop()
            )
        )
    }

    private val moons = fileData.asSequence()
        .map("""<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""".toRegex()::matchEntire)
        .map { o ->
            Point3d(o!!.groupValues[1].toInt(), o.groupValues[2].toInt(), o.groupValues[3].toInt())
        }.map(Point3d::asMoonLocation).toList()
}

fun Int.gravity1d(other: Int): Int {
    return when {
        this > other -> -1
        this < other -> 1
        else -> 0
    }
}

tailrec fun Array<PointVel>.stepForeverX(
    step: Int = 0,
    seen: MutableMap<String, MutableList<Int>> = TreeMap(),
    max: Int = 1000000
): MutableMap<String, MutableList<Int>> {
    val blob = this.contentDeepToString()
    if (blob in seen) {
        seen[blob]!!.add(step)
    } else {
        seen[blob] = mutableListOf(step)
    }
    when {
        (step < max) -> {
            indices.forEach { a ->
                indices.filterNot { b -> a == b }
                    .forEach {
                        this[a].velocity += this[a].point.gravity1d(this[it].point)
                    }
            }
            indices.forEach {
                this[it].point += this[it].velocity
            }
            return this.stepForeverX(step + 1, seen)
        }
        else -> return seen
    }
}

private tailrec fun List<Moon>.step(times: Int = 1): List<Moon> {
    return when (times) {
        0 -> this
        else -> {
            indices.map { a ->
                this[a].copy(velocity =
                this[a].velocity +
                        indices.filterNot { b -> a == b }.fold(ZERO_3D) { acc, it ->
                            acc + Velocity3d(
                                x = this[a].location.x.gravity1d(this[it].location.x),
                                y = this[a].location.y.gravity1d(this[it].location.y),
                                z = this[a].location.z.gravity1d(this[it].location.z)
                            )
                        })
            }.map {
                it.copy(location = it.location + it.velocity)
            }.step(times - 1)
        }
    }
}

private operator fun Point3d.plus(other: Point3d) = this.copy(x = x + other.x, y = y + other.y, z = z + other.z)

fun main() {
    println(Day12.part1())
    println(Day12.part2())
}

private fun List<PointVel>.findLoop() =
    toTypedArray().stepForeverX().asSequence().filter { it.value.size > 1 }
        .flatMap { result ->
            result.value.asSequence().windowed(2).map { (a, b) -> b - a }.toSet().asSequence()
                .map { it to result.value.first() }
        }.groupBy { it.first }.mapValues { it.value.map { v -> v.second }.toSortedSet() }.asSequence()
        .sortedBy { it.key }.first().key

tailrec fun gcd(n1: BigInteger, n2: BigInteger): BigInteger {
    require(n1 > BigInteger.ZERO || n2 > BigInteger.ZERO) { "a or b is less than 1 ($n1,$n2)" }
    return when (val remainder: BigInteger = n1 % n2) {
        BigInteger.ZERO -> n2
        else -> gcd(n2, remainder)
    }
}

fun lcm(n1: BigInteger, n2: BigInteger) = (n1 * n2) / gcd(n1, n2)

fun lcm(n1: Int, n2: Int): BigInteger = lcm(n1.toBigInteger(), n2.toBigInteger())

fun lcm(n1: Int, n2: BigInteger): BigInteger = lcm(n1.toBigInteger(), n2)
