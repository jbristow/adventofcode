import utility.*
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3
import kotlin.collections.component4
import kotlin.collections.set

object Day17 {

    @Suppress("unused")
    val sampleInput = """x=495, y=2..7
y=7, x=495..501
x=501, y=3..7
x=498, y=2..4
x=506, y=1..2
x=498, y=10..13
x=504, y=10..13
y=13, x=498..504""".lines()

    @JvmStatic
    fun main(args: Array<String>) {

        val input =
            Files.readAllLines(Paths.get("src/main/resources/day17.txt"))

        println("answer1:${Day17.answer1(input)}")
        println("answer1:${Day17.answer2(input)}")
    }

    private fun answer1(input: List<String>): Int {
        val map = input.map {
            it.parseClay()
        }.flatten().toMap().toMutableMap()
        val bottom = map.keys.maxY()!! + 1
        val top = map.keys.minY()!!

        val consider = LinkedList<Point>()
        consider.push(Point(500, 0).down)
        flow(consider, map, bottom)
        val left = map.keys.minX()!!
        val right = map.keys.maxX()!!
        printScan(map, bottom, left, right)
        return map.count { (k, v) -> k.y in (top..bottom) && v in "~|" }
    }
    private fun answer2(input: List<String>): Int {
        val map = input.map {
            it.parseClay()
        }.flatten().toMap().toMutableMap()
        val bottom = map.keys.maxY()!! + 1
        val top = map.keys.minY()!!

        val consider = LinkedList<Point>()
        consider.push(Point(500, 0).down)
        flow(consider, map, bottom)
        val left = map.keys.minX()!!
        val right = map.keys.maxX()!!
        printScan(map, bottom, left, right)
        return map.count { (k, v) -> k.y in (top..bottom) && v == "~" }
    }

    private val timestamp: String =
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            .replace(Regex("""\W"""), "")

    private fun printScan(
        map: Map<Point, String>,
        bottom: Int,
        left: Int,
        right: Int
    ) {
        PrintWriter(
            Files.newOutputStream(
                Paths.get("tracks/output-day17-part1-$timestamp.txt"),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            )
        ).use {
            (0..bottom).forEach { y ->
                it.println(
                    (left..right).joinToString("") { x ->
                        map[x, y] ?: "."
                    }
                )

            }
        }
    }
}

private fun String.parseClay(): List<Pair<Point, String>> {

    val yRangeRe = Regex("""x=(\d+), y=(\d+)\.\.(\d+)""")
    val xRangeRe = Regex("""y=(\d+), x=(\d+)\.\.(\d+)""")
    return when (this.matches(yRangeRe)) {
        true -> {
            val (_, x, ys, ye) = yRangeRe.matchEntire(this)!!.groupValues
            (ys.toInt()..ye.toInt()).map { y -> Point(x.toInt(), y) }
        }
        else -> {
            val (_, y, xs, xe) = xRangeRe.matchEntire(this)!!.groupValues
            (xs.toInt()..xe.toInt()).map { x -> Point(x, y.toInt()) }
        }
    }.toSet().map { it to "#" }
}

private tailrec fun flow(
    consider: LinkedList<Point>,
    map: MutableMap<Point, String>,
    bottom: Int
): Map<Point, String> {
    if (consider.isEmpty()) {
        return map
    }
    val cur = consider.pop()
    if (cur.down.y > bottom) {
        println("hit bottom: $cur (${consider.count()} left)")
        return flow(consider, map, bottom)
    }

    map[cur] = "|"

    return if (map.canPlace(cur.down)) {
        consider += cur.down
        flow(consider, map, bottom)
    } else {
        val flow = map.flow(cur, bottom)
        flow.points.forEach { map[it] = if (flow.contained) "~" else "|" }
        if (flow.contained) {
            if (cur.up.y >= 0) {
                consider += cur.up
            }
        } else {
            consider += flow.points.filter { (it.down.y <= bottom) && map[it.down] == null }
        }
        flow(consider, map, bottom)
    }
}

data class Flow(val points: Set<Point>, val contained: Boolean)

private fun MutableMap<Point, String>.flow(p: Point, bottom: Int): Flow {

    tailrec fun scan(
        cur: Point,
        dir: Point,
        points: Set<Point>
    ): Pair<Boolean, Set<Point>> {
        if (this[cur] ?: "." in "#") {
            return true to points
        }
        val below = cur.down
        return if (cur.y <= bottom && this[below] ?: "." in "#~") {
            scan(cur + dir, dir, points + cur)
        } else {
            false to points + cur
        }

    }


    val (wallLeft, pointsLeft) = scan(p, Point(-1, 0), setOf())
    val (wallRight, pointsRight) = scan(p, Point(1, 0), setOf())

    return Flow(pointsLeft + pointsRight + p, wallLeft && wallRight)
}

private fun MutableMap<Point, String>.canPlace(p: Point) =
    this[p] == null



