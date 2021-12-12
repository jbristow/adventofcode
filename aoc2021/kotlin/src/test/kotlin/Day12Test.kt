import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day12Test {

    @Nested
    inner class Examples {
        private val sample1 = """start-A
                                |start-b
                                |A-c
                                |A-b
                                |b-d
                                |A-end
                                |b-end""".trimMargin().lineSequence()
        
        private val sample2 = """dc-end
                                |HN-start
                                |start-kj
                                |dc-start
                                |dc-HN
                                |LN-dc
                                |HN-end
                                |kj-sa
                                |kj-HN
                                |kj-dc""".trimMargin().lineSequence()

        private val sample3 = """fs-end
                                |he-DX
                                |fs-he
                                |start-DX
                                |pj-DX
                                |end-zg
                                |zg-sl
                                |zg-pj
                                |pj-he
                                |RW-he
                                |fs-DX
                                |pj-RW
                                |zg-RW
                                |start-pj
                                |he-WI
                                |zg-he
                                |pj-fs
                                |start-RW""".trimMargin().lineSequence()

        @Test
        fun part1_sample1() {
            assertThat(Day12.part1(sample1)).isEqualTo(10)
        }

        @Test
        fun part2_sample1() {
            assertThat(Day12.part2(sample1)).isEqualTo(36)
        }

        @Test
        fun part1_sample2() {
            assertThat(Day12.part1(sample2)).isEqualTo(19)
        }

        @Test
        fun part2_sample2() {
            assertThat(Day12.part2(sample2)).isEqualTo(103)
        }

        @Test
        fun part1_sample3() {
            assertThat(Day12.part1(sample3)).isEqualTo(226)
        }

        @Test
        fun part2_sample3() {
            assertThat(Day12.part2(sample3)).isEqualTo(3509)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day12.part1(Day12.inputFileLineSequence)).isEqualTo(4885)
        }

        @Test
        fun part2() {
            assertThat(Day12.part2(Day12.inputFileLineSequence)).isEqualTo(117095)
        }
    }
}
