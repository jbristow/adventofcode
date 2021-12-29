package util

import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.asSequence

abstract class AdventOfCode() {
    private val fileName: String = "../resources/${this::class.simpleName!!.lowercase()}.input"
    private val file: Path = Path.of(fileName)

    val inputFileLineSequence: Sequence<String>
        get() = Files.lines(Path.of(fileName)).asSequence()
    val inputFileLines: List<String>
        get() = Files.readAllLines(Path.of(fileName))
    val inputFile: String
        get() = Files.readString(Path.of(fileName))
}
