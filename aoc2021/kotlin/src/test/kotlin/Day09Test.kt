import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Nested
    inner class Examples {
        private val sample = """2199943210
                               |3987894921
                               |9856789892
                               |8767896789
                               |9899965678""".trimMargin().lines()

        @Test
        fun part1() {
            assertThat(Day09.part1(sample)).isEqualTo(15)
        }

        @Test
        fun part2() {
            assertThat(Day09.part2(sample)).isEqualTo(1134)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day09.part1(Day09.inputFileLines)).isEqualTo(526)
        }

        @Test
        fun part2() {
            assertThat(Day09.part2(Day09.inputFileLines)).isEqualTo(1123524)
        }
    }
}
