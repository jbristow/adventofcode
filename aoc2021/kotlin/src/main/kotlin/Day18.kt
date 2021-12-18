import util.AdventOfCode
import kotlin.math.ceil
import kotlin.math.floor

object Day18 : AdventOfCode() {

    sealed class ParserChunk {
        data class Digits(val value: String) : ParserChunk()
    }

    sealed class SnailfishNumber : ParserChunk() {
        val magnitude: Long
            get() {
                return when (this) {
                    is Constant -> value
                    is Duo -> 3 * left.magnitude + 2 * right.magnitude
                }
            }
    }

    data class Constant(val value: Long) : SnailfishNumber() {
        constructor(str: String) : this(str.toLong())

        override fun toString() = value.toString()
    }

    data class Duo(val left: SnailfishNumber, val right: SnailfishNumber) : SnailfishNumber() {
        override fun toString() = "[$left,$right]"
    }

    operator fun Duo.plus(b: Duo): Duo {
        return Duo(this, b)
    }

    private fun simplifyInner(current: List<Pair<Constant, Int>>): List<Pair<Constant, Int>> {
        return when (val exploded = explode(current)) {
            current -> when (val split = split(current)) {
                current -> current
                else -> simplifyInner(split)
            }
            else -> simplifyInner(exploded)
        }
    }

    private fun simplify(n: Duo): Duo {
        val dfs = dfs(n)
        return reconstitute(simplifyInner(dfs))
    }

    private fun explode(dfs: List<Pair<Constant, Int>>): List<Pair<Constant, Int>> {
        if (dfs.all { it.second < 4 }) {
            return dfs
        }

        val before = dfs.takeWhile { it.second < 4 }
        val (l, r) = dfs.drop(before.count()).take(2)
        val after = dfs.drop(before.count() + 2)

        val newl = when {
            before.isEmpty() -> listOf()
            else -> listOf(Constant(before.last().first.value + l.first.value) to before.last().second)
        }
        val newr = when {
            after.isEmpty() -> listOf()
            else -> listOf(Constant(after.first().first.value + r.first.value) to after.first().second)
        }

        return before.dropLast(1) + newl + (Constant(0) to (l.second - 1)) + newr + after.drop(1)
    }

    fun split(dfs: List<Pair<Constant, Int>>): List<Pair<Constant, Int>> {
        val before = dfs.takeWhile { it.first.value < 10 }
        val after = dfs.drop(before.size)

        if (after.isEmpty()) {
            return before
        }

        val splitter = after.first()
        val half = (splitter.first.value * 0.5)
        val newDuo = listOf(
            Constant(floor(half).toLong()) to splitter.second + 1,
            Constant(ceil(half).toLong()) to splitter.second + 1
        )

        return before + newDuo + after.drop(1)
    }

    private tailrec fun reconstitute(
        list: List<Pair<SnailfishNumber, Int>>,
        stack: ArrayDeque<Pair<SnailfishNumber, Int>> = ArrayDeque()
    ): Duo {
        if (list.isEmpty()) {
            return stack.first().first as Duo
        }

        val (current, currentDepth) = list.first()
        val remaining = list.drop(1).toMutableList()
        val (_, topDepth) = stack.lastOrNull() ?: (null to null)
        return when {
            currentDepth != topDepth -> reconstitute(remaining, stack.apply { addLast(current to currentDepth) })
            currentDepth == topDepth -> {
                val newPiece = Duo(stack.removeLast().first, current)
                remaining.add(0, newPiece to (currentDepth - 1))
                reconstitute(remaining, stack)
            }
            else -> throw Exception("uhhhhh")
        }
    }

    private fun dfs(
        current: Duo,
        depth: Int = 0
    ): MutableList<Pair<Constant, Int>> {
        val seen: MutableList<Pair<Constant, Int>> = mutableListOf()
        when (current.left) {
            is Constant -> {
                seen.add(current.left to depth)
            }
            is Duo -> seen.addAll(dfs(current.left, depth + 1))
        }
        when (current.right) {
            is Constant -> {
                seen.add(current.right to depth)
            }
            is Duo -> seen.addAll(dfs(current.right, depth + 1))
        }
        return seen
    }

    private tailrec fun String.toSnailfishNumber(
        stack: ArrayDeque<ParserChunk> = ArrayDeque(),
        depth: Int = 0
    ): Duo {
        if (isEmpty()) {
            return stack.first() as Duo
        }
        val remaining = drop(1)
        return when (val current = first()) {
            '[' -> drop(1).toSnailfishNumber(stack, depth + 1)
            ',' -> when {
                stack.lastOrNull() is ParserChunk.Digits -> {
                    val digits = stack.removeLast() as ParserChunk.Digits
                    stack.addLast(Constant(digits.value))
                    remaining.toSnailfishNumber(stack, depth)
                }
                else -> remaining.toSnailfishNumber(stack, depth)
            }
            ']' -> {
                val right = when (val temp = stack.removeLast()) {
                    is ParserChunk.Digits -> Constant(temp.value.toLong())
                    is SnailfishNumber -> temp
                }
                val left = when (val temp = stack.removeLast()) {
                    is ParserChunk.Digits -> Constant(temp.value.toLong())
                    is SnailfishNumber -> temp
                }
                stack.addLast(Duo(left, right))
                remaining.toSnailfishNumber(stack, depth - 1)
            }
            in "01234567890" -> {
                when {
                    stack.lastOrNull() is ParserChunk.Digits -> {
                        val lastn = stack.removeLast() as ParserChunk.Digits
                        stack.addLast(lastn.copy(value = lastn.value + current))
                        remaining.toSnailfishNumber(stack, depth)
                    }
                    else -> {
                        stack.addLast(ParserChunk.Digits(current.toString()))
                        remaining.toSnailfishNumber(stack, depth)
                    }
                }
            }
            else -> throw Exception("Unknown character: $current")
        }
    }

    fun part1(input: List<String>): Long {
        val ns = input.map { it.toSnailfishNumber() }
        val answer = ns.drop(1).fold(ns.first()) { acc, it -> simplify(acc + it) }
        return answer.magnitude
    }

    fun part2(input: List<String>): Long {
        val ns = input.map { it.toSnailfishNumber() }

        return ns.indices.flatMap { a -> ns.indices.filter { b -> b != a }.map { b -> simplify(ns[a] + ns[b]) } }
            .maxOf { it.magnitude }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 16")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
