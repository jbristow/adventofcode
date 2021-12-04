import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day03Test {

    @Nested
    inner class Examples {
        private val sample = """00100
                               |11110
                               |10110
                               |10111
                               |10101
                               |01111
                               |00111
                               |11100
                               |10000
                               |11001
                               |00010
                               |01010""".trimMargin().lines()

        @Test
        fun part1() {
            assertThat(Day03.part1(sample)).isEqualTo(198)
        }

        @Test
        fun part2() {
            assertThat(Day03.part2(sample)).isEqualTo(230)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day03.part1(Day03.inputFileLines)).isEqualTo(3959450)
        }

        @Test
        fun part2() {
            assertThat(Day03.part2(Day03.inputFileLines)).isEqualTo(7440311)
        }
    }
}
