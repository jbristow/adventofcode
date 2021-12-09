import Day08.DigitDisplay.Eight
import Day08.DigitDisplay.Five
import Day08.DigitDisplay.Four
import Day08.DigitDisplay.Nine
import Day08.DigitDisplay.One
import Day08.DigitDisplay.Seven
import Day08.DigitDisplay.Six
import Day08.DigitDisplay.Three
import Day08.DigitDisplay.Two
import Day08.DigitDisplay.Zero
import util.AdventOfCode

private typealias Candidates = List<Day08.DigitDisplay>
private typealias SignalCandidates = Map<String, Candidates>

object Day08 : AdventOfCode() {

    private val validLens = setOf(One, Four, Seven, Eight).map { it.numSegments }.toSet()

    private fun seekUniqueSignatures(input: String) =
        input.split(" | ")[1]
            .split(" ")
            .count { display -> display.length in validLens }

    fun part1(input: List<String>) = input.sumOf { seekUniqueSignatures(it) }

    data class SignalInfo(val c: Char, val appearances: Int)

    private fun SignalCandidates.establish(
        markerDigit: DigitDisplay,
        excludeFn: ((Map.Entry<String, Candidates>)) -> Boolean,
        toRemove: (Candidates) -> Candidates,
        toSet: (Candidates) -> Candidates
    ): SignalCandidates {
        return mapValues {
            when {
                markerDigit in it.value && excludeFn.invoke(it) -> toRemove.invoke(it.value)
                markerDigit in it.value -> toSet.invoke(it.value)
                else -> it.value
            }
        }
    }

    private fun SignalCandidates.establish(
        markerDigit: DigitDisplay,
        excludeChar: Char,
        toRemove: (Candidates) -> Candidates,
        toSet: (Candidates) -> Candidates
    ) = establish(markerDigit, { (k, _) -> excludeChar in k }, toRemove, toSet)

    private fun SignalCandidates.establish(markerDigit: DigitDisplay, excludeChar: Char) =
        establish(markerDigit, { (k, _) -> excludeChar in k }, { it - markerDigit }, { listOf(markerDigit) })

    // Two is the only number lacking the most frequent segment
    private fun SignalCandidates.establish2(frequencies: List<SignalInfo>) =
        establish(Two, frequencies.last().c)

    // Three Five and Nine, are the only non-unique candidates that cannot contain the least frequent segment.
    // Since 3 and 5 always appear together, they will never have the least frequent segment anyway.
    // In the 069 triplet, 9 is the only one that does not have the least frequent segment.
    private fun SignalCandidates.establish9(frequencies: List<SignalInfo>) =
        establish(Nine, frequencies.first().c)

    // Three can never have the second least frequent number, and it always appears alongside 5
    private fun SignalCandidates.establish35(frequencies: List<SignalInfo>) =
        establish(Three, frequencies[1].c, { listOf(Five) }, { listOf(Three) })

    // The final candidate pair is 0 and 6.  The second and third most frequent chars cannot appear in 6
    private fun SignalCandidates.establish60(infos: List<SignalInfo>): SignalCandidates =
        infos.dropLast(1).takeLast(2).map(SignalInfo::c).let { (a, b) ->
            establish(Six, { (k, _) -> a in k && b in k }, { listOf(Zero) }, { listOf(Six) })
        }

    private fun processLine(input: String): Int {
        val (patterns, displays) = input.split(" | ").map { it.split(" ") }

        val frequencies = patterns.asSequence()
            .flatMap { it.toList() }
            .groupBy { it }
            .mapValues { it.value.count() }.entries
            .sortedBy { it.value }
            .map { (segmentChar, charFreq) -> SignalInfo(segmentChar, charFreq) }

        val knownCandidates = patterns.associateWith { DigitDisplay.candidatesForSize(it) }
            .establish2(frequencies)
            .establish9(frequencies)
            .establish35(frequencies)
            .establish60(frequencies).toMap()
            .mapValues { (_, v) -> v.first() }.mapKeys { (k, _) -> k.toSortedSet() }

        return displays.map { knownCandidates[it.toSortedSet()]?.digit }
            .joinToString("")
            .toInt()
    }

    fun part2(input: List<String>) = input.asSequence().map { processLine(it) }.sum()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    enum class DigitDisplay(val numSegments: Int, val digit: String) {
        Zero(6, "0"),
        One(2, "1"),
        Two(5, "2"),
        Three(5, "3"),
        Four(4, "4"),
        Five(5, "5"),
        Six(6, "6"),
        Seven(3, "7"),
        Eight(7, "8"),
        Nine(6, "9");

        companion object {
            fun candidatesForSize(input: String) =
                values().filter { it.numSegments == input.length }
        }
    }
}
