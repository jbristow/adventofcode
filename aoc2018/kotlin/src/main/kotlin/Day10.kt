import utility.*
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {

        val input =
            File("src/main/resources/day10.txt").readLines()
                .map(String::parsePositionVelocity)

        println("answer1: ${Day10.answer1(input)}")
    }

    private fun answer1(input: List<Pair<Point, Point>>): String {
        //println(input)
        println(input.minBy { Point(it.first.x, it.first.y) })
        println(input.maxBy { Point(it.first.x, it.first.y) })
        PrintWriter(
            File(
                "Day10-Part1-${LocalDateTime.now().format(
                    DateTimeFormatter.ISO_DATE_TIME
                )}.txt"
            )
        ).use { pw ->
            (10101..10109).forEach { n ->
                val points = input.map { it.first + it.second * n }
                println("step $n")
                pw.println("--------------")
                (points.minY()!!..points.maxY()!!).forEach { y ->

                    pw.println(
                        (points.minX()!!..points.maxX()!!).joinToString("") { x ->
                            if (Point(x, y) in points) {
                                "#"
                            } else {
                                "."
                            }
                        })
                }

            }
        }
        return ""
    }
}

private fun String.parsePositionVelocity(): Pair<Point, Point> {
    val re =
        Regex("""position=<\s*(-?\d+),\s+(-?\d+)> velocity=<\s*(-?\d+),\s+(-?\d+)>""")
    val gv = re.matchEntire(this)?.groupValues!!
    return Point(gv[1].trim().toInt(), gv[2].trim().toInt()) to Point(
        gv[3].trim().toInt(),
        gv[4].trim().toInt()
    )
}
