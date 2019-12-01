import arrow.core.Option
import arrow.core.extensions.option.applicative.just
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.max

private const val FILENAME = "src/main/resources/day01.txt"

fun fuelNeeded(mass: Int) = max((mass / 3) - 2, 0)

tailrec fun totalFuelNeeded(mass: Int, fuel: Option<Int> = Option.empty(), totalFuel: Int = 0): Int =
    when (val nextFuel = fuel.fold({ fuelNeeded(mass) }, ::fuelNeeded)) {
        0 -> totalFuel
        else -> totalFuelNeeded(mass, nextFuel.just(), totalFuel + nextFuel)
    }

fun part1() = Files.readAllLines(Paths.get(FILENAME))
    .sumBy { fuelNeeded(it.toInt()) }
    .toString()

fun part2() = Files.readAllLines(Paths.get(FILENAME))
    .sumBy { totalFuelNeeded(it.toInt()) }
    .toString()


fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}