package aoc

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.andThen
import arrow.core.compose
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import arrow.optics.optics
import intcode.CurrentState
import intcode.IntCode
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import util.PointGridL
import java.util.*

@optics
sealed class ShipTile {
    fun handle(repairRobot: RepairRobot): RepairRobot {
        return repairRobot.lastInstruction.fold(
            { repairRobot.copy(state = "Recieved output before sending instructions.".left()) },
            { dir -> this.handleDir(repairRobot, dir) }
        )
    }

    abstract fun handleDir(repairRobot: RepairRobot, dir: Direction): RepairRobot

    object Empty : ShipTile() {
        override fun handleDir(repairRobot: RepairRobot, dir: Direction): RepairRobot {
            val newPosition = repairRobot.position.inDirection(dir)
            repairRobot.grid[newPosition] = Oxygen
            listOf(dir.reverse().turnLeft(), dir, dir.turnLeft()).forEach(repairRobot.instructions::push)
            return repairRobot.copy(position = newPosition)
        }
    }

    object Oxygen : ShipTile() {
        override fun handleDir(repairRobot: RepairRobot, dir: Direction): RepairRobot {
            val newPosition = repairRobot.position.inDirection(dir)
            repairRobot.grid[newPosition] = Oxygen
            listOf(dir.reverse().turnLeft(), dir, dir.turnLeft()).forEach(repairRobot.instructions::push)
            return repairRobot.copy(position = newPosition)
        }
    }

    object Wall : ShipTile() {
        override fun handleDir(repairRobot: RepairRobot, dir: Direction): RepairRobot {
            repairRobot.grid[repairRobot.position.inDirection(dir)] = Wall
            return repairRobot
        }
    }

    companion object
}

private fun ShipTile?.isNull(): Boolean = this == null
private fun <K> Map<K, ShipTile>.findOxygen() = filterValues { it == ShipTile.Oxygen }.keys.first()
fun Long.toShipTile(): Option<ShipTile> = when (this) {
    0L -> ShipTile.Wall.some()
    1L -> ShipTile.Empty.some()
    2L -> ShipTile.Oxygen.some()
    else -> None
}

@optics
data class RepairRobot(
    val code: IntCode,
    val position: PointL = PointL(0, 0),
    val grid: PointGridL<ShipTile> = mutableMapOf(PointL(0, 0) to ShipTile.Empty),
    val lastInstruction: Option<Direction> = none(),
    val state: Either<String, CurrentState> = CurrentState().right(),
    val instructions: LinkedList<Direction> = LinkedList()
) {
    companion object
}

private tailrec fun RepairRobot.move(): RepairRobot {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when (state.value.pointer) {
            is None -> this
            is Some<Long> -> {
                val nRobot = this.processReturn().sendDirection()
                nRobot.copy(state = handleCodePoint(code, nRobot.state)).move()
            }
        }
    }
}

object Day15 {

    private const val FILENAME = "src/main/resources/day15.txt"
    private val fileData = FILENAME.toIntCodeProgram()
    private val repairRobot by lazy {
        RepairRobot(
            code = fileData.toMutableMap(),
            instructions = LinkedList(allDirections())
        ).move()
    }
    private val oxygenLoc by lazy { repairRobot.grid.findOxygen() }

    private tailrec fun distanceToOxygen(
        points: PointGridL<ShipTile>,
        end: PointL,
        queue: List<Pair<PointL, Int>>,
        seen: Set<PointL> = emptySet()
    ): Option<Int> {
        return when {
            queue.isEmpty() -> none()
            queue.first().first == end -> queue.first().second.some()
            else -> {
                val (p, dist) = queue.first()
                distanceToOxygen(
                    points,
                    end,
                    queue.drop(1) + allDirections().asSequence()
                        .map(p::inDirection)
                        .filterNot(seen::contains)
                        .filterNot(points::get andThen ShipTile?::isNull)
                        .filterNot(points::get andThen ShipTile.Wall::equals)
                        .map { it to (dist + 1) },
                    seen + queue.first().first
                )
            }
        }
    }

    fun part1(): Option<Int> {
        repairRobot.printScreen()
        return distanceToOxygen(repairRobot.grid, oxygenLoc, listOf(PointL(0, 0) to 0))
    }

    fun part2() =
        fillWithOxygen(
            repairRobot.grid
                .filter { it.key != oxygenLoc && it.value == ShipTile.Empty }
                .map { it.key }
                .toSet(),
            setOf(oxygenLoc)
        )

    private tailrec fun fillWithOxygen(remaining: Set<PointL>, position: Set<PointL>, minutes: Int = 0): Int =
        when {
            remaining.isEmpty() -> minutes + 1
            else -> {
                val filling = position.flatMap {
                    allDirections().map(it::inDirection).filter(remaining::contains)
                }.toSet()
                fillWithOxygen(remaining - filling, filling, minutes + 1)
            }
        }
}

fun main() {
    println("Part 1\n${Day15.part1()}")
    println("\nPart 2\n${Day15.part2()}")
}

private fun RepairRobot.sendDirection(): RepairRobot {
    return state.fold(
        ifLeft = { this },
        ifRight = {
            when {
                it.waitingForInput && instructions.isEmpty() -> copy(state = "Error: Attempted to get input with no inputs left.".left())
                it.waitingForInput -> {
                    val nInstr = instructions.pop()
                    it.inputs.add(nInstr.toLong())
                    copy(state = it.right(), lastInstruction = nInstr.some())
                }
                else -> this
            }
        }
    )
}

fun PointL.inDirection(direction: Direction): PointL =
    when (direction) {
        is Direction.Up -> PointL.y.set(this, this.y - 1)
        is Direction.Down -> PointL.y.set(this, this.y + 1)
        is Direction.Left -> PointL.x.set(this, this.x - 1)
        is Direction.Right -> PointL.x.set(this, this.x + 1)
    }

fun Point.inDirection(direction: Direction): Point =
    when (direction) {
        is Direction.Up -> Point.y.set(this, this.y - 1)
        is Direction.Down -> Point.y.set(this, this.y + 1)
        is Direction.Left -> Point.x.set(this, this.x - 1)
        is Direction.Right -> Point.x.set(this, this.x + 1)
    }

private tailrec fun RepairRobot.processReturn(): RepairRobot {
    return when (state) {
        is Either.Left<String> -> this
        is Either.Right<CurrentState> -> when {
            grid.filterValues(Boolean::not compose ShipTile.Wall::equals)
                .keys.all { allDirections().all { d -> it.inDirection(d) in grid } } ->
                this.copy(state = "All paths found.".left())
            state.value.output.isEmpty() -> this
            else -> {
                state.value.output.pop()
                    .toShipTile()
                    .fold(
                        { copy(state = "Bad Tile".left()) },
                        { tile -> tile.handle(this) }
                    ).processReturn()
            }
        }
    }
}

fun allDirections(): Collection<Direction> {
    return listOf(
        Direction.Left(),
        Direction.Up(),
        Direction.Right(),
        Direction.Down()
    )
}

fun Direction.reverse(): Direction {
    return this.turnLeft().turnLeft()
}

fun RepairRobot.printScreen() {
    println(instructions.size)
    val toConsider = grid + (position to ShipTile.Empty)
    val topLeft = toConsider.topLeft()
    val bottomRight = PointL(
        toConsider.keys.map(PointL::x).maxOrNull() ?: 0L,
        toConsider.keys.map(PointL::y).maxOrNull() ?: 0L
    )
    println(
        (topLeft.y..bottomRight.y).joinToString("\n") { y ->
            (topLeft.x..bottomRight.x).joinToString("") { x ->
                when (val p = PointL(x, y)) {
                    position -> "@"
                    else -> grid[p].toGlyph()
                }
            }
        }
    )
    println("---")
}

private fun <T> Map<PointL, T>.topLeft() = PointL(
    keys.minOf(PointL::x),
    keys.maxOf(PointL::y)
)

private fun ShipTile?.toGlyph(): String {
    return when (this) {
        is ShipTile.Empty -> "_"
        is ShipTile.Oxygen -> "O"
        is ShipTile.Wall -> "#"
        null -> " "
    }
}
