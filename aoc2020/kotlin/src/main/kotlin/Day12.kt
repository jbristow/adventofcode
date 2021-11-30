import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.manhattanDistance
import util.Point2d.Companion.plus
import util.Point2d.Companion.times

object Day12 : AdventOfCode() {

    sealed class Direction {
        object North : Direction()
        object South : Direction()
        object East : Direction()
        object West : Direction()

        override fun toString() =
            when (this) {
                is North -> "N"
                is East -> "E"
                is South -> "S"
                is West -> "W"
            }
    }

    private val Direction.right: Direction
        get() = when (this) {
            Direction.North -> Direction.East
            Direction.East -> Direction.South
            Direction.South -> Direction.West
            Direction.West -> Direction.North
        }
    private val Direction.left: Direction
        get() = when (this) {
            Direction.North -> Direction.West
            Direction.East -> Direction.North
            Direction.South -> Direction.East
            Direction.West -> Direction.South
        }

    private val Direction.motion: Point2d
        get() = when (this) {
            Direction.North -> Point2d(0, -1)
            Direction.East -> Point2d(1, 0)
            Direction.South -> Point2d(0, 1)
            Direction.West -> Point2d(-1, 0)
        }

    fun Direction.turnRight(degrees: Int) = (1..degrees / 90).fold(this) { acc, _ -> acc.right }
    fun Direction.turnLeft(degrees: Int) = (1..degrees / 90).fold(this) { acc, _ -> acc.left }

    fun Point2d.rotateLeft(degrees: Int) =
        when (degrees % 360) {
            0 -> this
            90 -> Point2d(y, -x)
            180 -> Point2d(-x, -y)
            270 -> Point2d(-y, x)
            else -> throw Exception("Unexpected rotation amount: $degrees")
        }

    fun Point2d.rotateRight(degrees: Int) =
        when (degrees % 360) {
            0 -> this
            90 -> Point2d(-y, x)
            180 -> Point2d(-x, -y)
            270 -> Point2d(y, -x)
            else -> throw Exception("Unexpected rotation amount: $degrees")
        }

    sealed class Instruction {
        data class N(val value: Int) : Instruction()
        data class S(val value: Int) : Instruction()
        data class E(val value: Int) : Instruction()
        data class W(val value: Int) : Instruction()
        data class L(val value: Int) : Instruction()
        data class R(val value: Int) : Instruction()
        data class F(val value: Int) : Instruction()
        companion object {
            fun of(input: String) =
                when (input.first()) {
                    'N' -> N(input.drop(1).toInt())
                    'E' -> E(input.drop(1).toInt())
                    'S' -> S(input.drop(1).toInt())
                    'W' -> W(input.drop(1).toInt())
                    'R' -> R(input.drop(1).toInt())
                    'L' -> L(input.drop(1).toInt())
                    'F' -> F(input.drop(1).toInt())
                    else -> throw Exception("Illegal input: $input")
                }
        }
    }

    val Instruction.motion: Point2d
        get() = when (this) {
            is Instruction.N -> Direction.North.motion
            is Instruction.E -> Direction.East.motion
            is Instruction.S -> Direction.South.motion
            is Instruction.W -> Direction.West.motion
            else -> throw Exception("Instruction $this doesn't have a direction.")
        }
    val Instruction.value: Int
        get() = when (this) {
            is Instruction.N -> value
            is Instruction.E -> value
            is Instruction.S -> value
            is Instruction.W -> value
            is Instruction.L -> value
            is Instruction.R -> value
            is Instruction.F -> value
        }

    fun Ship.execute(instr: Instruction) =
        when (instr) {
            is Instruction.N, is Instruction.E,
            is Instruction.S, is Instruction.W,
            -> copy(position = instr.motion * instr.value + position)
            is Instruction.L -> turnLeft(instr.value)
            is Instruction.R -> turnRight(instr.value)
            is Instruction.F -> copy(position = heading.motion * instr.value + position)
        }

    fun Ship.turnLeft(value: Int) = copy(heading = heading.turnLeft(value))
    fun Ship.turnRight(value: Int) = copy(heading = heading.turnRight(value))
    fun Ship.rotateLeft(value: Int) = position.rotateLeft(value)
    fun Ship.rotateRight(value: Int) = position.rotateRight(value)

    fun Pair<Ship, Ship>.executePart2(instr: Instruction): Pair<Ship, Ship> {
        val (ship, waypoint) = this
        return when (instr) {
            is Instruction.N,
            is Instruction.E,
            is Instruction.S,
            is Instruction.W,
            ->
                ship to waypoint.copy(position = instr.motion * instr.value + waypoint.position)
            is Instruction.L ->
                ship to waypoint.copy(position = waypoint.rotateLeft(instr.value))
            is Instruction.R ->
                ship to waypoint.copy(position = waypoint.rotateRight(instr.value))
            is Instruction.F ->
                ship.copy(position = waypoint.position * instr.value + ship.position) to waypoint
        }
    }

    data class Ship(val position: Point2d, val heading: Direction)

    val initialWaypoint: Ship
        get() = Ship(Direction.East.motion * 10 + Direction.North.motion * 1, Direction.East)
    val initialShip: Ship
        get() = Ship(Point2d(0, 0), Direction.East)

    fun part1(input: List<String>) =
        input.map(Day12.Instruction::of)
            .fold(initialShip) { ship, instr -> ship.execute(instr) }
            .position
            .manhattanDistance()

    fun part2(input: List<String>) =
        input.map(Day12.Instruction::of)
            .fold(initialShip to initialWaypoint) { acc, instr -> acc.executePart2(instr) }
            .first
            .position
            .manhattanDistance()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 12")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}

