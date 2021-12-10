import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day10Test {

    @Nested
    inner class Examples {
        private val sample = """[({(<(())[]>[[{[]{<()<>>
                               |[(()[<>])]({[<{<<[]>>(
                               |{([(<{}[<>[]}>{[]{[(<()>
                               |(((({<>}<{<{<>}{[]{[]{}
                               |[[<[([]))<([[{}[[()]]]
                               |[{[{({}]{}}([{[{{{}}([]
                               |{<[[]]>}<{[{[{[]{()[[[]
                               |[<(<(<(<{}))><([]([]()
                               |<{([([[(<>()){}]>(<<{{
                               |<{([{{}}[<[[[<>{}]]]>[]]""".trimMargin().lines()

        @Test
        fun part1() {
            assertThat(Day10.part1(sample)).isEqualTo(26397)
        }

        @Test
        fun part2() {
            assertThat(Day10.part2(sample)).isEqualTo(288957)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day10.part1(Day10.inputFileLines)).isEqualTo(216297)
        }

        @Test
        fun part2() {
            assertThat(Day10.part2(Day10.inputFileLines)).isEqualTo(2165057169L)
        }
    }
}
