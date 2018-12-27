import java.lang.Math.sqrt
import java.nio.file.Files
import java.nio.file.Paths

object Day19 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            Files.readAllLines(Paths.get("src/main/resources/day19.txt"))
        println("answer1: ${Day19.answer1(input)}")
        println("answer2: ${Day19.answer2(10551364)}")
    }

    private fun answer1(input: List<String>): MutableList<Long> {
        val ip = input.first().parseIpLine()
        return runProgram(
            ip,
            mutableListOf(0, 0, 0, 0, 0, 0, 0),
            input.drop(1).mapIndexed { i: Int, it: String ->
                val items = it.split(" ")
                i to (Day16.opcodes[items[0]]!! to items.drop(1).map { it.toInt() })
            }.toMap(),
            input.drop(1)
        )
    }

    private fun answer2(input: Int) =
        (1..sqrt(input.toDouble()).toInt()).filter { input % it == 0 }
            .flatMap { listOf(it, input / it) }.sum()

    private tailrec fun runProgram(
        ip: Int,
        registers: MutableList<Long>,
        instrs: Map<Int, Pair<Operation, List<Int>>>,
        input: List<String>
    ): MutableList<Long> {
        val line = registers[ip]

        if (line < 0 || line >= instrs.count()) {
            return registers
        }

        val (op, values) = instrs[line.toInt()]!!.let {
            it.first to it.second.toMutableList()
        }
        values.add(0, 0)
        val nextRegisters = op(values, registers).toMutableList()

        nextRegisters[ip] = nextRegisters[ip] + 1

        return runProgram(ip, nextRegisters, instrs, input)

    }
}

fun String.parseIpLine() =
    Regex("""#ip (\d+)""").matchEntire(this)!!.groupValues[1].toInt()
