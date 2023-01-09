import Day19.Resource.Clay
import Day19.Resource.Geode
import Day19.Resource.Obsidian
import Day19.Resource.Ore
import util.AdventOfCode
import java.util.PriorityQueue

object Day19 : AdventOfCode() {

    enum class Resource { Ore, Clay, Obsidian, Geode }

    private val blueprintRegex =
        (
            """Blueprint (\d+): Each ore robot costs (\d+) ore. """ +
                """Each clay robot costs (\d+) ore. """ +
                """Each obsidian robot costs (\d+) ore and (\d+) clay. """ +
                """Each geode robot costs (\d+) ore and (\d+) obsidian."""
            ).toRegex()

    data class Supply<T : Comparable<T>>(val ore: T, val clay: T, val obsidian: T, val geode: T) :
        Comparable<Supply<T>> {

        operator fun get(type: Resource) =
            when (type) {
                Ore -> ore
                Clay -> clay
                Obsidian -> obsidian
                Geode -> geode
            }

        override fun compareTo(other: Supply<T>): Int {
            if (geode == other.geode) {
                if (obsidian == other.obsidian) {
                    if (clay == other.clay) {
                        return ore.compareTo(other.ore)
                    }
                    return clay.compareTo(other.clay)
                }
                return obsidian.compareTo(other.obsidian)
            }
            return geode.compareTo(other.geode)
        }
    }

    operator fun Supply<Int>.minus(b: Supply<Int>) =
        copy(
            ore = ore - b.ore,
            clay = clay - b.clay,
            obsidian = obsidian - b.obsidian,
            geode = geode - b.geode
        )

    operator fun Supply<Int>.plus(b: Supply<Int>) =
        copy(
            ore = ore + b.ore,
            clay = clay + b.clay,
            obsidian = obsidian + b.obsidian,
            geode = geode + b.geode
        )

    fun Supply<Int>.increment(type: Resource, n: Int = 1) =
        when (type) {
            Ore -> copy(ore = ore + n)
            Clay -> copy(clay = clay + n)
            Obsidian -> copy(obsidian = obsidian + n)
            Geode -> copy(geode = geode + n)
        }

    data class Factory(
        val id: Int,
        val minutes: Int = 0,
        val supply: Supply<Int> = intSupply(),
        val robots: Supply<Int> = intSupply(ore = 1),
        val costs: Supply<Supply<Int>>,
        val maxRobots: Supply<Int>
    ) : Comparable<Factory> {

        override fun compareTo(other: Factory): Int {
            if (minutes == other.minutes) {
                if (supply == other.supply) {
                    return -robots.compareTo(other.robots)
                }
                return -supply.compareTo(other.supply)
            }
            return minutes.compareTo(other.minutes)
        }

        fun canAfford(type: Resource) = Resource.values().all { supply[it] >= costs[type][it] }
        fun canEventuallyProduce(type: Resource) = Resource.values().all { costs[type][it] == 0 || robots[it] > 0 }

        fun build(type: Resource): Factory = copy(robots = robots.increment(type), supply = supply - costs[type])

        fun advanceTime() = copy(minutes = minutes + 1)

        fun produce() = copy(supply = supply + robots)

        val seenKey = "$robots"
        val seenValue = minutes
    }

    private fun Factory.findBestGeodeState(minutes: Int) =
        try {
            pqSearch(PriorityQueue(listOf(this)), minutes)
        } catch (e: CannotProduceGeodesException) {
            this
        }

    private fun haveSeenBetter(current: Factory, seen: MutableMap<String, Int>): Boolean =
        (seen[current.seenKey] ?: Int.MAX_VALUE) < current.seenValue

    private fun String.toFactory(): Factory {
        return blueprintRegex.matchEntire(this)!!.groupValues.drop(1).map { it.toInt() }.let { m ->

            Factory(
                id = m[0],
                costs = Supply(
                    ore = intSupply(ore = m[1]),
                    clay = intSupply(ore = m[2]),
                    obsidian = intSupply(ore = m[3], clay = m[4]),
                    geode = intSupply(ore = m[5], obsidian = m[6])
                ),
                maxRobots = intSupply(
                    ore = listOf(m[2], m[3], m[5]).max(),
                    clay = m[4],
                    obsidian = m[6],
                    geode = Int.MAX_VALUE
                )
            )
        }
    }

    fun intSupply(ore: Int = 0, clay: Int = 0, obsidian: Int = 0, geode: Int = 0) = Supply(ore, clay, obsidian, geode)

    private tailrec fun pqSearch(
        pq: PriorityQueue<Factory>,
        minutes: Int,
        seen: MutableMap<String, Int> = mutableMapOf()
    ): Factory {
        if (pq.isEmpty()) {
            throw CannotProduceGeodesException()
        }

        val current = pq.remove()

        if (current.minutes == minutes) {
            return current
        }

        if (haveSeenBetter(current, seen)) {
            return pqSearch(pq, minutes, seen)
        }
        seen[current.seenKey] = current.seenValue

        pq.addAll(
            Resource.values()
                .filter { current.canEventuallyProduce(it) }
                .mapNotNull { current.buildNextRobotOfType(it) }
                .filter { it.minutes <= minutes }
                .filterNot { haveSeenBetter(it, seen) }
                .ifEmpty {
                    listOf(current.produce().advanceTime())
                }
        )

        return pqSearch(pq, minutes, seen)
    }

    class CannotProduceGeodesException : Throwable()

    private fun Factory.buildNextRobotOfType(type: Resource): Factory? {
        if (robots[type] >= maxRobots[type]) {
            return null
        }
        var factory = copy()
        while (!factory.canAfford(type)) {
            factory = factory.produce().advanceTime()
        }
        return factory.produce().build(type).advanceTime()
    }

    fun part1(input: Sequence<String>): Int {
        return input
            .map { it.toFactory() }
            .map { it.findBestGeodeState(24) }
            .sumOf { it.id * it.supply.geode }
    }

    fun part2(input: Sequence<String>): Int {
        return input.take(3)
            .map { it.toFactory() }
            .map { it.findBestGeodeState(32) }
            .map { it.supply.geode }
            .reduce(Int::times)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 19")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
