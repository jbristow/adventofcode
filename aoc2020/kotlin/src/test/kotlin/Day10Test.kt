import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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

        @Test
        fun answerPart01() {
            assertThat(Day10.part1(Day10.inputFileInts)).isEqualTo(1836)
        }

        @Test
        fun answerPart02() {
            assertThat(Day10.part2(Day10.inputFileInts)).isEqualTo(43406276662336)
        }
    }

    val simpleData = "16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4".lines().map(String::toInt)
    val complexData = (
        "28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19" +
            "\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3"
        ).lines().map(String::toInt)
}
