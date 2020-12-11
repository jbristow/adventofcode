import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Day10Test {

    @Nested
    inner class Part1 {
        @Test
        fun testSimple() {
            assertThat(Day10.part1(simpleData)).isEqualTo(35)
        }

        @Test
        fun testComplex() {
            assertThat(Day10.part1(complexData)).isEqualTo(220)
        }
    }

    @Nested
    inner class Part2 {
        @Test
        fun testSimple() {
            assertThat(Day10.part2(simpleData)).isEqualTo(8)
        }

        @Test
        fun testComplex() {
            assertThat(Day10.part2(complexData)).isEqualTo(19208)
        }
    }

    @Nested
    inner class Answers {
        private val fileData = Files.readAllLines(Paths.get(Day10.FILENAME)).map(String::toInt)

        @Test
        fun answerPart01() {
            assertThat(Day10.part1(fileData)).isEqualTo(1836)
        }

        @Test
        fun answerPart02() {
            assertThat(Day10.part2(fileData)).isEqualTo(43406276662336)
        }
    }

    val simpleData = """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent().lines().map(String::toInt)
    val complexData =
        """
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
        """.trimIndent().lines().map(String::toInt)
}
