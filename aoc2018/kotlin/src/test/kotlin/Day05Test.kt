import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class Day05Test {

    private val input = File("src/main/resources/day05.txt").readLines().first()

    @ParameterizedTest
    @MethodSource("part1_sample_data")
    fun part1_sample(length: Int, data: String) {
        assertEquals(length, Day05.answer1(data))
    }

    @Test
    fun part1_answer() {
        assertEquals(11668, Day05.answer1(input))
    }

    @Test
    fun part2_example() {
        assertEquals(4, Day05.answer2("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun part2_answer() {
        assertEquals(4652, Day05.answer2(input))
    }

    companion object {
        @JvmStatic
        fun part1_sample_data() = Stream.of(
            arguments(0, "aA"),
            arguments(0, "abBA"),
            arguments(4, "abAB"),
            arguments(6, "aabAAB"),
            arguments(10, "dabAcCaCBAcCcaDA")
        )
    }
}