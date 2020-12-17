package util

import java.nio.file.Files
import java.nio.file.Paths

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
}
