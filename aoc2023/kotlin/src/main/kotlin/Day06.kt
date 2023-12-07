import util.AdventOfCode
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Day06 : AdventOfCode() {
    private fun String.toLongList() =
        split(""":\s+""".toRegex())[1].split("""\s+""".toRegex()).map { it.toLong() }

    private fun String.toLongIgnoreSpace() =
        split(""":\s+""".toRegex())[1].replace("""\s+""".toRegex(), "").toLong()


    private fun part1(lines: List<String>): Long {
        val times = lines[0].toLongList()
        val records = lines[1].toLongList()

        return times.zip(records)
            .map { (time, record) -> (0..time).map { (time - it) * it }.count { it > record } }
            .fold(1) { prod, curr -> prod * curr }
    }

    private fun part2(lines: List<String>): Long {
        val time = lines[0].toLongIgnoreSpace()
        val record = lines[1].toLongIgnoreSpace()

        val first = (1 until time).dropWhile { ((time - it) * it) <= record }.first()
        val last = (1 until time).reversed().dropWhile { ((time - it) * it) <= record }.first()

        return last - first + 1
    }

    private fun part1Alt(lines: List<String>): Long {
        val times = lines[0].toLongList()
        val records = lines[1].toLongList()

        return times.zip(records)
            .map { (t, r) -> numberOfWaysToWin(t, r) }
            .fold(1) { prod, curr -> prod * curr }
    }

    private fun part2Alt(lines: List<String>): Long {
        val time = lines[0].toLongIgnoreSpace()
        val record = lines[1].toLongIgnoreSpace()
        return numberOfWaysToWin(time, record)
    }

    private fun numberOfWaysToWin(time: Long, record: Long): Long {

        val a = 0.5 * (time - sqrt(-4.0 * record + time * time))
        val b = 0.5 * (time + sqrt(-4.0 * record + time * time))
        return ((ceil(b) - 1).toLong() - (floor(a)).toLong())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tCONSTANT TIME: (second pass)")
        println("\t\tPart 1(Alt): ${part1Alt(inputFileLines)}")
        println("\t\tPart 2(Alt): ${part2Alt(inputFileLines)}")
        println("\tBRUTE FORCE")
        println("\t\tPart 1: ${part1(inputFileLines)}")
        println("\t\tPart 2: ${part2(inputFileLines)}")
    }
}