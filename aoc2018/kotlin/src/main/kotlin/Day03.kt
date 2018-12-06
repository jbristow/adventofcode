import utility.Point
import utility.init
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.math.max
import kotlin.math.min


fun String.parseRect(): Rectangle {
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
            this(id, Point(x, y), Point(x + width, y + height))
}

private fun Rectangle.overlaps(b: Rectangle) = when {
    this.id == b.id -> false
    b.br.x <= tl.x || b.tl.x >= br.x -> false
    b.br.y <= tl.y || b.tl.y >= br.y -> false
    else -> true
}

fun <T> Sequence<Set<T>>.unionAll() =
    this.fold(emptySet<T>()) { a, b -> a.union(b) }

object Day03 {
    fun answer1(input: List<Rectangle>) =
        input.init.asSequence()
            .mapIndexed { i, a ->
                input.drop(i + 1).asSequence().filter {
                    a.overlaps(it)
                }.map { b ->
                    a.intersection(b)
                }.unionAll()
            }.unionAll()
            .count()


    fun answer2(rects: List<Rectangle>) =
        rects.find { a -> rects.none(a::overlaps) }?.id

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val input = File("src/main/resources/day03.txt").readLines()
                .map(String::parseRect)

            println("answer 1: ${answer1(input)}")
            println("answer 2: ${answer2(input)}")
        }
    }
}

fun Rectangle.intersection(b: Rectangle): Set<Point> =
    (max(tl.x, b.tl.x) until min(br.x, b.br.x)).asSequence()
        .map { x ->
            (max(tl.y, b.tl.y) until min(br.y, b.br.y)).asSequence()
                .map { y -> Point(x, y) }
        }.flatten().toSet()

