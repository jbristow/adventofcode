import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Day15Test {
    @Nested
    inner class Part1 {

        @ParameterizedTest
        @MethodSource("dataSupplier")
        fun testPart1(input: List<Int>, expected: Int) {
            assertThat(Day15.part1(input)).isEqualTo(expected)
        }

        fun dataSupplier() = Stream.of(
            arguments(listOf(0, 3, 6), 436),
            arguments(listOf(1, 3, 2), 1),
            arguments(listOf(2, 1, 3), 10),
            arguments(listOf(1, 2, 3), 27),
            arguments(listOf(2, 3, 1), 78),
            arguments(listOf(3, 2, 1), 438),
            arguments(listOf(3, 1, 2), 1836))
    }

    @Nested
    inner class Part2 {

        @ParameterizedTest
        @MethodSource("dataSupplier")
        fun testPart2(input: List<Int>, expected: Int) {
            assertThat(Day15.part2(input)).isEqualTo(expected)
        }

        fun dataSupplier() = Stream.of(
            arguments(listOf(0, 3, 6), 175594),
            arguments(listOf(1, 3, 2), 2578),
            arguments(listOf(2, 1, 3), 3544142),
            arguments(listOf(1, 2, 3), 261214),
            arguments(listOf(2, 3, 1), 6895259),
            arguments(listOf(3, 2, 1), 18),
            arguments(listOf(3, 1, 2), 362))
    }

    @Nested
    inner class Answer {
        val fileInput = Day15.inputFileString.split(",").map(String::toInt)

        @Test
        fun part1() {
            assertThat(Day15.part1(fileInput)).isEqualTo(257)
        }
        @Test
        fun part2() {
            assertThat(Day15.part2(fileInput)).isEqualTo(8546398)
        }

    }
}