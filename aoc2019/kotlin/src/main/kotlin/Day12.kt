import arrow.optics.optics
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.*
import kotlin.math.abs

val ZERO_3D = Point3d(0, 0, 0)

@optics
data class Point3d(var x: Int, var y: Int, var z: Int) {
    override fun toString() = "$x,$y,$z"

    companion object
}
typealias Velocity3d = Point3d

fun Point3d.sum() = abs(x) + abs(y) + abs(z)

@optics
data class Moon(
    val name: String = UUID.randomUUID().toString(),
    var location: Point3d,
    val velocity: Velocity3d = ZERO_3D.copy()
) {
    override fun toString() = "$location,$velocity"

    companion object
}


val Moon.potential: Int get() = location.sum()
val Moon.kinetic: Int get() = velocity.sum()
val Moon.total: Int get() = potential * kinetic

object Day12 {
    private const val FILENAME = "src/main/resources/day12.txt"
    val fileData = Files.readAllLines(Paths.get(FILENAME))
}

fun Int.gravity1d(other: Int): Int {
//    println("gravity: $this, $other")
    return when {
        this > other -> -1
        this < other -> 1
        else -> 0
    }
}


@ExperimentalStdlibApi
fun main() {
    val moons = arrayOf(
        Moon(location = Point3d(-16, -1, -12)),
        Moon(location = Point3d(0, -4, -17)),
        Moon(location = Point3d(-11, 11, 0)),
        Moon(location = Point3d(2, 2, -6))
    )
    val testMoons = arrayOf(
        Moon(location = Point3d(x = -1, y = 0, z = 2)),
        Moon(location = Point3d(x = 2, y = -10, z = -7)),
        Moon(location = Point3d(x = 4, y = -8, z = 8)),
        Moon(location = Point3d(x = 3, y = 5, z = -1))
    )
    val testMoons2 = arrayOf(
        Moon(location = Point3d(-8, -10, 0)),
        Moon(location = Point3d(5, 5, 10)),
        Moon(location = Point3d(2, -7, 3)),
        Moon(location = Point3d(9, -8, -3))
    )

    val newMoons = PrintWriter(Files.newOutputStream(Paths.get("day12out.csv"))).use {
        moons.stepForever(out = it)
    }
    println(newMoons.first)
    println(newMoons.second.joinToString("\n"))
}

private tailrec fun Array<Moon>.step(times: Int = 1): Array<Moon> {
    return when (times) {
        0 -> this
        else -> {
            indices.forEach { a ->
                indices.filterNot { b -> a == b }
                    .forEach {
                        this[a].velocity.x += this[a].location.x.gravity1d(this[it].location.x)
                        this[a].velocity.y += this[a].location.y.gravity1d(this[it].location.y)
                        this[a].velocity.z += this[a].location.z.gravity1d(this[it].location.z)
                    }
            }
            indices.forEach {
                this[it].location += this[it].velocity
            }
            println("$times)\n\t${this.joinToString("\n\t")}")
            println("\t${this.sumBy { it.total }}")
            step(times - 1)
        }
    }
}

fun String.toMD5(): ByteArray {
    return MessageDigest.getInstance("MD5").digest(toByteArray())
}

val base64 = Base64.getEncoder()

@ExperimentalStdlibApi
private tailrec fun Array<Moon>.stepForever(
    step: Int = 0,
    seen: MutableMap<Int, MutableSet<String>> = mutableMapOf(),
    out: PrintWriter
): Pair<Int, Array<Moon>> {

    val total = this[0].total
    val blob = this[0].toString()
    return when {
        total in seen && seen[total]?.contains(blob) ?: false -> step to this
        else -> {
            val next = seen.getOrDefault(total, mutableSetOf())
            next.add(blob)
            seen[total] = next
            indices.forEach { a ->
                indices.filterNot { b -> a == b }
                    .forEach {
                        this[a].velocity.x += this[a].location.x.gravity1d(this[it].location.x)
                        this[a].velocity.y += this[a].location.y.gravity1d(this[it].location.y)
                        this[a].velocity.z += this[a].location.z.gravity1d(this[it].location.z)
                    }
            }
            indices.forEach {
                this[it].location += this[it].velocity
            }
            //out.println("$step,${this.joinToString(",")}")
            stepForever(step + 1, seen, out)
        }
    }
}

private operator fun Point3d.plus(other: Point3d) = this.apply {
    x += other.x
    y += other.y
    z += other.z
}
