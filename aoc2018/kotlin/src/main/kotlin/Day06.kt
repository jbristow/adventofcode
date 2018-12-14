import utility.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun String.parsePoint() = this.split(""", """).let { found ->
    Point(found[0].toInt(), found[1].toInt())
}

object Day06 {

    fun answer1(input: List<Point>): Int? {
        val minX = input.minX()!!
        val minY = input.minY()!!
        val maxX = input.maxX()!!
        val maxY = input.maxY()!!

        val output =
            (minY..maxY).mapNotNull { y ->
                (minX..maxX).mapNotNull { x ->
                    input.findClosest(Point(x, y))
                }.ifEmpty { null }
            }.flatten()

        drawPicturePart1(output, input)

        val infinites = output.filter { (p, _) ->
            p.x == minX || p.x == maxX || p.y == minY || p.y == maxY
        }.map { it.second }.toSet()

        return output.filterNot { (_, closest) -> closest in infinites }
            .groupBy { (_, c) -> c }
            .mapValues { (_, v) -> v.count() }
            .maxBy { (_, ps) -> ps }!!
            .value
    }

    private fun List<Point>.findClosest(
        point: Point
    ) =
        groupBy { k -> point.manhattanDistance(k) }
            .minByKey()!!
            .value
            .mapToOrigin(point)

    private fun List<Point>.mapToOrigin(point: Point) =
        when {
            count() == 1 -> point to first()
            else -> null
        }

    fun answer2(dist: Int, input: List<Point>): Int? {

        val minX = input.minX()!!
        val minY = input.minY()!!
        val maxX = input.maxX()!!
        val maxY = input.maxY()!!

        val points =
            (minX..maxX).map { x ->
                (minY..maxY).map { y -> Point(x, y) }
                    .filter { point -> input.sumBy(point::manhattanDistance) < dist }
            }.fold(emptySet<Point>()) { a, b -> a + b }

        drawPicturePart2(points, input)

        return points.count()
    }

    private fun drawPicturePart1(
        ptc: List<Pair<Point, Point>>,
        input: List<Point>
    ) {
        val minX = input.minX()!!
        val minY = input.minY()!!

        val topLeft = Point(minX, minY)

        val rand = Random()
        val colors = generateSequence {
            (rand.nextInt(0x8) + 3) * 0x110000 +
                    (rand.nextInt(0x8) + 3) * 0x1100 +
                    (rand.nextInt(0x8) + 3) * 0x11
        }.distinct().take(input.count()).toList()
        val color = input.mapIndexed { i, point ->
            point to colors[i]
        }.toMap()

        BufferedImage(
            input.maxX()!! - minX + 1,
            input.maxY()!! - minY + 1,
            BufferedImage.TYPE_INT_RGB
        ).floodFill(0x000000)
            .colorPixels(ptc.map { (loc, origin) -> (loc - topLeft) to color[origin]!! })
            .colorPixels(input.map { it - topLeft }, 0xFFFFFF)
            .resizeToWidth(1080)
            .writePng("day06-output-part1-${System.currentTimeMillis()}")
    }


    private fun drawPicturePart2(
        ptc: Set<Point>,
        input: List<Point>
    ) {
        val minX = min(ptc.minX()!!, input.minX()!!)
        val minY = min(ptc.minY()!!, input.minY()!!)

        val topLeft = Point(minX, minY)

        BufferedImage(
            max(ptc.maxX()!!, input.maxX()!!) - minX + 1,
            max(ptc.maxY()!!, input.maxY()!!) - minY + 1,
            BufferedImage.TYPE_INT_RGB
        ).floodFill(0x000000)
            .colorPixels(ptc.map { it -> it - topLeft }, 0x559999)
            .colorPixels(input.map { it -> it - topLeft }, 0xFF0000)
            .resizeToWidth(1080)
            .writePng("day06-output-part2-${System.currentTimeMillis()}")
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            File("src/main/resources/day06.txt").readLines()
                .map(String::parsePoint)
        println("answer 1: ${this.answer1(input)}")
        println("answer 2: ${this.answer2(10000, input)}")
    }
}






