import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Day01Test {
    private val testData01 = """1721
                               |979
                               |366
                               |299
                               |675
                               |1456"""
        .trimMargin().lines().map(String::toInt)
    private val actualData = Files.readAllLines(Paths.get(Day01.FILENAME)).map(String::toInt)

    @Test
    fun part01Test() {
        assertThat(Day01.part1(testData01)).isEqualTo(514579)
    }

    @Test
    fun part02Test() {
        assertThat(Day01.part2(testData01)).isEqualTo(241861950)
    }

    @Test
    fun part01Answer() {
        assertThat(Day01.part1(actualData)).isEqualTo(889779)
    }

    @Test
    fun part02Answer() {
        assertThat(Day01.part2(actualData)).isEqualTo(76110336)
    }
}
