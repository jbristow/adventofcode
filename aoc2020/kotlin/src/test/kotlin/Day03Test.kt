import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day03Test {
    private val testData = """..##.......
                             |#...#...#..
                             |.#....#..#.
                             |..#.#...#.#
                             |.#...##..#.
                             |..#.##.....
                             |.#.#.#....#
                             |.#........#
                             |#.##...#...
                             |#...##....#
                             |.#..#...#.#""".trimMargin().lines()

    @Test
    fun testPart01() {
        assertThat(Day03.part1(testData)).isEqualTo(7)
    }

    @Test
    fun testPart02() {
        assertThat(Day03.part2(testData)).isEqualTo(336)
    }

    @Nested
    inner class Answer {
        @Test
        fun part01() {
            assertThat(Day03.part1(Day03.inputFileLines)).isEqualTo(257)
        }

        @Test
        fun part02() {
            assertThat(Day03.part2(Day03.inputFileLines)).isEqualTo(1744787392)
        }
    }
}
