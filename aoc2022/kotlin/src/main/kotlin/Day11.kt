import util.AdventOfCode
import java.math.BigInteger
import java.util.LinkedList

object Day11 : AdventOfCode() {
    private val THREE: BigInteger = 3.toBigInteger()

    sealed class Operation {
        abstract val value: BigInteger

        class Plus(override val value: BigInteger) : Operation()
        class Times(override val value: BigInteger) : Operation()
        object Square : Operation() {
            override val value: BigInteger = BigInteger.ZERO
        }

        fun execute(old: BigInteger): BigInteger = when (this) {
            is Plus -> old + value
            is Times -> old * value
            is Square -> old * old
        }
    }

    data class Monkey(
        val id: Int,
        val items: LinkedList<BigInteger> = LinkedList(),
        val test: BigInteger,
        val onTrue: Int,
        val onFalse: Int,
        val operation: Operation
    ) {
        fun elevateWorry(old: BigInteger) = this.operation.execute(old)

        fun decideThrowTarget(worryLevel: BigInteger): Int = if (worryLevel.mod(test) == BigInteger.ZERO) {
            onTrue
        } else {
            onFalse
        }
    }

    private fun parseIntoMonkeys(input: String): List<Monkey> {
        return input.split("\n\n").map { parseIntoMonkey(it) }
    }

    private fun parseIntoMonkey(input: String): Monkey {
        val lines = input.lines()
        return Monkey(
            id = """Monkey (\d+):""".toRegex().matchEntire(lines[0])!!.groups[1]!!.value.toInt(),
            items = LinkedList(lines[1].split(": ")[1].split(", ").map { it.toBigInteger() }),
            operation = parseOperation(lines[2]),
            test = lines[3].split(" by ")[1].toBigInteger(),
            onTrue = lines[4].split(" monkey ")[1].toInt(),
            onFalse = lines[5].split(" monkey ")[1].toInt()
        )
    }

    private fun parseOperation(line: String): Operation {
        val matchGroups = """ {2}Operation: new = old ([*+]) (\d+|old)""".toRegex().matchEntire(line)!!.groups

        return when (matchGroups[1]?.value) {
            "+" -> Operation.Plus(matchGroups[2]!!.value.toBigInteger())
            "*" -> when (matchGroups[2]!!.value) {
                "old" -> Operation.Square
                else -> Operation.Times(matchGroups[2]!!.value.toBigInteger())
            }
            else -> throw IllegalArgumentException("Bad Operation Line: $line")
        }
    }

    private fun monkeyTurn(monkey: Monkey) =
        monkey.items.map { monkey.elevateWorry(it).divide(THREE) }.map { it to monkey.decideThrowTarget(it) }

    private tailrec fun monkeyToss(
        monkeyMap: Map<Int, Monkey>,
        roundsLeft: Int,
        activity: MutableMap<Int, BigInteger> = mutableMapOf(),
        monkeyIds: List<Int> = monkeyMap.keys.sorted()
    ): MutableMap<Int, BigInteger> {
        if (roundsLeft == 0) {
            return activity
        }
        monkeyIds.forEach {
            val result = monkeyTurn(monkeyMap[it]!!)
            result.forEach { (item, target) ->
                monkeyMap[target]!!.items.addLast(item)
                activity[it] = activity.getOrDefault(it, BigInteger.ZERO) + BigInteger.ONE
            }
        }
        return monkeyToss(monkeyMap, roundsLeft - 1, activity, monkeyIds)
    }

    private fun worryingMonkeyTurn(monkey: Monkey, commonModulo: BigInteger) =
        monkey.items.map { monkey.elevateWorry(it).mod(commonModulo) }.map { it to monkey.decideThrowTarget(it) }

    private tailrec fun worryingMonkeyToss(
        monkeyMap: Map<Int, Monkey>,
        commonModulo: BigInteger,
        turn: Long = 0,
        activity: MutableMap<Int, BigInteger> = mutableMapOf(),
        monkeyIds: List<Int> = monkeyMap.keys.sorted()
    ): Map<Int, BigInteger> {
        if (turn == 10_000L) {
            return activity
        }
        monkeyIds.forEach {
            val result = worryingMonkeyTurn(monkeyMap[it]!!, commonModulo)
            result.forEach { (item, target) ->
                monkeyMap[target]!!.items.addLast(item)
                activity[it] = activity.getOrDefault(it, BigInteger.ZERO) + BigInteger.ONE
            }
        }
        return worryingMonkeyToss(monkeyMap, commonModulo, turn + 1, activity, monkeyIds)
    }

    private fun part1(input: String): BigInteger {
        val monkeys = parseIntoMonkeys(input)
        val activity = monkeyToss(monkeys.associateBy { it.id }, 20, monkeyIds = monkeys.map { it.id })
        return activity.values.sorted().takeLast(2).reduce { a, b -> a * b }
    }

    private fun part2(input: String): BigInteger {
        val monkeys = parseIntoMonkeys(input)
        val commonModulo = monkeys.map { it.test }.reduce(BigInteger::times)
        val activity =
            worryingMonkeyToss(monkeys.associateBy { it.id }, commonModulo, monkeyIds = monkeys.map { it.id })
        return activity.values.sorted().takeLast(2).reduce(BigInteger::times)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 11")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
