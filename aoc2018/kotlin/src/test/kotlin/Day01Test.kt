import Day01.answer1
import Day01.answer2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Day01Test {

    private val input = File("src/main/resources/day01.txt").readLines()

    @Test
    fun answer1_a() {
        val data = listOf("+1", "+1", "+1")
        assertEquals(3, answer1(data))
    }

    @Test
    fun answer1_b() {
        val data = listOf("+1", "+1", "-2")
        assertEquals(0, answer1(data))
    }

    @Test
    fun answer1_c() {
        val data = listOf("-1", "-2", "-3")
        assertEquals(-6, answer1(data))
    }

    @Test
    fun answer1_answer() {
        assertEquals(556, answer1(input))
    }

    @Test
    fun answer2_a() {
        val data = listOf("+1", "-1")
        assertEquals(0, answer2(data))
    }

    @Test
    fun answer2_b() {
        val data = listOf("+3", "+3", "+4", "-2", "-4")
        assertEquals(10, answer2(data))
    }

    @Test
    fun answer2_c() {
        val data = listOf("-6", "+3", "+8", "+5", "-6")
        assertEquals(5, answer2(data))
    }

    @Test
    fun answer2_d() {
        val data = listOf("+7", "+7", "-2", "-7", "-4")
        assertEquals(14, answer2(data))
    }

    @Test
    fun answer2_answer() {
        assertEquals(448, answer2(input))
    }
}