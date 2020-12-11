import Day08.processLine
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class Day08Test {
    private val testData = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6
    """.trimIndent().lines()

    private val testDataFixed = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        nop -4
        acc +6
    """.trimIndent().lines()

    @Test
    fun testPart1() {
        assertThat(Day08.part1(testData)).isEqualTo(5)
    }

    @Test
    fun testPart2() {
        assertThat(Day08.part2(testData)).isEqualTo(8)
    }

    @Test
    fun testLoopDetector() {
        assertThat(Day08.loopDetector(0, 0, testData.map { processLine(it) })).isEqualTo(
            Day08.Outcome.LoopDetected(
                line = 1,
                acc = 5,
                seen = listOf(0, 1, 2, 6, 7, 3, 4)
            )
        )
    }

    @Test
    fun testLoopDetectorTerminates() {
        assertThat(Day08.loopDetector(0, 0, testDataFixed.map { processLine(it) })).isEqualTo(
            Day08.Outcome.Terminated(
                line = 9,
                acc = 8,
                seen = listOf(0, 1, 2, 6, 7, 8)
            )
        )
    }

    @ParameterizedTest
    @MethodSource("parserDataProvider")
    fun testLineParser(input: String, output: Day08.Operation) {
        assertThat(Day08.processLine(input)).isEqualTo(output)
    }

    @ParameterizedTest
    @MethodSource("operationDataProvider")
    fun testOperate(op: Day08.Operation, currLine: Int, currAcc: Int, answer: Pair<Int, Int>) {
        assertThat(op.operate(currLine, currAcc)).isEqualTo(answer)
    }

    fun parserDataProvider() = Stream.of(
        arguments("nop +0", Day08.Operation.Nop(0)),
        arguments("acc +1", Day08.Operation.Acc(1)),
        arguments("jmp +4", Day08.Operation.Jmp(4)),
        arguments("jmp -3", Day08.Operation.Jmp(-3)),
        arguments("acc -99", Day08.Operation.Acc(-99)),
        arguments("nop -222222", Day08.Operation.Nop(-222222))
    )

    fun operationDataProvider() = Stream.of(
        arguments(Day08.Operation.Nop(0), 1, 2, 2 to 2),
        arguments(Day08.Operation.Acc(1), 3, 4, 4 to 5),
        arguments(Day08.Operation.Acc(-99), 5, 6, 6 to -93),
        arguments(Day08.Operation.Jmp(1), 7, 8, 8 to 8),
        arguments(Day08.Operation.Jmp(-99), 9, 10, -90 to 10),
    )

    @Nested
    inner class Answers {
        private val data = Files.readAllLines(Paths.get(Day08.FILENAME))

        @Test
        fun part1() {
            assertThat(Day08.part1(data)).isEqualTo(1930)
        }

        @Test
        fun part2() {
            assertThat(Day08.part2(data)).isEqualTo(1688)
        }
    }
}
