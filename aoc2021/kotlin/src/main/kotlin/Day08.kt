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

object Day08 : AdventOfCode() {

    private val validLens = setOf(One, Four, Seven, Eight).map { it.numSegments }.toSet()

    private fun seekUniqueSignatures(input: String) =
        input.split(" | ")[1]
            .split(" ")
            .count { display -> display.length in validLens }

    fun part1(input: List<String>) = input.sumOf { seekUniqueSignatures(it) }

    data class SignalInfo(val c: Char, val appearances: Int)

    // Two is the only number lacking the most common segment
    private fun Map<String, List<DigitDisplay>>.establishTwo(frequencies: List<SignalInfo>) =
        mapValues { (k, v) ->
            when (frequencies.last().c) {
                in k -> v - Two
                else -> listOf(Two)
            }
        }

    // Three Five and Nine, are the only non-unique candidates that contain the least common segment.
    private fun Map<String, List<DigitDisplay>>.removeIllegal359(frequencies: List<SignalInfo>) =
        mapValues { (k, v) ->
            when (frequencies[0].c) {
                in k -> v.filter { it !in listOf(Three, Five, Nine) }
                else -> v
            }
        }

    // Three can never have the second least common number
    private fun Map<String, List<DigitDisplay>>.removeIllegal3(frequencies: List<SignalInfo>) =
        mapValues { (k, v) ->
            when (frequencies[1].c) {
                in k -> v - Three
                else -> v
            }
        }

    // When we run this, we already have a five, so remove it from any lists that still contain more than just Five
    private fun Map<String, List<DigitDisplay>>.removeIllegal5() =
        mapValues { (_, v) ->
            when {
                Five in v && v.size > 1 -> v - Five
                else -> v
            }
        }

    // Nine will be chilling uniquely in a 0 6 9 triplet.
    private fun Map<String, List<DigitDisplay>>.isolate9() =
        mapValues { (_, v) ->
            when {
                v.size == 3 && v.containsAll(listOf(Zero, Six, Nine)) -> listOf(Nine)
                else -> v
            }
        }

    // The final candidate pair is 0 and 6.  The second and third most common chars only appear in 6.
    private fun Map<String, List<DigitDisplay>>.establish60(infos: List<SignalInfo>) =
        mapValues { (k, v) ->
            val (a, b) = infos.dropLast(1).takeLast(2).map(SignalInfo::c)
            val containsChar = (a !in k || b !in k)
            when {
                containsChar && v.contains(Six) -> listOf(Six)
                !containsChar && v.contains(Six) -> listOf(Zero)
                else -> v
            }
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
            .establishTwo(frequencies)
            .removeIllegal359(frequencies)
            .removeIllegal3(frequencies)
            .removeIllegal5()
            .isolate9()
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
