import util.AdventOfCode

object Day24 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 24")

        val pairs = listOf(
            // input[0] + 13 = input[13] + 9
            findPairsForRule(0, 13, 13, 9),
            // input[1] + 16 = input[12] + 10
            findPairsForRule(1, 16, 12, 10),
            // input[2] + 2 = input[11] + 8
            findPairsForRule(2, 2, 11, 8),
            // input[3] + 8 = input[8] + 9
            findPairsForRule(3, 8, 8, 9),
            // input[4] + 11 = input[5] + 11
            findPairsForRule(4, 11, 5, 11),
            // input[6] + 12 = input[7] + 16
            findPairsForRule(6, 12, 7, 16),
            // input[9] + 15 = input[10] + 8
            findPairsForRule(9, 15, 10, 8)
        )

        val biggest = pairs.flatMap { it.last() }.sortedBy { it.first }.map { it.second }
        val smallest = pairs.flatMap { it.first() }.sortedBy { it.first }.map { it.second }
        println("\tPart 1 - ${biggest.joinToString("")}")
        println("\tPart 2 - ${smallest.joinToString("")}")

        println()
        println("Checking biggest good: ${biggest.validSerial}")
        println("Checking smallest good: ${smallest.validSerial}")
    }

    private val List<Int>.validSerial: Boolean
        get() = zip(monads).fold(listOf<Int>()) { zl, (inp, f) -> f(zl, inp) }.isEmpty()

    private val monads = listOf(
        ::monad0,
        ::monad1,
        ::monad2,
        ::monad3,
        ::monad4,
        ::monad5,
        ::monad6,
        ::monad7,
        ::monad8,
        ::monad9,
        ::monad10,
        ::monad11,
        ::monad12,
        ::monad13
    )

    private fun findPairsForRule(a: Int, offsetA: Int, b: Int, offsetB: Int) =
        (1..9).flatMap { ai -> (1..9).map { bi -> ai to bi } }
            .filter { (ao, bo) -> ao + offsetA == bo + offsetB }
            .map { (ao, bo) -> listOf(a to ao, b to bo) }

    // reverse engineered from my code input

    @Suppress("UNUSED_PARAMETER")
    private fun monad0(zInput: List<Int>, input: Int): List<Int> {
        return listOf(input + 13)
    } // 0

    private fun monad1(zInput: List<Int>, input: Int): List<Int> {
        return zInput + (input + 16)
    } // 1

    private fun monad2(zInput: List<Int>, input: Int): List<Int> {
        return zInput + (input + 2)
    } // 2

    private fun monad3(zInput: List<Int>, input: Int): List<Int> {
        return zInput + (input + 8)
    } // 3

    private fun monad4(zInput: List<Int>, input: Int): List<Int> {
        return zInput + (input + 11)
    } // 4

    private fun monad5(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() == input + 11) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 11}")
        }
    } // 5-looks at 4, top of stack is 3

    private fun monad6(zInput: List<Int>, input: Int): List<Int> {
        return zInput + (input + 12)
    } // 6

    private fun monad7(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() == input + 16) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 16}, was $input should have been ${zInput.last() - 16}")
        }
    } // 7-looks at 6, top of stack is 3

    private fun monad8(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() == input + 9) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 9}, should have been ${zInput.last() - 9} ")
        }
    } // 8-looks at 3, top of stack is 2

    private fun monad9(zInput: List<Int>, input: Int): List<Int> {
        return zInput + (input + 15)
    } // 9

    private fun monad10(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() - 8 == input) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 8}, should have been ${zInput.last() - 8}")
        }
    } // 10-looks at 9, top of stack is 2

    private fun monad11(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() == input + 8) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 8}, should have been ${zInput.last() - 8}")
        }
    } // 11 - looking at 2, top of stack is 1

    private fun monad12(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() == input + 10) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 10}, should have been ${zInput.last() - 10}")
        }
    } // 12 - looking at 1, top of stack is 0

    private fun monad13(zInput: List<Int>, input: Int): List<Int> {
        return if (zInput.last() == input + 9) {
            zInput.dropLast(1)
        } else {
            throw Exception("${zInput.last()} != ${input + 9}, should have been ${zInput.last() - 9}")
        }
    } // 13 - looking at 0, stack is empty
}
