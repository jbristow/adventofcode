import java.nio.file.Files
import java.nio.file.Paths

typealias Reg = MutableList<Long>

object Day21 {
    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            Files.readAllLines(Paths.get("src/main/resources/day21.txt"))

        i6(mutableListOf(0, 0, 0, 0, 0, 0), emptySet())
//        println("answer1: ${Day21.answer1(input)}")
    }

    private fun answer1(input: MutableList<String>): String {
        val ip = input.first().parseIpLine()


        val curr = programStep(
            0,
            ip, mutableListOf(0, 0, 0, 0, 0, 0),
            input.drop(1).mapIndexed { i: Int, it: String ->
                val items = it.split(" ")
                i to (Day16.opcodes[items[0]]!! to items.drop(1).map { it.toInt() })
            }.toMap(),
            mutableListOf(),
            mutableListOf()
        )
        return curr.toString()
    }

    private tailrec fun programStep(
        n: Int = 0,
        ip: Int,
        registers: MutableList<Long>,
        instrs: Map<Int, Pair<(List<Int>, List<Long>) -> List<Long>, List<Int>>>,
        seenR1s: MutableList<Long>,
        seenR4s: MutableList<Long>
    ): MutableList<Long> {

        val line = registers[ip].toInt()

        if (n > 1000000000 || line < 0 || line >= instrs.count()) {
            println(
                seenR1s.zip(seenR4s)
                    .groupBy { it.first }
                    .mapValues { (_, v) -> v.map { it.second } }
                    .toList()
                    .sortedBy { -1 * it.second.count() })
            return registers
        }

        val (op, values) = instrs[line]!!.let {
            it.first to it.second.toMutableList()
        }
        values.add(0, 0)
        val nextRegisters = op(values, registers).toMutableList()
//        if (line == 6 || line == 27)
//        println("line:$line ${registers} => $nextRegisters")
//        if (line == 19 || line == 20 || line == 21)
//            println("line:$line changes 3 ${registers[3]} => ${nextRegisters[3]}")
//        if (line < 15) {
//            println("line:$line escapeJumpX ${registers} => $nextRegisters\"")
//
//        }
        if (line == 29) {
//            println("line:$line ($n) escapeCheck ${registers} => $nextRegisters\"")
            if (nextRegisters[1] in seenR1s && seenR1s.count() > 200) {
                println("loop detected $seenR1s $seenR4s")
            }
//            if (nextRegisters[1] !in seenR1s) {
            seenR1s.add(nextRegisters[1])
            seenR4s.add(n.toLong())
//            }

        } else {
//        if (nextRegisters[1] != registers[1]) {
//            println(
//                "line:$line) r1 changed from ${"%x".format(registers[1])} to ${"%x".format(
//                    nextRegisters[1]
//                )} + $registers => $nextRegisters"
//            )
//        }

        }
        nextRegisters[ip] = nextRegisters[ip] + 1

        return programStep(n + 1, ip, nextRegisters, instrs, seenR1s, seenR4s)
    }


    private tailrec fun i6(rs: Reg, seen: Set<Long>): Reg {
        rs[2] = rs[1] or 65536
        rs[4] = rs[2] and 255
        rs[1] = (((6663054 + rs[4]) and 16777215L) * 65899) and 16777215
        while (256 <= rs[2]) {
            rs[4] = 0
            rs[3] = (rs[4] + 1) * 256
            while (rs[3] <= rs[2]) {
                rs[4] = rs[4] + 1
                rs[3] = (rs[4] + 1) * 256
            }
            rs[3] = 1
            rs[2] = rs[4]
            rs[4] = rs[2] and 255
            rs[1] = rs[1] + rs[4]
            rs[1] = rs[1] and 16777215
            rs[1] = rs[1] * 65899
            rs[1] = rs[1] and 16777215
        }
        rs[4] = 1
        return if (rs[1] != rs[0]) {
            println(rs[1])
            rs[4] = 1
            if (rs[1] in seen) {
                println(seen.last())
                return rs
            }
            i6(rs, seen + rs[1])
        } else {
            rs
        }
    }

}

