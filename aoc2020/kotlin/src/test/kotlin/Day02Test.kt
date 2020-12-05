import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Day02Test {

    val data = Files.readAllLines(Paths.get(Day02.FILENAME))
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

    @Test
    fun part01Answer() {
        assertThat(Day02.part1(data)).isEqualTo(506)
    }

    @Test
    fun part02Answer() {
        assertThat(Day02.part2(data)).isEqualTo(443)
    }
}