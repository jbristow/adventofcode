import util.AdventOfCode

object Day10 : AdventOfCode() {
    private tailrec fun Map<Int, List<Int>>.countPossible(
        toConsider: List<Int>,
        totals: Map<Int, Long> = mapOf(0 to 1L)
    ): Long {
        val current = toConsider.first()
        val childCounts = sumOfChildCounts(current, totals)
        return when {
            toConsider.size > 1 -> countPossible(
                toConsider.drop(1),
                totals.plus(current to childCounts)
            )
            else -> childCounts
        }
    }

    private fun Map<Int, List<Int>>.sumOfChildCounts(
        current: Int,
        totals: Map<Int, Long>
    ) = this[current]?.fold(0L) { a, it -> a + (totals[it] ?: 0L) } ?: 0

    fun part2(data: List<Int>): Long {
        val fullData = data + 0
        val temp = fullData.map { curr ->
            curr to fullData.filter { it != curr && it in (curr - 3) until curr }
        }.sortedBy { (a, _) -> a }.toMap().toMutableMap()

        return temp.countPossible(fullData.sorted().drop(1))
    }

    fun part1(d: List<Int>) =
        d.sorted()
            .let {
                listOf(0) + it + listOf(it.last() + 3)
            }.windowed(2, 1)
            .fold(0 to 0) { (ones, threes), (a, b) ->
                when (b - a) {
                    1 -> (ones + 1) to threes
                    3 -> ones to (threes + 1)
                    else -> ones to threes
                }
            }.let { (o, t) ->
                o * t
            }
}

fun main() {
    println("Part 1: ${Day10.part1(Day10.inputFileInts)}")
    println("Part 2: ${Day10.part2(Day10.inputFileInts)}")
}
