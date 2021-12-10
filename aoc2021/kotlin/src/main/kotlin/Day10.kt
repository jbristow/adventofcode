import Day10.Brace.Companion.toBrace
import util.AdventOfCode

object Day10 : AdventOfCode() {
    sealed class Brace(val glyph: Char) {

        sealed class Open(glyph: Char, val twin: Closed) : Brace(glyph) {
            object Paren : Open(glyph = '(', twin = Closed.Paren)
            object Square : Open(glyph = '[', twin = Closed.Square)
            object Curly : Open(glyph = '{', twin = Closed.Curly)
            object Angle : Open(glyph = '<', twin = Closed.Angle)
        }

        sealed class Closed(
            glyph: Char,
            val corruptionScore: Int,
            val incompleteScore: Int
        ) : Brace(glyph) {
            object Paren : Closed(glyph = ')', corruptionScore = 3, incompleteScore = 1)
            object Square : Closed(glyph = ']', corruptionScore = 57, incompleteScore = 2)
            object Curly : Closed(glyph = '}', corruptionScore = 1197, incompleteScore = 3)
            object Angle : Closed(glyph = '>', corruptionScore = 25137, incompleteScore = 4)
        }

        override fun toString() = this.glyph.toString()

        companion object {
            fun Char.toBrace(): Brace {
                return when (this) {
                    Open.Paren.glyph -> Open.Paren
                    Open.Square.glyph -> Open.Square
                    Open.Curly.glyph -> Open.Curly
                    Open.Angle.glyph -> Open.Angle
                    Closed.Paren.glyph -> Closed.Paren
                    Closed.Square.glyph -> Closed.Square
                    Closed.Curly.glyph -> Closed.Curly
                    Closed.Angle.glyph -> Closed.Angle
                    else -> throw IllegalArgumentException("Could not convert $this to brace")
                }
            }
        }
    }

    private tailrec fun findProblem(line: String, seen: ArrayDeque<Brace.Open> = ArrayDeque()): Problem {
        if (line.isEmpty()) {
            return Problem.Incomplete(seen)
        }

        return when (val brace = line.first().toBrace()) {
            is Brace.Open -> findProblem(line.drop(1), seen.apply { add(brace) })
            is Brace.Closed -> {
                when (brace) {
                    seen.last().twin -> findProblem(line.drop(1), seen.apply { removeLast() })
                    else -> Problem.Corrupt(brace.corruptionScore)
                }
            }
        }
    }

    sealed interface Problem {
        data class Incomplete(val seen: ArrayDeque<Brace.Open>) : Problem
        data class Corrupt(val score: Int) : Problem
    }

    fun part1(input: List<String>) =
        input.asSequence()
            .map { findProblem(it) }
            .filterIsInstance<Problem.Corrupt>()
            .sumOf { it.score }

    private fun findProblem(seen: ArrayDeque<Brace.Open>) =
        seen.map { it.twin.incompleteScore }
            .reversed()
            .fold(0L) { acc, it -> 5 * acc + it }

    fun part2(input: List<String>) =
        input.asSequence()
            .map { findProblem(it) }
            .filterIsInstance<Problem.Incomplete>()
            .map { findProblem(it.seen) }
            .sorted()
            .toList()
            .let { it.drop((it.size - 1) / 2).first() }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
