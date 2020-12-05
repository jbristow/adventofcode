import java.nio.file.Files
import java.nio.file.Paths

object Day02 {
    const val FILENAME = "src/main/resources/day02.txt"

    fun validateLinePart1(input: String): Boolean {
        val groups = applyRegex(input)!!.groupValues
        val minCount = groups[1].toInt()
        val maxCount = groups[2].toInt()
        val matchChar = groups[3].first()
        val password = groups[4]

        return password.count(matchChar::equals) in minCount..maxCount
    }

    fun validateLinePart2(input: String): Boolean {
        val groups = applyRegex(input)!!.groupValues
        val first = groups[1].toInt() - 1
        val last = groups[2].toInt() - 1
        val matchChar = groups[3].first()
        val password = groups[4]

        return password[first] == matchChar && password[last] != matchChar
            || password[first] != matchChar && password[last] == matchChar
    }

    private fun applyRegex(input: String) = """(\d+)-(\d+) (.): (.*)""".toRegex().matchEntire(input)

    fun part1(lines:List<String>) = lines.count(Day02::validateLinePart1)
    fun part2(lines:List<String>) = lines.count(Day02::validateLinePart2)
}

fun main() {
    val lines = Files.readAllLines(Paths.get(Day02.FILENAME))
    println("Part1: ${Day02.part1(lines)}")
    println("Part2: ${Day02.part2(lines)}")
}