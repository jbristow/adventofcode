import util.AdventOfCode

object Day13 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        val data = "939\n7,13,x,x,59,x,31,19".lines()
        println("Part 1: ${part1(inputFileLines)}")
        println("Part 2: ${part2(data)}")
    }

    tailrec fun waitForBus(buses: List<Int>, time: Int): Pair<Int, Int> {
        val here = buses.find { time % it == 0 }
        return when (here) {
            null -> waitForBus(buses, time + 1)
            else -> here to time
        }
    }

    private fun part1(data: List<String>): Int? {
        val earliest = data[0].toInt()
        val buses = data[1].split(",").filter { it != "x" }.map { it.toInt() }
        val (bus, arrival) = waitForBus(buses, earliest)
        return bus * (arrival - earliest)
    }

    tailrec fun busConvergence(
        buses: List<Pair<Long, Long>>,
        currStep: Long = buses.first().second,
        currTime: Long = 0L,
    ): Long {
        println("$currStep, $currTime, $buses")
        val remainder = (currTime + currStep + buses.first().first) % buses.first().second
        return when {
            remainder == 0L && buses.size == 1 -> currTime + currStep
            remainder == 0L ->
                busConvergence(
                    buses.drop(1),
                    lcm(currStep, buses.first().second),
                    currTime + currStep
                )
            else -> busConvergence(buses, currStep, currTime + currStep)
        }
    }

    private fun part2(data: List<String>): Long {
        val buses = data[1].split(",").mapIndexedNotNull { t, it ->
            when (it) {
                "x" -> null
                else -> t.toLong() to it.toLong()
            }
        }

        return busConvergence(buses)
    }

    fun lcm(a: Long, b: Long): Long {
        return (a * b) / gcd(a, b)
    }

    private tailrec fun gcd(a: Long, b: Long): Long {
        return when (val r = a % b) {
            0L -> b
            else -> gcd(b, r)
        }
    }
}
