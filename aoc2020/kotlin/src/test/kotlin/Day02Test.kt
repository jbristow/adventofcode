import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day02Test {

    @Test
    fun validTest01() {
        assertThat(Day02::validateLinePart1)
            .accepts("1-3 a: abcde", "2-9 c: ccccccccc")
            .rejects("1-3 b: cdefg")
    }

    @Test
    fun validTest02() {
        assertThat(Day02::validateLinePart2)
            .accepts("1-3 a: abcde")
            .rejects("1-3 b: cdefg", "2-9 c: ccccccccc")
    }

    @Nested
    inner class Answer {
        @Test
        fun part01() {
            assertThat(Day02.part1(Day02.inputFileLines)).isEqualTo(506)
        }

        @Test
        fun part02() {
            assertThat(Day02.part2(Day02.inputFileLines)).isEqualTo(443)
        }
    }
}
