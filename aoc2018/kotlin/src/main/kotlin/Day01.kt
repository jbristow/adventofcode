import java.io.File


fun answer1(input: List<String>) = input.sumBy(String::parseN)

private fun String.parseN() = let { (head, tail) ->
    when (head) {
        '+' -> 1
        '-' -> -1
        else -> throw Exception("Illegal input!")
    } * tail.toInt()
}


fun answer2(input: List<String>) =
        generateSequence(input.scan(0) { acc, other -> acc + other.parseN() }.tail) {
            it.map(556::plus)
        }.findFirstDupe(emptySet())


fun Sequence<List<Int>>.findFirstDupe(seen: Set<Int>): Int = let { (head, tail) ->
    val inter = head.intersect(seen)
    when {
        inter.isNotEmpty() -> head.find { it in inter }!!
        else -> tail.findFirstDupe(seen + head)
    }
}

fun main(args: Array<String>) {
    val input = File("src/main/resources/day01.txt").readLines()
    println("answer 1: ${answer1(input)}")
    println("answer 2: ${answer2(input)}")
}