import Day06.simulation
import Day06.simulationB
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.PriorityQueue

internal class Day06Test {

    @Nested
    inner class Examples {
        private val sample = "3,4,3,1,2"
        private val sampleFish = sample.split(",").map(String::toInt)

        @Test
        fun part1() {
            assertThat(Day06.part1(sample)).isEqualTo(5934)
        }

        @Test
        fun part1_18d() {
            assertThat(sampleFish.simulation(18)).isEqualTo(26)
        }

        @Test
        fun part2_18d() {
            val fish = PriorityQueue<Day06.DaySpawn>()
            fish.addAll(sampleFish.map { Day06.DaySpawn(it, 1) })
            assertThat(fish.simulationB(18)).isEqualTo(26)
        }

        @Test
        fun part2_80d() {
            val fish = PriorityQueue<Day06.DaySpawn>()
            fish.addAll(sampleFish.map { Day06.DaySpawn(it, 1) })
            assertThat(fish.simulationB(80)).isEqualTo(5934)
        }

        @Test
        fun part2() {
            assertThat(Day06.part2(sample)).isEqualTo(26984457539)
        }
    }

    @Nested
    inner class Answer {

        @Test
        fun part1() {
            assertThat(Day06.part1(Day06.inputFileString)).isEqualTo(361169)
        }

        @Test
        fun part2() {
            assertThat(Day06.part2(Day06.inputFileString)).isEqualTo(1634946868992)
        }
    }
}
