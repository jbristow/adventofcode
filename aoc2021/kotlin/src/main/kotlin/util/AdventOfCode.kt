package util

import java.io.File
import java.time.Instant

abstract class AdventOfCode {
    private val fileName: String =
        "src/main/resources/${this::class.simpleName!!.lowercase()}.txt"
    private val file: File = File(fileName)

    val inputFileLines: List<String>
        get() = file.readLines()
    val inputFileString: String
        get() = file.readText()
    val inputFileInts: List<Int>
        get() = inputFileLines.map(String::toInt)
    val inputFileLongs: List<Long>
        get() = inputFileLines.map(String::toLong)

    fun <T:Any?> timed(function: () -> T): T {
        val start = Instant.now()
        val value = function()
        println("Took: ${start.elapsed()}")
        return value
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
