package aoc

import aoc.Day01.fuelNeeded
import aoc.Day01.totalFuelNeeded
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day01KtTest {

    @Suppress("unused")
    fun part1Provider(): Stream<Pair<Int, Int>> =
        Stream.of(
            12 to 2,
            14 to 2,
            1969 to 654,
            100756 to 33583
        )

    @ParameterizedTest
    @MethodSource("part1Provider")
    fun fuelNeededTest(data: Pair<Int, Int>) {
        val (input, expected) = data
        assertThat(fuelNeeded(input)).isEqualTo(expected)
    }

    @Suppress("unused")
    fun part2Provider(): Stream<Pair<Int, Int>> =
        Stream.of(
            12 to 2,
            14 to 2,
            1969 to 966,
            100756 to 50346
        )

    @ParameterizedTest
    @MethodSource("part2Provider")
    fun totalFuelNeededTest(data: Pair<Int, Int>) {
        val (input, expected) = data
        assertThat(totalFuelNeeded(input)).isEqualTo(expected)
    }
}
