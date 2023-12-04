import util.AdventOfCode
import kotlin.math.pow

object Day04 : AdventOfCode() {

    data class Card(val id: Int, val winning: List<Int>, val revealed: List<Int>) {
        private val numMatched = revealed.count { it in winning }
        // converting 2^-1 to long results in 0
        val points = 2.0.pow((numMatched - 1).toDouble()).toLong()
        val cardsWon = (1..numMatched).map { it + id }
    }

    private fun List<String>.toCards(): List<Card> {
        return map { line ->
            val (header, values) = line.split(""":\s+""".toRegex())
            val (_, id) = header.split("""\s+""".toRegex())
            val (winning, revealed) = values.split(" | ")
                .map { section -> section.trim().split("""\s+""".toRegex()).map { it.toInt() } }
            Card(id.trim().toInt(), winning, revealed)
        }
    }

    private tailrec fun List<Card>.redeem(
        cardCounts: Map<Int, Long> = this.associate { it.id to 1L }
    ): Map<Int, Long> {
        if (this.isEmpty()) {
            return cardCounts
        }

        val current = this.first()
        val remaining = this.drop(1)

        val currentMult = cardCounts.getValue(current.id)
        return remaining
            .redeem(cardCounts + current.cardsWon.associateWith { cardCounts.getValue(it) + currentMult })
    }

    private fun part1(input: List<String>) = input.toCards().sumOf { it.points }

    private fun part2(input: List<String>) =
        input.toCards().redeem().values.sum()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 4")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}