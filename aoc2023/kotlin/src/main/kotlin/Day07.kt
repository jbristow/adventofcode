import util.AdventOfCode
import util.frequency
import java.util.Comparator.comparing
import java.util.Comparator.comparingInt

object Day07 : AdventOfCode() {
    fun Char.toRank(): Int {
        return when (this) {
            '2', '3', '4', '5', '6', '7', '8', '9' -> this.digitToInt()
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> throw Exception("Unknown rank $this")
        }
    }

    private fun Char.toJokerRank(): Int {
        return when (this) {
            '2', '3', '4', '5', '6', '7', '8', '9' -> this.digitToInt()
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 0
            'T' -> 10
            else -> throw Exception("Unknown rank $this")
        }
    }

    data class Card(val label: Char, val rank: Int = label.toRank()) : Comparable<Card> {
        override fun compareTo(other: Card): Int = this.rank.compareTo(other.rank)

        override fun toString() = "$label"
    }

    sealed class HandType(val typeRank: Int, val identifiedBy: (Map<Card, Int>) -> Boolean) {
        companion object {
            fun identify(grouped: Map<Card, Int>): HandType {
                return when {
                    FiveOfAKind.identifiedBy(grouped) -> FiveOfAKind
                    FourOfAKind.identifiedBy(grouped) -> FourOfAKind
                    FullHouse.identifiedBy(grouped) -> FullHouse
                    ThreeOfAKind.identifiedBy(grouped) -> ThreeOfAKind
                    TwoPair.identifiedBy(grouped) -> TwoPair
                    OnePair.identifiedBy(grouped) -> OnePair
                    else -> HighCard
                }
            }
        }

        data object FiveOfAKind : HandType(6, { it.size == 1 })

        data object FourOfAKind : HandType(5, { it.size == 2 && it.containsValue(4) && it.containsValue(1) })

        data object FullHouse : HandType(4, { it.size == 2 && it.containsValue(3) && it.containsValue(2) })

        data object ThreeOfAKind : HandType(3, { it.size in 2..3 && it.containsValue(3) })

        data object TwoPair : HandType(2, { it.size == 3 })

        data object OnePair : HandType(1, { it.size == 4 })

        data object HighCard : HandType(0, { it.size == 5 })
    }

    private val countComparator: Comparator<Pair<Card, Int>> = comparingInt<Pair<Card, Int>> { it.second }.reversed()
    private val cardComparator: Comparator<Pair<Card, Int>> = comparing<Pair<Card, Int>, Card> { it.first }.reversed()
    val kvComparator: Comparator<Pair<Card, Int>> = countComparator.thenComparing(cardComparator)

    sealed class Hand : Comparable<Hand> {
        abstract val cards: List<Card>
        abstract val type: HandType
        abstract val bid: Long

        override fun compareTo(other: Hand): Int {
            return if (this.type != other.type) {
                this.type.typeRank.compareTo(other.type.typeRank)
            } else {
                cards.zip(other.cards).firstOrNull { (a, b) -> a != b }?.let { (a, b) -> a.compareTo(b) } ?: 0
            }
        }

        data class NormalHand(
            override val cards: List<Card>,
            override val bid: Long,
            override val type: HandType = HandType.identify(cards.frequency()),
            val cardGroup: List<Pair<Card, Int>> =
                cards.frequency().toList().sortedByDescending { it.first }
                    .sortedByDescending { it.second },
        ) : Hand()

        class JokerHand(override val cards: List<Card>, override val bid: Long) : Hand() {
            private val jokerCount = cards.count { it.label == 'J' }
            override val type: HandType
            private val freqNoJokers = cards.filterNot { it.label == 'J' }.frequency()
            private val bestGroup = freqNoJokers.toList().sortedWith(kvComparator).firstOrNull()?.first

            init {
                type =
                    if (bestGroup == null) {
                        HandType.FiveOfAKind
                    } else {
                        val freq = freqNoJokers.toMutableMap()
                        freq[bestGroup] = freqNoJokers.getValue(bestGroup) + jokerCount
                        HandType.identify(freq)
                    }
            }
        }
    }

    private fun String.toHand(): Hand.NormalHand {
        val (cardPart, bidPart) = this.split("""\s+""".toRegex())
        return Hand.NormalHand(cardPart.map { Card(it) }, bidPart.toLong())
    }

    private fun List<String>.toHands(): List<Hand.NormalHand> {
        return map { it.toHand() }
    }

    private fun String.toJokerHand(): Hand.JokerHand {
        val (cardPart, bidPart) = this.split("""\s+""".toRegex())
        return Hand.JokerHand(cardPart.map { Card(it, it.toJokerRank()) }, bidPart.toLong())
    }

    private fun List<String>.toJokerHands(): List<Hand.JokerHand> {
        return map { it.toJokerHand() }
    }

    private fun part1(input: List<String>) = input.toHands().sorted().withIndex().sumOf { (i, it) -> (i + 1) * it.bid }

    private fun part2(input: List<String>) = input.toJokerHands().sorted().withIndex().sumOf { (i, it) -> (i + 1) * it.bid }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 7")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
