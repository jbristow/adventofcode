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
        println("\tPart 2: ${part2(inputFileString)}")
    }

    private fun part2(inputFileString: String): Int {
        val (mazeInput, dirInput) = inputFileString.split("\n\n")
        val bigMazeInput =
            mazeInput
                .replace("#", "##")
                .replace("O", "[]")
                .replace(".", "..")
                .replace("@", "@.")
        val boxes =
            bigMazeInput
                .lines()
                .flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, c ->
                        when (c) {
                            '[' -> Point2d(x, y) to BigBox.BigBoxLeft
                            ']' -> Point2d(x, y) to BigBox.BigBoxRight
                            else -> null
                        }
                    }
                }.toMap()
        val start =
            bigMazeInput
                .lines()
                .mapIndexedNotNull { y, line ->
                    line
                        .mapIndexedNotNull { x, c ->
                            when (c) {
                                '@' -> Point2d(x, y)
                                else -> null
                            }
                        }.firstOrNull()
                }.first()
        val walls =
            bigMazeInput
                .lines()
                .flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, c ->
                        when (c) {
                            '#' -> Point2d(x, y) to Wall
                            else -> null
                        }
                    }
                }.toMap()
        val directions =
            dirInput.lines().flatMap { line ->
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
        return output.toList().filter { it.second is BigBox.BigBoxLeft }.sumOf { (k, _) -> k.y * 100 + k.x }
    }

    private fun part1(inputFileString: String): Int {
        val (mazeInput, dirInput) = inputFileString.split("\n\n")
        val boxes =
            mazeInput
                .lines()
                .flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, c ->
                        when (c) {
                            'O' -> Point2d(x, y) to Box
                            else -> null
                        }
                    }
                }.toMap()
        val start =
            mazeInput
                .lines()
                .mapIndexedNotNull { y, line ->
                    line
                        .mapIndexedNotNull { x, c ->
                            when (c) {
                                '@' -> Point2d(x, y)
                                else -> null
                            }
                        }.firstOrNull()
                }.first()
        val walls =
            mazeInput
                .lines()
                .flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, c ->
                        when (c) {
                            '#' -> Point2d(x, y) to Wall
                            else -> null
                        }
                    }
                }.toMap()
        val directions =
            dirInput.lines().flatMap { line ->
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

        val (canMove, movedBoxes) =
            if (nextPos in boxes) {
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
        if (directions.isEmpty()) {
            return boxes to start
        }

        val currMove = directions.first()
        val nextPos = start + currMove.offset

        val (canMove, movedBoxes) =
            if (nextPos in boxes) {
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
        } else if (direction is Right && (nextPos + direction.offset) in walls) {
            return false to boxes
        } else if (direction is Left && (nextPos + direction.offset) in walls) {
            return false to boxes
        } else if (direction is Right && (nextPos + direction.offset) !in boxes) {
            val mBoxesF = boxes.toMutableMap()
            mBoxesF.remove(position)
            mBoxesF[nextPos] = BigBox.BigBoxLeft
            mBoxesF[nextPos + direction.offset] = BigBox.BigBoxRight
            return true to mBoxesF
        } else if (direction is Left && (nextPos + direction.offset) !in boxes) {
            val mBoxesF = boxes.toMutableMap()
            mBoxesF.remove(position)
            mBoxesF[nextPos] = BigBox.BigBoxRight
            mBoxesF[nextPos + direction.offset] = BigBox.BigBoxLeft
            return true to mBoxesF
        } else if (direction is Right) {
            val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, nextPos + direction.offset, direction)
            if (canMove) {
                val mBoxesF = mBoxes.toMutableMap()
                mBoxesF.remove(position)
                mBoxesF[nextPos] = BigBox.BigBoxLeft
                mBoxesF[nextPos + direction.offset] = BigBox.BigBoxRight
                return true to mBoxesF
            }
        } else if (direction is Left) {
            val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, nextPos + direction.offset, direction)
            if (canMove) {
                val mBoxesF = mBoxes.toMutableMap()
                mBoxesF.remove(position)
                mBoxesF[nextPos] = BigBox.BigBoxRight
                mBoxesF[nextPos + direction.offset] = BigBox.BigBoxLeft
                return true to mBoxesF
            }
        } else {
            val current = boxes.getValue(position)
            val (lpos, rpos) =
                if (current is BigBox.BigBoxLeft) {
                    position to (position + Right.offset)
                } else {
                    (position + Left.offset) to position
                }
            val (nextLpos, nextRpos) =
                if (current is BigBox.BigBoxLeft) {
                    (position + direction.offset) to (position + Right.offset + direction.offset)
                } else {
                    (position + Left.offset + direction.offset) to (position + direction.offset)
                }
            if (nextLpos in walls || nextRpos in walls) {
                return false to boxes
            }
            if (nextLpos !in boxes && nextRpos !in boxes) {
                val mBoxesF = boxes.toMutableMap()
                mBoxesF.remove(lpos)
                mBoxesF.remove(rpos)
                mBoxesF[nextLpos] = BigBox.BigBoxLeft
                mBoxesF[nextRpos] = BigBox.BigBoxRight
                return true to mBoxesF
            }

            if (boxes[nextLpos] is BigBox.BigBoxLeft && boxes[nextRpos] is BigBox.BigBoxRight) {
                val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, nextLpos, direction)
                if (canMove) {
                    val mBoxesF = mBoxes.toMutableMap()
                    mBoxesF.remove(lpos)
                    mBoxesF.remove(rpos)
                    mBoxesF[nextLpos] = BigBox.BigBoxLeft
                    mBoxesF[nextRpos] = BigBox.BigBoxRight
                    return true to mBoxesF
                } else {
                    return false to boxes
                }
            }

            if (nextLpos in boxes && nextRpos !in boxes) {
                val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, nextLpos, direction)
                if (canMove) {
                    val mBoxesF = mBoxes.toMutableMap()
                    mBoxesF.remove(lpos)
                    mBoxesF.remove(rpos)
                    mBoxesF[nextLpos] = BigBox.BigBoxLeft
                    mBoxesF[nextRpos] = BigBox.BigBoxRight
                    return true to mBoxesF
                } else {
                    return false to boxes
                }
            }
            if (nextLpos !in boxes && nextRpos in boxes) {
                val (canMove, mBoxes) = tryMoveBigBoxes(boxes, walls, nextRpos, direction)
                if (canMove) {
                    val mBoxesF = mBoxes.toMutableMap()
                    mBoxesF.remove(lpos)
                    mBoxesF.remove(rpos)
                    mBoxesF[nextLpos] = BigBox.BigBoxLeft
                    mBoxesF[nextRpos] = BigBox.BigBoxRight
                    return true to mBoxesF
                } else {
                    return false to boxes
                }
            }
            val (canMoveL, mBoxesL) = tryMoveBigBoxes(boxes, walls, nextLpos, direction)
            if (canMoveL) {
                val (canMoveR, mBoxesR) = tryMoveBigBoxes(mBoxesL, walls, nextRpos, direction)
                if (canMoveR) {
                    val mBoxesF = mBoxesR.toMutableMap()
                    mBoxesF.remove(lpos)
                    mBoxesF.remove(rpos)
                    mBoxesF[nextLpos] = BigBox.BigBoxLeft
                    mBoxesF[nextRpos] = BigBox.BigBoxRight
                    return true to mBoxesF
                } else {
                    return false to boxes
                }
            }

            return false to boxes
        }
        return false to boxes
    }
}
