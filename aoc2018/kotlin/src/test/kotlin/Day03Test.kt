import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class Day03Test {
    private val input =
        File("src/main/resources/day03.txt").readLines().map(String::parseRect)

    private val sampleData =
        "#1 @ 1,3: 4x4\n#2 @ 3,1: 4x4\n#3 @ 5,5: 2x2".lines()
            .map(String::parseRect)

    @Test
    fun part1_sample() {
        assertEquals(4, Day03.answer1(sampleData))
    }

    @Test
    fun part2_sample() {
        assertEquals(
            "3",
            Day03.answer2(
                "#1 @ 1,3: 4x4\n#2 @ 3,1: 4x4\n#3 @ 5,5: 2x2".lines()
                    .map(String::parseRect)
            )
        )
    }

    @Test
    fun part1_answer() {
        assertEquals(113966, Day03.answer1(input))
    }

    @Test
    fun part2_answer() {
        assertEquals("235", Day03.answer2(input))
    }
}
