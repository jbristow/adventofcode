import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kroo.net.GifSequenceWriter
import utility.Point
import utility.drawImage
import utility.head
import utility.tail
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.stream.FileImageOutputStream

enum class Choice {
    LEFT {
        override fun makeChoice(cart: Cart) = when (cart.direction) {
            Direction.RIGHT -> cart.nextPosition(Direction.UP, Choice.LEFT)
            Direction.LEFT -> cart.nextPosition(Direction.DOWN, Choice.LEFT)
            Direction.UP -> cart.nextPosition(Direction.LEFT, Choice.LEFT)
            Direction.DOWN -> cart.nextPosition(Direction.RIGHT, Choice.LEFT)
        }
    },
    RIGHT {
        override fun makeChoice(cart: Cart) = when (cart.direction) {
            Direction.LEFT -> cart.nextPosition(Direction.UP, Choice.RIGHT)
            Direction.RIGHT -> cart.nextPosition(Direction.DOWN, Choice.RIGHT)
            Direction.UP -> cart.nextPosition(Direction.RIGHT, Choice.RIGHT)
            Direction.DOWN -> cart.nextPosition(Direction.LEFT, Choice.RIGHT)
        }
    },
    STRAIGHT {
        override fun makeChoice(cart: Cart) =
            when (cart.direction) {
                Direction.RIGHT, Direction.LEFT ->
                    cart.nextPosition(cart.direction, Choice.STRAIGHT)
                Direction.DOWN, Direction.UP ->
                    cart.nextPosition(cart.direction, Choice.STRAIGHT)
            }
    };

    abstract fun makeChoice(cart: Cart): Cart
}

enum class Direction(val char: Char) {
    UP('^') {
        override fun turnBack() = LEFT
        override fun turnForward() = RIGHT
        override fun move(loc: Point) = Point(loc.x, loc.y - 1)
    },
    DOWN('v') {
        override fun turnBack() = RIGHT
        override fun turnForward() = LEFT
        override fun move(loc: Point) = Point(loc.x, loc.y + 1)
    },
    LEFT('<') {
        override fun turnBack() = UP
        override fun turnForward() = DOWN
        override fun move(loc: Point) = Point(loc.x - 1, loc.y)
    },
    RIGHT('>') {
        override fun turnBack() = DOWN
        override fun turnForward() = UP
        override fun move(loc: Point) = Point(loc.x + 1, loc.y)
    };

    abstract fun move(loc: Point): Point
    abstract fun turnBack(): Direction
    abstract fun turnForward(): Direction

    override fun toString(): String = this.char.toString()
}

fun Char.toDirection(): Direction? {
    return Direction.values().find { it.char == this }
}

fun String.toDirection(): Direction? {
    require(this.count() == 1) { "Directions can only be parsed from single char strings" }
    return first().toDirection()
}

private fun <E> Direction?.whenNotNull(function: (Direction) -> E): E? = when {
    this != null -> function(this)
    else -> null
}

class OffTheRailsException(cart: Point) : Exception("Off the rails! $cart")

data class Cart(
    val loc: Point,
    val direction: Direction,
    val lastChoice: Choice?,
    val id: Int
) : Comparable<Cart> {
    override fun compareTo(other: Cart) =
        when (val ycomp = y.compareTo(other.y)) {
            0 -> x.compareTo(other.x)
            else -> ycomp
        }

    constructor(loc: Point, direction: Direction, lastChoice: Choice?) : this(
        loc,
        direction,
        lastChoice,
        loc.hashCode() + direction.hashCode()
    )

}

private fun Cart.nextPosition(direction: Direction, lastChoice: Choice?) =
    Cart(direction.move(loc), direction, lastChoice, id)

private fun Cart.nextPosition(direction: Direction) =
    Cart(direction.move(loc), direction, lastChoice, id)

// '\'
private fun Cart.turnBackCorner() = nextPosition(direction.turnBack())

// '/'
private fun Cart.turnForwardCorner() = nextPosition(direction.turnForward())


val Cart.x: Int get() = loc.x
val Cart.y: Int get() = loc.y

fun Cart.move(tracks: List<String>) = when (tracks[y][x]) {
    '|', '^', 'v' -> nextPosition(
        direction
    )
    '-', '<', '>' -> nextPosition(
        direction
    )
    '\\' -> turnBackCorner()
    '/' -> turnForwardCorner()
    '+' -> intersection()
    else -> throw OffTheRailsException(loc)
}

private fun Cart.intersection() = when (lastChoice) {
    null, Choice.RIGHT -> Choice.LEFT.makeChoice(this)
    Choice.STRAIGHT -> Choice.RIGHT.makeChoice(this)
    Choice.LEFT -> Choice.STRAIGHT.makeChoice(this)
}

object Day13 {


    private val timestamp: String =
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            .replace(Regex("""\W"""), "")

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val input = File("src/main/resources/day13.txt").readLines()
            val trackLayer = drawRails(input)
            println("Answer 1: ${Day13.answer1(input, trackLayer)}");
            println("Answer 2: ${Day13.answer2(input, trackLayer)}");
        }
    }

    private suspend fun answer1(
        input: List<String>,
        trackLayer: BufferedImage
    ) =

        FileImageOutputStream(File("day13-output-$timestamp.gif")).use { output ->
            GifSequenceWriter(output, TYPE_INT_ARGB, 10, false).use { writer ->
                val channel = Channel<Image>()
                val retVal = GlobalScope.async {
                    val x = step(
                        0,
                        input.findCarts(),
                        input,
                        channel,
                        trackLayer.width to trackLayer.height
                    )
                    channel.close()
                    x
                }
                for (image in channel) {
                    val bgl = BufferedImage(
                        trackLayer.width,
                        trackLayer.height,
                        TYPE_INT_ARGB
                    )
                    bgl.graphics.color = Color(0x00, 0x00, 0x00, 0xff)
                    bgl.graphics.drawRect(
                        0,
                        0,
                        trackLayer.width,
                        trackLayer.height
                    )
                    bgl.drawImage(trackLayer).drawImage(image)
                    bgl.flush()
                    writer.writeToSequence(bgl)
                }

                retVal.await()
            }
        }


    private suspend fun answer2(
        input: List<String>,
        trackLayer: BufferedImage
    ) =
        FileImageOutputStream(File("day13-output-part2-$timestamp.gif")).use { output ->
            GifSequenceWriter(output, TYPE_INT_ARGB, 10, false).use { writer ->
                val channel = Channel<Image>()
                val retVal = GlobalScope.async {
                    demolitionStep(
                        0,
                        input.findCarts(),
                        input,
                        channel,
                        trackLayer.width to trackLayer.height
                    ).let {
                        channel.close()
                        it
                    }
                }
                for (image in channel) {
                    val bgl = BufferedImage(
                        trackLayer.width,
                        trackLayer.height,
                        TYPE_INT_ARGB
                    )
                    bgl.graphics.color = Color(0x00, 0x00, 0x00, 0xff)
                    bgl.graphics.drawRect(
                        0,
                        0,
                        trackLayer.width,
                        trackLayer.height
                    )
                    bgl.drawImage(trackLayer).drawImage(image)
                    bgl.flush()
                    writer.writeToSequence(bgl)
                }
                retVal.await()
            }
        }

    tailrec suspend fun demolitionStep(
        i: Int,
        carts: List<Cart>,
        tracks: List<String>,
        channel: Channel<Image>,
        imgDim: Pair<Int, Int>
    ): Point {
        val (nextCarts, collisions) = moveCarts(
            carts = carts.sorted(),
            tracks = tracks
        )
        channel.send(
            drawCarts(
                imgDim.first,
                imgDim.second,
                nextCarts + collisions
            )
        )
        return when {
            carts.count() == 1 -> carts.first().loc
            else ->
                demolitionStep(
                    i + 1,
                    carts,
                    tracks,
                    channel,
                    imgDim
                )
        }
    }

    tailrec fun moveCarts(
        carts: List<Cart>,
        moved: List<Cart> = emptyList(),
        collided: List<Cart> = emptyList(),
        tracks: List<String>
    ): Pair<List<Cart>, List<Cart>> {

        if (carts.isEmpty()) return moved to collided

        val h = carts.head.move(tracks)
        val (ucRem, cRem) = carts.tail.splitBy { it.loc == h.loc }
        val (ucMoved, cMoved) = moved.splitBy { it.loc == h.loc }

        return when {
            cRem.isEmpty() && cMoved.isEmpty() ->
                moveCarts(ucRem, ucMoved + h, collided, tracks)
            cRem.isEmpty() ->
                moveCarts(ucRem, ucMoved, collided + h + cMoved, tracks)
            cMoved.isEmpty() ->
                moveCarts(ucRem, ucMoved, collided + h + cRem, tracks)
            else ->
                moveCarts(ucRem, ucMoved, collided + h + cRem + cMoved, tracks)
        }

    }

    tailrec suspend fun step(
        i: Int,
        carts: List<Cart>,
        tracks: List<String>,
        channel: Channel<Image>,
        imgDim: Pair<Int, Int>
    ): Point {
        val (nextCarts, collisions) =
                moveCarts(carts = carts.sorted(), tracks = tracks)

        channel.send(
            drawCarts(imgDim.first, imgDim.second, nextCarts + collisions)
        )
        return when {
            collisions.isEmpty() -> step(
                i + 1,
                nextCarts,
                tracks,
                channel,
                imgDim
            )
            else -> {
                collisions.first().loc
            }
        }
    }

    private fun drawCarts(
        width: Int,
        height: Int,
        cartSet: List<Cart>
    ): BufferedImage {
        val image = BufferedImage(width, height, TYPE_INT_ARGB)

        val graphics = image.graphics
        graphics.drawRect(
            0,
            0,
            width,
            height
        )

        cartSet.groupBy { it.loc }.forEach { (point, carts) ->
            val (x, y) = point
            if (carts.count() > 1) {
                graphics.drawCrash(x, y)
            } else {
                graphics.drawCart(x, y)
            }
        }
        return image
    }

    private fun drawRails(tracks: List<String>): BufferedImage {

        val image = BufferedImage(
            tracks.first().count() * 6,
            tracks.count() * 6,
            TYPE_INT_ARGB
        )
        val graphics = image.graphics
        graphics.color = Color(0x00, 0x00, 0x00, 0xff)
        graphics.drawRect(0, 0, tracks.first().count() * 6, tracks.count() * 6)
        tracks.forEachIndexed { y, xs ->
            xs.forEachIndexed { x, c ->
                when (c) {
                    '|', 'v', '^' -> graphics.drawVertTrack(x, y)
                    '-', '>', '<' -> graphics.drawHorizTrack(x, y)
                    '+' -> graphics.drawCrossTrack(x, y)
                    '/' -> when {
                        (y == 0) -> graphics.drawTopLeftCornerTrack(x, y)
                        (x == 0) -> graphics.drawTopLeftCornerTrack(x, y)
                        (tracks[y - 1].count() > x && (tracks[y - 1][x] in """|+v^""")) && (x == 0 || (tracks[y][x - 1] in """-+><""")) ->
                            graphics.drawBtmRightCornerTrack(x, y)
                        else ->
                            graphics.drawTopLeftCornerTrack(x, y)
                    }
                    '\\' -> when {
                        (y == 0) -> graphics.drawTopRightCornerTrack(x, y)
                        (tracks.count() - y > 1) && (tracks[y + 1][x] in "|+v^") ->
                            graphics.drawTopRightCornerTrack(x, y)
                        else ->
                            graphics.drawBtmLeftCornerTrack(x, y)
                    }
                }

            }
        }
        return image
    }

    private fun Graphics.drawVertTrack(x: Int, y: Int): Graphics {
        val prevColor = color
        color = Color(0x66, 0x66, 0x66, 0xff)
        fillRect(x * 6 + 3, y * 6, 2, 6)
        color = prevColor
        return this
    }

    private fun Graphics.drawTopRightCornerTrack(
        x: Int,
        y: Int
    ): Graphics {
        val prevColor = color
        color = Color(0x66, 0x66, 0x66, 0xff)
        fillRect(x * 6 + 3, y * 6 + 3, 2, 4)
        fillRect(x * 6, y * 6 + 3, 4, 2)
        color = prevColor
        return this
    }


    private fun Graphics.drawTopLeftCornerTrack(
        x: Int,
        y: Int
    ): Graphics {
        val prevColor = color
        color = Color(0x66, 0x66, 0x66, 0xff)
        fillRect(x * 6 + 3, y * 6 + 3, 2, 4)
        fillRect(x * 6 + 3, y * 6 + 3, 4, 2)
        color = prevColor
        return this
    }

    private fun Graphics.drawBtmLeftCornerTrack(
        x: Int,
        y: Int
    ): Graphics {
        val prevColor = color
        color = Color(0x66, 0x66, 0x66, 0xff)
        fillRect(x * 6 + 3, y * 6, 2, 4)
        fillRect(x * 6 + 3, y * 6 + 3, 4, 2)
        color = prevColor
        return this
    }

    private fun Graphics.drawBtmRightCornerTrack(
        x: Int,
        y: Int
    ): Graphics {
        val prevColor = color
        color = Color(0x66, 0x66, 0x66, 0xff)
        fillRect(x * 6 + 3, y * 6, 2, 4)
        fillRect(x * 6, y * 6 + 3, 4, 2)
        color = prevColor
        return this
    }

    private fun Graphics.drawHorizTrack(x: Int, y: Int): Graphics {
        val prevColor = color
        color = Color(0x66, 0x66, 0x66, 0xff)
        fillRect(x * 6, y * 6 + 3, 6, 2)
        color = prevColor
        return this
    }

    private fun Graphics.drawCrossTrack(x: Int, y: Int): Graphics {
        return drawVertTrack(x, y).drawHorizTrack(x, y)
    }

    private fun Graphics.drawCart(x: Int, y: Int): Graphics {
        val prevColor = color
        color = Color(0xdd, 0xdd, 0x66, 0xff)
        fillRect(x * 6 + 2, y * 6 + 2, 4, 4)
        color = prevColor
        return this
    }

    private fun Graphics.drawCrash(x: Int, y: Int): Graphics {
        val prevColor = color
        color = Color(0xff, 0x99, 0x00, 0xff)
        fillOval(x * 6, y * 6, 6, 6)
        color = Color(0xff, 0x66, 0x00, 0xff)
        fillOval(x * 6 + 2, y * 6 + 2, 2, 2)
        color = Color(0xff, 0x00, 0x00, 0xff)
        drawOval(x * 6 - 2, y * 6 - 2, 10, 10)
        drawOval(x * 6 + 1, y * 6 + 1, 4, 4)
        color = prevColor
        return this
    }
}

private fun <E> List<E>.splitBy(predicate: (E) -> Boolean) =
    groupBy(predicate).let {
        (it[false] ?: emptyList()) to (it[true] ?: emptyList())
    }

private fun List<String>.findCarts() =
    mapIndexed { y, xl ->
        xl.mapIndexedNotNull { x, c ->
            c.toDirection().whenNotNull {
                Cart(Point(x, y), it, null)
            }
        }
    }.flatten()





