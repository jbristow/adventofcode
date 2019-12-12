import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import arrow.core.right
import arrow.optics.optics
import arrow.syntax.function.andThen
import intcode.CurrentState
import intcode.handleCodePoint
import java.nio.file.Files
import java.nio.file.Paths


object Day11 {
    private const val FILENAME = "src/main/resources/day11.txt"
    val fileData =
        Files.readAllLines(Paths.get(FILENAME)).first()
            .splitToSequence(",")
            .mapIndexed { i, it -> i.toLong() to it.toLong() }
            .toMap()
            .toMutableMap()

}

sealed class Direction {
    object Up : Direction()
    object Down : Direction()
    object Left : Direction()
    object Right : Direction()
}

val Direction.glyph: String
    get() = when (this) {
        is Direction.Up -> "⬆️"
        is Direction.Down -> "⬇️"
        is Direction.Left -> "⬅️"
        is Direction.Right -> "➡️"
    }


sealed class HullColor {
    object Black : HullColor()
    object White : HullColor()
}

private fun HullColor?.toInput(): Long {
    return when (this) {
        is HullColor.White -> 1L
        is HullColor.Black -> 0L
        else -> 0L
    }
}

@optics
data class Robot(
    val location: Point = Point(0, 0),
    val direction: Direction = Direction.Up,
    val state: Either<String, CurrentState> = CurrentState().right(),
    val code: MutableMap<Long, Long>,
    val hull: MutableMap<Point, HullColor> = mutableMapOf(),
    val bodyCounter: Int = 0,
    val brainCounter: Int = 0
) {
    companion object
}


fun Robot.turnLeft(): Robot = copy(
    direction = when (direction) {
        Direction.Up -> Direction.Left
        Direction.Down -> Direction.Right
        Direction.Left -> Direction.Down
        Direction.Right -> Direction.Up
    }
)

fun Robot.turnRight(): Robot = copy(
    direction = when (direction) {
        Direction.Up -> Direction.Right
        Direction.Down -> Direction.Left
        Direction.Left -> Direction.Up
        Direction.Right -> Direction.Down
    }
)

fun Robot.goForward(): Robot = copy(
    location = when (direction) {
        Direction.Up -> Point.y.set(location, location.y - 1)
        Direction.Down -> Point.y.set(location, location.y + 1)
        Direction.Left -> Point.x.set(location, location.x - 1)
        Direction.Right -> Point.x.set(location, location.x + 1)
    }
)

fun Robot.paint(instr: Long) = apply {
    hull[location] = when (instr) {
        0L -> HullColor.Black
        1L -> HullColor.White
        else -> throw Error("Illegal paint instruction: $instr")
    }
}

fun Robot.move(instr: Long): Robot = when (instr) {
    0L -> turnLeft()
    1L -> turnRight()
    else -> throw Error("Bad move instruction: $instr")
}.goForward()


tailrec fun Robot.bodyStep(): Robot = when {
    state is Either.Left<String> || state is Either.Right<CurrentState> && state.b.output.size < 2 -> this
    state is Either.Right<CurrentState> ->
        paint(state.b.output.pop())
            .move(state.b.output.pop())
            .copy(bodyCounter = bodyCounter + 1)
            .bodyStep()
    else -> throw Error("BodyStep Problem: $this")
}


private fun Either<String, CurrentState>.withUpdatedInputs(hullColor: HullColor?) =
    map {
        it.apply {
            if (waitingForInput) {
                inputs.add(hullColor.toInput())
            }
        }
    }

private fun Either<String, Robot>.fullOutput(): String {
    return fold({
        it
    }, {
        val hull = it.hull.toMutableMap()
        hull[it.location] = HullColor.Black
        val topLeft = Point(hull.keys.map { p -> p.x }.min() ?: 0, hull.keys.map { p -> p.y }.min() ?: 0)
        val bottomRight = Point(hull.keys.map { p -> p.x }.max() ?: 0, hull.keys.map { p -> p.y }.max() ?: 0)
        return (topLeft.y..bottomRight.y).joinToString("\n") { y ->
            (topLeft.x..bottomRight.x).joinToString("") { x ->
                when {
                    Point(x, y) == it.location -> it.direction.glyph
                    hull[Point(x, y)] is HullColor.White -> "◽️"
                    hull[Point(x, y)] is HullColor.Black -> "◼️"
                    else -> "◼️"
                }
            }
        }

    })
}

tailrec fun Robot.brainStep(): Either<String, Robot> =
    when (state) {
        is Either.Left<String> -> state
        is Either.Right<CurrentState> -> when (state.b.pointer) {
            is None -> this.right()
            is Some<Long> -> {
                copy(
                    state = handleCodePoint(
                        code,
                        state.withUpdatedInputs(hull[location])
                    )
                    , brainCounter = brainCounter + 1
                ).bodyStep().brainStep()
            }
        }
    }

fun main() {
    println("Part 1")
    Robot(code = Day11.fileData).brainStep().let {
        println(it.map(Robot.hull::get andThen Map<Point, HullColor>::size))
        println(it.fullOutput())
    }

    println("\nPart 2")
    println(
        Robot(code = Day11.fileData, hull = mutableMapOf(Point(0, 0) to HullColor.White)).brainStep().fullOutput()
    )
}

