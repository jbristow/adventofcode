import util.AdventOfCode
import java.util.PriorityQueue
import kotlin.math.max

object Day21 : AdventOfCode() {
    val sample = """""".lines()

    data class Player(val id: Int, val position: Int, val score: Int = 0)

    private fun Player.move(distance: Int): Player {
        val newSpace: Int = when {
            (distance % 10 + position) > 10 -> distance % 10 + position - 10
            else -> distance % 10 + position
        }
        return copy(position = newSpace, score = score + newSpace)
    }

    data class Result(val winner: Player, val loser: Player, val turns: Int)

    private tailrec fun play(
        p1: Player,
        p2: Player,
        which: Int = 1,
        dieValue: Int = 1,
        turn: Int = 0
    ): Result {
        if (p1.score >= 1000) {
            return Result(p1, p2, turn)
        }
        if (p2.score >= 1000) {
            return Result(p2, p1, turn)
        }
        val nextDieValue = if (dieValue + 3 > 100) {
            dieValue - 97
        } else {
            dieValue + 3
        }
        return when (which) {
            1 -> play(p1.move(3 * dieValue + 3), p2, 2, nextDieValue, turn + 1)
            else -> play(p1, p2.move(3 * dieValue + 3), 1, nextDieValue, turn + 1)
        }
    }

    sealed class Turn(val p1: Player, val p2: Player, val multiple: Long) : Comparable<Turn> {
        class Player1Turn(p1: Player, p2: Player, multiple: Long = 1) : Turn(p1, p2, multiple)
        class Player2Turn(p1: Player, p2: Player, multiple: Long = 1) : Turn(p1, p2, multiple)

        override fun compareTo(other: Turn): Int {
            if (this is Player1Turn && other is Player2Turn) {
                return -1
            }
            if (this is Player2Turn && other is Player1Turn) {
                return 1
            }
            return if (p1.score == other.p1.score) {
                if (p1.position == other.p1.position) {
                    if (p2.score == other.p2.score) {
                        p2.position.compareTo(other.p2.position)
                    } else {
                        p2.score.compareTo(other.p2.score)
                    }
                } else {
                    p1.position.compareTo(other.p1.position)
                }
            } else {
                p1.score.compareTo(other.p1.score)
            }
        }

        fun sameTurnPos(curr: Turn) =
            ((this is Player1Turn && curr is Player1Turn) || (this is Player2Turn && curr is Player2Turn)) &&
                p1.score == curr.p1.score &&
                p1.position == curr.p1.position &&
                p2.score == curr.p2.score &&
                p2.position == curr.p2.position
    }

    private fun Turn.copy(p1: Player = this.p1, p2: Player = this.p2, multiple: Long = this.multiple): Turn {
        return when (this) {
            is Turn.Player1Turn -> Turn.Player1Turn(p1, p2, multiple)
            is Turn.Player2Turn -> Turn.Player2Turn(p1, p2, multiple)
        }
    }

    private val quantumValues = mapOf(
        3 to 1,
        4 to 3,
        5 to 6,
        6 to 7,
        7 to 6,
        8 to 3,
        9 to 1
    )

    private fun Turn.moreTurns() =
        when (this) {
            is Turn.Player1Turn -> quantumValues.map { (dist, mult) ->
                Turn.Player2Turn(p1.move(dist), p2, multiple * mult)
            }
            is Turn.Player2Turn -> quantumValues.map { (dist, mult) ->
                Turn.Player1Turn(p1, p2.move(dist), multiple * mult)
            }
        }

    private tailrec fun playQuantum(rounds: PriorityQueue<Turn>, p1Wins: Long = 0, p2Wins: Long = 0): Long {
        if (rounds.isEmpty()) {
            return max(p1Wins, p2Wins)
        }

        val current = rounds.remove()
        var currMult = current.multiple

        while (rounds.isNotEmpty() && rounds.peek().sameTurnPos(current)) {
            val temp = rounds.remove()
            currMult += temp.multiple
        }

        if (current.p1.score >= 21) {
            return playQuantum(rounds, p1Wins + currMult, p2Wins)
        }
        if (current.p2.score >= 21) {
            return playQuantum(rounds, p1Wins, p2Wins + currMult)
        }

        rounds.addAll(current.copy(multiple = currMult).moreTurns())
        return playQuantum(rounds, p1Wins, p2Wins)
    }

    fun part1(): Long {
        val p1 = Player(1, 8)
        val p2 = Player(2, 9)
        val result = play(p1, p2)
        return 3L * result.loser.score * result.turns
    }

    fun part2(): Long {
        val p1 = Player(1, 8)
        val p2 = Player(2, 9)
        val pq = PriorityQueue<Turn>()
        pq.add(Turn.Player1Turn(p1, p2))
        return playQuantum(pq)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1()}")
        println("\tPart 2: ${part2()}")
    }
}
