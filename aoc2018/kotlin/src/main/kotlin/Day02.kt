import java.io.File

fun <A, B> Map<A, B>.anyValue(test: (B) -> Boolean) = any { (_, v) -> test(v) }

object Day02 {
    fun answer1(input: List<String>) =
        input.map {
            val freqs = it.frequencies()
            freqs.anyValue(2::equals) to freqs.anyValue(3::equals)
        }
            .unzip().toList()
            .map { bs -> bs.count { it } }
            .fold(1, Int::times)


    fun answer2(input: List<String>) =
        input.cartesian { s1, s2 ->
            s1.zip(s2)
        }.find {
            it.count(Pair<Char, Char>::different) == 1
        }?.filter {
            it.same()
        }?.joinToString("") { it.first.toString() }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day02.txt").readLines()
        println("answer 1: ${answer1(input)}")
        println("answer 2: ${answer2(input)}")
    }
}


