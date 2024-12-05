import util.*

object Day05 : AdventOfCode() {

    val sample = """47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47"""

    private fun part1(input: String): Int {
        val split = input.split("\n\n")
        val before = split[0].lines().map { line -> line.split("|").map { it.toInt() }.let { (a, b) -> a to b } }.groupBy({it.first},{it.second})
        val after = before.map { (k, v) -> v to k }.groupBy({ it.first }, { it.second })
        val updates = split[1].lines().map { line -> line.split(""",\s*""".toRegex()).map { line -> line.toInt() } }
        println(before)
        println(after)
        println(updates)
        //println(updates.map { it to orderedRaw(before, it) }.joinToString("\n") { "${it}" })
        val updated = updates.filter { ordered(before, it) }.sumOf {
            if ((it.size % 2) == 0) {
                it[it.size / 2 + 1]
            } else {
                it[it.size / 2]
            }
        }
        println(updated)
        return -1
    }

    private fun ordered(before: Map<Int, List<Int>>, input: List<Int>): Boolean {
        val s = "," + input.joinToString("") { "$it," }
        println("$s => ")
        return input.all { instr ->
            val positioning = (before[instr] ?: listOf()).all { b ->
                val rBefore = """,${instr}(,\d+)*,${b},"""
                val fBefore = rBefore.toRegex().find(s)
                println("- [$instr] '$rBefore' $fBefore")
                b !in input || fBefore != null
            }

            positioning
        }
    }

    private fun orderedRaw(before: Map<Int, Int>, after: Map<Int, List<Int>>, input: List<Int>): List<List<Any>> {
        val x = input.map { instr ->
            val curr = input.indexOf(instr)
            val bef = after[instr]?.map { input.indexOf(it) }?.filter { it >= 0 } ?: listOf()
            val aft = input.indexOf(before[instr])
            listOf(
                bef.isEmpty(), bef.all { it < curr }, aft == -1 || aft > curr,
            )
        }
        return x
    }

    private fun part2(input: String): Int {
        return -1
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
