import utility.component1
import utility.component2
import utility.last
import utility.scan
import java.io.File

private fun String.parseN() = let { (head, tail) ->
    when (head) {
        '+' -> 1
        '-' -> -1
        else -> throw Exception("Illegal input!")
    } * tail.toInt()
}

object Day01 {
    fun answer1(input: List<String>) = input.sumBy(String::parseN)

    fun answer2(input: List<String>): Int {
        val ns = input.map(String::parseN).scan(Int::plus)
        val ending = ns.last
        return (sequenceOf(listOf(0)) + generateSequence(ns) {
            it.map(ending::plus)
        }).findFirstDupe(emptySet())
    }


    tailrec fun Sequence<List<Int>>.findFirstDupe(seen: Set<Int>): Int {
        val (head, tail) = this
        val intersections = head intersect seen
        return when {
            intersections.isNotEmpty() -> head.find { it in intersections }!!
            else -> tail.findFirstDupe(seen + head)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day01.txt").readLines()
        println("answer 1: ${this.answer1(input)}")
        println("answer 2: ${this.answer2(input)}")
    }
}

