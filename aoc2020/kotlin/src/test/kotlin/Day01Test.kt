import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day01Test {
    private val testData01 = """1721
                               |979
                               |366
                               |299
                               |675
                               |1456"""
        .trimMargin().lines().map(String::toInt)

    @Test
    fun part01Test() {
        assertThat(Day01.part1(testData01)).isEqualTo(514579)
    }

    @Test
    fun part02Test() {
        assertThat(Day01.part2(testData01)).isEqualTo(241861950)
    }

    @Nested
    inner class Answer {
        @Test
        fun part01() {
            assertThat(Day01.part1(Day01.inputFileInts)).isEqualTo(889779)
        }

        @Test
        fun part02() {
            assertThat(Day01.part2(Day01.inputFileInts)).isEqualTo(76110336)
        }
    }
}
