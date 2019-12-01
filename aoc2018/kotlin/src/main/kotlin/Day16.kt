import ArgType.INSTANT
import ArgType.REGISTER
import java.nio.file.Files
import java.nio.file.Paths

fun String.parse4Ints(
    pre: String = "",
    suf: String = ""
) =
    Regex("""^$pre(\d+),? (\d+),? (\d+),? (\d+)$suf$""")
        .matchEntire(this)!!.groupValues.drop(1)
        .map { it.toInt() }

enum class ArgType {
    REGISTER {
        override fun findValue(
            n: Int,
            codeline: List<Int>,
            registers: List<Long>
        ): Long {
            return registers[codeline[n]]
        }
    },
    INSTANT {
        override fun findValue(
            n: Int,
            codeline: List<Int>,
            registers: List<Long>
        ): Long {
            return codeline[n].toLong()
        }

    };

    abstract fun findValue(
        n: Int,
        codeline: List<Int>,
        registers: List<Long>
    ): Long

}

typealias Operation = ((List<Int>, List<Long>) -> List<Long>)

object Day16 {

    private fun generateOperation(
        arg1: ArgType,
        arg2: ArgType,
        action: (Long, Long) -> Long
    ) =
        fun(codeline: List<Int>, registers: List<Long>) =
            registers.take(codeline[3]) +
                    action(
                        arg1.findValue(1, codeline, registers),
                        arg2.findValue(2, codeline, registers)
                    ) + registers.drop((codeline[3] + 1))

    private fun generateOperation(arg: ArgType, action: (Long) -> Long) =
        fun(codeline: List<Int>, registers: List<Long>) =
            registers.take(codeline[3]) +
                    action(arg.findValue(1, codeline, registers)) +
                    registers.drop((codeline[3] + 1))

    private fun rrOperation(action: (Long, Long) -> Long) =
        generateOperation(REGISTER, REGISTER, action)

    private fun riOperation(action: (Long, Long) -> Long) =
        generateOperation(REGISTER, INSTANT, action)

    private fun rnOperation(action: (Long) -> Long) =
        generateOperation(REGISTER, action)

    private fun irOperation(action: (Long, Long) -> Long) =
        generateOperation(INSTANT, REGISTER, action)

    private fun inOperation(action: (Long) -> Long) =
        generateOperation(INSTANT, action)

    private fun boolAction(action: (Long, Long) -> Boolean) =
        { a: Long, b: Long -> if (action(a, b)) 1L else 0 }

    val opcodes = mapOf(
        "addr" to rrOperation { a, b -> a + b },
        "addi" to riOperation { a, b -> a + b },

        "mulr" to rrOperation { a, b -> a * b },
        "muli" to riOperation { a, b -> a * b },

        "banr" to rrOperation { a, b -> a and b },
        "bani" to riOperation { a, b -> a and b },

        "borr" to rrOperation { a, b -> a or b },
        "bori" to riOperation { a, b -> a or b },

        "setr" to rnOperation { a -> a },
        "seti" to inOperation { a -> a },

        "gtir" to irOperation(boolAction { a, b -> a > b }),
        "gtri" to riOperation(boolAction { a, b -> a > b }),
        "gtrr" to rrOperation(boolAction { a, b -> a > b }),

        "eqir" to irOperation(boolAction { a, b -> a == b }),
        "eqri" to riOperation(boolAction { a, b -> a == b }),
        "eqrr" to rrOperation(boolAction { a, b -> a == b })
    )


    fun answer1(data: List<List<String>>) =
        analyzeData(data)
            .count { (_, b) -> b.count() >= 3L }

    fun analyzeData(data: List<List<String>>) =
        data.map { list ->
            val registers =
                list[0].parse4Ints("""Before:\s+\[""", """]""")
            val codeline = list[1].parse4Ints()
            val expected =
                list[2].parse4Ints("""After:\s+\[""", """]""")

            val candidates = opcodes
                .map { (label, fn) ->
                    fn(
                        codeline,
                        registers.toMutableList().map { it.toLong() }) to label
                }
                .filter { it.first == expected }
                .map { it.second }
            codeline.first() to candidates
        }.filter { (_, b) -> b.isNotEmpty() }

    @JvmStatic
    fun main(args: Array<String>) {
        val text =
            Files.readAllLines(Paths.get("src/main/resources/day16.txt"))
                .joinToString("\n")

        val (input1, input2) = text.split("\n\n\n\n")

        val data = input1.split("\n\n").map { it.lines() }

        println("answer1: ${answer1(data)}")

        println("answer2: ${answer2(data, input2.lineSequence())}")
    }

    private fun answer2(
        data: List<List<String>>,
        program: Sequence<String>
    ): Long {

        val opcodeMap1 = findOpcodes(
            emptyMap<Int, String>().toMutableMap(),
            analyzeData(data)
        )

        println(opcodeMap1.toSortedMap().toList().joinToString("\n"))
        val opcodeMap = opcodeMap1.mapValues { (_, v) ->
            opcodes[v]!!
        }

        return runProgram(program, opcodeMap).first()
    }

    private fun runProgram(
        instrs: Sequence<String>,
        opcodeMap: Map<Int, (List<Int>, List<Long>) -> List<Long>>
    ) =
        instrs.map { it -> it.parse4Ints() }
            .fold(listOf<Long>(0, 0, 0, 0)) { registers, codeline ->
                (opcodeMap[codeline[0]]!!)(codeline, registers)
            }

    private tailrec fun findOpcodes(
        foundOpcodes: Map<Int, String>,
        samples: List<Pair<Int, List<String>>>
    ): Map<Int, String> {

        if (samples.isEmpty()) return foundOpcodes

        // I'd rather do this than break immutability.
        val (remaining, nextFoundOpcodes) = samples
            .filter { it.second.count() == 1 }
            .map { it.first to it.second.first() }.distinct()
            .fold(samples to foundOpcodes) { (ls, m), (num, name) ->

                ls.filterNot { num == it.first }
                    .map { (a, b) ->
                        a to b.filterNot { name == it }
                    }.filter { (_, b) ->
                        b.isNotEmpty()
                    } to (m + (num to name))
            }

        if (remaining == samples) {
            throw Exception("Could not infer all opcodes present in sample. $remaining")
        }
        return findOpcodes(nextFoundOpcodes, remaining.toList())

    }
}

