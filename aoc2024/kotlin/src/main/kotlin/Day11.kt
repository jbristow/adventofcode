import util.AdventOfCode

object Day11 : AdventOfCode() {

    class RockBlinker(
        private val maxBlinks: Int,
    ) {
        private val cache = mutableMapOf<Pair<Long, Int>, Long>()
        fun blink(
            stone: Long,
            blinks: Int,
        ): Long {
            if (blinks == maxBlinks) {
                return 1L
            }

            val key = stone to blinks
            if (key in cache) {
                return cache.getValue(key)
            }

            val stoneStr = stone.toString()

            cache[key] = when {
                stone == 0L -> blink(1, blinks + 1)
                stoneStr.length % 2 == 0 -> {
                    blink(stoneStr.substring(0, stoneStr.length / 2).toLong(), blinks + 1) + blink(
                        stoneStr.substring(stoneStr.length / 2).toLong(),
                        blinks + 1,
                    )
                }

                else -> blink(stone * 2024, blinks + 1)
            }
            return cache.getValue(key)
        }
    }

    fun part1(input: String): Long {
        val blinker = RockBlinker(25)
        return input.split("""\s+""".toRegex()).sumOf { blinker.blink(it.toLong(), 0) }
    }

    fun part2(input: String): Long {
        val blinker = RockBlinker(75)
        return input.split("""\s+""".toRegex()).sumOf { blinker.blink(it.toLong(), 0) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 11")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
