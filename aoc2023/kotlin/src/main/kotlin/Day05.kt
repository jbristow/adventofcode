import arrow.optics.optics
import util.AdventOfCode
import java.util.*

object Day05 : AdventOfCode() {

    @optics
    data class Farm(
        val seedToSoil: List<AlmanacMap> = emptyList(),
        val soilToFertilizer: List<AlmanacMap> = emptyList(),
        val fertilizerToWater: List<AlmanacMap> = emptyList(),
        val waterToLight: List<AlmanacMap> = emptyList(),
        val lightToTemperature: List<AlmanacMap> = emptyList(),
        val temperatureToHumidity: List<AlmanacMap> = emptyList(),
        val humidityToLocation: List<AlmanacMap> = emptyList(),
    ) {
        companion object {
            val almanacSections = listOf(
                Farm.seedToSoil,
                Farm.soilToFertilizer,
                Farm.fertilizerToWater,
                Farm.waterToLight,
                Farm.lightToTemperature,
                Farm.temperatureToHumidity,
                Farm.humidityToLocation
            )
        }
    }

    private fun LinkedList<LongRange>.consumeRanges(
        mappings: List<AlmanacMap>,
        dests: LinkedList<LongRange> = LinkedList()
    ): LinkedList<LongRange> {
        if (this.isEmpty()) {
            return LinkedList(dests.sortedBy { it.first })
        }

        val current = this.removeFirst()

        val overlap = mappings.firstOrNull {
            current.first in it.sourceRange || current.last in it.sourceRange
        }

        return when {
            overlap == null -> {
                dests.add(current)
                this.consumeRanges(mappings, dests)
            }

            current.first < overlap.sourceRange.first -> {
                val nextDest = current.first until overlap.sourceRange.first
                val newFirst = overlap.sourceRange.first..current.last
                dests.add(nextDest)
                this.addFirst(newFirst)
                this.consumeRanges(mappings, dests)
            }

            current.last <= overlap.sourceRange.last -> {
                val nextDest = overlap.convert(current.first)..overlap.convert(current.last)
                dests.add(nextDest)
                this.consumeRanges(mappings, dests)
            }

            else -> {
                val offset = current.first - overlap.sourceRange.first
                val converted = overlap.convert(current.first)
                val nextDest = overlap.convert(current.first) until (converted + (overlap.size - offset))
                val nextIncoming = (overlap.sourceRange.last + 1)..current.last
                dests.add(nextDest)
                this.addFirst(nextIncoming)
                this.consumeRanges(mappings, dests)
            }
        }
    }


    private fun Long.applyAlmanac(almanacRange: List<AlmanacMap>): Long {
        return when (val range = almanacRange.find { it.canConvert(this) }) {
            null -> this
            else -> range.convert(this)
        }
    }

    private tailrec fun List<String>.parse(
        seeds: List<Long> = emptyList(),
        farm: Farm = Farm()
    ): Pair<List<Long>, Farm> {
        if (this.isEmpty()) {
            return seeds to farm
        }

        val current = this.first().trim()
        val remaining = this.drop(1)

        return when {
            current.isEmpty() -> remaining.parse(seeds, farm)
            current.startsWith("seeds:") -> {
                remaining.parse(current.split(" ").drop(1).map { it.trim().toLong() }, farm)
            }

            else -> {
                val (nextRemaining, nextFarm) = almanacMaps(current, remaining, farm)
                nextRemaining.parse(seeds, nextFarm)
            }
        }
    }

    private tailrec fun List<String>.parseRange(
        seeds: List<LongRange> = emptyList(), farm: Farm = Farm()
    ): Pair<List<LongRange>, Farm> {
        if (this.isEmpty()) {
            return seeds to farm
        }

        val current = this.first().trim()
        val remaining = this.drop(1)

        return if (current.isEmpty()) {
            remaining.parseRange(seeds, farm)
        } else if (current.startsWith("seeds:")) {
            remaining.parseRange(
                current.split(" ").drop(1).map { it.trim().toLong() }.chunked(2)
                    .map { (a, b) -> (a until a + b) }, farm
            )
        } else {
            val (nextRemaining, nextFarm) = almanacMaps(current, remaining, farm)
            nextRemaining.parseRange(seeds, nextFarm)
        }
    }

    private fun almanacMaps(current: String, remaining: List<String>, farm: Farm): Pair<List<String>, Farm> {
        when (current) {
            "seed-to-soil map:" -> {
                val (nextRemaining, soilToSeed) = remaining.parseMap()
                return nextRemaining to farm.copy(seedToSoil = soilToSeed.sortedBy { it.sourceRangeStart })
            }

            "soil-to-fertilizer map:" -> {
                val (nextRemaining, fertilizerToSoil) = remaining.parseMap()
                return nextRemaining to farm.copy(soilToFertilizer = fertilizerToSoil.sortedBy { it.sourceRangeStart })
            }

            "fertilizer-to-water map:" -> {
                val (nextRemaining, waterToFertilizer) = remaining.parseMap()
                return nextRemaining to farm.copy(fertilizerToWater = waterToFertilizer.sortedBy { it.sourceRangeStart })
            }

            "water-to-light map:" -> {
                val (nextRemaining, lightToWater) = remaining.parseMap()
                return nextRemaining to farm.copy(waterToLight = lightToWater.sortedBy { it.sourceRangeStart })
            }

            "light-to-temperature map:" -> {
                val (nextRemaining, temperatureToLight) = remaining.parseMap()
                return nextRemaining to farm.copy(lightToTemperature = temperatureToLight.sortedBy { it.sourceRangeStart })
            }

            "temperature-to-humidity map:" -> {
                val (nextRemaining, humidityToTemperature) = remaining.parseMap()
                return nextRemaining to farm.copy(temperatureToHumidity = humidityToTemperature.sortedBy { it.sourceRangeStart })
            }

            "humidity-to-location map:" -> {
                val (nextRemaining, locationToHumidity) = remaining.parseMap()
                return nextRemaining to farm.copy(humidityToLocation = locationToHumidity.sortedBy { it.sourceRangeStart })
            }

            else -> throw Exception("Unknown section: $current")

        }
    }

    private tailrec fun List<String>.parseMap(list: List<AlmanacMap> = emptyList()): Pair<List<String>, List<AlmanacMap>> {
        val current = this.firstOrNull()?.trim()
        val remaining = this.drop(1)

        return if (current.isNullOrEmpty()) {
            remaining to list
        } else {
            val (destRangeStart, sourceRangeStart, size) = current.split(" ").map { it.trim().toLong() }
            remaining.parseMap(list + AlmanacMap(destRangeStart, sourceRangeStart, size))
        }
    }

    data class AlmanacMap(val destRangeStart: Long, val sourceRangeStart: Long, val size: Long) {
        val sourceRange = sourceRangeStart until (sourceRangeStart + size)

        fun canConvert(item: Long) = item in sourceRange
        fun convert(item: Long): Long {
            return (item - sourceRangeStart) + destRangeStart
        }
    }


    private fun part1(input: List<String>): Long {
        val (seeds, farm) = input.parse()
        return seeds.minOf { seed ->
            Farm.almanacSections.fold(seed) { item, prop -> item.applyAlmanac(prop.get(farm)) }
        }

    }

    private fun part2(input: List<String>): Long {
        val (seeds, farm) = input.parseRange()
        val seedll = LinkedList(seeds.sortedBy { it.first })
        return Farm.almanacSections.fold(seedll) { ll, section ->
            ll.consumeRanges(section.get(farm))
        }.minBy { it.first }.first
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 5")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
