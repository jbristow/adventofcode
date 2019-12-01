import kroo.net.GifSequenceWriter
import utility.floodFill
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.stream.FileImageOutputStream


object Day12 {

    private val timestamp: String =
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            .replace(Regex("""\W"""), "")

    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day12.txt").readLines()
        val initial =
            "...${input.first().drop("initial state: ".count())}.........."
        val rules =
            input.drop(2).map {
                val parts = it.split(""" => """)
                parts[0] to parts[1]
            }.toMap()

        println("answer1: ${Day12.answer1(initial, rules)}")
        println("answer2: ${Day12.answer2(initial, rules)}")

        FileImageOutputStream(File("day12-output-$timestamp.gif")).use { output ->
            GifSequenceWriter(output, TYPE_INT_RGB, 100, true).use { writer ->
                toTerminal(0, 200, initial, rules, writer)
            }
        }


    }

    private fun answer2(initial: String, rules: Map<String, String>) =
        generateSequence(0L) { it + 1 }
            .mapIndexed { i, it ->
                calculatePlants(
                    step(0, it.toInt(), initial, rules)
                ).let { plants ->
                    plants + (50000000000L - it) * 86
                }
            }
            .windowed(2, 1)
            .dropWhile { it[0] != it[1] }
            .first()
            .first()


    private fun answer1(initial: String, rules: Map<String, String>): Int {
        val final = step(0, 20, initial, rules)
        return calculatePlants(final)

    }

    private fun calculatePlants(final: String) =
        final.mapIndexed { a, b -> a to b }
            .filter { (a, b) -> b == '#' }.sumBy { (first) ->
                first + -3
            }

    private tailrec fun step(
        n: Int,
        max: Int,
        current: String,
        rules: Map<String, String>
    ): String =
        if (n >= max) {
            current
        } else {
            step(
                n + 1,
                max,
                "..$current..."
                    .windowed(5, 1)
                    .joinToString("") { rules[it] ?: "." },
                rules
            )
        }


    private tailrec fun toTerminal(
        n: Int,
        max: Int,
        current: String,
        rules: Map<String, String>,
        writer: GifSequenceWriter

    ) {
        //println("%3d: ".format(n) + current)
        renderGifFrame(current, writer)

        if (n < max) {
            toTerminal(
                n + 1,
                max,
                "..$current..."
                    .windowed(5, 1)
                    .joinToString("") { rules[it] ?: "." },
                rules,
                writer
            )
        } else {
            repeat(9) {
                renderGifFrame(current, writer)
            }
        }
    }

    private fun renderGifFrame(
        current: String,
        writer: GifSequenceWriter
    ) {
        val bimage =
            BufferedImage(1920, 60, TYPE_INT_RGB).floodFill(0x666666)

        val graphics = bimage.graphics
        val window = current.take((1920 - 10) / 15)

        (window + (".".repeat((1920 - 10) / 15 - window.count()))).forEachIndexed { i, it ->
            when (it) {
                '#' -> {
                    graphics.color = Color(0, 0x66, 0)
                    graphics.fillRect(5 + i * 15, 5, 10, 50)
                    graphics.color = Color(0, 0xcc, 0)
                    graphics.drawRect(5 + i * 15, 5, 10, 50)
                }
                else -> {
                    graphics.color = Color(0x77, 0x55, 0x33)
                    graphics.fillRect(5 + i * 15, 5, 10, 50)
                    graphics.color = Color(0x99, 0x77, 0x55)
                    graphics.drawRect(5 + i * 15, 5, 10, 50)

                }
            }

        }
        writer.writeToSequence(bimage)
    }

    private fun renderGifFrame2(
        current: String,
        writer: GifSequenceWriter
    ) {
        val bimage =
            BufferedImage(1920, 1080, TYPE_INT_RGB).floodFill(0x666666)

        val graphics = bimage.graphics
        current.take((1920 - 10) / 15).forEachIndexed { i, it ->
            graphics.color = Color(0, 0x66, 0)
            if (it == '#') graphics.fillRect(5 + i * 15, 5, 90, 90)
            graphics.color = Color(0, 0xcc, 0)
            if (it == '#') graphics.drawRect(5 + i * 15, 5, 10, 90)

        }
        writer.writeToSequence(bimage)
    }
}