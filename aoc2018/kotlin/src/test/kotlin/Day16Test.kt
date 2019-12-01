import Day16.opcodes
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day16Test {

    @Test
    fun analysisTest() {
        val actual = Day16.analyzeData(
            listOf(
                """Before: [3, 2, 1, 1]
9 2 1 2
After:  [3, 2, 2, 1]
""".lines()
            )
        )

        assertEquals(listOf(9 to listOf("addi", "mulr", "seti")), actual)
    }

    @Test
    fun addrTest() {
        assertEquals(
            mutableListOf(1L, 1, 1, 2),
            opcodes["addr"]?.invoke(
                listOf(0, 1, 2, 3),
                mutableListOf(1L, 1, 1, 1)
            )
        )
    }

    @Test
    fun addiTest() {
        assertEquals(
            mutableListOf(1L, 1, 1, 3),
            opcodes["addi"]?.invoke(
                listOf(0, 1, 2, 3),
                mutableListOf(1L, 1, 1, 1)
            )
        )
    }

    @Test
    fun mulrTest() {
        assertEquals(
            mutableListOf(5L, 5, 5, 25),
            opcodes["mulr"]?.invoke(
                listOf(0, 1, 2, 3),
                mutableListOf(5L, 5, 5, 5)
            )
        )
    }

    @Test
    fun muliTest() {
        assertEquals(
            mutableListOf(5L, 5, 5, 10),
            opcodes["muli"]?.invoke(
                listOf(0, 1, 2, 3),
                mutableListOf(5L, 5, 5, 5)
            )
        )
    }

    @Test
    fun banrTest() {
        assertEquals(
            mutableListOf(51L, 65, 71, 81),
            opcodes["banr"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(51, 61, 71, 81)
            )
        )
    }

    @Test
    fun baniTest() {
        assertEquals(
            mutableListOf(51L, 2, 71, 82),
            opcodes["bani"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(51L, 61, 71, 82)
            )
        )
    }

    @Test
    fun borrTest() {
        assertEquals(
            mutableListOf(51L, 87, 71, 81),
            opcodes["borr"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(51, 61, 71, 81)
            )
        )
    }

    @Test
    fun boriTest() {
        assertEquals(
            mutableListOf(51L, 82, 71, 82),
            opcodes["bori"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(51, 61, 71, 82)
            )
        )
    }

    @Test
    fun setrTest() {
        assertEquals(
            mutableListOf(51L, 81, 71, 81),
            opcodes["setr"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(51L, 61, 71, 81)
            )
        )
    }

    @Test
    fun setiTest() {
        assertEquals(
            mutableListOf(51L, 3, 71, 82),
            opcodes["seti"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(51L, 61, 71, 82)
            )
        )
    }

    @Test
    fun gtirTest() {
        assertEquals(
            mutableListOf(1L, 1, 0, 1),
            opcodes["gtir"]?.invoke(
                listOf(0, 1, 3, 2),
                mutableListOf(1L, 1, 1, 1)
            )
        )

        assertEquals(
            mutableListOf(0L, 0, 1, 0),
            opcodes["gtir"]?.invoke(
                listOf(0, 1, 3, 2),
                mutableListOf(0L, 0, 0, 0)
            )
        )
    }

    @Test
    fun gtrrTest() {
        assertEquals(
            mutableListOf(10L, 1, 30, 40),
            opcodes["gtrr"]?.invoke(
                listOf(0, 3, 2, 1),
                mutableListOf(10L, 20, 30, 40)
            )
        )

        assertEquals(
            mutableListOf(10L, 20, 30, 0),
            opcodes["gtrr"]?.invoke(
                listOf(0, 1, 2, 3),
                mutableListOf(10L, 20, 30, 40)
            )
        )
    }
}