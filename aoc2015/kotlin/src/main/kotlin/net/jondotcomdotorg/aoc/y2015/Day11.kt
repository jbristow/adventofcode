package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode

object Day11 : AdventOfCode() {

    private const val currentPassword = "cqjxjnds"

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 11:")
        val p1 = part1()
        println("Part 1: $p1")
        println("Part 2: ${part2(p1)}")
    }

    private fun String.toNumber() =
        fold(0L) { acc, c -> acc * 26L + c.code - 'a'.code }

    private tailrec fun Long.toPassword(output: String = ""): String {
        if (this == 0L) {
            return output
        }
        val c = ((this % 26).toInt() + 'a'.code).toChar()
        return (this / 26L).toPassword("$c$output")
    }

    private val String.isAscendingStraight: Boolean
        get() = this[0].code + 1 == this[1].code && this[1].code + 1 == this[2].code

    private val String.hasNonOverlappingPair: Boolean
        get() = Regex("""(.)\1+""").findAll(this).count() > 1

    private fun passwordCheck(pwCode: Long): Boolean {
        val check = pwCode.toPassword()
        return 'i' !in check && 'o' !in check && 'l' !in check &&
            check.windowed(3, 1).any { it.isAscendingStraight } &&
            check.hasNonOverlappingPair
    }

    private fun String.findNextPassword() =
        generateSequence(toNumber() + 1) { (it + 1L) % 208827064576 }
            .filter(this@Day11::passwordCheck)
            .first().toPassword()

    private fun part1(): String = currentPassword.findNextPassword()

    private fun part2(input: String): String = input.findNextPassword()
}
