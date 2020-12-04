import Day04.part1
import Day04.part2
import java.nio.file.Files
import java.nio.file.Paths

object Day04 {
    private const val FILENAME = "src/main/resources/day04.txt"

    fun part1(): Int {
        val data = Files.readString(Paths.get(FILENAME))
        val chunks = data.split("\n\n")
        val passports = chunks.map(Passport::of)

        return passports.count(Passport::valid)
    }

    fun part2(): Int {
        val data = Files.readString(Paths.get(FILENAME))
        val chunks = data.split("\n\n")
        return chunks.count(StrictPassport::validateChunk)
    }

    data class Passport(
        val byr: String?,
        val iyr: String?,
        val eyr: String?,
        val hgt: String?,
        val hcl: String?,
        val ecl: String?,
        val pid: String?,
        val cid: String?,
    ) {
        val valid: Boolean
            get() {

                return byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null
            }

        companion object {
            private val byrRegex = """byr:([^ ]+)""".toRegex()
            private val iyrRegex = """iyr:([^ ]+)""".toRegex()
            private val eyrRegex = """eyr:([^ ]+)""".toRegex()
            private val hgtRegex = """hgt:([^ ]+)""".toRegex()
            private val hclRegex = """hcl:([^ ]+)""".toRegex()
            private val eclRegex = """ecl:([^ ]+)""".toRegex()
            private val pidRegex = """pid:([^ ]+)""".toRegex()
            private val cidRegex = """cid:([^ ]+)""".toRegex()
            fun of(input: String): Passport {
                return Passport(
                    byr = input.parseUsing(byrRegex),
                    iyr = input.parseUsing(iyrRegex),
                    eyr = input.parseUsing(eyrRegex),
                    hgt = input.parseUsing(hgtRegex),
                    hcl = input.parseUsing(hclRegex),
                    ecl = input.parseUsing(eclRegex),
                    pid = input.parseUsing(pidRegex),
                    cid = input.parseUsing(cidRegex),
                )
            }
        }
    }

    object StrictPassport {

        private val byrRegex = """\bbyr:(\d{4})\b""".toRegex()
        private val iyrRegex = """\biyr:(\d{4})\b""".toRegex()
        private val eyrRegex = """\beyr:(\d{4})\b""".toRegex()
        private val hgtRegex = """\bhgt:(\d+(cm|in))\b""".toRegex()
        private val hclRegex = """\bhcl:(#[0-9a-f]{6})\b""".toRegex()
        private val eclRegex = """\becl:(amb|blu|brn|gry|grn|hzl|oth)\b""".toRegex()
        private val pidRegex = """\bpid:([0-9]{9})\b""".toRegex()
        private val cidRegex = """\bcid:([^\s]+)""".toRegex()
        fun String?.parseHeight(): Int? {
            return when {
                this == null -> null
                endsWith("cm") -> {
                    when (val value = dropLast(2).toIntOrNull()) {
                        in 150..193 -> value
                        else -> null
                    }
                }
                endsWith("in") -> {
                    when (val value = dropLast(2).toIntOrNull()) {
                        in 59..76 -> value
                        else -> null
                    }
                }
                else -> null
            }
        }

        fun validateChunk(input: String): Boolean {
            val byr = input.parseUsing(byrRegex)?.toIntOrNull()
            val iyr = input.parseUsing(iyrRegex)?.toIntOrNull()
            val eyr = input.parseUsing(eyrRegex)?.toIntOrNull()
            val hgt = input.parseUsing(hgtRegex)
            val hcl = input.parseUsing(hclRegex)
            val ecl = input.parseUsing(eclRegex)
            val pid = input.parseUsing(pidRegex)

            return byr != null && byr in 1920..2002 && iyr != null && (iyr in 2010..2020) && eyr != null && (eyr in 2020..2030) && hgt.parseHeight() != null && hcl != null && ecl != null && pid != null
        }
    }

    fun String.parseUsing(regex: Regex): String? {
        return regex.findAll(this).firstOrNull()?.groupValues?.get(1)
    }
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}