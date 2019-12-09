import arrow.core.right
import arrow.core.some
import java.nio.file.Files
import java.nio.file.Paths

object Day09 {

    private const val FILENAME = "src/main/resources/day09.txt"
    val fileData: String = Files.readAllLines(Paths.get(FILENAME)).first()

    private fun extraThousand(): String {
        return (0 until 10000).joinToString(",") { "0" }
    }

    fun part1(): String {
        return Day05.step(
            (fileData + extraThousand()).split(",").map { it.toLong() }.toTypedArray(),
            CurrentState(0.some(), inputs = mutableListOf(1)).right()
        )

    }

    fun test1(input: String): String {
        return Day05.step(
            (input + extraThousand()).split(",").map { it.toLong() }.toTypedArray(),
            CurrentState(0.some(), inputs = mutableListOf(1)).right()
        )

    }
}

fun main() {

//    println(Day09.part1())
    println(Day09.test1("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"))
}