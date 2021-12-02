import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day02Test {

    @Nested
    inner class Examples {
        private val sample = """forward 5
                               |down 5
                               |forward 8
                               |up 3
                               |down 8
                               |forward 2""".trimMargin().lineSequence()

        @Test
        fun part1() {
            assertThat(Day02.part1(sample)).isEqualTo(150)
        }

        @Test
        fun part2() {
            assertThat(Day02.part2(sample)).isEqualTo(900)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day02.part1(Day02.inputFileLines)).isEqualTo(1660158)
        }

        @Test
        fun part2() {
            assertThat(Day02.part2(Day02.inputFileLines)).isEqualTo(1604592846)
        }
    }
}
