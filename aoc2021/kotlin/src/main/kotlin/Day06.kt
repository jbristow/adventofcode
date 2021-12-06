import util.AdventOfCode
import java.util.PriorityQueue
import java.util.UUID

object Day06 : AdventOfCode() {

    data class Lanternfish(var timer: Int = 8) {

        fun tick(): Lanternfish? {
            return if (timer == 0) {
                timer = 6
                Lanternfish()
            } else {
                timer -= 1
                null
            }
        }
    }

    var sample = "3,4,3,1,2"

    fun part1(input: String): Int {
        val start = input.split(",").map { Lanternfish(it.toInt()) }
        return simulation(80, start)
    }

    tailrec fun simulation(daysToSimulate: Int, fish: List<Lanternfish>): Int {
        if (daysToSimulate == 0) {
            return fish.size
        }

        val newFish = fish.fold(mutableListOf<Lanternfish>()) { acc, f ->
            acc.add(f)
            when (val newf = f.tick()) {
                null -> {
                    /*empty*/
                }
                else -> acc.add(newf)
            }
            acc
        }
        return simulation(daysToSimulate - 1, newFish)
    }

    tailrec fun simulationB(
        day: Int = 1,
        fish: PriorityQueue<DaySpawn>,
        count: Long,
        daysLeftToSimulate: Int
    ): Long {
        if (daysLeftToSimulate == 0) {
            return count
        }

        val topOfQueue = fish.peek()
        if (topOfQueue.spawnDate != day) {
            return simulationB(day + 1, fish, count, daysLeftToSimulate - 1)
        }

        val spawners = mutableListOf<DaySpawn>()

        while (topOfQueue.spawnDate == fish.peek()?.spawnDate) {
            spawners.add(fish.remove())
        }

        val totalSpawning = spawners.sumOf { it.count }
        fish.add(DaySpawn(day + 7, totalSpawning))
        fish.add(DaySpawn(day + 9, totalSpawning))

        return simulationB(day + 1, fish, count + totalSpawning, daysLeftToSimulate - 1)
    }

    data class DaySpawn(
        val spawnDate: Int,
        val count: Long,
        val id: UUID = UUID.randomUUID()
    ) : Comparable<DaySpawn> {
        override fun compareTo(other: DaySpawn): Int {
            return spawnDate.compareTo(other.spawnDate)
        }
    }

    fun part2(input: String): Long {
        val start = input.split(",").map { DaySpawn(it.toInt(), 1) }

        val fish = PriorityQueue<DaySpawn>()
        fish.addAll(start)
        return simulationB(1, fish, fish.size.toLong(), 256 - 1)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 6")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
