import arrow.core.right
import util.*

object Day15 : AdventOfCode() {
    data object Box
    data object Wall
    sealed class BigBox {
        data object BigBoxLeft : BigBox()
        data object BigBoxRight : BigBox()
    }

    var sample3 = """#######
#...#.#
#.....#
#..OO@#
#..O..#
#.....#
#######

<vv<<^^<<^^"""

    val sample2 = """########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

<^^>>>vv<v>>v<<"""
    val sample = """##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^"""

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 15")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(sample3)}")
    }

    private fun part2(inputFileString: String): Int {
        val (mazeInput, dirInput) = inputFileString.split("\n\n")
        val bigMazeInput = mazeInput.replace("#", "##")
            .replace("O", "[]")
            .replace(".", "..")
            .replace("@", "@.")
        val boxes = bigMazeInput
            .lines().flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, c ->
                    when (c) {
                        '[' -> Point2d(x, y) to BigBox.BigBoxLeft
                        ']' -> Point2d(x, y) to BigBox.BigBoxRight
                        else -> null
                    }
                }
            }.toMap()
        val start = bigMazeInput.lines().mapIndexedNotNull { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '@' -> Point2d(x, y)
                    else -> null
                }
            }.firstOrNull()
        }.first()
        val walls = bigMazeInput.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> Point2d(x, y) to Wall
                    else -> null
                }
            }
        }.toMap()
        val directions = dirInput.lines().flatMap { line ->
            line.mapNotNull { c ->
                when (c) {
                    '^' -> Up
                    '>' -> Right
                    'v' -> Down
                    '<' -> Left
                    else -> null
                }
            }
        }

        val (output, end) = runBigProgram(directions, walls, boxes, start)

        println(
            Point2dRange(walls).joinToString {
                when {
                    it in walls -> "#"
                    it in boxes && boxes.getValue(it) is BigBox.BigBoxLeft -> "["
                    it in boxes && boxes.getValue(it) is BigBox.BigBoxRight -> "]"
                    it == end -> "@"
                    else -> "."
                }
            },
        )
        return output.toList().filter { it.second is BigBox.BigBoxLeft }.sumOf { (k, _) -> k.y * 100 + k.x }
    }

    private fun part1(inputFileString: String): Int {
        val (mazeInput, dirInput) = inputFileString.split("\n\n")
        val boxes = mazeInput.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    'O' -> Point2d(x, y) to Box
                    else -> null
                }
            }
        }.toMap()
        val start = mazeInput.lines().mapIndexedNotNull { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '@' -> Point2d(x, y)
                    else -> null
                }
            }.firstOrNull()
        }.first()
        val walls = mazeInput.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> Point2d(x, y) to Wall
                    else -> null
                }
            }
        }.toMap()
        val directions = dirInput.lines().flatMap { line ->
            line.mapNotNull { c ->
                when (c) {
                    '^' -> Up
                    '>' -> Right
                    'v' -> Down
                    '<' -> Left
                    else -> null
                }
            }
        }

        val (output, _) = runProgram(directions, walls, boxes, start)

        return output.toList().sumOf { (k, _) -> k.y * 100 + k.x }
    }

    private tailrec fun runProgram(
        directions: List<Direction>,
        walls: Map<Point2d, Wall>,
        boxes: Map<Point2d, Box>,
        start: Point2d,
    ): Pair<Map<Point2d, Box>, Point2d> {
        if (directions.isEmpty()) {
            return boxes to start
        }

        val currMove = directions.first()
        val nextPos = start + currMove.offset

        val (canMove, movedBoxes) = if (nextPos in boxes) {
            tryMoveBoxes(boxes, walls, nextPos, currMove)
        } else if (nextPos in walls) {
            false to boxes
        } else {
            true to boxes
        }
        return if (canMove) {
            runProgram(directions.drop(1), walls, movedBoxes, nextPos)
        } else {
            runProgram(directions.drop(1), walls, movedBoxes, start)
        }
    }

    private tailrec fun runBigProgram(
        directions: List<Direction>,
        walls: Map<Point2d, Wall>,
        boxes: Map<Point2d, BigBox>,
        start: Point2d,
    ): Pair<Map<Point2d, BigBox>, Point2d> {
        println(
            Point2dRange(walls).joinToString {
                when {
                    it in walls -> "#"
                    it in boxes && boxes.getValue(it) is BigBox.BigBoxLeft -> "["
                    it in boxes && boxes.getValue(it) is BigBox.BigBoxRight -> "]"
                    it == start -> "@"
                    else -> "."
                }
            },
        )
        if (directions.isEmpty()) {
            return boxes to start
        }

        val currMove = directions.first()
        val nextPos = start + currMove.offset

        val (canMove, movedBoxes) = if (nextPos in boxes) {
            tryMoveBigBoxes(boxes, walls, nextPos, currMove)
        } else if (nextPos in walls) {
            false to boxes
        } else {
            true to boxes
        }
        return if (canMove) {
            runBigProgram(directions.drop(1), walls, movedBoxes, nextPos)
        } else {
            runBigProgram(directions.drop(1), walls, movedBoxes, start)
        }
    }

    fun tryMoveBoxes(
        boxes: Map<Point2d, Box>,
        walls: Map<Point2d, Wall>,
        position: Point2d,
        direction: Direction,
    ): Pair<Boolean, Map<Point2d, Box>> {
        val nextPos = position + direction.offset
        return if (nextPos in boxes) {
            val (canMove, mBoxes) = tryMoveBoxes(boxes, walls, nextPos, direction)
            if (canMove) {
                val mBoxes2 = mBoxes.toMutableMap()
                mBoxes2.remove(position)
                mBoxes2[nextPos] = Box
                true to mBoxes2
            } else {
                false to mBoxes
            }
        } else if (nextPos in walls) {
            false to boxes
        } else {
            val mBoxes = boxes.toMutableMap()
            mBoxes.remove(position)
            mBoxes[nextPos] = Box
            true to mBoxes
        }

    }

    fun tryMoveBigBoxes(
        boxes: Map<Point2d, BigBox>,
        walls: Map<Point2d, Wall>,
        position: Point2d,
        direction: Direction,
    ): Pair<Boolean, Map<Point2d, BigBox>> {
        val nextPos = position + direction.offset



        if (nextPos in walls) {
            return false to boxes
        }
        if (direction is Right && (nextPos+direction.offset) !in boxes) {
            val mBoxesF = boxes.toMutableMap()
            mBoxesF.remove(position)
            mBoxesF[position+direction.offset]=BigBox.BigBoxLeft
            mBoxesF[position+(direction.offset*2)]=BigBox.BigBoxRight
            return true to mBoxesF
        }
        if (direction is Left && (nextPos+direction.offset) !in boxes) {
            val mBoxesF = boxes.toMutableMap()
            mBoxesF.remove(position)
            mBoxesF[nextPos]=BigBox.BigBoxRight
            mBoxesF[nextPos+direction.offset]=BigBox.BigBoxLeft
            return true to mBoxesF
        }

        if (direction is Right) {
            val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, nextPos+Right.offset, direction)
            if (canMove) {
                val mBoxesF = mBoxes.toMutableMap()
                mBoxesF.remove(position)
                mBoxesF[position+Right.offset]=BigBox.BigBoxLeft
                mBoxesF[position+(Right.offset*2)]=BigBox.BigBoxRight
                return true to mBoxesF
            }
        } else if (direction is Left) {
            val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, position+(Left.offset*2), direction)
            if (canMove) {
                val mBoxesF = mBoxes.toMutableMap()
                mBoxesF.remove(position)
                mBoxesF[position+Left.offset]=BigBox.BigBoxRight
                mBoxesF[position+(Left.offset*2)]=BigBox.BigBoxLeft
                return true to mBoxesF
            }
        }
        return false to boxes

    }
}
