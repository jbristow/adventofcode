package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class Day22Test {

    val sample1 = """deal with increment 7
deal into new stack
deal into new stack""".lines()

    val sample2 = """cut 6
deal with increment 7
deal into new stack""".lines()

    @Test
    fun part1sample1() {
        val actual = Day22.part1(sample1, (0 until 10).toList())
        assertThat(actual).containsExactly(0, 3, 6, 9, 2, 5, 8, 1, 4, 7)
    }

    @Test
    fun part1sample2() {
        val actual = Day22.part1(sample2, (0 until 10).toList())
        assertThat(actual).containsExactly(3, 0, 7, 4, 1, 8, 5, 2, 9, 6)
    }

    @Test
    fun testDeal() {
        val result = ShuffleMethods.DealIntoStack((0 until 10).toList())
        assertThat(result).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    }

    @Test
    fun testCut() {
        val result = ShuffleMethods.Cut(3)((0 until 10).toList())
        assertThat(result).containsExactly(3, 4, 5, 6, 7, 8, 9, 0, 1, 2)
    }

    @Test
    fun testCutNegative() {
        val result = ShuffleMethods.Cut(-4)((0 until 10).toList())
        assertThat(result).containsExactly(6, 7, 8, 9, 0, 1, 2, 3, 4, 5)
    }

    @Test
    fun testDealInto3() {
        val result = ShuffleMethods.DealIncrement(3)((0 until 10).toList())
        assertThat(result).containsExactly(0, 7, 4, 1, 8, 5, 2, 9, 6, 3)
    }

    @Test
    fun testDealInto7() {
        val result = ShuffleMethods.DealIncrement(7)((0 until 10).toList())
        println(result)
        assertThat(result).containsExactly(0, 3, 6, 9, 2, 5, 8, 1, 4, 7)
    }

    @Test
    fun testReverseEquality() {
        val actual = ShuffleMethods.DealIntoStack((0 until 11).toList())
        val result = ShuffleMethods.DealIncrement(10)((0 until 11).toList())
        println(result)
        val result2 = ShuffleMethods.Cut(1)(result)
        println(result2)
        assertThat(result2).containsExactlyElementsOf(actual)
    }

    @Test
    @Disabled
    fun testCutEquality() {
        val actual = ShuffleMethods.DealIncrement(2)((ShuffleMethods.Cut(4)((0 until 11).toList())))
        println("actual:$actual")
        repeat(99) {
            val result = ShuffleMethods.DealIncrement(it + 1)((0 until 11).toList())
            println("$it:$result")
            println(actual == result)
        }
        fail("Not implemented.")
    }
}
