import util.AdventOfCode

object Day06Alt : AdventOfCode("day06.txt") {

    private tailrec fun <T> findFirstUniqueOfSize(
        seq: Sequence<T>,
        size: Int,
        offset: Int = 0,
        seen: MutableList<T> = mutableListOf()
    ): Int? {
        val head = seq.firstOrNull()

        if (seen.size == size) {
            return offset
        }

        if (head == null) {
            return null
        }

        val dupe = seen.indexOf(head)
        if (dupe > -1) {
            repeat(dupe + 1) {
                seen.removeAt(0)
            }
        }
        seen.add(head)
        return findFirstUniqueOfSize(seq.drop(1), size, offset + 1, seen)
    }

    fun part1(input: String) = findFirstUniqueOfSize(input.asSequence(), 4)
    fun part2(input: String) = findFirstUniqueOfSize(input.asSequence(), 14)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }

}
