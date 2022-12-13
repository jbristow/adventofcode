import util.AdventOfCode
import java.util.LinkedList
import kotlin.math.max

object Day13 : AdventOfCode() {

    sealed interface CharType {
        object BeginList : CharType
        object Comma : CharType
    }

    sealed class SignalType : CharType {
        open fun toList(): ListType {
            throw IllegalAccessError("Not implemented")
        }

        data class IntType(val value: Int) : SignalType() {
            override fun toList() = ListType(LinkedList(listOf(this)))
            override fun toString() = "$value"
        }

        data class ListType(val value: LinkedList<SignalType>) : SignalType() {
            constructor(value: SignalType) : this(LinkedList(listOf(value)))

            override fun toString() = "[${value.joinToString(",")}]"
            override fun toList() = this
            fun isEmpty() = value.isEmpty()
            fun isNotEmpty() = value.isNotEmpty()
            operator fun get(i: Int) = value[i]
        }
    }

    private fun toSignalValue(line: String, stack: LinkedList<CharType> = LinkedList()): SignalType {
        if (line.isEmpty()) {
            return stack.pop() as SignalType
        }
        when (val head = line[0]) {
            '[' -> {
                if (stack.peek() is CharType.Comma) {
                    stack.pop()
                }
                stack.push(CharType.BeginList)
            }
            ',' -> stack.push(CharType.Comma)
            ']' -> {
                val accumulator = LinkedList<SignalType>()
                while (stack.peek() !is CharType.BeginList) {
                    accumulator.push(stack.pop() as SignalType)
                }
                stack.pop()
                stack.push(SignalType.ListType(accumulator))
            }
            in "01234567890" -> {
                when (stack.peek()) {
                    is SignalType.IntType -> {
                        val lastNum = stack.pop() as SignalType.IntType
                        stack.push(SignalType.IntType(lastNum.value * 10 + head.digitToInt()))
                    }
                    is CharType.Comma -> {
                        stack.pop() // remove comma
                        stack.push(SignalType.IntType(head.digitToInt()))
                    }
                    is SignalType.ListType, is CharType.BeginList -> {
                        stack.push(SignalType.IntType(head.digitToInt()))
                    }
                }
            }
        }
        return toSignalValue(line.substring(1), stack)
    }

    fun properlyOrdered(leftIn: SignalType, rightIn: SignalType): Boolean? {
        if (leftIn is SignalType.IntType && rightIn is SignalType.IntType) {
            return when {
                leftIn.value < rightIn.value -> true
                leftIn.value > rightIn.value -> false
                else -> null
            }
        }

        val left = leftIn.toList()
        val right = rightIn.toList()

        return (0 until max(left.value.size, right.value.size)).fold(null) { acc: Boolean?, i ->

            acc ?: when {
                left.value.size <= i -> true
                right.value.size <= i -> false
                else -> properlyOrdered(left[i], right[i])
            }
        }
    }

    fun part1(input: String): Int {
        val pairs = input.split("\n\n").map { it.lines().map { line -> toSignalValue(line) } }
            .withIndex()
            .filter { lines ->
                properlyOrdered(lines.value[0], lines.value[1]) ?: true
            }

        return pairs.sumOf { it.index + 1 }
    }

    fun part2(input: String): Int {
        val lines = input.lines().filter { it.isNotEmpty() }.map { toSignalValue(it) }
        val packetDivider1 = SignalType.ListType(SignalType.ListType(SignalType.IntType(2)))
        val packetDivider2 = SignalType.ListType(SignalType.ListType(SignalType.IntType(6)))
        val beforePd1 = lines.filter { properlyOrdered(it, packetDivider1) ?: true }
        val beforePd2 = lines.filter { properlyOrdered(it, packetDivider2) ?: true }
        return (beforePd1.size + 1) * (beforePd2.size + 2)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
