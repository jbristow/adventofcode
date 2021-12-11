import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day11Test {

    @Nested
    inner class Examples {
        private val sample = """5483143223
                               |2745854711
                               |5264556173
                               |6141336146
                               |6357385478
                               |4167524645
                               |2176841721
                               |6882881134
                               |4846848554
                               |5283751526""".trimMargin().lines()

        @Test
        fun part1() {
            assertThat(Day11.part1(sample)).isEqualTo(1656)
        }

        @Test
        fun part2() {
            assertThat(Day11.part2(sample)).isEqualTo(195)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day11.part1(Day11.inputFileLines)).isEqualTo(1652)
        }

        @Test
        fun part2() {
            assertThat(Day11.part2(Day11.inputFileLines)).isEqualTo(220)
        }
    }
}
