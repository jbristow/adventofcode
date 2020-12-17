import util.AdventOfCode

object Day16 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("part1: ${part1(inputFileString)}")
        println("part2: ${part2(inputFileString)}")
    }

    private fun part1(data: String): Int {
        val (rulesRaw, _, nearbyRaw) = data.split("\n\n")

        val rules = rulesRaw.lines()
            .mapNotNull { """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex().matchEntire(it) }
            .map {
                val (name, a, b, c, d) = it.destructured
                Rule(name, a.toInt()..b.toInt(), c.toInt()..d.toInt())
            }

        return nearbyRaw.lines().drop(1)
            .flatMap { line ->
                line.split(",")
                    .map(String::toInt)
                    .filter { n -> rules.none { n in it } }
            }.sum()
    }

    tailrec fun identifyColumns(
        cols: List<Pair<String, Set<Int>>>,
        solved: Map<String, Int> = emptyMap(),
    ): Map<String, Int> {

        if (cols.isEmpty()) {
            return solved
        }

        val (name, list) = cols.first()

        if (list.size != 1) {
            throw Exception("Too many options for this idea.")
        }

        val index = list.first()
        return identifyColumns(
            cols.drop(1).map { (n, l) -> n to (l - index) },
            solved + (name to index)
        )
    }

    private fun part2(data: String): Long {
        val (rulesRaw, ticketRaw, nearbyRaw) = data.split("\n\n")

        val ticket = ticketRaw.lines().last().split(",").map(String::toInt)
        val rules = rulesRaw.lines()
            .mapNotNull { """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex().matchEntire(it) }
            .map {
                val (name, a, b, c, d) = it.destructured
                Rule(name, a.toInt()..b.toInt(), c.toInt()..d.toInt())
            }

        val legit = nearbyRaw.lines().drop(1)
            .map { it.split(",").map(String::toInt) }
            .filter { ns ->
                ns.all { n -> rules.any { n in it } }
            }

        val potentials = rules
            .map { rule -> rule.name to legalColumns(legit, rule) }
            .sortedBy { it.second.size }

        return identifyColumns(potentials).filterKeys { it.startsWith("departure") }.values.map { ticket[it] }
            .map(Int::toLong)
            .reduce { a, b -> a * b }
    }

    data class Rule(val name: String, val r1: ClosedRange<Int>, val r2: ClosedRange<Int>) {
        operator fun contains(n: Int) = n in r1 || n in r2
    }

    private fun legalColumns(
        legit: List<List<Int>>,
        rule: Rule,
    ) = legit.first().indices
        .map { i -> i to legit.map { it[i] } }
        .filter { (_, col) -> col.all(rule::contains) }
        .map { it.first }
        .toSet()
}
