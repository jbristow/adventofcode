import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
internal class Day09Test {

    @Test
    fun answer2() {
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Part1 {

        @ParameterizedTest
        @MethodSource("sampleData")
        fun sample1(p: Int, m: Int, expected: Long?) {
            assertEquals(
                expected,
                Day09.answer1(
                    biggestMarble = m,
                    players = p
                )
            )
        }

        @Test
        fun answer1() {
            assertEquals(
                398048,
                Day09.answer1(biggestMarble = 71307, players = 458)
            )
        }

        @Test
        fun answer2() {
            assertEquals(
                3180373421,
                Day09.answer2(biggestMarble = 71307, players = 458)
            )
        }

        @Suppress("unused")
        fun sampleData(): Stream<Arguments> = Stream.of(
            arguments(10, 1618, 8317L),
            arguments(13, 7999, 146373L),
            arguments(17, 1104, 2764L),
            arguments(21, 6111, 54718L),
            arguments(30, 5807, 37305L)
        )

    }

}