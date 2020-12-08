import Day05.FILENAME
import Day05.part1
import Day05.part1Alt
import Day05.part2
import Day05.toSeatID
import Day05.toSeatLocation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day05Test {

    @ParameterizedTest
    @MethodSource("providePassesToSeat")
    fun testSeat(pass: String, seat: Pair<Int, Int>) {
        assertThat(pass.toSeatLocation()).isEqualTo(seat)
    }

    @ParameterizedTest
    @MethodSource("provideSeatToId")
    fun testSeatId(seat: Pair<Int, Int>, id: Int) {
        assertThat(seat.toSeatID()).isEqualTo(id)
    }

    val testData = listOf(
        "BFFFBBFRRR",
        "FFFBBBFRRR",
        "BBFFBBFRLL"
    )
    val data = Files.readAllLines(Paths.get(FILENAME))

    @Test
    fun testFindMax() {
        assertThat(part1(testData)).isEqualTo(820)
    }

    @Test
    fun testFindMaxAlt() {
        assertThat(part1Alt(testData)).isEqualTo(820)
    }

    fun providePassesToSeat(): Stream<Arguments> = Stream.of(
        Arguments.of("BFFFBBFRRR", 70 to 7),
        Arguments.of("FFFBBBFRRR", 14 to 7),
        Arguments.of("BBFFBBFRLL", 102 to 4),
    )

    fun provideSeatToId(): Stream<Arguments> = Stream.of(
        Arguments.of(70 to 7, 567),
        Arguments.of(14 to 7, 119),
        Arguments.of(102 to 4, 820),
    )

    @Test
    fun answerPart01() {
        assertThat(part1(data)).isEqualTo(885)
    }

    @Test
    fun answerPart01Alt() {
        assertThat(part1Alt(data)).isEqualTo(885)
    }

    @Test
    fun answerPart02() {
        assertThat(part2(data)).isEqualTo(623)
    }
}
