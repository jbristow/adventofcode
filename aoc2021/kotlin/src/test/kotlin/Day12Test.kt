import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day12Test {

    @Nested
    inner class Examples {
        val sample1 = (
            "start-A\n" +
                "start-b\n" +
                "A-c\n" +
                "A-b\n" +
                "b-d\n" +
                "A-end\n" +
                "b-end"
            ).lineSequence()
        val sample2 = (
            "dc-end\n" +
                "HN-start\n" +
                "start-kj\n" +
                "dc-start\n" +
                "dc-HN\n" +
                "LN-dc\n" +
                "HN-end\n" +
                "kj-sa\n" +
                "kj-HN\n" +
                "kj-dc"
            ).lineSequence()
        val sample3 = (
            "fs-end\n" +
                "he-DX\n" +
                "fs-he\n" +
                "start-DX\n" +
                "pj-DX\n" +
                "end-zg\n" +
                "zg-sl\n" +
                "zg-pj\n" +
                "pj-he\n" +
                "RW-he\n" +
                "fs-DX\n" +
                "pj-RW\n" +
                "zg-RW\n" +
                "start-pj\n" +
                "he-WI\n" +
                "zg-he\n" +
                "pj-fs\n" +
                "start-RW"
            ).lineSequence()

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
