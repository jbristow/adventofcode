import arrow.core.padZip
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import util.AdventOfCode

object Day13Alt : AdventOfCode("day13.txt") {

    private val mapper = jacksonObjectMapper()

    private fun String.asJson(): JsonNode = mapper.readValue(this)
    private fun List<String>.asJson(): List<JsonNode> = this.mapNotNull { if (it.isBlank()) null else it.asJson() }

    private fun JsonNode.toJsonArray() =
        JsonNodeFactory.instance.arrayNode().apply { add(this@toJsonArray) }

    private val properOrder: Comparator<in JsonNode?> = Comparator { a, b ->
        when (properlyOrdered(listOf(a, b))) {
            false -> 1
            else -> -1
        }
    }

    private fun part1(input: String): Int {
        val output = input.split("\n\n")
            .map { pair -> pair.lines().asJson() }
            .withIndex()
            .filter { (_, lines) -> properlyOrdered(lines) ?: true }

        return output.sumOf { it.index + 1 }
    }

    private fun part2(input: String): Int {
        val packetDividers = listOf("[[6]]", "[[2]]").asJson().sortedWith(properOrder)

        val packets = input.lines().asJson()

        return packetDividers.asSequence()
            .map { divider ->
                packets.asSequence().map { packet -> listOf(packet, divider) }
            }.withIndex()
            .map { (index, pairs) ->
                (index + 1) + pairs.count { properlyOrdered(it) ?: true }
            }.reduce(Int::times)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 13 (Alt)")
        println("  Part 1: ${part1(inputFileString)}")
        println("  Part 1: ${part2(inputFileString)}")
    }

    private fun properlyOrdered(left: ArrayNode, right: ArrayNode): Boolean? {
        return left.padZip(right)
            .fold(null as Boolean?) { opt, (l, r) ->
                opt ?: properlyOrdered(listOf(l, r))
            }
    }

    private fun properlyOrdered(left: IntNode, right: IntNode) = when {
        left.asInt() < right.asInt() -> true
        left.asInt() > right.asInt() -> false
        else -> null
    }

    private fun properlyOrdered(lines: List<JsonNode?>): Boolean? {
        val (left, right) = lines
        return when (left) {
            null -> true
            is ArrayNode -> when (right) {
                null -> false
                is ArrayNode -> properlyOrdered(left, right)
                is IntNode -> properlyOrdered(left, right.toJsonArray())
                else -> throw IllegalArgumentException("Could not parse right: $right")
            }
            is IntNode -> when (right) {
                null -> false
                is ArrayNode -> properlyOrdered(left.toJsonArray(), right)
                is IntNode -> properlyOrdered(left, right)
                else -> throw IllegalArgumentException("Could not parse right: $right")
            }
            else -> throw IllegalArgumentException("Could not parse left: $left")
        }
    }
}
