import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import intcode.CurrentState
import intcode.IntCode
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import java.util.LinkedList

data class Packet(val destination: Long, val x: Long, val y: Long)

fun Packet.asList() = listOf(x, y)

data class NetworkedComputer(
    val id: Int,
    val code: MutableMap<Long, Long>,
    val state: Either<String, CurrentState> = CurrentState().right(),
    val packetOutput: List<Packet> = emptyList(),
    val packetInput: LinkedList<Long> = LinkedList(),
    val idle: Int = 0
)

private fun Either<String, NetworkedComputer>.step(): Either<String, NetworkedComputer> {
    return flatMap { c ->
        c.handleOutput().handleInput().let { newC ->
            newC.state.flatMap { s ->
                s.pointer.fold(
                    ifEmpty = { "Program terminated.".left() },
                    ifSome = {
                        handleCodePoint(newC.code, newC.state).fold(
                            ifLeft = { "Step Error: $it".left() },
                            ifRight = { newC.copy(state = it.right()).right() }
                        )
                    }
                )
            }
        }
    }
}

private fun NetworkedComputer.handleInput(): NetworkedComputer {
    return this.state.fold(
        ifLeft = { this },
        ifRight = { s ->
            when {
                s.waitingForInput && packetInput.isEmpty() -> {
                    s.inputs.add(-1)
                    copy(idle = idle + 1)
                }
                s.waitingForInput -> {
                    s.inputs.add(packetInput.pop())
                    copy(idle = 0)
                }
                else -> this
            }
        }
    )
}

private fun NetworkedComputer.handleOutput(): NetworkedComputer {
    return state.fold(
        ifLeft = { this },
        ifRight = { s ->
            when {
                s.output.size < 3 -> this
                else -> copy(
                    packetOutput = packetOutput + Packet(s.output.pop(), s.output.pop(), s.output.pop()),
                    idle = 0
                ).handleOutput()
            }
        }
    )
}

data class NAT(val packet: Option<Packet> = None, val ySent: List<Long> = emptyList())

fun NAT.sentIdenticalLast2() = ySent.takeLast(2).let { (a, b) -> a == b }

fun NAT.updateFromOutput(outputs: Map<Long, List<Packet>>) = when {
    255 in outputs -> NAT(packet = outputs[255]?.firstOrNull().toOption())
    else -> this
}

object Day23 {

    private const val FILENAME = "src/main/resources/day23.txt"
    private val fileData: IntCode get() = FILENAME.toIntCodeProgram()

    fun part1(): Long {
        val computers: Map<Int, Either<String, NetworkedComputer>> = (0 until 50).map {
            it to NetworkedComputer(
                id = it,
                code = fileData.toMutableMap(),
                state = CurrentState(inputs = LinkedList(listOf(it.toLong()))).right()
            ).right()
        }.toMap()

        return runPart1(computers)
    }

    private tailrec fun runPart1(computers: Map<Int, Either<String, NetworkedComputer>>, count: Int = 0): Long {
        val nextComputers = computers.mapValues { it.value.step() }
        val outputs = nextComputers.collectOutput()
        nextComputers.haltIfAnyError()
        return when {
            255 in outputs -> outputs[255]!!.first().y
            else -> runPart1(nextComputers.transmitPackets(outputs), count + 1)
        }
    }

    fun part2(): Long {
        val computers: Map<Int, Either<String, NetworkedComputer>> = (0 until 50).map {
            it to NetworkedComputer(
                id = it,
                code = fileData.toMutableMap(),
                state = CurrentState(inputs = LinkedList(listOf(it.toLong()))).right()
            ).right()
        }.toMap()

        return runPart2(computers, NAT())
    }

    private tailrec fun runPart2(
        computers: Map<Int, Either<String, NetworkedComputer>>,
        nat: NAT,
        count: Int = 0
    ): Long {
        val nextComputers = computers.mapValues { it.value.step() }.haltIfAnyError()

        val outputs = nextComputers.collectOutput()

        val nextNat = nat.updateFromOutput(outputs)

        val nextNatPacket = networkIsIdle(nextComputers, outputs).noneIfFalse { nextNat.packet }

        val finalNat = nextNat.copy(ySent = nextNatPacket.fold({ nextNat.ySent }, { nextNat.ySent + it.y }))

        return when {
            finalNat.ySent.size > 2 && finalNat.sentIdenticalLast2() -> finalNat.ySent[finalNat.ySent.lastIndex]
            else -> runPart2(
                computers = nextComputers.transmitPackets(
                    nextNatPacket.fold(
                        ifEmpty = { outputs },
                        ifSome = { outputs + (0L to listOf(it)) })
                ),
                nat = finalNat,
                count = count + 1
            )
        }
    }

    private fun networkIsIdle(
        nextComputers: Map<Int, Either<String, NetworkedComputer>>,
        outputs: Map<Long, List<Packet>>
    ) =
        outputs.isEmpty() && nextComputers.all { (k, v) ->
            v.exists { it.packetInput.size == 0 && it.idle > 1 }
        }

    private fun Map<Int, Either<String, NetworkedComputer>>.collectOutput() =
        map { (_, v) ->
            v.fold(
                ifLeft = { emptyList() },
                ifRight = NetworkedComputer::packetOutput
            )
        }.flatten().groupBy { it.destination }

    private fun Map<Int, Either<String, NetworkedComputer>>.transmitPackets(
        outputs: Map<Long, List<Packet>>
    ) = mapValues { (k, v) ->
        v.map { c ->
            c.packetInput.addAll((outputs[k.toLong()]?.flatMap(Packet::asList) ?: emptyList()))
            c.copy(
                packetInput = c.packetInput,
                packetOutput = LinkedList()
            )
        }
    }

    private fun Map<Int, Either<String, NetworkedComputer>>.haltIfAnyError() =
        if (any { it.value.isLeft() }) {
            error("PROBLEM: " +
                    asSequence()
                        .joinToString {
                            "${it.key}:${it.value.map { v -> v.state }}"
                        })
        } else {
            this
        }
}

fun main() {
    println("Part 1:\n${Day23.part1()}")
    println("\nPart 2:\n${Day23.part2()}")
}

fun <T> Boolean.noneIfFalse(f: () -> Option<T>): Option<T> {
    return if (!this) {
        None
    } else {
        f()
    }
}
