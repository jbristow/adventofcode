import util.AdventOfCode

object Day25 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 25")
        println("\tPart 1: ${part1(inputFileString)}")
    }

    fun part1(input: String): Int {
        val (lockData, keyData) = input.split("\n\n").partition { it.startsWith("#####") }
        return lockData.map { it.toLock() }.asSequence()
            .flatMap { lock -> keyData.map { it.toKey() }.asSequence().map { key -> lock to key } }
            .count { (lock, key) -> key.fitsInto(lock) }
    }

    fun String.toLock() = lines().drop(1).parse()

    fun String.toKey() = lines().dropLast(1).parse()

    fun List<String>.parse(): Array<Int> {
        val pinHeights = arrayOf(0, 0, 0, 0, 0)
        this.forEach { row ->
            row.forEachIndexed { pinNum, c ->
                if (c == '#') {
                    pinHeights[pinNum]++
                }
            }
        }
        return pinHeights
    }

    fun Array<Int>.fitsInto(lock: Array<Int>) = lock.zip(this).all { (l, k) -> l + k <= 5 }
}
