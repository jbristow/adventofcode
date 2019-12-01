import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utility.Point

internal class Day11Test {

    @Test
    fun powerLevel1() {
        assertEquals(4, Day11.powerLevel(8, Point(3, 5)))
    }

    @Test
    fun powerLevel2() {
        assertEquals(-5, Day11.powerLevel(57, Point(122, 79)))
    }
}


