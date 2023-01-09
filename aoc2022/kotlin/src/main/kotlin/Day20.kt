import util.AdventOfCode

object Day20 : AdventOfCode() {

    private fun mix(numbers: List<Long>, startingIndices: List<Int> = numbers.indices.toList()): List<Int> {
        val n = numbers.size

        tailrec fun loop(indices: List<Int>, index: Int = 0): List<Int> {
            if (index >= n) {
                return indices
            }

            val value = numbers[index]
            val newIndices = indices.toMutableList()
            val locationOfIndex = indices.indexOfFirst { it == index }
            newIndices.removeAt(locationOfIndex)

            var insertAt = (locationOfIndex + value) % (n - 1)
            while (insertAt < 0) {
                insertAt += n - 1
            }
            insertAt %= (n - 1)

            newIndices.add(insertAt.toInt(), index)
            return loop(newIndices, index + 1)
        }

        return loop(startingIndices.toMutableList(), 0)
    }

    private fun part1(input: List<Long>) = coordinateSum(mix(input).map { input[it] })

    private fun part2(input: List<Long>): Long {
        val numbers = input.map { it * 811589153 }
        val mixed = (0..9).fold(numbers.indices.toList()) { acc, _ -> mix(numbers, acc) }.map { numbers[it] }
        return coordinateSum(mixed)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 20")
        println("\tPart 1: ${part1(inputFileLongs)}")
        println("\tPart 2: ${part2(inputFileLongs)}")
    }

    private fun coordinateSum(
        numbers: List<Long>
    ): Long {
        val zero = numbers.indexOfFirst { it == 0L }
        return listOf(1000, 2000, 3000).map { (zero + it) % numbers.size }.sumOf { numbers[it] }
    }
}
