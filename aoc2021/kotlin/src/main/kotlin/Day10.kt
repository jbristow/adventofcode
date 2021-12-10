import Day10.Brace.Companion.toBrace
import Day10.BraceDirection.Closed
import Day10.BraceDirection.Open
import Day10.BraceType.Angle
import Day10.BraceType.Curly
import Day10.BraceType.Paren
import Day10.BraceType.Square
import util.AdventOfCode

object Day10 : AdventOfCode() {
    sealed class Brace(
        val direction: BraceDirection,
        val type: BraceType,
        val score: Int? = null,
        val completer: Brace? = null,
        val completerScore: Int? = null
    ) {
        object OpenParen : Brace(direction = Open, type = Paren, completer = ClosedParen)
        object OpenSquare : Brace(direction = Open, type = Square, completer = ClosedSquare)
        object OpenCurly : Brace(direction = Open, type = Curly, completer = ClosedCurly)
        object OpenAngle : Brace(direction = Open, type = Angle, completer = ClosedAngle)
        object ClosedParen : Brace(direction = Closed, type = Paren, score = 3, completerScore = 1)
        object ClosedSquare : Brace(direction = Closed, type = Square, score = 57, completerScore = 2)
        object ClosedCurly : Brace(direction = Closed, type = Curly, score = 1197, completerScore = 3)
        object ClosedAngle : Brace(direction = Closed, type = Angle, score = 25137, completerScore = 4)

        companion object {
            fun Char.toBrace(): Brace {
                return toString().toBrace()
            }

            private fun String.toBrace(): Brace {
                return when (this) {
                    "(" -> OpenParen
                    "[" -> OpenSquare
                    "{" -> OpenCurly
                    "<" -> OpenAngle
                    ")" -> ClosedParen
                    "]" -> ClosedSquare
                    "}" -> ClosedCurly
                    ">" -> ClosedAngle
                    else -> throw IllegalArgumentException("Could not convert $this to brace")
                }
            }
        }
    }

    enum class BraceType {
        Paren, Square, Curly, Angle
    }

    enum class BraceDirection {
        Open, Closed
    }

    private tailrec fun scoreLine(line: String, seen: ArrayDeque<Brace> = ArrayDeque()): Int? {
        if (line.isEmpty()) {
            return 0
        }

        val c = line.first()
        val remaining = line.drop(1)

        val brace = c.toBrace()
        return when (brace.direction) {
            Open -> {
                seen.add(brace)
                scoreLine(remaining, seen)
            }
            Closed -> {
                when (seen.lastOrNull()?.type) {
                    brace.type -> {
                        seen.removeLast()
                        scoreLine(remaining, seen)
                    }
                    else -> brace.score
                }
            }
        }
    }

    fun part1(input: List<String>) = input.mapNotNull { scoreLine(it) }.sum()

    private tailrec fun completeAndScoreLine(line: String, seen: ArrayDeque<Brace> = ArrayDeque()): Long? {
        if (line.isEmpty()) {
            return completeLine(seen)
        }

        val c = line.first()
        val remaining = line.drop(1)

        val brace = c.toBrace()
        return when (brace.direction) {
            Open -> {
                seen.add(brace)
                completeAndScoreLine(remaining, seen)
            }
            Closed -> {
                when (seen.lastOrNull()?.type) {
                    brace.type -> {
                        seen.removeLast()
                        completeAndScoreLine(remaining, seen)
                    }
                    else -> null
                } // corrupt!
            }
        }
    }

    private tailrec fun completeLine(seen: ArrayDeque<Brace>, score: Long = 0): Long {
        if (seen.isEmpty()) {
            return score
        }
        val current = seen.removeLast()
        return completeLine(seen, 5 * score + current.completer!!.completerScore!!)
    }

    fun part2(input: List<String>) =
        input.mapNotNull { completeAndScoreLine(it) }.sorted().let { scores ->
            scores.drop((scores.size - 1) / 2).first()
        }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
