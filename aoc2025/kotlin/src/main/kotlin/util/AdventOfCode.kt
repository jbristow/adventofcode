package util

import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.readLines
import kotlin.streams.asSequence

private const val ONE_THOUSAND = 1000

private const val SIXTY = 60

abstract class AdventOfCode {
    private val fileName: String
    private val file: Path

    constructor() {
        fileName = "src/main/resources/${this::class.simpleName!!.lowercase()}.txt"
        file = Path.of(fileName)
    }

    constructor(f: String) {
        fileName = "src/main/resources/$f"
        file = Path.of(fileName)
    }

    val inputFileLines: List<String>
        get() = file.readLines()
    val inputFileLineSequence: Sequence<String>
        get() = Files.lines(file).asSequence()
    val inputFileString: String
        get() = Files.readString(file)
    val inputFileInts: List<Int>
        get() = inputFileLines.map(String::toInt)
    val inputFileLongs: List<Long>
        get() = inputFileLines.map(String::toLong)

    fun <T : Any?> timed(function: () -> T): T {
        val start = Instant.now()
        val value = function()
        println("Took: ${start.elapsed()}")
        return value
    }

    private fun Instant.elapsed(): String {
        val el = Instant.now().toEpochMilli() - this.toEpochMilli()
        val ms = el % ONE_THOUSAND
        val sec = (el / ONE_THOUSAND) % SIXTY
        val minutes = (el / ONE_THOUSAND / SIXTY)
        val msStr = "${ms}ms"
        val secStr =
            if (el > ONE_THOUSAND) {
                "${sec}s"
            } else {
                ""
            }
        val minStr =
            if (el > ONE_THOUSAND * SIXTY) {
                "${minutes}m"
            } else {
                ""
            }
        return listOf(minStr, secStr, msStr).filter { it.isNotBlank() }.joinToString(" ")
    }
}
