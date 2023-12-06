package util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RangesKtTest {
    @Test
    fun `no overlap - all a lt all b`() {
        val a = 1..5
        val b = 10..15
        assertThat(a.splitOnIntersect(b)).containsExactly(a, b)
    }

    @Test
    fun `no overlap - all b lt all a`() {
        val a = 1..5
        val b = 10..15
        assertThat(b.splitOnIntersect(a)).containsExactly(a, b)
    }

    @Test
    fun `a completely inside b`() {
        val a = 101..105
        val b = 50..150

        assertThat(a.splitOnIntersect(b))
            .containsExactly(
                50..100,
                a,
                106..150
            )
            .isEqualTo(b.splitOnIntersect(a))
    }

    @Test
    fun `a completely inside b, overlap start`() {
        val a = 50..105
        val b = 50..150

        val actual = a.splitOnIntersect(b)
        assertThat(actual)
            .containsExactly(
                a,
                106..150
            )
            .isEqualTo(b.splitOnIntersect(a))
    }

    @Test
    fun `a completely inside b, overlap end`() {
        val a = 100..150
        val b = 50..150

        val actual = a.splitOnIntersect(b)
        assertThat(actual)
            .containsExactly(
                50..99,
                a
            )
            .isEqualTo(b.splitOnIntersect(a))
    }

    @Test
    fun `a overlaps b - a_start lt b_start`() {

        val a = 27..35
        val b = 30..77
        val actual = a.splitOnIntersect(b)
        assertThat(actual)
            .containsExactly(
                27..29,
                30..35,
                36..77
            )
            .isEqualTo(b.splitOnIntersect(a))
    }

    @Test
    fun `a overlaps b - a_end gt b_end keep a`() {

        val a = 35..81
        val b = 30..77
        val actual = a.splitOnIntersect(b)
        assertThat(actual)
            .containsExactly(
                30..34, 35..77, 78..81
            )
            .isEqualTo(b.splitOnIntersect(a))
    }

    @Test
    fun `a completely inside b (LONG)`() {
        val a = 10101010101..20202020202
        val b = 10L..30303030303

        assertThat(a.splitOnIntersect(b))
            .containsExactly(
                10..10101010100,
                10101010101..20202020202,
                20202020203..30303030303,
            )
            .isEqualTo(b.splitOnIntersect(a))
    }

    @Test
    fun `merge noIntersection`() {
        val a = 101..105
        val b = 110..115
        assertThat(a.merge(b)).containsExactly(a, b)
    }

    @Test
    fun `merge A then B`() {
        val a = 12..50
        val b = 25..100
        assertThat(a.merge(b)).containsExactly(12..100)
    }

    @Test
    fun `merge B then A`() {
        val a = 250..1000
        val b = 120..500
        assertThat(a.merge(b)).containsExactly(120..1000)
    }

    @Test
    fun `merge equality`() {
        val a = 250..1000
        val b = 250..1000
        assertThat(a.merge(b)).containsExactly(250..1000)
    }

    @Test
    fun `merge a inside b`() {
        val a = 300..400
        val b = 200..450
        assertThat(a.merge(b)).containsExactly(200..450)
    }


    @Test
    fun `merge b inside a`() {
        val a = 1..5
        val b = 3..4
        assertThat(a.merge(b)).containsExactly(1..5)
    }
}