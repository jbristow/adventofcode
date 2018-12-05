import java.io.File
import java.util.*

fun String.react() =
    fold(LinkedList<Char>()) { acc, c ->
        when {
            acc.isEmpty() -> acc.push(c)
            acc.peek() == c -> acc.push(c)
            !acc.peek().equals(c, ignoreCase = true) -> acc.push(c)
            else -> acc.pop()
        }
        acc
    }


object Day05 {
fun answer1(input: String) = input.react().count()

fun answer2(input: String) =
    ('a'..'z').zip('A'..'Z')
        .map { (lc, uc) ->
            input.filterNot { it == lc || it == uc }
                .react()
                .count()
        }.min()


    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            File("src/main/resources/day05.txt").readLines().first()
        println("answer 1: ${this.answer1(input)}")
        println("answer 2: ${this.answer2(input)}")
    }
}


