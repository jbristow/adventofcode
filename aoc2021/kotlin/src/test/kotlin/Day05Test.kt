import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day05Test {

    @Nested
    inner class Examples {
        private val sample = ("0,9 -> 5,9\n" +
            "8,0 -> 0,8\n" +
            "9,4 -> 3,4\n" +
            "2,2 -> 2,1\n" +
            "7,0 -> 7,4\n" +
            "6,4 -> 2,0\n" +
            "0,9 -> 2,9\n" +
            "3,4 -> 1,4\n" +
            "0,0 -> 8,8\n" +
            "5,5 -> 8,2").lines()

        @Test
        fun part1() {
            assertThat(Day05.part1(sample)).isEqualTo(5)
        }

        @Test
        fun part2() {
            assertThat(Day05.part2(sample)).isEqualTo(12)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day05.part1(Day05.inputFileLines)).isEqualTo(4873)
        }

        @Test
        fun part2() {
            assertThat(Day05.part2(Day05.inputFileLines)).isEqualTo(19472)
        }
    }
}
