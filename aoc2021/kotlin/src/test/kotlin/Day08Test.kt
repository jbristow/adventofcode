import Day08.DigitDisplay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class Day08Test {

    @Nested
    inner class Examples {
        private val sample = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".lines()

        @Test
        fun part1() {
            assertThat(Day08.part1(sample)).isEqualTo(26)
        }

        @Test
        fun part2() {
            assertThat(Day08.part2(sample)).isEqualTo(61229)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day08.part1(Day08.inputFileLines)).isEqualTo(355)
        }

        @Test
        fun part2() {
            assertThat(Day08.part2(Day08.inputFileLines)).isEqualTo(983030)
        }
    }

    @Test
    fun enumSanityTest() {
        // prevents 1,4,7,8 from getting lonesome

        assertThat(
            listOf(DigitDisplay.One, DigitDisplay.Four, DigitDisplay.Seven, DigitDisplay.Eight)
                .joinToString("") { it.digit }
                .toInt())
            .isEqualTo(1478)
    }
}
