import util.AdventOfCode

object Day04 : AdventOfCode() {

    fun part1(data: String): Int {
        val chunks = data.split("\n\n")
        val passports = chunks.map(Passport::of)

        return passports.count { it.valid }
    }

    fun part2(data: String): Int {
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

    val Passport.valid: Boolean
        get() {
            return byr != null &&
                iyr != null &&
                eyr != null &&
                hgt != null &&
                hcl != null &&
                ecl != null &&
                pid != null
        }

    object StrictPassport {

        private val byrRegex = """\bbyr:(\d{4})\b""".toRegex()
        private val iyrRegex = """\biyr:(\d{4})\b""".toRegex()
        private val eyrRegex = """\beyr:(\d{4})\b""".toRegex()
        private val hgtRegex = """\bhgt:(\d+(cm|in))\b""".toRegex()
        private val hclRegex = """\bhcl:(#[0-9a-f]{6})\b""".toRegex()
        private val eclRegex = """\becl:(amb|blu|brn|gry|grn|hzl|oth)\b""".toRegex()
        private val pidRegex = """\bpid:([0-9]{9})\b""".toRegex()
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
            println("###\n${input}\n###")
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

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 4")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}

