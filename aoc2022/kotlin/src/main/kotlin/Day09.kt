import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.minus
import util.Point2d.Companion.plus
import java.util.LinkedList
import kotlin.math.abs

object Day09 : AdventOfCode() {
    data class Rope(val head: Point2d = Point2d(0, 0), val tail: Point2d = Point2d(0, 0)) : RopeLike {
        override val tailPosition: Point2d
            get() = tail

        override fun move(direction: Point2d): Rope {
            if (direction == Point2d(0, 0)) {
                return this
            }

            val newHead = head + direction

            val xdiff = abs(newHead.x - tail.x)
            val ydiff = abs(newHead.y - tail.y)

            val newTail: Point2d = tail + when {
                xdiff <= 1 && ydiff <= 1 -> Point2d()
                newHead.x < tail.x && newHead.y < tail.y -> Point2d(-1, -1)
                newHead.x < tail.x && newHead.y == tail.y -> Point2d(-1, 0)
                newHead.x < tail.x -> Point2d(-1, 1)
                newHead.x == tail.x && newHead.y < tail.y -> Point2d(0, -1)
                newHead.x == tail.x && newHead.y > tail.y -> Point2d(0, 1)
                newHead.x > tail.x && newHead.y < tail.y -> Point2d(1, -1)
                newHead.x > tail.x && newHead.y == tail.y -> Point2d(1, 0)
                newHead.x > tail.x -> Point2d(1, 1)
                else -> throw IllegalArgumentException("Unknown error, could not move $this in direction $direction")
            }
            return Rope(newHead, newTail)
        }
    }

    interface RopeLike {
        val tailPosition: Point2d
        fun move(direction: Point2d): RopeLike
    }

    data class LongRope(
        val knot0: Point2d = Point2d(),
        val knots: List<Point2d> = (1..9).map { Point2d() }
    ) : RopeLike {
        override val tailPosition: Point2d
            get() = knots.last()

        override fun move(direction: Point2d): LongRope {
            val rope0 = Rope(knot0, knots.first()).move(direction)
            val newKs =
                knots.windowed(2).runningFold(rope0.tail) { prev, (k1, k2) -> Rope(k1, k2).move(prev - k1).tail }
            return LongRope(
                rope0.head,
                newKs
            )
        }
    }

    data class Move(val direction: Point2d, val amount: Int)

    private fun toMove(line: String): Move {
        val parts = line.split(" ")
        return Move(
            when (parts[0]) {
                "U" -> Point2d(0, -1)
                "R" -> Point2d(1, 0)
                "D" -> Point2d(0, 1)
                "L" -> Point2d(-1, 0)
                else -> throw IllegalArgumentException("Bad move $line")
            },
            parts[1].toInt()
        )
    }

    private tailrec fun doMoves(
        moves: LinkedList<Move>,
        rope: RopeLike,
        seen: MutableSet<Point2d> = mutableSetOf(rope.tailPosition)
    ): Set<Point2d> {
        if (moves.isEmpty()) {
            return seen
        }
        val head = moves.removeFirst()
        val newRope = rope.move(head.direction)
        seen.add(newRope.tailPosition)
        if ((head.amount - 1) != 0) {
            moves.add(0, head.copy(amount = head.amount - 1))
        }
        return doMoves(moves, newRope, seen)
    }

    fun part1(input: List<String>) = doMoves(LinkedList(input.map { toMove(it) }), Rope()).count()

    fun part2(input: List<String>) = doMoves(LinkedList(input.map { toMove(it) }), LongRope()).count()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 9")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
