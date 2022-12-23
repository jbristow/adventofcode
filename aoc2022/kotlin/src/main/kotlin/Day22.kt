import util.AdventOfCode
import util.Point2d
import util.Point2d.Companion.plus
import util.Point2dRange

object Day22 : AdventOfCode() {
    enum class Tile {
        Space, Rock
    }

    enum class Direction(val movement: Point2d) {
        Up(Point2d(0, -1)),
        Right(Point2d(1, 0)),
        Down(Point2d(0, 1)),
        Left(Point2d(-1, 0));
    }

    fun Direction.points(): Int {
        return when (this) {
            Direction.Right -> 0
            Direction.Down -> 1
            Direction.Left -> 2
            Direction.Up -> 3
        }
    }

    fun Direction.turn(turn: Instruction.Turn): Direction {
        return when (turn.direction) {
            "L" -> when (this) {
                Direction.Up -> Direction.Left
                Direction.Right -> Direction.Up
                Direction.Down -> Direction.Right
                Direction.Left -> Direction.Down
            }
            "R" -> when (this) {
                Direction.Up -> Direction.Right
                Direction.Right -> Direction.Down
                Direction.Down -> Direction.Left
                Direction.Left -> Direction.Up
            }
            else -> throw IllegalArgumentException("Wrong turn... $turn")
        }
    }

    val instructionRegex = """(\d+|[RL])""".toRegex()

    sealed interface Instruction {
        data class Move(val n: Int) : Instruction
        data class Turn(val direction: String) : Instruction
    }

    data class CubeTileMap(val map: Map<Int, Map<Point2d, Tile>>) {
        val ranges = map.mapValues { Point2dRange(it.value) }
        val topEdges = map.mapValues { (face, faceMap) ->
            ranges[face]!!.xRange.map { x ->
                faceMap.filter { (k, _) -> k.x == x }.minBy { (k, _) -> k.y }
            }.associate { it.key.x to it.key }
        }
        val rightEdges = map.mapValues { (face, faceMap) ->
            ranges[face]!!.yRange.map { y ->
                faceMap.filter { (k, _) -> k.y == y }.maxBy { (k, _) -> k.x }
            }.associate { it.key.y to it.key }
        }
        val bottomEdges = map.mapValues { (face, faceMap) ->
            ranges[face]!!.xRange.map { x ->
                faceMap.filter { (k, _) -> k.x == x }.maxBy { (k, _) -> k.y }
            }.associate { it.key.x to it.key }
        }
        val leftEdges = map.mapValues { (face, faceMap) ->
            ranges[face]!!.yRange.map { y ->
                faceMap.filter { (k, _) -> k.y == y }.minBy { (k, _) -> k.x }
            }.associate { it.key.y to it.key }
        }

        operator fun get(p: Int): Map<Point2d, Tile> {
            return map[p]!!
        }

        fun whichEdge(fromFace: Int, travel: Direction, p: Point2d): Int? {
            return when {
                travel == Direction.Up && p == topEdges[fromFace]!![p.x] -> when (fromFace) {
                    1 -> 6
                    2 -> 6
                    3 -> 1
                    4 -> 3
                    5 -> 3
                    6 -> 5
                    else -> throw IllegalArgumentException("bad face $fromFace")
                }
                travel == Direction.Right && p == rightEdges[fromFace]!![p.y] -> when (fromFace) {
                    1 -> 2
                    2 -> 4
                    3 -> 2
                    4 -> 2
                    5 -> 4
                    6 -> 4
                    else -> throw IllegalArgumentException("bad face $fromFace")
                }
                travel == Direction.Down && p == bottomEdges[fromFace]!![p.x] -> when (fromFace) {
                    1 -> 3
                    2 -> 3
                    3 -> 4
                    4 -> 6
                    5 -> 6
                    6 -> 2
                    else -> throw IllegalArgumentException("bad face $fromFace")
                }
                travel == Direction.Left && p == leftEdges[fromFace]!![p.y] -> when (fromFace) {
                    1 -> 5
                    2 -> 1
                    3 -> 5
                    4 -> 5
                    5 -> 1
                    6 -> 1
                    else -> throw IllegalArgumentException("bad face $fromFace")
                }
                else -> null
            }
        }

        fun getEdgeFun(fromFace: Int, toFace: Int): (Point2d) -> Pair<Direction, Point2d?> {
            return when (fromFace) {
                1 -> when (toFace) {
                    6 -> { p: Point2d -> Direction.Right to leftEdges[6]!![p.x + 100] } // (50,0),(100,50) -> (0,150),(0,200)
                    3 -> { p -> Direction.Down to topEdges[3]!![p.x] }
                    5 -> { p -> Direction.Right to leftEdges[5]!![151 - p.y] } // (0,0),(0,2),(0,50) -> (0,150),(0,148),(0,100)
                    2 -> { p -> Direction.Right to leftEdges[2]!![p.y] }
                    else -> throw IllegalArgumentException("Can't go from $fromFace to $toFace")
                }
                2 -> when (toFace) {
                    1 -> { p -> Direction.Left to rightEdges[1]!![p.y] }
                    3 -> { p -> Direction.Left to rightEdges[3]!![p.x - 50] } // (100,50),(150,50) ->(100,50),(100,100)
                    4 -> { p -> Direction.Left to rightEdges[4]!![151 - p.y] }
                    6 -> { p -> Direction.Up to bottomEdges[6]!![p.x - 100] } // (100,0),(150,0) -> (0,200),(50,200)
                    else -> throw IllegalArgumentException("Not done yet. $fromFace to $toFace")
                }
                3 -> when (toFace) {
                    1 -> { p -> Direction.Up to bottomEdges[1]!![p.x] }
                    2 -> { p -> Direction.Up to bottomEdges[2]!![p.y + 50] }
                    4 -> { p -> Direction.Down to topEdges[4]!![p.x] }
                    5 -> { p -> Direction.Down to topEdges[5]!![p.y - 50] }
                    else -> throw IllegalArgumentException("Not done yet. $fromFace to $toFace")
                }
                4 -> when (toFace) {
                    2 -> { p -> Direction.Left to rightEdges[2]!![151 - p.y] } // (100, 100)(100, 150) -> (150,50),(150,0)
                    3 -> { p -> Direction.Up to bottomEdges[3]!![p.x] }
                    5 -> { p -> Direction.Left to rightEdges[5]!![p.y] }
                    6 -> { p -> Direction.Left to rightEdges[6]!![p.x + 100] }
                    else -> throw IllegalArgumentException("Not done yet. $fromFace to $toFace")
                }

                5 -> when (toFace) {
                    1 -> { p -> Direction.Right to leftEdges[1]!![151 - p.y] } // 5=(1,150),(1,101)  1=(51,1)..(51,50)
                    3 -> { p -> Direction.Right to leftEdges[3]!![p.x + 50] }
                    4 -> { p -> Direction.Right to leftEdges[4]!![p.y] }
                    6 -> { p -> Direction.Down to topEdges[6]!![p.x] }
                    else -> throw IllegalArgumentException("Not done yet. $fromFace to $toFace")
                }
                6 -> when (toFace) {
                    1 -> { p -> Direction.Down to topEdges[1]!![p.y - 100] } // (0,150),(0,200)->(50,0),(100,0)
                    2 -> { p -> Direction.Down to topEdges[2]!![p.x + 100] }
                    4 -> { p -> Direction.Up to bottomEdges[4]!![p.y - 100] } // (50,150),(50,200) -> (50,150),(100,150)
                    5 -> { p -> Direction.Up to bottomEdges[5]!![p.x] }
                    else -> throw IllegalArgumentException("Not done yet. $fromFace to $toFace")
                }
                else -> throw IllegalArgumentException("Not done yet. $fromFace to $toFace")
            }
        }
    }

    data class TileMap(
        val map: Map<Point2d, Tile>
    ) {
        val prange = Point2dRange(map)

        val bottomEdge = findMinEdge()

        private fun findMinEdge() = prange.xRange.map { x -> map.filterKeys { it.x == x }.maxBy { (k, _) -> k.y } }
            .filter { map[it.key] != Tile.Rock }
            .associate { it.key.x to it.key }

        val topEdge = prange.xRange.map { x -> map.filterKeys { it.x == x }.minBy { (k, _) -> k.y } }
            .filter { map[it.key] != Tile.Rock }
            .associate { it.key.x to it.key }
        val leftEdge = prange.yRange.map { y -> map.filterKeys { it.y == y }.minBy { (k, _) -> k.x } }
            .filter { map[it.key] != Tile.Rock }
            .associate { it.key.y to it.key }
        val rightEdge = prange.yRange.map { y -> map.filterKeys { it.y == y }.maxBy { (k, _) -> k.x } }
            .filter { map[it.key] != Tile.Rock }
            .associate { it.key.y to it.key }

        fun getNeighbor(p: Point2d, facing: Direction): Point2d? {
            return when (facing) {
                Direction.Right -> if (rightEdge[p.y] == p) leftEdge[p.y] else p + facing.movement
                Direction.Left -> if (leftEdge[p.y] == p) rightEdge[p.y] else p + facing.movement
                Direction.Up -> if (topEdge[p.x] == p) bottomEdge[p.x] else p + facing.movement
                Direction.Down -> if (bottomEdge[p.x] == p) topEdge[p.x] else p + facing.movement
            }
        }

        operator fun get(p: Point2d): Tile? {
            return map[p]
        }
    }

    fun part1(input: String): Long {
        val (rawMap, rawInstructions) = input.split("\n\n")

        val map = rawMap.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '.' -> Point2d(1 + x, 1 + y) to Tile.Space
                    '#' -> Point2d(1 + x, 1 + y) to Tile.Rock
                    else -> null
                }
            }
        }.toMap()

        val instructions = instructionRegex.findAll(rawInstructions)
            .map {
                if (it.groupValues[1].first().isDigit()) {
                    Instruction.Move(it.groupValues[1].toInt())
                } else {
                    Instruction.Turn(it.groupValues[1])
                }
            }

        val tmap = TileMap(
            map = map
        )
        val (endpoint, facing) = trace(
            tmap,
            instructions.asSequence(),
            Direction.Right,
            tmap.topEdge.values.sortedBy { it.x }.find { it.y == 1 }!!
        )

        return 1000L * (endpoint.y) + 4L * (endpoint.x) + facing.points()
    }

    private tailrec fun trace(
        map: TileMap,
        instructions: Sequence<Instruction>,
        facing: Direction,
        location: Point2d
    ): Pair<Point2d, Direction> {
        val head = instructions.firstOrNull() ?: return location to facing

        val (newFacing, newLocation) = when (head) {
            is Instruction.Move -> facing to handleMovement(map, head.n, facing, location)
            is Instruction.Turn -> {
                facing.turn(head) to location
            }
        }

        return trace(map, instructions.drop(1), newFacing, newLocation)
    }

    private tailrec fun handleMovement(map: TileMap, n: Int, facing: Direction, location: Point2d): Point2d {
        if (n == 0) {
            return location
        }

        val nextLoc = map.getNeighbor(location, facing)

        return when {
            nextLoc == null || map[nextLoc] == Tile.Rock -> location
            else -> handleMovement(map, n - 1, facing, nextLoc)
        }
    }

    val sides = mapOf(
        1 to Point2dRange(xRange = 51..100, yRange = 1..50),
        2 to Point2dRange(xRange = 101..150, yRange = 1..50),
        3 to Point2dRange(xRange = 51..100, yRange = 51..100),
        4 to Point2dRange(xRange = 51..100, yRange = 101..150),
        5 to Point2dRange(xRange = 1..50, yRange = 101..150),
        6 to Point2dRange(xRange = 1..50, yRange = 151..200)
    )

    private fun part2(input: String): Long {
        val (rawMap, rawInstructions) = input.split("\n\n")
        val map = rawMap.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '.' -> Point2d(1 + x, 1 + y) to Tile.Space
                    '#' -> Point2d(1 + x, 1 + y) to Tile.Rock
                    else -> null
                }
            }
        }.toMap()
        val instructions = instructionRegex.findAll(rawInstructions)
            .map {
                if (it.groupValues[1].first().isDigit()) {
                    Instruction.Move(it.groupValues[1].toInt())
                } else {
                    Instruction.Turn(it.groupValues[1])
                }
            }
        val sideMaps = sides.mapValues { (_, v) -> map.filter { it.key in v } }
        val tmap = CubeTileMap(sideMaps)
        val (endpoint, facing) = traceCube(tmap, instructions, 1, Direction.Right, Point2d(51, 1))

        return 1000L * (endpoint.y) + 4L * (endpoint.x) + facing.points()
    }

    data class MoveTuple(val face: Int, val direction: Direction, val location: Point2d)

    private tailrec fun traceCube(
        map: CubeTileMap,
        instructions: Sequence<Instruction>,
        face: Int,
        direction: Direction,
        location: Point2d
    ): Pair<Point2d, Direction> {
        val head = instructions.firstOrNull() ?: return location to direction

        val (newFace, newDirection, newLocation) = when (head) {
            is Instruction.Move -> handleCubeMovement(map, head.n, face, direction, location)
            is Instruction.Turn -> {
                MoveTuple(face, direction.turn(head), location)
            }
        }

        return traceCube(map, instructions.drop(1), newFace, newDirection, newLocation)
    }

    private tailrec fun handleCubeMovement(
        map: CubeTileMap,
        n: Int,
        face: Int,
        direction: Direction,
        location: Point2d
    ): MoveTuple {
        if (n == 0) {
            return MoveTuple(face, direction, location)
        }
        val newFace = map.whichEdge(face, direction, location)
        when (newFace) {
            null -> {
                return if (map[face][location + direction.movement] == Tile.Rock) {
                    MoveTuple(face, direction, location)
                } else {
                    handleCubeMovement(map, n - 1, face, direction, location + direction.movement)
                }
            }
            else -> {
                val edgeFn = map.getEdgeFun(face, newFace)
                val (nextDirection, nextLoc) = edgeFn(location)
                return if (map[newFace][nextLoc!!] == Tile.Rock) {
                    MoveTuple(face, direction, location)
                } else {
                    handleCubeMovement(map, n - 1, newFace, nextDirection, nextLoc)
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 21")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
