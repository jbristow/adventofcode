import utility.Point
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object Day20 {

    @Suppress("unused")
    const val sampleInput1 = """^WNE$"""

    @Suppress("unused")
    const val sampleInput2 = """^ENWWW(NEEE|SSE(EE|N))$"""

    @Suppress("unused")
    const val sampleInput3 = """^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"""

    @Suppress("unused")
    const val sampleInput4 =
        """^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))${'$'}"""

    @Suppress("unused")
    const val sampleInput5 = """^W(N|S)E$"""

    @JvmStatic
    fun main(args: Array<String>) {

        val input =
            Files.readAllLines(Paths.get("src/main/resources/day20.txt"))
                .first()
        val maze = explode(
            input.asSequence(),
            emptySet(),
            setOf(Point(0, 0)),
            LinkedList(),
            setOf(Point(0, 0)),
            emptySet()
        )
        val distances = findAllDistances(
            listOf(Point(0, 0)),
            1,
            maze,
            mapOf(Point(0, 0) to 0)
        )
        println("answer1: ${Day20.answer1(distances)}")

        println("answer2: ${Day20.answer2(distances)}")
    }


    private fun answer1(input: Map<Point, Int>) = input.values.max()
    private fun answer2(input: Map<Point, Int>) =
        input.values.count { it >= 1000 }

    private fun Char.move(p: Point) = when (this) {
        'N' -> p.up
        'S' -> p.down
        'E' -> p.right
        'W' -> p.left
        else -> throw Exception("Illegal direction `$this`")
    }

    private fun Char.makeDoor(p: Point) = Door(
        p, this.move(p)
    )

    private tailrec fun explode(
        paths: Sequence<Char>,
        maze: Set<Door>,
        pos: Set<Point>,
        stack: LinkedList<Pair<Set<Point>, Set<Point>>>,
        starts: Set<Point>,
        ends: Set<Point>
    ): Set<Door> {

        val c = paths.first()
        val remaining = paths.drop(1)
        return when (c) {
            '$' -> maze
            '|' -> {
                explode(remaining, maze, starts, stack, starts, ends + pos)
            }
            in "NESW" -> {
                explode(
                    remaining,
                    maze + pos.map { c.makeDoor(it) },
                    pos.map { p -> c.move(p) }.toSet(),
                    stack,
                    starts,
                    ends
                )
            }
            '(' -> {
                stack.push(starts to ends)

                explode(remaining, maze, pos, stack, pos, emptySet())
            }
            ')' -> {
                val (s, e) = stack.pop()
                explode(remaining, maze, pos + ends, stack, s, e)
            }
            else -> {
                explode(remaining, maze, pos, stack, starts, ends)
            }
        }
    }


    private tailrec fun findAllDistances(
        points: List<Point>,
        dist: Int,
        doors: Set<Day20.Door>,
        map: Map<Point, Int>
    ): Map<Point, Int> {
        if (doors.isEmpty()) {
            return map
        }

        val nextPoints = points.flatMap { point ->
            doors.filter { it.connectsTo(point) }
                .map { it.getOtherRoom(point) }
        }

        val nextDists = nextPoints.map { room ->
            room to when {
                map[room] == null -> dist
                map[room]!! < dist -> map[room]!!
                else -> dist
            }
        }

        return findAllDistances(
            nextPoints,
            dist + 1,
            doors.filter { door -> points.none { door.connectsTo(it) } }.toSet(),
            map + nextDists
        )
    }

    class Door(val a: Point, val b: Point) {
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Door) return false
            return (a == other.a && b == other.b) || (a == other.b && b == other.a)
        }

        override fun hashCode(): Int {
            return a.hashCode() + b.hashCode()
        }

        override fun toString(): String {
            return "$a to $b"
        }
    }


    private fun Day20.Door.getOtherRoom(point: Point): Point {
        return when (point) {
            a -> b
            b -> a
            else -> throw Exception("Door $this does not connect to $point")
        }
    }

    private fun Day20.Door.connectsTo(point: Point): Boolean {
        return this.a == point || this.b == point
    }
}