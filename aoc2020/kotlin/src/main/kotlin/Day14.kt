import util.AdventOfCode

object Day14 : AdventOfCode() {

    data class Mask(val mask: String)

    data class Memory(val map: Map<Int, Int>)

    fun changeChar(p: Pair<Char, Char>): Char {
        val (a, b) = p
        return when (a) {
            'X' -> b
            else -> a
        }
    }

    fun Int.paddedBinString(len: Int) =
        String.format("%${len}s", Integer.toBinaryString(this)).replace(' ', '0')

    fun Int.applyMask(mask: String) =
        mask.zip(this.paddedBinString(mask.length)).map(::changeChar).joinToString("").toLong(2)

    fun Int.applyMask2(mask: String): String {
        println(mask)
        println(this.paddedBinString(mask.length))
        return mask.zip(this.paddedBinString(mask.length)).map { (a, b) ->
            when (a) {
                'X' -> 'X'
                '1' -> '1'
                else -> b
            }
        }.joinToString("")
    }

    tailrec fun updateMemory(instrs: List<String>, mem: Map<CharSequence, Long>, mask: String? = null): Long {
        if (instrs.isEmpty()) {
            println(mem)
            return mem.values.sum()
        }
        val currLine = instrs.first()
        val parts = currLine.split(" = ")
        return when (parts[0]) {
            "mask" -> updateMemory(instrs.drop(1), mem, parts[1])
            else -> {
                val address = parts[0].subSequence(4, parts[0].length - 1)
                updateMemory(instrs.drop(1), mem + (address to parts[1].toInt().applyMask(mask!!)), mask)
            }
        }
    }

    tailrec fun updateMemory2(
        instrs: List<String>,
        mem: Map<Long, Long>,
        mask: String? = null,
    ): Map<Long, Long> {
        if (instrs.isEmpty()) {
            return mem
        }
        val currLine = instrs.first()
        val parts = currLine.split(" = ")
        return when (parts[0]) {
            "mask" -> updateMemory2(instrs.drop(1), mem, parts[1])
            else -> {
                val address = parts[0].subSequence(4, parts[0].length - 1).toString().toInt().applyMask2(mask!!)
                val writtenTo = address.process().map { it to parts[1].toLong() }
                updateMemory2(instrs.drop(1), mem + writtenTo, mask)
            }
        }
    }

    private tailrec fun procRecurse(chars: String, output: List<String> = emptyList()): List<Long> =
        when {
            chars.isEmpty() ->
                output.map { it.toLong(2) }
            chars.first() == 'X' && output.isEmpty() ->
                procRecurse(chars.drop(1), output + listOf("0", "1"))
            chars.first() == 'X' ->
                procRecurse(chars.drop(1), output.map { it + "0" } + output.map { it + "1" })
            output.isEmpty() ->
                procRecurse(chars.drop(1), listOf(chars.first().toString()))
            else ->
                procRecurse(chars.drop(1), output.map { it + chars.first().toString() })
        }

    private fun String.process() = procRecurse(this)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Part 1: ${part1(inputFileLines)}")
        println("Part 2: ${part2(inputFileLines)}")
    }

    private fun part1(data: List<String>) = updateMemory(data, mutableMapOf(), null)
    private fun part2(data: List<String>) = updateMemory2(data, mutableMapOf(), null).values.sum()
}
