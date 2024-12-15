import Day14.Robot
import kroo.net.GifSequenceWriter
import util.AdventOfCode
import util.Point2d
import util.Point2dRange
import util.floodFill
import java.awt.Color
import java.awt.Stroke
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.stream.ImageOutputStream

object Day14 : AdventOfCode() {

    val width = 101
    val height = 103

    data class Robot(val id: Int, val position: Point2d, val velocity: Point2d) {
        fun move(): Robot {
            var newPosition = addVelocityAndClip(position, velocity)
            return this.copy(position = newPosition)
        }

        private fun addVelocityAndClip(p: Point2d, v: Point2d): Point2d {
            var newPosition = p + v
            if (newPosition.x >= width) {
                newPosition -= Point2d(width, 0)
            } else if (newPosition.x < 0) {
                newPosition += Point2d(width, 0)
            }
            if (newPosition.y >= height) {
                newPosition -= Point2d(0, height)
            } else if (newPosition.y < 0) {
                newPosition += Point2d(0, height)
            }
            return newPosition
        }

        fun timeUntilPosition(p: Point2d): Int? {
            // pi + v*t
            val seen = mutableSetOf<Point2d>()
            var curr = this.position
            var count = 0
            while (curr !in seen) {
                if (curr == p) {
                    return count
                }
                seen.add(curr)
                curr = addVelocityAndClip(curr, velocity)
                count += 1
            }
            return null

        }

    }

    fun part1(input: List<String>): Long {
        val robots = parse(input)

        val moved = (0 until 100).fold(robots) { acc, curr ->
            acc.map { it.move() }
        }


        println(
            moved.count { robot -> robot.position.x in 0 until 50 && robot.position.y in 0 until 51 }
                .toLong() * moved.count { robot -> robot.position.x in 51 until 101 && robot.position.y in 0 until 51 }.toLong() *

                moved.count { robot -> robot.position.x in 0 until 50 && robot.position.y in 52 until 103 } *
                moved.count { robot -> robot.position.x in 51 until 101 && robot.position.y in 52 until 103 },
        )
        return -1
    }

    private fun parse(string: List<String>): List<Robot> {
        //p=39,0 v=-91,50
        return string.mapIndexed { i, line ->
            """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex().matchEntire(line)!!.groupValues.let { (_, px, py, vx, vy) ->
                Robot(i, Point2d(px.toInt(), py.toInt()), Point2d(vx.toInt(), vy.toInt()))
            }
        }
    }

    fun part2(input: List<String>): Long {
        val robots = parse(input)

        var checkY = 0
        var checkX = 0
        var curr = robots
        val gsw = GifSequenceWriter(
            outputStream = FileImageOutputStream(File("day14_60.gif")),
            imageType = BufferedImage.TYPE_INT_RGB,
            msBtwnFrames = 16,
            loopContinuously = true,
        )
        val range = Point2dRange(0 until width, 0 until height)
        repeat(7687) {
            drawRobots(curr, gsw)
            curr = curr.map { it.move() }
        }
        repeat(120) {
            drawRobots(curr, gsw)
        }
        gsw.close()

        return -1
    }

    private fun drawRobots(curr: List<Robot>, gsw: GifSequenceWriter) {
        var curr1 = curr
        val image = BufferedImage(width * 4, height * 4, BufferedImage.TYPE_INT_RGB)
        val graphics = image.createGraphics()
        graphics.color = Color(16, 16, 23)
        graphics.fillRect(0, 0, width, height)
        curr1.forEach { r ->
            graphics.color = Color(0, 153, 0)
            graphics.fillRect(r.position.x * 4, r.position.y * 4, 4, 4)
        }
        gsw.writeToSequence(image)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 14")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}

tailrec fun Robot.move(i: Int): Robot {
    return if (i > 0) {
        this.move().move(i - 1)
    } else {
        this
    }
}
