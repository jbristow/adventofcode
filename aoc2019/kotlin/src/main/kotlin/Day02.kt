import Day02.handleOpcode
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import java.nio.file.Files
import java.nio.file.Paths


object Day02 {

    private const val FILENAME = "src/main/resources/day02.txt"

    fun handleOpcode(offset: Int, intList: Either<Array<Int>, Array<Int>>): Either<Array<Int>, Array<Int>> =
        intList.flatMap {
            val opcodeChunk = it.drop(offset).take(4)

            when (opcodeChunk[0]) {
                1 -> {
                    it[opcodeChunk[3]] = it[opcodeChunk[1]] + it[opcodeChunk[2]]
                    it.right()
                }
                2 -> {
                    it[opcodeChunk[3]] = it[opcodeChunk[1]] * it[opcodeChunk[2]]
                    it.right()
                }
                99 -> it.left()
                else -> throw Error("Unknown opcode: ${opcodeChunk[0]}")
            }
        }


    fun part1() = Files.readString(Paths.get(FILENAME))
        .trim()
        .prepInput().let {
            it[1] = 12
            it[2] = 2
            it
        }.operate()[0]
        .toString()

    fun part2(): String {
        val input = Files.readString(Paths.get(FILENAME))
            .trim().prepInput()

        return (0..99).fold(Unit.right() as Either<Pair<Int, Int>, Unit>) { foundSoFar, noun ->
            foundSoFar.flatMap {
                val found = (0..99).find { verb ->
                    val inputCopy = input.copyOf()
                    inputCopy[1] = noun
                    inputCopy[2] = verb
                    inputCopy.operate()[0] == 19690720
                }
                if (found == null) {
                    foundSoFar
                } else {
                    (noun to found).left()
                }
            }
        }.fold({ (noun, verb) ->
            "%02d%02d".format(noun, verb)
        }, { throw Error("No match found.") })
    }
}

fun String.prepInput() = split(",").map { it.toInt() }.toTypedArray()

fun Array<Int>.operate() =
    this.let {
        (0..this.size / 4).map { it * 4 }
            .fold(this.right() as Either<Array<Int>, Array<Int>>) { intArr, offset ->
                handleOpcode(offset, intArr)
            }.fold({ it }, { it })
    }

fun main() {
    println("Part 1: ${Day02.part1()}")
    println("Part 2: ${Day02.part2()}")
}