import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import kotlin.test.assertEquals

internal class Day02Test {
    private val input = File("src/main/resources/day02.txt").readLines()

    @Test
    fun part1_sample() {
        val data = listOf(
            "abcdef", "bababc", "abbcde", "abcccd",
            "aabcdd", "abcdee", "ababab"
        )
        assertEquals(12, Day02.answer1(data))
    }

    @Test
    fun part2_sample() {
        val data = "abcde\nfghij\nklmno\npqrst\nfguij\naxcye\nwvxyz".split("\n")
        assertEquals("fgij", Day02.answer2(data))
    }

    @Test
    fun `part1 answer`() {
        assertEquals(6944, Day02.answer1(input))
    }

    @RepeatedTest(value = 100, name = "part2 {currentRepetition}")
    fun part2_answer() {
        val shuff = input.shuffled()
        assertEquals("srijafjzloguvlntqmphenbkd", Day02.answer2(shuff))
    }

    @ParameterizedTest
    @MethodSource("inputProvider")
    fun bencher(data: List<String>) {
        assertEquals("abc", Day02.answer2(data))
    }
}