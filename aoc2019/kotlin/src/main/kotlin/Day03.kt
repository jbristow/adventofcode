import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs

object Day03 {

    data class Instruction(val direction: String, val count: Int)

    data class Wire(val lastLoc: Point, val locations: LinkedList<Point>)

    private fun String.makeInstruction(): Instruction = Instruction(take(1), drop(1).toInt())

    const val FILENAME = "src/main/resources/day03.txt"

    private fun String.processLine(): List<Instruction> = split(",").map { it.makeInstruction() }

    private fun List<String>.toWires(): List<Wire> {
        return map { line ->
            line.processLine()
                .fold(Wire(Point(0, 0), LinkedList())) { wire, instr ->
                    val newLocs = when (instr.direction) {
                        "U" -> (wire.lastLoc.y + 1..wire.lastLoc.y + instr.count).map { Point(wire.lastLoc.x, it) }
                        "D" -> (wire.lastLoc.y - instr.count until wire.lastLoc.y).reversed().map { Point(wire.lastLoc.x, it) }
                        "R" -> (wire.lastLoc.x + 1..wire.lastLoc.x + instr.count).map { Point(it, wire.lastLoc.y) }
                        "L" -> (wire.lastLoc.x - instr.count until wire.lastLoc.x).reversed().map { Point(it, wire.lastLoc.y) }
                        else -> throw Error("bad instruction $instr")
                    }
                    wire.locations.addAll(newLocs)
                    Wire(newLocs.last(), wire.locations)
                }
        }
    }

    private val wires =
        Files.readAllLines(Paths.get(FILENAME)).toWires().map { it.locations }

    private val wiresAsSet = wires.map { it.toSet() }

    private fun List<Set<Point>>.overlaps(): Set<Point> {
        return this[0].intersect(this[1].toSet())
    }

    private fun Set<Point>.calculateShortest(wires0: LinkedList<Point>, wires1: LinkedList<Point>): String {
        return map { match ->
            wires0.takeWhile { p -> p != match } to wires1.takeWhile { p -> p != match }
        }.minBy { it.first.size + it.second.size }!!
            .let {
                it.first.size + it.second.size + 2
            }.toString()
    }

    fun part1() =
        wiresAsSet.overlaps().map { abs(it.x.toDouble()) + abs(it.y.toDouble()) }.min()!!.toInt().toString()

    fun part2() = wiresAsSet.overlaps()
        .calculateShortest(wires[0], wires[1])

    fun draw(input: List<String>, filename: String) {
        val ws = input.toWires()
        val wsl = ws.map { it.locations }
        val minX = wsl.map { it.map { p -> p.x }.min()!! }.min()!!
        val maxX = wsl.map { it.map { p -> p.x }.max()!! }.max()!!
        val minY = wsl.map { it.map { p -> p.y }.min()!! }.min()!!
        val maxY = wsl.map { it.map { p -> p.y }.max()!! }.max()!!
        println("$minX,$minY - $maxX,$maxY")

        val field = mutableMapOf<Point, String>()

        input.forEach { line ->

            line.processLine().fold(Wire(Point(0, 0), LinkedList())) { wire, instr ->
                val newLocs = when (instr.direction) {
                    "U" -> (wire.lastLoc.y + 1..wire.lastLoc.y + instr.count).map { Point(wire.lastLoc.x, it) }
                    "D" -> (wire.lastLoc.y - instr.count until wire.lastLoc.y).reversed().map { Point(wire.lastLoc.x, it) }
                    "R" -> (wire.lastLoc.x + 1..wire.lastLoc.x + instr.count).map { Point(it, wire.lastLoc.y) }
                    "L" -> (wire.lastLoc.x - instr.count until wire.lastLoc.x).reversed().map { Point(it, wire.lastLoc.y) }
                    else -> throw Error("bad instruction $instr")
                }
                newLocs.forEach { p ->
                    if (field[p] != null) {
                        field[p] = "X"
                    } else {
                        field[p] = when (instr.direction) {
                            "U", "D" -> "|"
                            "L", "R" -> "-"
                            else -> "."
                        }
                    }
                }
                field[newLocs.last()] = "+"
                Wire(newLocs.last(), wire.locations)
            }
        }

        field[Point(0, 0)] = "o"

        println(field.size)
        PrintWriter(Files.newBufferedWriter(Paths.get("output-day03-$filename.txt"))).use { out ->
            (minY..maxY).reversed().forEach { y ->
                out.println((minX..maxX).joinToString("") { x ->
                    field[Point(x, y)] ?: "."
                })
            }
        }
    }
}

fun main() {
    println("Part 1: ${Day03.part1()}")
    println("Part 2: ${Day03.part2()}")
    println()
    Day03.draw(
        listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"),
        "example2"
    )
    Day03.draw(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"), "example1")
//    Day03.draw(Files.readAllLines(Paths.get(Day03.FILENAME)), "full")
}
