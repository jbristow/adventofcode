package aoc

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.none
import arrow.core.right
import arrow.core.some
import arrow.core.toOption
import java.nio.file.Files
import java.nio.file.Paths

object Day07 {

    private const val FILENAME = "src/main/resources/day07.txt"
    private val data: List<Long>
        get() = Files.readAllLines(
            Paths.get(FILENAME)
        ).first().split(",").map { it.toLong() }

    private fun permute(input: Option<List<Long>>): Sequence<List<Long>> {
        return generateSequence(input) { currOpt ->
            currOpt.flatMap { curr ->
                val optK = curr.windowed(2)
                    .mapIndexed { i, it -> i to it }
                    .findLast { (_, ak) -> ak[0] < ak[1] }
                    .toOption()
                    .map { it.first }

                optK.map { k ->
                    val l = curr.indices
                        .findLast { l -> curr[k] < curr[l] }!!
                    val newList = curr.toMutableList()
                    newList[k] = curr[l]
                    newList[l] = curr[k]
                    newList.take(k + 1) + newList.drop(k + 1).reversed()
                }
            }
        }.takeWhile { it !is None }
            .map { it.getOrElse { throw Error("Bad permutation.") } }
    }

    private val permutations = permute((0L until 5L).toList().some())

    private fun List<Long>.runPermutation(code: Array<Long>): Long {
        return fold(arrow.core.none<Long>()) { result, phase ->
            Day05.step(
                code,
                CurrentState(inputs = result.fold({ mutableListOf(phase, 0L) }, { mutableListOf(phase, it) })).right()
            )
                .toLongOrNull()
                .toOption()
        }.getOrElse { -1000000 }
    }

    fun part1() =
        permutations.maxByOrNull { p -> p.runPermutation(data.toTypedArray()) }
            .toOption()
            .map { it.runPermutation(data.toTypedArray()) }

    data class Amplifier(
        val code: Array<Long>,
        val state: Either<String, CurrentState> = CurrentState(0.some(), inputs = mutableListOf()).right()
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Amplifier

            if (!code.contentEquals(other.code)) return false
            if (state != other.state) return false

            return true
        }

        override fun hashCode(): Int {
            var result = code.contentHashCode()
            result = 31 * result + state.hashCode()
            return result
        }
    }

    private fun Amplifier.step(): Amplifier {
        val result = state.flatMap { currState ->
            currState.pointer.fold(
                { currState.right() },
                { Day05.handleCodePoint(code, currState) }
            )
        }
        return Amplifier(code, result)
    }

    private fun runFeedback(list: List<Long>, input: Array<Long>): Either<String, Long> {
        var amps = list.map { ampId ->
            Amplifier(
                code = input.copyOf(),
                state = CurrentState(0.some(), mutableListOf(ampId)).right()
            )
        }.also { amps ->
            amps[0].state.map { it.inputs.add(0) }
        }
        while (amps.any { amp -> amp.state.fold({ false }, { s -> s.pointer is Some<Int> }) }
        ) {
            val nextAmps = amps.map { it.step() }
            val nextOutputs = nextAmps.map { it.state.fold({ none() }, { b -> b.output }) }
            nextOutputs.forEachIndexed { i, output ->
                nextAmps[(i + 1) % nextAmps.size].state.map { s -> output.map { s.inputs.add(it) } }
            }

            amps = nextAmps.map { amp ->
                Amplifier(
                    code = amp.code,
                    state =
                    amp.state.map { state ->
                        CurrentState(
                            pointer = state.pointer,
                            inputs = state.inputs.toMutableList()
                        )
                    }
                )
            }
        }
        return amps.first().state.map { it.inputs.last() }
    }

    fun part2() =
        permute(listOf(5L, 6L, 7L, 8L, 9L).some())
            .map { p -> runFeedback(p, data.toTypedArray()) }
            .fold(mutableListOf<Long>()) { acc, curr ->
                curr.tap { acc.add(it) }
                acc
            }.maxOrNull()
}

fun main() {
    println(Day07.part1())
    println(Day07.part2())
}
