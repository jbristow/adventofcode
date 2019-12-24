import arrow.core.getOrElse
import arrow.core.toOption
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths

sealed class ShuffleMethods : (List<Int>) -> List<Int> {
    object DealIntoStack : ShuffleMethods() {
        override fun invoke(p1: List<Int>) = p1.reversed()
    }

    class Cut(val n: Int) : ShuffleMethods() {
        override fun invoke(p1: List<Int>): List<Int> {
            return if (n >= 0) {
                p1.drop(n) + p1.take(n)
            } else {
                p1.takeLast(-1 * n) + p1.dropLast(-1 * n)
            }
        }
    }

    class DealIncrement(val n: Int) : ShuffleMethods() {
        override fun invoke(p1: List<Int>): List<Int> {
            return p1.withIndex().sortedBy { (it.index * n) % p1.size }.map { it.value }
        }
    }
}

val regexIncrement = """deal with increment (\d+)""".toRegex()
const val dealIntoStack = "deal into new stack"
val regexCut = """cut (-?\d+)""".toRegex()


object Day22 {
    private const val FILENAME = "src/main/resources/day22.txt"
    val fileData: MutableList<String> get() = Files.readAllLines(Paths.get(FILENAME))

    private fun String.toShuffleMethod(): ShuffleMethods {
        if (this == dealIntoStack) {
            return ShuffleMethods.DealIntoStack
        }
        return when (val incrementMatch = regexIncrement.matchEntire(this)) {
            null -> {
                val cutMatch = regexCut.matchEntire(this).toOption()
                ShuffleMethods.Cut(cutMatch.getOrElse { throw error("unknown style: $this") }.groupValues[1].toInt())
            }
            else -> ShuffleMethods.DealIncrement(incrementMatch.groupValues[1].toInt())
        }
    }

    private fun List<Int>.shuffle(methods: List<ShuffleMethods>) =
        methods.fold(this) { acc, curr -> curr(acc) }

    fun part1(input: List<String>, deck: List<Int> = (0 until 10007).toList()) =
        deck.shuffle(input.map { it.toShuffleMethod() })

    private fun String.toModOffsetPair(): Pair<BigInteger, BigInteger> {
        if (this == dealIntoStack) {
            return SingleShuffleMethods.dealIntoStack()
        }
        return when (val incrementMatch = regexIncrement.matchEntire(this)) {
            null -> {
                val cutMatch = regexCut.matchEntire(this).toOption()
                SingleShuffleMethods.cut(cutMatch.getOrElse { throw error("unknown style: $this") }.groupValues[1].toBigInteger())
            }
            else -> SingleShuffleMethods.dealIncrement(incrementMatch.groupValues[1].toBigInteger())
        }
    }

    object SingleShuffleMethods {
        fun dealIntoStack() = BigInteger.ONE.negate() to BigInteger.ONE.negate()
        fun cut(n: BigInteger) = BigInteger.ONE to n.negate()
        fun dealIncrement(n: BigInteger) = n to BigInteger.ZERO
    }

    private fun power(
        applyShuffle: (Pair<BigInteger, BigInteger>, Pair<BigInteger, BigInteger>) -> Pair<BigInteger, BigInteger>,
        x: Pair<BigInteger, BigInteger>,
        n: BigInteger
    ): Pair<BigInteger, BigInteger> {
        return when {
            n == BigInteger.ONE -> x
            n % BigInteger.TWO == BigInteger.ZERO -> power(applyShuffle, applyShuffle(x, x), (n / BigInteger.TWO))
            else -> applyShuffle(x, power(applyShuffle, applyShuffle(x, x), (n / BigInteger.TWO)))
        }
    }

    fun part2(input: List<String>): BigInteger {
        val numberOfCards = BigInteger.valueOf(119315717514047)
        val shuffleCount = BigInteger.valueOf(101741582076661)
        val positionToCheck = BigInteger.valueOf(2020)

        val comp = { f: Pair<BigInteger, BigInteger>, g: Pair<BigInteger, BigInteger> ->
            (g.first * f.first) % numberOfCards to (g.first * f.second + g.second) % numberOfCards
        }
        val oneShuffle = input.map { it.toModOffsetPair() }.reduce(comp)
        val finalModOffset = power(comp, oneShuffle, shuffleCount).second
        val inverseModValue =
            oneShuffle.first.modPow(shuffleCount, numberOfCards).modInverse(numberOfCards)

        return (inverseModValue * positionToCheck + (inverseModValue.negate() * finalModOffset) % numberOfCards) % numberOfCards
    }

}

fun main() {
    println(Day22.part1(Day22.fileData).withIndex().first { (_, v) -> v == 2019 })
    println(Day22.part2(Day22.fileData))
}
