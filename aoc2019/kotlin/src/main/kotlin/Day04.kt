const val MIN_BOUND = 245182
const val MAX_BOUND = 790572
val passwordRange = (MIN_BOUND..MAX_BOUND)


object Day04 {

    private fun has5DistinctNumbers(it: Int) = it.toString().toSet().size < 6

    private fun hasAllIncreasingDigits(it: Int) =
        it.toString()
            .map { it.toInt() }
            .windowed(2, 1)
            .all { (a, b) -> b >= a }

    private fun hasAtLeastOneDigitAppearingExactlyTwice(it: Int) =
        it.toString()
            .groupBy { it }
            .mapValues { (k, v) -> v.size }
            .any { (k, v) -> v == 2 }

    fun part1() =
        passwordRange.asSequence()
            .filter { hasAllIncreasingDigits(it) && has5DistinctNumbers(it) }
            .count()

    fun part2() =
        passwordRange.asSequence()
            .filter { hasAllIncreasingDigits(it) && hasAtLeastOneDigitAppearingExactlyTwice(it) }
            .count()

}

fun main() {
    println(Day04.part1())
    println(Day04.part2())
}