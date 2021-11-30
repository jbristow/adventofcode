import util.AdventOfCode

object Day22 : AdventOfCode() {

    tailrec fun combatRound(player1: ArrayDeque<Int>, player2: ArrayDeque<Int>): ArrayDeque<Int> =
        when {
            player1.isEmpty() -> player2
            player2.isEmpty() -> player1
            player1.first() > player2.first() -> {
                player1.addLast(player1.removeFirst())
                player1.addLast(player2.removeFirst())
                combatRound(player1, player2)
            }
            player1.first() < player2.first() -> {
                player2.addLast(player2.removeFirst())
                player2.addLast(player1.removeFirst())
                combatRound(player1, player2)
            }
            else -> throw Exception("Impossible: Players have a card of equal value.")
        }

    fun recordOf(player1: ArrayDeque<Int>, player2: ArrayDeque<Int>) =
        """${player1.hashCode()}-${player2.hashCode()}"""

    sealed class Winner(val deck: ArrayDeque<Int>) {
        class Player1(deck: ArrayDeque<Int>) : Winner(ArrayDeque(deck))
        class Player2(deck: ArrayDeque<Int>) : Winner(ArrayDeque(deck))
    }

    fun recursiveGame(player1: ArrayDeque<Int>, player2: ArrayDeque<Int>): Winner {
        return recursiveRound(player1, player2)
    }

    tailrec fun recursiveRound(
        player1: ArrayDeque<Int>,
        player2: ArrayDeque<Int>,
        deckRecords: Set<String> = emptySet(),
    ): Winner {
        val deckState = recordOf(player1, player2)

        if (deckState in deckRecords) {
            return Winner.Player1(player1)
        }
        if (player1.isEmpty()) {
            return Winner.Player2(player2)
        }
        if (player2.isEmpty()) {
            return Winner.Player1(player1)
        }

        val player1card = player1.removeFirst()
        val player2card = player2.removeFirst()

        if (player1.size >= player1card && player2.size >= player2card) {
            val winner = recursiveGame(
                ArrayDeque(player1.take(player1card.toInt())),
                ArrayDeque(player2.take(player2card.toInt()))
            )

            when (winner) {
                is Winner.Player1 -> {
                    player1.addLast(player1card)
                    player1.addLast(player2card)
                }
                is Winner.Player2 -> {
                    player2.addLast(player2card)
                    player2.addLast(player1card)
                }
            }
        } else if (player1card > player2card) {
            player1.addLast(player1card)
            player1.addLast(player2card)
        } else if (player2card > player1card) {
            player2.addLast(player2card)
            player2.addLast(player1card)
        } else {
            throw Exception("Impossible: equal cards seen")
        }
        return recursiveRound(player1, player2, deckRecords + deckState)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1(inputFileString))
        println(part2(inputFileString))

        //println(recursiveGame(ArrayDeque(listOf(43,19)), ArrayDeque(listOf(2,29,14))).deck)
    }

    private fun part2(data: String): Int {
        val (player1, player2) = data.split("\n\n").map { it.lines().drop(1).map(String::toInt) }
        val winningDeck = recursiveGame(ArrayDeque(player1), ArrayDeque(player2)).deck
        return winningDeck.toList().reversed().mapIndexed { i, it -> (i + 1) * it }.sum()
    }

    private fun part1(data: String): Int {
        val (player1, player2) = data.split("\n\n").map { it.lines().drop(1).map(String::toInt) }
        val winningDeck = combatRound(ArrayDeque(player1), ArrayDeque(player2))
        return winningDeck.toList().reversed().mapIndexed { i, it -> (i + 1) * it }.sum()
    }
}