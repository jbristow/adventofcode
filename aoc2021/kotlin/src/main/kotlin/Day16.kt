import util.AdventOfCode

object Day16 : AdventOfCode() {
    sealed class LiteralValue(val bits: Int) {
        class Incomplete(val value: String = "", bits: Int = 0) : LiteralValue(bits)
        class Complete(val value: String, bits: Int = 0) : LiteralValue(bits)
    }

    private fun LiteralValue.toLong(): Long {
        when (this) {
            is LiteralValue.Incomplete -> throw Exception("Attempted to unwrap incomplete literal.")
            is LiteralValue.Complete -> return this.value.toLong(2)
        }
    }

    sealed class Packet(val version: Int, val type: Int, val bits: Int) {

        class Literal(version: Int, type: Int, val value: Long, bits: Int) : Packet(version, type, bits)
        class Operator(version: Int, type: Int, val packets: List<Packet>, bits: Int) : Packet(version, type, bits)
    }

    private fun Packet.versionSum(): Int {
        return when (this) {
            is Packet.Literal -> version
            is Packet.Operator -> version + packets.sumOf { it.versionSum() }
        }
    }

    private fun Packet.evaluate(): Long {
        return when (this) {
            is Packet.Literal -> return value
            is Packet.Operator -> when (type) {
                0 -> packets.sumOf { it.evaluate() }
                1 -> packets.fold(1L) { acc, it -> acc * it.evaluate() }
                2 -> packets.minOf { it.evaluate() }
                3 -> packets.maxOf { it.evaluate() }
                5 -> packets.let { if (it[0].evaluate() > it[1].evaluate()) 1 else 0 }
                6 -> packets.let { if (it[0].evaluate() < it[1].evaluate()) 1 else 0 }
                7 -> packets.let { if (it[0].evaluate() == it[1].evaluate()) 1 else 0 }
                else -> throw Exception("Unexpected operator type: $this")
            }
        }
    }

    private fun Packet.Operator.symbol(): String {
        return when (type) {
            0 -> "add"
            1 -> "multiply"
            2 -> "min"
            3 -> "max"
            5 -> "gt"
            6 -> "lt"
            7 -> "eq"
            else -> throw Exception("Unexpected operator type: $this")
        }
    }

    private fun Packet.prettyPrint(indent: Int = 0): String {
        val indentation = "\t".repeat(indent)
        return indentation + when (this) {
            is Packet.Literal -> "$value"
            is Packet.Operator -> "${symbol()}:" + when {
                packets.all { it is Packet.Literal } ->
                    "[ ${packets.joinToString { it.prettyPrint(0) }} ]"
                packets.size == 1 ->
                    "[ ${packets.joinToString { it.prettyPrint(indent + 1) }.trim()} ]"
                else -> "[\n${packets.joinToString("\n") { it.prettyPrint(indent + 1) }}\n$indentation]"
            }
        }
    }

    private fun String.parsePacket(): Packet {
        val version = this.take(3).toInt(2)

        return when (val type = this.drop(3).take(3).toInt(2)) {
            4 -> this.drop(6).toLiteralPacket(version, type)
            else -> this.drop(6).toOperatorPacket(version, type)
        }
    }

    private fun String.toOperatorPacket(version: Int, type: Int): Packet.Operator {
        return when (this.take(1)) {
            "0" -> this.drop(1).toLengthBasedOperator(version, type)
            else -> this.drop(1).toCountBasedOperator(version, type)
        }
    }

    private fun String.toCountBasedOperator(version: Int, type: Int): Packet.Operator {
        val numPackets = this.take(11).toLong(2)
        val packets = mutableListOf<Packet>()
        var bits = this.drop(11)
        var count = 0
        while (count < numPackets) {
            val packet = bits.parsePacket()
            bits = bits.drop(packet.bits)
            packets.add(packet)
            count += 1
        }
        return Packet.Operator(version, type, packets, 7 + 11 + packets.sumOf { it.bits })
    }

    private fun String.toLengthBasedOperator(version: Int, type: Int): Packet.Operator {
        val length = this.take(15).toInt(2)
        var consumed = 0L
        var bits = this.drop(15).take(length)
        val packets = mutableListOf<Packet>()

        while (consumed < length) {
            val next = bits.parsePacket()
            bits = bits.drop(next.bits)
            consumed += next.bits
            packets.add(next)
        }
        return Packet.Operator(version, type, packets, 7 + 15 + length)
    }

    private fun String.toLiteralPacket(version: Int, type: Int): Packet.Literal {
        val value = this.chunked(5).fold(LiteralValue.Incomplete()) { acc: LiteralValue, bits ->
            when (acc) {
                is LiteralValue.Incomplete -> when {
                    bits.startsWith("1") -> LiteralValue.Incomplete(acc.value + bits.drop(1), acc.bits + 5)
                    else -> LiteralValue.Complete(acc.value + bits.drop(1), acc.bits + 5)
                }
                is LiteralValue.Complete -> acc
            }
        }
        return Packet.Literal(version, type, value.toLong(), 6 + value.bits)
    }

    private fun String.fromHexToBinDigits() =
        map { it.digitToInt(16) }
            .joinToString("") { it.toString(2).padStart(4, '0') }

    fun part1(input: String): Int = input.fromHexToBinDigits().parsePacket().versionSum()

    fun part2(input: String): Long = input.fromHexToBinDigits().parsePacket().evaluate()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 16")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")

        println()
        println("-- pretty print tree --")
        println(inputFileString.fromHexToBinDigits().parsePacket().prettyPrint())
    }
}
