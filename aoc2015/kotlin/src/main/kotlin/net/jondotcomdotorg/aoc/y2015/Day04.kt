package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import java.security.MessageDigest

object Day04 : AdventOfCode() {
    private const val key = "yzbqklnj"
    private val md5 = MessageDigest.getInstance("MD5")

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day04:")
        println("\tPart 1: ${part1()}")
        println("\tPart 2: ${part2()}")
    }

    private fun part1(): Long? {
        return generateSequence(1L) { it + 1 }.map {
            it to md5.digest("$key$it".toByteArray()).joinToString("") { b -> "%02x".format(b) }
        }.find { it.second.startsWith("00000") }?.first
    }

    private fun part2(): Long? {
        return generateSequence(1L) { it + 1 }.map {
            it to md5.digest("$key$it".toByteArray()).joinToString("") { b -> "%02x".format(b) }
        }.find { it.second.startsWith("000000") }?.first
    }
}
