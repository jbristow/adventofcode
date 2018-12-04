import java.io.File
import java.io.PrintWriter
import kotlin.math.max
import kotlin.math.min

typealias Point = Pair<Int, Int>

val Point.x get() = first
val Point.y get() = second

private fun String.parseRect(): Rectangle {
    val result = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""").find(this)
    val groups = result!!.groups
    return Rectangle(
        groups[1]!!.value,
        groups[2]!!.value.toInt(),
        groups[3]!!.value.toInt(),
        groups[4]!!.value.toInt(),
        groups[5]!!.value.toInt()
    )
}

class Rectangle(val id: String, val tl: Point, val br: Point) {
    constructor(id: String, x: Int, y: Int, width: Int, height: Int) :
            this(id, x to y, x + width to y + height)
}

private fun Rectangle.overlaps(b: Rectangle) = when {
    this.id == b.id -> false
    b.br.x <= tl.x || b.tl.x >= br.x -> false
    b.br.y <= tl.y || b.tl.y >= br.y -> false
    else -> true
}


object Day03 {
    fun answer1(input: List<String>): Int {
        val points = input
            .cartesian { a, b -> a.parseRect() to b.parseRect() }
            .filter { (a, b) -> a.overlaps(b) }
            .map { (a, b) -> a.intersection(b) }
            .reduce { set, other -> set.union(other) }


        val maxX = points.map { it.x }.max()!!
        val maxY = points.map { it.y }.max()!!
        PrintWriter(File("outputDay03-${System.currentTimeMillis()}.txt")).use { pw ->
            pw.println((0..maxY).joinToString("\n") { y ->
                (0..maxX).joinToString("") { x ->
                    when {
                        x to y in points -> "X"
                        else -> " "
                    }
                }


            })
        }

        return points.count()
    }

    fun answer2(input: List<String>) =
        input.asSequence()
            .map(String::parseRect)
            .let { rects ->
                rects.find { a -> rects.none(a::overlaps) }?.id
            }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day03.txt").readLines()
        println("answer 1: ${answer1(input)}")
        println("answer 2: ${answer2(input)}")
    }


}


fun Rectangle.intersection(b: Rectangle): Set<Point> =
    (max(tl.x, b.tl.x) until min(br.x, b.br.x)).flatMap { x ->
        (max(tl.y, b.tl.y) until min(br.y, b.br.y)).map { y -> x to y }
    }.toSet()

