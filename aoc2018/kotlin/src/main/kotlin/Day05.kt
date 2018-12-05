import java.io.File

object Day05 {
    fun String.react() =
        fold("") { acc, c ->
            if (c.toLowerCase() == acc.lastOrNull()?.toLowerCase() && c != acc.lastOrNull()) {
                acc.init
            } else {
                acc + c
            }
        }

    fun answer1(input: String) = input.react().count()

    fun answer2(input: String): Int? {
        val xs = ('a'..'z').zip('A'..'Z').map { (lc, uc) ->
            input.filterNot { it == lc || it == uc }.react()
        }
        println(xs.joinToString("\n"))
        return xs.minBy { it.count() }?.count()
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day05.txt").readLines()
        println("answer 1: ${this.answer1(input.first())}")
        println("answer 2: ${this.answer2(input.first())}")
    }
}

