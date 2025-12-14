import Day10.IndicatorData.Companion.toIndicatorData
import Day10.JoltageButtonSet.Companion.toJoltageButtonSet
import edu.harvard.econcs.jopt.solver.client.SolverClient
import edu.harvard.econcs.jopt.solver.mip.CompareType
import edu.harvard.econcs.jopt.solver.mip.Constraint
import edu.harvard.econcs.jopt.solver.mip.MIP
import edu.harvard.econcs.jopt.solver.mip.VarType
import edu.harvard.econcs.jopt.solver.mip.Variable
import util.AdventOfCode
import util.DjikstraUnbounded
import kotlin.math.roundToLong

object Day10 : AdventOfCode() {
    private fun <T> Iterable<T>.dropOuter(n: Int) = drop(n).dropLast(n)

    private fun String.dropOuter(n: Int) = substring(n, this.length - n)
    data class IndicatorData(
        val target: Long,
        val buttons: List<Long>,
    ) {
        companion object {

            private fun String.toButton(length: Int): Long =
                dropOuter(1).split(",").map { it.toInt() }
                    .fold(Array<Byte>(length) { _ -> 0 }) { acc, curr ->
                        acc[curr] = 1
                        acc
                    }.joinToString("").toLong(2)

            private fun String.toTarget() =
                dropOuter(1)
                    .replace(".", "0")
                    .replace('#', '1')
                    .toLong(2)

            fun String.toIndicatorData(): IndicatorData =
                split(" ").let { chunks ->
                    val targetChunk = chunks.first()
                    val buttons = chunks.dropOuter(1)
                        .map { chunk -> chunk.toButton(targetChunk.length - 2) }
                    IndicatorData(targetChunk.toTarget(), buttons)
                }
        }
    }

    private fun leastPressesToMatch(
        target: Long,
        buttons: List<Long>,
    ): Int {
        val output = DjikstraUnbounded.run(
            start = 0L,
            isEnd = { p -> p == target },
            neighborFn = { p -> buttons.map { it.xor(p) } },
            distanceFn = { _, _ -> 1 },
        )
        return output?.first ?: error("no path found")
    }

    private fun part1(lines: List<String>): Int =
        lines.map { line -> line.toIndicatorData() }
            .sumOf { (target, buttons) -> leastPressesToMatch(target, buttons) }

    data class JoltageButtonSet(
        val targets: List<Long>,
        val buttons: List<Set<Int>>,
    ) {
        val buttonVars = buttons.indices.map {
            Variable("button$it", VarType.INT, 0.0, MIP.MAX_VALUE.toDouble())
        }
        val constraints = targets.mapIndexed { i, target ->
            Constraint(CompareType.EQ, target.toDouble()).apply {
                buttons.mapIndexedNotNull { buttonIndex, button ->
                    when (i) {
                        in button -> buttonVars[buttonIndex]
                        else -> null
                    }
                }.forEach { buttonVar ->
                    addTerm(1.0, buttonVar)
                }
            }
        }

        companion object {

            private fun String.toJoltageTargets(): List<Long> = this.dropOuter(1).split(",").map { it.toLong() }

            fun String.toJoltageButtonSet(): JoltageButtonSet {
                val parts = this.split(" ")
                return JoltageButtonSet(
                    targets = parts.last().toJoltageTargets(),
                    buttons = parts.dropOuter(1)
                        .map { buttonText ->
                            buttonText.dropOuter(1)
                                .split(",")
                                .map { it.toInt() }.toSet()
                        },
                )
            }
        }
    }

    private fun part2(lines: List<String>): Long =
        lines.map { it.toJoltageButtonSet() }
            .sumOf { jbs ->
                val mip = MIP().apply {
                    isObjectiveMax = false
                    jbs.buttonVars.forEach { button ->
                        add(button)
                        addObjectiveTerm(1.0, button)
                    }
                    jbs.constraints.forEach { add(it) }
                }
                SolverClient().solve(mip).objectiveValue.roundToLong()
            }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 10")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
