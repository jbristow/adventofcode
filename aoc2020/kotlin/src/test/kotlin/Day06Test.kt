import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day06Test {
    val testData = """
            abc
            
            a
            b
            c
            
            ab
            ac
            
            a
            a
            a
            a
            
            b
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        fun part1() {
            assertThat(Day06.part1(testData)).isEqualTo(11)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        fun part2() {
            assertThat(Day06.part2(testData)).isEqualTo(6)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day06.part1(Day06.inputFileString)).isEqualTo(6170)
        }

        @Test
        fun part2() {
            assertThat(Day06.part2(Day06.inputFileString)).isEqualTo(2947)
        }
    }
}
