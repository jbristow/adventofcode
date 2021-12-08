import Day07.triangleNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class Day07Test {

    @Nested
    inner class Examples {
        private val sample = "16,1,2,0,4,2,7,1,2,14"

        @Test
        fun part1() {
            assertThat(Day07.part1(sample)).isEqualTo(37)
        }

        @Test
        fun part2() {
            assertThat(Day07.part2(sample)).isEqualTo(168)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day07.part1(Day07.inputFileString)).isEqualTo(329389)
        }

        @Test
        fun part2() {
            assertThat(Day07.part2(Day07.inputFileString)).isEqualTo(86397080)
        }
    }

    @ParameterizedTest(name = "[{index}] {0} movement should take {1} fuel")
    @MethodSource("fuelSource")
    fun part2_increasing_fuel_cost_test(moveAmount: Int, expectedFuel: Int) {
        assertThat(moveAmount.triangleNumber()).isEqualTo(expectedFuel)
    }

    companion object {

        @JvmStatic
        fun fuelSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(11, 66),
                Arguments.of(4, 10),
                Arguments.of(3, 6),
                Arguments.of(5, 15),
                Arguments.of(1, 1),
                Arguments.of(2, 3),
                Arguments.of(9, 45)
            )
        }
    }
}
