import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day01Test {

    @Nested
    inner class Examples {
        private val sample = """199
                               |200
                               |208
                               |210
                               |200
                               |207
                               |240
                               |269
                               |260
                               |263""".trimMargin().lines().map(String::toInt)

        @Test
        fun part1() {
            assertThat(Day01.part1(sample)).isEqualTo(7)
        }

        @Test
        fun part2() {
            assertThat(Day01.part2(sample)).isEqualTo(5)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day01.part1(Day01.inputFileInts)).isEqualTo(1564)
        }

        @Test
        fun part2() {
            assertThat(Day01.part2(Day01.inputFileInts)).isEqualTo(1611)
        }
    }
}
