import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Day09Test {

    private val first25 = (1L..25L).toList()
    private val first25plus46 = (1L..19L) + (21L..25L) + listOf(45L)
    private val testData =
        listOf(35L, 20, 15, 25, 47, 40, 62, 55, 65, 95, 102, 117, 150, 182, 127, 219, 299, 277, 309, 576)

    @Test
    fun testValidNext() {
        assertThat { actual: Long -> Day09.isValid(actual to first25) }
            .accepts(26, 49)
            .rejects(100, 50)
    }

    @Test
    fun testValidNextPlus46() {
        assertThat { actual: Long -> Day09.isValid(actual to first25plus46) }
            .accepts(26, 64, 66)
            .rejects(65)
    }

    @Test
    fun testPart1() {
        assertThat(Day09.part01(testData, 5)).hasValue(127)
    }

    @Test
    fun testPart2() {
        assertThat(Day09.part02(testData, 127))
    }

    @Nested
    inner class Answers {
        private val fileData = Files.readAllLines(Paths.get(Day09.FILENAME)).map(String::toLong)

        @Test
        fun answerPart01() {
            assertThat(Day09.part01(fileData)).hasValue(217430975)
        }

        @Test
        fun answerPart02() {
            assertThat(Day09.part02(fileData, 217430975)).isEqualTo(28509180L)
        }
    }
}
