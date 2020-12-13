import Day04.valid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
    fun testValidatorPart2() {
        assertThat(Day04.StrictPassport::validateChunk)
            .accepts(
                """
                    pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
                    hcl:#623a2f
                """.trimIndent(),
                """
                    eyr:2029 ecl:blu cid:129 byr:1989
                    iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
                """.trimIndent(),
                """
                    hcl:#888785
                    hgt:164cm byr:2001 iyr:2015 cid:88
                    pid:545766238 ecl:hzl
                    eyr:2022
                """.trimIndent(),
                "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
            )
            .rejects(
                """
                    eyr:1972 cid:100
                    hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
                """.trimIndent(),
                """
                    iyr:2019
                    hcl:#602927 eyr:1967 hgt:170cm
                    ecl:grn pid:012533040 byr:1946
                """.trimIndent(),
                """
                    hcl:dab227 iyr:2012
                    ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
                """.trimIndent(),
                """
                    hgt:59cm ecl:zzz
                    eyr:2038 hcl:74454a iyr:2023
                    pid:3556412378 byr:2007
                """.trimIndent()
            )
    }

    @Nested
    inner class Answer {
        @Test
        fun part01() {
            assertThat(Day04.part1(Day04.inputFileString)).isEqualTo(170)
        }

        @Test
        fun part02() {
            assertThat(Day04.part2(Day04.inputFileString)).isEqualTo(103)
        }
    }
}
