import org.junit.jupiter.api.Test
import utility.Point
import java.io.File
import kotlin.random.Random
import kotlin.test.assertEquals

internal class Day06Test {
    private val input =
        File("src/main/resources/day06.txt").readLines().map(String::parsePoint)
    private val data =
        "1, 1\n1, 6\n8, 3\n3, 4\n5, 5\n8, 9".lines().map(String::parsePoint)

    @Test
    fun part1_sample() {
        assertEquals(17, Day06.answer1(data))
    }

    @Test
    fun part1_sample2() {
        val rand = Random
        assertEquals(
            17,
            Day06.answer1(generateSequence {
                Point(rand.nextInt(30), rand.nextInt(30))
            }.distinct().take(25).toList())
        )
    }

    @Test
    fun part1_answer() {
        assertEquals(4754, Day06.answer1(input))
    }

    @Test
    fun part2_example() {
        assertEquals(16, Day06.answer2(32, data))
    }

    @Test
    fun part2_answer() {
        assertEquals(42344, Day06.answer2(10000, input))
    }

}