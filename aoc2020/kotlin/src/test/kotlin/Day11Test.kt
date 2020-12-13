import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class Day11Test {

    @ParameterizedTest
    @MethodSource("charToSeatProvider")
    fun testSeatmaker(c: Char, seat: Day11.Seat?) {
        assertThat(Day11.Seat.from(c)).isEqualTo(seat)
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day11.part1(Day11.inputFileLines)).isEqualTo(2476)
        }

        @Test
        fun part2() {
            assertThat(Day11.part2(Day11.inputFileLines)).isEqualTo(2257)
        }
    }

    fun charToSeatProvider() =
        Stream.of(
            arguments('L', Day11.Seat.Empty),
            arguments('#', Day11.Seat.Occupied),
            arguments('.', null)
        )
}