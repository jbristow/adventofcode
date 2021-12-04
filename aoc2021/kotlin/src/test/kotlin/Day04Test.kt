import Day04.extractBalls
import Day04.extractBingoBoards
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day04Test {

    @Nested
    inner class Examples {
        private val sample = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
                               |
                               |22 13 17 11  0
                               | 8  2 23  4 24
                               |21  9 14 16  7
                               | 6 10  3 18  5
                               | 1 12 20 15 19
                               |
                               | 3 15  0  2 22
                               | 9 18 13 17  5
                               |19  8  7 25 23
                               |20 11 10 24  4
                               |14 21 16 12  6
                               |
                               |14 21 17 24  4
                               |10 16 15  9 19
                               |18  8 23 26 20
                               |22 11 13  6  5
                               | 2  0 12  3  7""".trimMargin().lines()

        private val sampleBalls = sample.extractBalls()
        private val sampleBoards = sample.extractBingoBoards()

        @Test
        fun part1() {
            assertThat(Day04.part1(sampleBalls, sampleBoards)).isEqualTo(4512)
        }

        @Test
        fun part2() {
            assertThat(Day04.part2(sampleBalls, sampleBoards)).isEqualTo(1924)
        }
    }

    @Nested
    inner class Answer {
        private val input = Day04.inputFileLines.toList()
        private val balls = input.extractBalls()
        private val boards = input.extractBingoBoards()

        @Test
        fun part1() {
            assertThat(Day04.part1(balls, boards)).isEqualTo(55770)
        }

        @Test
        fun part2() {
            assertThat(Day04.part2(balls, boards)).isEqualTo(2980)
        }
    }
}
