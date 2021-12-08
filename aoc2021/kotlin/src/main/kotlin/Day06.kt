import util.AdventOfCode
import java.util.PriorityQueue

object Day06 : AdventOfCode() {

    fun part1(input: String) = input.split(",").map { it.toInt() }.simluate(80)

    tailrec fun List<Int>.simluate(daysToSimulate: Int): Int {
        if (daysToSimulate == 0) {
            return size
        }

        val newFish = this.fold(mutableListOf<Int>()) { acc, f ->
            when (f) {
                0 -> {
                    acc.add(8)
                    acc.add(6)
                }
                else -> acc.add(f - 1)
            }
            acc
        }
        return newFish.simluate(daysToSimulate - 1)
    }

    tailrec fun PriorityQueue<DaySpawn>.megaSimulate(
        daysLeftToSimulate: Int,
        day: Int = 0,
        count: Long = sumOf { it.count }
    ): Long {
        if (daysLeftToSimulate == 0) {
            return count
        }

        val topOfQueue = peek()
        if (topOfQueue.spawnDate != day) {
            return megaSimulate(daysLeftToSimulate - 1, day + 1, count)
        }

        val spawners = mutableListOf<DaySpawn>()
        while (topOfQueue.spawnDate == peek()?.spawnDate) {
            spawners.add(remove())
        }

        val totalSpawning = spawners.sumOf { it.count }
        add(DaySpawn(day + 7, totalSpawning))
        add(DaySpawn(day + 9, totalSpawning))

        return megaSimulate(daysLeftToSimulate - 1, day + 1, count + totalSpawning)
    }

    data class DaySpawn(val spawnDate: Int, val count: Long) : Comparable<DaySpawn> {
        override fun compareTo(other: DaySpawn) = spawnDate.compareTo(other.spawnDate)
    }

    fun part2(input: String): Long {
        val start = input.split(",")
            .groupBy { it }
            .map { (k, v) -> DaySpawn(k.toInt(), v.size.toLong()) }

        val fish = PriorityQueue<DaySpawn>()
        fish.addAll(start)
        return fish.megaSimulate(256)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
