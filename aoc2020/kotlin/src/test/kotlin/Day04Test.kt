import Day04.valid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Day04Test {
    val testChunk1 = """
        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
        byr:1937 iyr:2017 cid:147 hgt:183cm
        """.trimIndent()

    val testChunk2 = """
        iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
        hcl:#cfa07d byr:1929
        """.trimIndent()

    val testChunk3 = """
        hcl:#ae17e1 iyr:2013
        eyr:2024
        ecl:brn pid:760753108 byr:1931
        hgt:179cm
        """.trimIndent()

    val testChunk4 = """
        hcl:#cfa07d eyr:2025 pid:166559648
        iyr:2011 ecl:brn hgt:59in
        """.trimIndent()

    val testData = listOf(testChunk1, testChunk2, testChunk3, testChunk4).joinToString("\n\n")

    val data = Files.readString(Paths.get(Day04.FILENAME))

    @Test
    fun testValidatorPart1() {
        val pred = { p: Day04.Passport -> p.valid }
        assertThat(pred)
            .acceptsAll(listOf(testChunk1, testChunk3).map(Day04.Passport::of))
            .rejectsAll(listOf(testChunk2, testChunk4).map(Day04.Passport::of))
    }

    @Test
    fun testPart01() {
        assertThat(Day04.part1(testData)).isEqualTo(2)
    }

    @Test
    fun testPart02() {
        assertThat(Day04.part2(testData)).isEqualTo(2)
    }

    @Test
    fun answerPart01() {
        assertThat(Day04.part1(data)).isEqualTo(170)
    }

    @Test
    fun answerPart02() {
        assertThat(Day04.part2(data)).isEqualTo(103)
    }
}