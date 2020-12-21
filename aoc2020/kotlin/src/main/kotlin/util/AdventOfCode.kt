package util

import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant

abstract class AdventOfCode {
    private val fileName: String =
        "src/main/resources/${this::class.simpleName!!.toLowerCase()}.txt"

    private val inputFile = Paths.get(fileName)
    val inputFileLines: List<String>
        get() = Files.readAllLines(inputFile)
    val inputFileString: String
        get() = Files.readString(inputFile)
    val inputFileInts: List<Int>
        get() = inputFileLines.map(String::toInt)
    val inputFileLongs: List<Long>
        get() = inputFileLines.map(String::toLong)

    fun timed(function: () -> Unit) {
        val start = Instant.now()
        function()
        println("Took: ${start.elapsed()}")
    }

    private fun Instant.elapsed(): String {
        val el = Instant.now().toEpochMilli() - this.toEpochMilli()
        val ms = el % 1000
        val sec = (el / 1000) % 60
        val minutes = (el / 1000 / 60)
        val msStr = "${ms}ms"
        val secStr = if (el > 1000) {
            "${sec}s"
        } else {
            ""
        }
        val minStr = if (el > 1000 * 60) {
            "${minutes}m"
        } else {
            ""
        }
        return listOf(minStr, secStr, msStr).filter { it.isNotBlank() }.joinToString(" ")
    }
}
