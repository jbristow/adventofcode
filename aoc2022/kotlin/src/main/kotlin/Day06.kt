import util.AdventOfCode

object Day06 : AdventOfCode() {

    fun <T> List<T>.isUnique() = this.toSet().size == this.size

    private fun <T> Sequence<T>.findFirstUniqueOfSize(size: Int) =
        this.windowed(size)
            .withIndex()
            .find { (_, line) -> line.isUnique() }
            ?.index
            ?.plus(size)

    fun part1(input: String) = input.asSequence().findFirstUniqueOfSize(4)

    fun part2(input: String) = input.asSequence().findFirstUniqueOfSize(14)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
