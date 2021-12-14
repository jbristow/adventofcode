import util.AdventOfCode
import java.util.LinkedList

object Day14 : AdventOfCode() {
    fun part1(input: List<String>): Int {
        val template = input.first()
        val rules = input.drop(2).associate { it.split(" -> ").let { (a, b) -> a to b } }

        val result = mutation(template, rules, 10)
        val freqs = result.groupBy { it }.mapValues { it.value.size }
        return (freqs.maxOf { it.value }) - (freqs.minOf { it.value })
    }

    private tailrec fun mutation(template: String, rules: Map<String, String>, maxSteps: Int, step: Int = 0): String {
        if (maxSteps == step) {
            return template
        }

        val nextTemplate = (
            template.windowed(2, 1).fold(template.first().toString()) { acc, s ->
                val insertion = rules[s] ?: ""
                "$acc$insertion${s[1]}"
            }
            )

        return mutation(nextTemplate, rules, maxSteps, step + 1)
    }

    fun part2(input: List<String>): Long {
        val template = input.first()
        val rules = input.drop(2).associate {
            it.split(" -> ").let { (a, b) ->
                a to Rule("${a[0]}$b", "$b${a[1]}", b.first())
            }
        }

        val templateLinked = LinkedList<Char>()
        templateLinked.addAll(template.toCharArray().toList())
        val pairs = template.windowed(2, 1)
            .groupBy { it }
            .mapValues { it.value.count().toLong() }
            .toMutableMap()
        val result = mutationByFrequency(
            pairs,
            template.toCharArray().toList().freq().toMutableMap(),
            rules,
            40
        )
        return result.maxOf { it.value } - result.minOf { it.value }
    }

    data class Rule(val left: String, val right: String, val char: Char)

    private tailrec fun mutationByFrequency(
        pairFrequency: Map<String, Long>,
        charFrequency: MutableMap<Char, Long>,
        rules: Map<String, Rule>,
        maxSteps: Int,
        step: Int = 0
    ): MutableMap<Char, Long> {
        if (maxSteps == step) {
            return charFrequency
        }

        val nextPairFrequency: MutableMap<String, Long> = mutableMapOf()
        pairFrequency.forEach {
            val rule = rules[it.key]!!
            nextPairFrequency[rule.left] = (nextPairFrequency[rule.left] ?: 0) + it.value
            nextPairFrequency[rule.right] = (nextPairFrequency[rule.right] ?: 0) + it.value
            charFrequency[rule.char] = (charFrequency[rule.char] ?: 0) + it.value
        }

        return mutationByFrequency(nextPairFrequency, charFrequency, rules, maxSteps, step + 1)
    }

    private fun <T> Iterable<T>.freq(): Map<T, Long> {
        val countm = mutableMapOf<T, Long>()
        forEach { c ->
            countm[c] = (countm[c] ?: 0) + 1
        }
        return countm.toMap()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 14")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
