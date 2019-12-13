import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.firstOrNone
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.core.some
import intcode.CurrentState
import intcode.handleCodePoint
import intcode.toIntCodeProgram
import util.TwoD

data class PointL(
    override val x: Long,
    override val y: Long
) : TwoD<Long> {
    companion object
}

sealed class GameTile {
    object Empty : GameTile()
    object Wall : GameTile()
    object Block : GameTile()
    object Paddle : GameTile()
    object Ball : GameTile()
}

fun GameTile?.toGlyph() = when (this) {
    GameTile.Wall -> "▫️"
    GameTile.Block -> "🎁"
    GameTile.Paddle -> "🏓"
    GameTile.Ball -> "🏐"
    else -> "◾️"
}

fun Long.toGameTile(): GameTile {
    return when (this) {
        0L -> GameTile.Empty
        1L -> GameTile.Wall
        2L -> GameTile.Block
        3L -> GameTile.Paddle
        4L -> GameTile.Ball
        else -> throw Error("Bad GameTile index: $this")
    }
}

data class ArcadeGame(
    val code: MutableMap<Long, Long>,
    val screen: MutableMap<PointL, GameTile> = mutableMapOf(),
    val state: Either<String, CurrentState> = CurrentState().right(),
    val display: Option<Long> = Option.empty(),
    val ballPosition: Option<PointL> = Option.empty(),
    val paddlePosition: Option<PointL> = Option.empty()
)

fun ArcadeGame.drawSingleTile(state: Either.Right<CurrentState>): ArcadeGame =
    when (val p = PointL(state.b.output.pop(), state.b.output.pop())) {
        PointL(-1, 0) -> copy(display = state.b.output.pop().some())
        else -> {
            screen[p] = state.b.output.pop().toGameTile()
            this
        }
    }

tailrec fun ArcadeGame.draw(): ArcadeGame = when (state) {
    is Either.Left<String> -> this
    is Either.Right<CurrentState> -> when {
        state.b.output.size >= 3 -> drawSingleTile(state).draw()
        else -> this
    }
}

private fun <K, V : Any> MutableMap<K, V>.findFirst(paddle: V): Option<K> =
    filterValues(paddle::equals).keys.firstOrNone()

private fun Either<String, CurrentState>.withJoystickPosition(screen: MutableMap<PointL, GameTile>) = map {
    if (it.waitingForInput) {
        screen.findFirst(GameTile.Ball).map { ball ->
            screen.findFirst(GameTile.Paddle).map { paddle ->
                it.inputs.add(ball.x.compareTo(paddle.x).toLong())
            }
        }
    }
    it
}

object Day13 {
    private tailrec fun ArcadeGame.play(): Either<String, ArcadeGame> {
        return when (state) {
            is Either.Left<String> -> state
            is Either.Right<CurrentState> -> when (state.b.pointer) {
                is None -> right()
                is Some<Long> -> {
                    copy(
                        state = handleCodePoint(code, state.withJoystickPosition(screen))
                    ).draw().output().play()
                }
                else -> "Unknown error.".left()
            }
        }
    }

    private const val FILENAME = "src/main/resources/day13.txt"
    private val fileData = FILENAME.toIntCodeProgram()

    fun part1() {
        val game = ArcadeGame(code = fileData.toMutableMap())
        val finished = game.play()
        println(
            finished.fold({ "Problem: $it" }, { it.screen.filterValues { v -> v is GameTile.Block }.count() })
        )
    }

    fun part2() {
        val game = ArcadeGame(code = fileData.toMutableMap().apply { this[0] = 2 })
        val finished = game.play()
        println(
            finished.fold(
                { "Problem: $it" },
                { it.display.getOrElse { "No score displayed." } }
            )
        )
    }
}

private fun ArcadeGame.output(): ArcadeGame {
    val newBall = screen.findFirst(GameTile.Ball)
    val newPaddle = screen.findFirst(GameTile.Ball)
    if (newBall != ballPosition || newPaddle != paddlePosition) {
        printScreen()
    }
    return this.copy(ballPosition = newBall, paddlePosition = newPaddle)
}

fun main() {
    Day13.part1()
    Day13.part2()
}

fun ArcadeGame.printScreen() {
    val topLeft = PointL(
        screen.keys.map(PointL::x).min() ?: 0L,
        screen.keys.map(PointL::y).min() ?: 0L
    )
    val bottomRight = PointL(
        screen.keys.map(PointL::x).max() ?: 0L,
        screen.keys.map(PointL::y).max() ?: 0L
    )
    println(
        (topLeft.y..bottomRight.y).joinToString("\n") { y ->
            (topLeft.x..bottomRight.x).joinToString("") { x ->
                screen[PointL(x, y)].toGlyph()
            }
        }
    )
}
