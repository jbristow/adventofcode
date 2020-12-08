import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class Day07Test {
    @ParameterizedTest(name = "processLine: {0}")
    @MethodSource("bagDataProvider")
    fun testLineParser(
        input: String,
        expectedOuterBag: String,
        expectedInnerBags: List<Pair<Int, String>>
    ) {
        assertThat(Day07.processLine(input)).`as`(input).satisfies { (k, v) ->
            assertThat(k).isEqualTo(expectedOuterBag)
            assertThat(v).containsExactlyInAnyOrderElementsOf(expectedInnerBags)
        }
    }

    @Nested
    inner class Part01 {
        @Test
        fun test() {
            assertThat(Day07.part1(testData)).isEqualTo(4)
        }
    }
    @Nested
    inner class Part02 {
        @Test
        fun test() {
            assertThat(Day07.part2(testData)).isEqualTo(32)
        }

        @Test
        fun testAlternate() {
            assertThat(Day07.part2(testDataAlternate)).isEqualTo(126)
        }

        val testDataAlternate =
            """
            shiny gold bags contain 2 dark red bags.
            dark red bags contain 2 dark orange bags.
            dark orange bags contain 2 dark yellow bags.
            dark yellow bags contain 2 dark green bags.
            dark green bags contain 2 dark blue bags.
            dark blue bags contain 2 dark violet bags.
            dark violet bags contain no other bags.
            """.trimIndent().lines()
    }

    @Nested
    inner class Answer {
        val data = Files.readAllLines(Paths.get(Day07.FILENAME))
        @Test
        fun part1() {
            assertThat(Day07.part1(data)).isEqualTo(238)
        }
        @Test
        fun part2() {
            assertThat(Day07.part2(data)).isEqualTo(82930)
        }
    }

    val testData =
        """
        light red bags contain 1 bright white bag, 2 muted yellow bags.
        dark orange bags contain 3 bright white bags, 4 muted yellow bags.
        bright white bags contain 1 shiny gold bag.
        muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
        shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
        dark olive bags contain 3 faded blue bags, 4 dotted black bags.
        vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
        faded blue bags contain no other bags.
        dotted black bags contain no other bags.
        """.trimIndent().lines()

    fun bagDataProvider() =
        Stream.of(
            arguments(
                "light red bags contain 1 bright white bag, 2 muted yellow bags.",
                "light red",
                listOf(1 to "bright white", 2 to "muted yellow")
            ),
            arguments(
                "dark orange bags contain 3 bright white bags, 4 muted yellow bags.",
                "dark orange",
                listOf(3 to "bright white", 4 to "muted yellow"),
            ),
            arguments(
                "bright white bags contain 1 shiny gold bag.",
                "bright white",
                listOf(1 to "shiny gold")
            ),
            arguments(
                "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.",
                "muted yellow",
                listOf(2 to "shiny gold", 9 to "faded blue")
            ),
            arguments(
                "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.",
                "shiny gold",
                listOf(1 to "dark olive", 2 to "vibrant plum")
            ),
            arguments(
                "dark olive bags contain 3 faded blue bags, 4 dotted black bags.",
                "dark olive",
                listOf(3 to "faded blue", 4 to "dotted black")
            ),
            arguments(
                "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.",
                "vibrant plum",
                listOf(5 to "faded blue", 6 to "dotted black")
            ),
            arguments(
                "faded blue bags contain no other bags.",
                "faded blue",
                emptyList<String>()
            ),
            arguments(
                "dotted black bags contain no other bags.",
                "dotted black",
                emptyList<String>()
            ),
        )
}
