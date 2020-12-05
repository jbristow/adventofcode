import Day04Alt.Read
import arrow.core.Either
import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import arrow.core.computations.validated
import arrow.core.extensions.applicativeNel
import arrow.core.fix
import arrow.core.invalid
import arrow.core.left
import arrow.core.right
import arrow.core.valid
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

object Day04Alt {
    abstract class Read<A> {

        abstract fun read(s: String): Either<ParseError, A?>

        companion object {
            val stringRead: Read<String> = object : Read<String>() {
                override fun read(s: String) = s.right()
            }

            val hexColorRead: Read<String> = object : Read<String>() {
                override fun read(s: String) =
                    when {
                        s.matches(Regex("#[0-9a-f]{6}")) -> s.right()
                        else -> ParseError.InvalidHexColor(s).left()
                    }
            }
            val eyeColorRead: Read<String> = object : Read<String>() {
                override fun read(s: String) =
                    when {
                        s.matches(Regex("(amb|blu|brn|gry|grn|hzl|oth)")) -> s.right()
                        else -> ParseError.InvalidEyeColorOption(s).left()
                    }
            }

            fun nDigitRead(digitCount: Int): Read<String> = object : Read<String>() {
                override fun read(s: String) =
                    when {
                        s.matches(Regex("""\d{${digitCount}}""")) -> s.right()
                        else -> ParseError.WrongDigitCount(s).left()
                    }
            }

            fun heightRead(ranges: Map<String, Pair<Int, Int>>): Read<String> = object : Read<String>() {
                override fun read(s: String) =
                    when (val range = ranges[s.takeLast(2)]) {
                        null -> ParseError.InvalidUnits(s).left()
                        else -> when (val i = s.dropLast(2).toIntOrNull()) {
                            null -> ParseError.NotAnInteger(s.dropLast(2)).left()
                            in range.first..range.second -> s.right()
                            else -> ParseError.OutOfBounds(i, range.first, range.second).left()
                        }

                    }
            }

            fun intRead(minRange: Int, maxRange: Int): Read<Int> {
                return object : Read<Int>() {
                    override fun read(s: String) =
                        when (val i = s.toIntOrNull()) {
                            null -> ParseError.NotAnInteger(s).left()
                            in minRange..maxRange -> i.right()
                            else -> ParseError.OutOfBounds(i, minRange, maxRange).left()
                        }
                }
            }
        }
    }
}

class Input(value: String) {
    private val fields: Map<String, String> = value.replace("""\s+""".toRegex(), " ")
        .split(" ")
        .mapNotNull {
            val pieces = it.split(":")
            when (pieces.size) {
                2 -> pieces[0] to pieces[1]
                else -> null
            }
        }.toMap()

    fun <A> parse(key: String, read: Read<A>) =
        when (val v = fields[key]) {
            null -> PassportError.Missing(key).invalid()
            else ->
                when (val e = read.read(v)) {
                    is Either.Left -> PassportError.Parse(key, e.a.toString()).invalid()
                    is Either.Right -> e.b.valid()
                }
        }.toValidatedNel()
}

fun validatePart1(input: Input) = Validated.applicativeNel<PassportError>().tupledN(
    input.parse("eyr", Read.stringRead),
    input.parse("byr", Read.stringRead),
    input.parse("iyr", Read.stringRead),
    input.parse("hcl", Read.stringRead),
    input.parse("ecl", Read.stringRead),
    input.parse("hgt", Read.stringRead),
    input.parse("pid", Read.stringRead)
).fix()

fun validatePart2(input: Input) = Validated.applicativeNel<PassportError>().tupledN(
    input.parse("eyr", Read.intRead(2020, 2030)),
    input.parse("byr", Read.intRead(1920, 2002)),
    input.parse("iyr", Read.intRead(2010, 2020)),
    input.parse("hcl", Read.hexColorRead),
    input.parse("ecl", Read.eyeColorRead),
    input.parse("hgt", Read.heightRead(mapOf("cm" to (150 to 193), "in" to (59 to 76)))),
    input.parse("pid", Read.nDigitRead(9))
).fix()

sealed class PassportError {
    data class Missing(val key: String) : PassportError()
    data class Parse(val key: String, val problem: String) : PassportError()
}

sealed class ParseError {
    data class OutOfBounds(val value: Int, val minAllowed: Int, val maxAllowed: Int) : ParseError()
    data class NotAnInteger(val value: String) : ParseError()
    data class InvalidHexColor(val value: String) : ParseError()
    data class InvalidEyeColorOption(val value: String) : ParseError()
    data class InvalidUnits(val value: String) : ParseError()
    data class WrongDigitCount(val value: String) : ParseError()
}

fun main() {
    val data = Files.readString(Paths.get("src/main/resources/day04.txt")).trim()
    val chunks = data.split("\n\n")


    val part1Valid = chunks.parallelStream().map { validatePart1(Input(it)) }.collect(Collectors.toList())
    val part1Count = part1Valid.count { it.isValid }
    println("------")
    println(part1Valid.filter { it.isInvalid }.joinToString("\n") {
        when (it) {
            is Valid -> "Valid"
            is Invalid -> "Invalid:\n" + it.e.joinToString("\n") { err -> "\t${err}" }
        }
    })
    val validatedChunks = chunks.parallelStream()
        .map { validatePart2(Input(it)) }
        .collect(Collectors.toList())

    println(validatedChunks.filter { it.isInvalid }.joinToString("\n") {
        when (it) {
            is Valid -> "Valid"
            is Invalid -> "Invalid:\n" + it.e.joinToString("\n") { err -> "\t${err}" }
        }
    })
    println("Valid Count Part1: $part1Count")
    println("Valid Count Part2: ${validatedChunks.count { it.isValid }}")
}

