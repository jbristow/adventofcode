import kroo.net.GifSequenceWriter
import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.plus
import util.Point2dRange
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.stream.FileImageOutputStream

object Day14Alt : AdventOfCode("day14.txt") {
    private var frameCounter: FrameCounter = FrameCounter()
    private val sandGenerator = Point2d(500, 0)
    private val down = Point2d(0, 1)
    private val downAndLeft = Point2d(-1, 1)
    private val downAndRight = Point2d(1, 1)

    private fun String.parseLine(): List<List<Point2d>> {
        return split(" -> ").map { point -> point.parsePoint() }.windowed(2)
    }

    private fun String.parsePoint(): Point2d {
        return split(",").let { (x, y) -> Point2d(x.toInt(), y.toInt()) }
    }

    sealed interface Disposition {
        class Settled(val location: Point2d) : Disposition
        object Void : Disposition
    }

    data class FrameCounter(var count: Int = 0, val renderEveryNth: Int = 1000) {
        operator fun plus(b: Int): FrameCounter {
            count += b
            return this
        }

        fun shouldRender(): Boolean {
            return count % renderEveryNth == 0
        }
    }

    private tailrec fun GifSequenceWriter.dropSand(
        rocks: Set<Point2d>,
        maxY: Int,
        sand: MutableSet<Point2d> = mutableSetOf()
    ): Int {
        return when (val restingPoint = this.sandFalls(Point2d(500, 0), rocks, sand, maxY)) {
            is Disposition.Void -> {
                val finalMap = Point2dRange(rocks + sand)
                println("${finalMap.xRange} || ${finalMap.yRange}")
                repeat(100) {
                    frameCounter = frameCounter.copy(renderEveryNth = 1)
                    this.renderGifFrame(rocks, sand)
                }
                sand.size
            }
            is Disposition.Settled -> {
                sand.add(restingPoint.location)

//                this.renderGifFrame(restingPoint.location, rocks, sand)
                dropSand(rocks, maxY, sand)
            }
        }
    }

    private val fallDirs = listOf(down, downAndLeft, downAndRight)

    private var xRange = 0..1
    private var yRange = 0..1
    private fun GifSequenceWriter.renderGifFrame(
        rocks: Set<Point2d>,
        sand: Set<Point2d>,
        curLoc: Point2d? = null
    ) {
        if (frameCounter.shouldRender()) {
            val sqSize = 4
            val bimage =
                BufferedImage(
                    10 + (xRange.last - xRange.first) * sqSize,
                    10 + yRange.last * sqSize,
                    BufferedImage.TYPE_INT_RGB
                ).floodFill(0x333333)
            val graphics = bimage.graphics
            rocks.forEach {
                graphics.color = Color(0x999999)
                graphics.fillRect(5 + (it.x - xRange.first) * sqSize, 5 + it.y * sqSize, sqSize, sqSize)
            }
            sand.forEach {
                graphics.color = Color(0xC2, 0xB2, 0x80)
                graphics.fillRect(5 + (it.x - xRange.first) * sqSize, 5 + it.y * sqSize, sqSize, sqSize)
            }
            if (curLoc != null) {
                graphics.color = Color.RED
                graphics.fillRect(5 + (curLoc.x - xRange.first) * sqSize, 5 + curLoc.y * sqSize, sqSize, sqSize)
            }
            writeToSequence(bimage)
        }
        frameCounter += 1
    }

    private tailrec fun GifSequenceWriter.sandFalls(
        location: Point2d,
        rocks: Set<Point2d>,
        sand: Set<Point2d>,
        maxY: Int
    ): Disposition {
        if (location.y >= maxY) {
            return Disposition.Void
        }

        this.renderGifFrame(rocks, sand, location)
        return when (val nextLocation = fallDirs.map { it + location }.firstOrNull { it !in rocks && it !in sand }) {
            null -> Disposition.Settled(location)
            else -> sandFalls(nextLocation, rocks, sand, maxY)
        }
    }

    private tailrec fun GifSequenceWriter.sandFallsFloor(
        location: Point2d,
        rocks: Set<Point2d>,
        sand: Set<Point2d>,
        maxY: Int
    ): Point2d {
        this.renderGifFrame(rocks, sand, location)
        return when (
            val nextLoc =
                fallDirs.map { it + location }.firstOrNull { it !in rocks && it !in sand && it.y < maxY }
        ) {
            null -> location
            else -> sandFallsFloor(nextLoc, rocks, sand, maxY)
        }
    }

    private tailrec fun GifSequenceWriter.dropSandFloor(
        rocks: Set<Point2d>,
        maxY: Int,
        sand: MutableSet<Point2d> = mutableSetOf()
    ): Int {
        if (sandGenerator in rocks) {
            val finalMap = Point2dRange(rocks + sand)
            frameCounter = frameCounter.copy(renderEveryNth = 1)
            repeat(100) {
                this.renderGifFrame(rocks, sand)
            }
            println("${finalMap.xRange} || ${finalMap.yRange}")
            return sand.size
        }
        val restingPoint = sandFallsFloor(sandGenerator, rocks, sand, maxY)
        sand.add(restingPoint)
        return dropSandFloor(rocks + restingPoint, maxY, sand)
    }

    fun part1(input: Sequence<String>): Int {
        val rocks = input.flatMap { it.parseLine() }
            .flatMap { Point2dRange(it).toList() }
            .toSet()
        val rockRange = Point2dRange(rocks)
        val result: Int
        xRange = (rockRange.xRange.first - 1)..(rockRange.xRange.last + 1)
        yRange = 0..rockRange.yRange.last + 1
        val timestamp: String =
            LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                .replace(Regex("""\W"""), "")
        FileImageOutputStream(File("day14-part1-output-$timestamp.gif")).use { output ->
            GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 100, true).use { writer ->
                frameCounter = FrameCounter(renderEveryNth = rockRange.yRange.last / 10)
                result = writer.dropSand(rocks, rockRange.yRange.last)
            }
        }
        return result
    }

    fun part2(input: Sequence<String>): Int {
        val rocks = input.flatMap { it.parseLine() }
            .flatMap { Point2dRange(it).toList() }
            .toSet()
        val rockRange = Point2dRange(rocks)

        xRange =
            (rockRange.xRange.first - rockRange.yRange.last - 2)..(rockRange.xRange.last + rockRange.yRange.last + 2)
        yRange = 0..rockRange.yRange.last + 2

        val result: Int
        val timestamp: String =
            LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                .replace(Regex("""\W"""), "")
        FileImageOutputStream(File("day14-part2-output-$timestamp.gif")).use { output ->
            GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 20, true).use { writer ->
                frameCounter = FrameCounter(renderEveryNth = yRange.last * 100)
                result = writer.dropSandFloor(rocks, rockRange.yRange.last + 2)
            }
        }
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 14")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }
}
