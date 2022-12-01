import Day01.calculateElfLoad
import Day01.splitByElf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class Day01Test {
    val lines = """1000
                  |2000
                  |3000
                  |
                  |4000
                  |
                  |5000
                  |6000
                  |
                  |7000
                  |8000
                  |9000
                  |
                  |10000""".trimMargin()

    @Nested
    inner class Part1 {
        @Test
        fun testSplitting() {
            assertThat(lines.splitByElf()).hasSize(5)
        }

        @Test
        fun testMapping() {
            assertThat(lines.splitByElf().map { it.calculateElfLoad() })
                .containsExactly(6000, 4000, 11000, 24000, 10000)
        }

        @Test
        fun testExampleAnswer() {
            assertThat(Day01.part1(lines)).isEqualTo(24000)
        }
    }

    @Nested
    inner class Part2 {
        @Test
        fun testExampleAnswer() {
            assertThat(Day01.part2(lines)).isEqualTo(45000)
        }
    }

    @Nested
    inner class Answers {
        @Test
        fun part1() {
            assertThat(Day01.part1(Day01.inputFileString)).isEqualTo(64929)
        }

        @Test
        fun part2() {
            assertThat(Day01.part2(Day01.inputFileString)).isEqualTo(193697)
        }
    }
}