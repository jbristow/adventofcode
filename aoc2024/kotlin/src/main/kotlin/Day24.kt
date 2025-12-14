import util.AdventOfCode

object Day24 : AdventOfCode() {
    val sample = """x00: 0
x01: 1
x02: 0
x03: 1
x04: 0
x05: 1
y00: 0
y01: 0
y02: 1
y03: 1
y04: 0
y05: 1

x00 AND y00 -> z05
x01 AND y01 -> z02
x02 AND y02 -> z01
x03 AND y03 -> z03
x04 AND y04 -> z04
x05 AND y05 -> z00"""

    @JvmStatic
    fun main(args: Array<String>) {
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(sample)}")
    }

    private fun part2(input: String): String {
        val (initial, rules) = input.split("\n\n").map { it }

        val xN = initial.lines().filter { it.startsWith("x") }.sortedDescending().joinToString("") { it.split(": ")[1] }.toBigInteger(2)
        println(xN)
        val yN = initial.lines().filter { it.startsWith("y") }.sortedDescending().joinToString("") { it.split(": ")[1] }.toBigInteger(2)
        println(yN)
        val zN = xN + yN
        println(zN)
        val zInitial = zN.toString(2).reversed().mapIndexed { i, it -> "z%02d: $it".format(i) }

        val (rules1, used) = initializeRules(rules, initial.lines())
        println(initial.lines().filter { it.startsWith("x") }.sorted())
        println(initial.lines().filter { it.startsWith("y") }.sorted())
        println(zInitial)
        val errorCount = simplify(rules1, used).filter { it.startsWith("z") && it !in zInitial }
        println("errors: " + errorCount)

        val initial0 = initial.replace(""": 0""".toRegex(), ": 1")
        val (rules01, used0) = initializeRules(rules, initial0.lines())
        println(simplify(rules01, used0).filter { it.startsWith("z") }.sorted())
        return ""
    }

    private tailrec fun oneLineify(
        rules: List<String>,
        toApply: List<String> = rules.toList(),
    ): List<String> {
        if (toApply.isEmpty()) {
            return rules
        }
        val current = toApply.first()
        val (op, symbol) = current.split(""" -> """.toRegex())
        return oneLineify(
            rules.map {
                if (it != current && symbol in it && !symbol.startsWith("z")) {
                    it.replace(symbol, "($op)")
                } else {
                    it
                }
            },
            toApply.drop(1),
        )
    }

    private fun part1(input: String): Long {
        val (initial, rules) = input.split("\n\n").map { it }

        val (rules1, used) = initializeRules(rules, initial.lines())
        val rules2 = simplify(rules1, used)
        return rules2.filter { it.startsWith("z") }.sortedDescending().joinToString("") { it.split(""":\s+""".toRegex())[1] }.toLong(2)
    }

    private tailrec fun simplify(
        rules: List<String>,
        used: List<String> = emptyList(),
    ): List<String> {
        val (a, b) = rules.partition { it.matches("""\d [A-Z]+ \d -> \w+""".toRegex()) }
        val rules = b.joinToString("\n")

        val initial = a.map { it.split("""\s+""".toRegex()) }.map { (a, op, b, _, symbol) ->
            when (op) {
                "XOR" -> "$symbol: ${a.toInt().xor(b.toInt())}"
                "AND" -> "$symbol: ${a.toInt().and(b.toInt())}"
                "OR" -> "$symbol: ${a.toInt().or(b.toInt())}"
                else -> throw IllegalArgumentException("Unknown operation $op")
            }
        }
        if (rules.isEmpty()) {
            return initial + used
        }
        val (initializedRules, newUsed) = initializeRules(rules, initial)
        return simplify(initializedRules, used + newUsed)
    }

    private tailrec fun initializeRules(
        rules: String,
        initializers: List<String>,
        used: List<String> = emptyList(),
    ): Pair<List<String>, List<String>> {
        if (initializers.isEmpty()) {
            return rules.lines() to used
        }
        val current = initializers.first()
        val (symbol, value) = current.split(""":\s+""".toRegex())
        return initializeRules(rules.replace("""\b$symbol\b""".toRegex(), value), initializers.drop(1), used + current)
    }
}
