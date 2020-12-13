import util.AdventOfCode

object Day05 : AdventOfCode() {

    fun String.toSeatLocation(): Pair<Int, Int> {
        val ranges = fold((0..127) to (0..7)) { (rowrange, colrange), instr ->
            when (instr) {
                'F' -> (rowrange.first..((rowrange.first + rowrange.last) / 2)) to colrange
                'B' -> (((rowrange.first + rowrange.last + 1) / 2)..rowrange.last) to colrange
                'L' -> rowrange to (colrange.first..((colrange.first + colrange.last) / 2))
                'R' -> rowrange to (((colrange.first + colrange.last + 1) / 2)..colrange.last)
                else -> throw Exception("Fart: $this, $rowrange -- $colrange -- $instr")
            }
        }
        return ranges.first.first to ranges.second.last
    }

    fun Pair<Int, Int>.toSeatID(): Int = first * 8 + second

    fun part1(data: List<String>): Int? =
        data.map { it.toSeatLocation().toSeatID() }
            .maxByOrNull { it }

    fun part2(data: List<String>): Int =
        data.asSequence()
            .map { it.toSeatLocation().toSeatID() }
            .sorted()
            .windowed(2, 1)
            .first { (a, b) -> a + 1 != b }
            .first() + 1

    fun part1Alt(data: List<String>) =
        data.map {
            it.replace("""[FL]""".toRegex(), "0")
                .replace("""[BR]""".toRegex(), "1")
                .toInt(2)
        }.maxOrNull()
}

fun main() {
    println("Part 1: ${Day05.part1Alt(Day05.inputFileLines)}")
    println("Part 2: ${Day05.part2(Day05.inputFileLines)}")
}
