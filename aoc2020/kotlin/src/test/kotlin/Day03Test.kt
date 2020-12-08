import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Day03Test {
    val testData = """..##.......
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

    val data = Files.readAllLines(Paths.get(Day03.FILENAME))
    @Test
    fun testPart01() {
        assertThat(Day03.part1(testData)).isEqualTo(7)
    }

    @Test
    fun testPart02() {
        assertThat(Day03.part2(testData)).isEqualTo(336)
    }

    @Test
    fun answerPart01() {
        assertThat(Day03.part1(data)).isEqualTo(257)
    }

    @Test
    fun answerPart02() {
        assertThat(Day03.part2(data)).isEqualTo(1744787392)
    }
}
