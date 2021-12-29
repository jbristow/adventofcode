package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day08 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8:")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    sealed class StrPiece {
        object Escape : StrPiece()
        data class CharRepl(val value: String = "") : StrPiece()
    }

    private fun StrPiece.CharRepl.toChar(): Char {
        return this.value.toInt(16).toChar()
    }

    private const val hexDigits = "0123456789abcdef"

    private tailrec fun String.clean(escape: StrPiece? = null, cleaned: String = ""): String {
        if (this.isEmpty()) {
            return cleaned
        }

        val current = this.first()
        val remaining = this.drop(1)
        return when (escape) {
            StrPiece.Escape -> when (current) {
                'x' -> remaining.clean(StrPiece.CharRepl(""), cleaned)
                else -> remaining.clean(null, cleaned + current)
            }
            is StrPiece.CharRepl -> when {
                escape.value.length == 2 -> this.clean(null, cleaned + escape.toChar())
                current in hexDigits -> remaining.clean(escape.copy(escape.value + current), cleaned)
                else -> this.clean(null, cleaned + escape.toChar())
            }
            else -> when (current) {
                '"' -> remaining.clean(null, cleaned)
                '\'' -> remaining.clean(null, cleaned)
                '\\' -> remaining.clean(StrPiece.Escape, cleaned)
                else -> remaining.clean(null, cleaned + current)
            }
        }
    }

    private fun part1(input: Sequence<String>) = input.sumOf { it.length - it.clean().length }

    private val specialRe = Regex("""["'\\]""")

    private fun String.simpleEscape(): String = """"${replace(specialRe, """\$0""")}""""

    private fun part2(input: Sequence<String>) = input.sumOf { it.simpleEscape().length - it.length }
}
