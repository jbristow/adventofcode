import util.AdventOfCode
import java.util.stream.Collectors

object Day18 : AdventOfCode() {

    sealed class Symbol {
        object OpenParen : Symbol()
        object CloseParen : Symbol()
        object Multi : Symbol()
        object Plus : Symbol()
        data class Number(val n: Long) : Symbol()

        operator fun plus(x: Symbol): Number {
            return when {
                this is Number && x is Number -> this.copy(n = this.n + x.n)
                else -> throw IllegalArgumentException()
            }
        }

        operator fun times(x: Symbol): Number {
            return when {
                this is Number && x is Number -> this.copy(n = this.n * x.n)
                else -> throw IllegalArgumentException()
            }
        }

        fun toLong(): Long = when (this) {
            is Number -> this.n
            else -> throw Exception("Can't extract number from non-number value")
        }

        fun greaterPrecedenceThan(other: Symbol): Boolean {
            return when {
                this is Plus && other is Multi -> true
                this is Multi && other is Plus -> false
                else -> false
            }
        }
    }

    fun calculate(s: ArrayDeque<Symbol>, dq: ArrayDeque<Symbol>): Long {
        return when (val currToken = s.firstOrNull()) {
            null -> return dq.first().toLong()
            is Symbol.Number -> {
                when (dq.lastOrNull()) {
                    null -> dq.add(currToken)
                    Symbol.Multi -> {
                        dq.removeLast()
                        dq.add(dq.removeLast() * currToken)
                    }
                    Symbol.Plus -> {
                        dq.removeLast()
                        dq.add(dq.removeLast() + currToken)
                    }
                    Symbol.OpenParen -> {
                        dq.add(currToken)
                    }
                }
                s.removeFirst()
                calculate(s, dq)
            }
            Symbol.Plus -> {
                dq.add(Symbol.Plus)
                s.removeFirst()
                calculate(s, dq)
            }
            Symbol.Multi -> {
                dq.add(Symbol.Multi)
                s.removeFirst()
                calculate(s, dq)
            }
            Symbol.OpenParen -> {
                dq.add(Symbol.OpenParen)
                s.removeFirst()
                calculate(s, dq)
            }
            Symbol.CloseParen -> {
                val a = dq.removeLast()
                dq.removeLast()
                s.removeFirst()
                s.addFirst(a)
                calculate(s, dq)
            }
        }
    }

    fun priorityCalculation(tokens: ArrayDeque<Day18.Symbol>): Long {
        val output = shuntingYard(tokens)
        return rpn(output)
    }

    tailrec fun <T> ArrayDeque<T>.popOntoWhile(other: ArrayDeque<T>, fn: (ArrayDeque<T>) -> Boolean) {
        if (fn(this)) {
            other.addLast(this.removeLast())
            popOntoWhile(other, fn)
        }
    }

    tailrec fun <T> ArrayDeque<T>.popOnto(other: ArrayDeque<T>) {
        if (this.isNotEmpty()) {
            other.addLast(this.removeLast())
            popOnto(other)
        }
    }

    tailrec fun shuntingYard(
        tokens: ArrayDeque<Symbol>,
        output: ArrayDeque<Symbol> = ArrayDeque(),
        operators: ArrayDeque<Symbol> = ArrayDeque(),
    ): ArrayDeque<Symbol> {
        if (tokens.isEmpty()) {
            operators.popOnto(output)
            return output
        }

        when (val currToken = tokens.removeFirst()) {
            is Symbol.Number -> output.addLast(currToken)
            Symbol.Plus, Symbol.Multi -> {
                operators.popOntoWhile(output) {
                    it.isNotEmpty() &&
                        it.last().greaterPrecedenceThan(currToken) &&
                        it.last() !is Symbol.OpenParen
                }
                operators.addLast(currToken)
            }
            Symbol.OpenParen -> operators.addLast(currToken)
            Symbol.CloseParen -> {
                operators.popOntoWhile(output) {
                    operators.isNotEmpty() &&
                        operators.lastOrNull() !is Symbol.OpenParen
                }
                if (operators.lastOrNull() is Symbol.OpenParen) {
                    operators.removeLast()
                }
            }
        }
        return shuntingYard(tokens, output, operators)
    }

    tailrec fun rpn(tokens: ArrayDeque<Symbol>, memory: ArrayDeque<Long> = ArrayDeque()): Long {
        if (tokens.isEmpty()) {
            return memory.first()
        }
        when (val current = tokens.removeFirst()) {
            is Symbol.Number -> memory.addLast(current.toLong())
            is Symbol.Plus -> {
                memory.addLast(memory.removeLast() + memory.removeLast())
            }
            is Symbol.Multi -> {
                memory.addLast(memory.removeLast() * memory.removeLast())
            }
            else -> throw Exception("Unexpected: ${tokens.firstOrNull()}")
        }
        return rpn(tokens, memory)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("hello")
        println(calculate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2".toDeque(), ArrayDeque()))

        println(
            inputFileLines.parallelStream().map { calculate(it.toDeque(), ArrayDeque()) }
                .collect(Collectors.summingLong { it })
        )
        println(
            inputFileLines.parallelStream().map { priorityCalculation(it.toDeque()) }
                .collect(Collectors.summingLong { it })
        )
    }
}

private fun String.toDeque(): ArrayDeque<Day18.Symbol> {
    return ArrayDeque(
        this.mapNotNull {
            when (it) {
                '+' -> Day18.Symbol.Plus
                '*' -> Day18.Symbol.Multi
                '(' -> Day18.Symbol.OpenParen
                ')' -> Day18.Symbol.CloseParen
                in '0'..'9' -> Day18.Symbol.Number(it.toString().toLong())
                else -> null
            }
        }
    )
}
