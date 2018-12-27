import utility.Point
import utility.allPoints
import utility.frequencies
import utility.get
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.component1
import kotlin.collections.component2

val Point.adjacent
    get() = (Point(x - 1, y - 1) to Point(
        x + 1,
        y + 1
    )).allPoints().flatten().toSet() - this

/*
    An open acre will become filled with trees if three or more adjacent acres
    contained trees. Otherwise, nothing happens.
    An acre filled with trees will become a lumberyard if three or more adjacent
     acres were lumberyards. Otherwise, nothing happens.
    An acre containing a lumberyard will remain a lumberyard if it was adjacent to
    at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open.

 */
fun Point.applyRule(acres: Map<Point, Char>): Char {
    val freq = adjacent.mapNotNull { acres[it] }.frequencies()
    return when {
        acres[this] == '.' && freq['|'] ?: 0 >= 3 -> '|'
        acres[this] == '.' -> '.'
        acres[this] == '|' && freq['#'] ?: 0 >= 3 -> '#'
        acres[this] == '|' -> '|'
        acres[this] == '#' && freq['|'] != null && freq['#'] != null -> '#'
        acres[this] == '#' -> '.'
        else -> throw Exception("Oh no! $this")
    }
}

@Suppress("unused")
val sampleInput = """.#.#...|#.
.....#|##|
.|..|...#.
..|#.....#
#.#|||#|#|
...#.||...
.|....|...
||...#|.#|
|.||||..|.
...#.|..|.""".lines()

object Day18 {
    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            Files.readAllLines(Paths.get("src/main/resources/day18.txt"))
        println("answer1: ${Day18.answer1(input)}")
        println("answer2: ${Day18.answer2(input)}")
    }

    private fun answer1(sampleInput: List<String>): Int {

        val acres = sampleInput.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Point(x, y) to c
            }
        }.flatten().toMap()
        val answer = iterate(10, acres)
        return calculateResourceScore(answer)
    }

    private fun answer2(sampleInput: List<String>): Int {

        val acres = sampleInput.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Point(x, y) to c
            }
        }.flatten().toMap()
        return iterate2(
            0, acres, emptyMap(),
            1000000000,
            sampleInput.first().count(),
            sampleInput.count()
        )
    }

    private fun calculateResourceScore(answer: Map<Point, Char>) =
        answer.count { (_, v) -> v == '#' } *
                answer.count { (_, v) -> v == '|' }

    private tailrec fun iterate2(
        i: Int,
        acres: Map<Point, Char>,
        seen: Map<String, List<Int>>,
        maxI: Int,
        width: Int,
        height: Int
    ): Int {
        val iter = (0 until width).joinToString("\n") { y ->
            (0 until height).joinToString("") { x -> acres[x, y].toString() }
        }

        val nextSeen = seen + (iter to ((seen[iter] ?: emptyList()) + i))

        if (i >= maxI || nextSeen[iter]!!.count() > 1) {
//            println(nextSeen[iter])
            val final = nextSeen[iter]!!.let { (a, b) ->
                val loopLen = b - a
                val loopPos = (maxI - b) % loopLen
                println("loop found: $a to $b")
                println("loop length: $loopLen, at $maxI position $loopPos")
                if (loopPos == 0) {
                    iter
                } else {
                    println("last is == iteration ${a + loopPos}")
                    nextSeen.toList().find { (_, v) ->
                        (a + loopPos) in v
                    }!!.first

                }

            }
            return final.count { it == '#' } * final.count { it == '|' }
        }


        val newAcres = acres.mapValues { (k, v) ->
            k.applyRule(acres)
        }
        return iterate2(i + 1, newAcres, nextSeen, maxI, width, height)
    }


    private tailrec fun iterate(
        i: Int,
        acres: Map<Point, Char>
    ): Map<Point, Char> {
//        (0..9).forEach { y ->
//            println((0..9).joinToString("") { x -> acres[x, y].toString() })
//        }
//        println("---------")
        if (i <= 0) {
            return acres
        }

        val newAcres = acres.mapValues { (k, v) ->
            k.applyRule(acres)
        }
        return iterate(i - 1, newAcres)
    }

}

