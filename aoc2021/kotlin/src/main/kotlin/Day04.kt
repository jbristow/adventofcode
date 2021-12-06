import util.AdventOfCode
import util.Point2d

object Day04 : AdventOfCode() {
    class BingoBoard(numbers: List<Int>) {
        private val numberLocs: Map<Int, Point2d>
        private val flipped: Map<Point2d, Int>
        private val hitLocs: MutableMap<Point2d, Boolean> = mutableMapOf()

        private val hasWinningColumn: Boolean
            get() = (0 until 5).any { x -> (0 until 5).all { y -> hitLocs[Point2d(x, y)] ?: false } }

        private val hasWinningRow: Boolean
            get() = (0 until 5).any { y -> (0 until 5).all { x -> hitLocs[Point2d(x, y)] ?: false } }

        val hasWon: Boolean
            get() = hitLocs.size >= 5 && (hasWinningColumn || hasWinningRow)

        val sum: Int
            get() = flipped.filterKeys { it !in hitLocs }.values.sum()

        init {
            numberLocs = (0 until 5).flatMap { x ->
                (0 until 5).map { y ->
                    numbers[y * 5 + x] to Point2d(x, y)
                }
            }.toMap()
            flipped = numberLocs.map { (k, v) -> v to k }.toMap()
        }

        fun recordBall(ball: Int) {
            if (ball in numberLocs) {
                val ballLoc = numberLocs[ball]!!
                hitLocs[ballLoc] = true
            }
        }
    }

    fun List<String>.extractBalls() = first().split(",").map { it.toInt() }
    fun List<String>.extractBingoBoards() =
        drop(2).joinToString("\n").split("\n\n").map { rawBoard ->
            val numbers = rawBoard.lines().flatMap { boardLine ->
                boardLine.trim().split(Regex("""\s+""")).map { it.toInt() }
            }
            BingoBoard(numbers)
        }

    private tailrec fun playGameToWin(
        balls: List<Int>,
        boards: List<BingoBoard>,
        lastBall: Int = -1
    ): Pair<Int, BingoBoard> {
        val winner = boards.find { it.hasWon }
        if (winner != null) {
            return lastBall to winner
        } else if (balls.isEmpty()) {
            throw Exception("Could not find a winner.")
        }

        boards.forEach { it.recordBall(balls.first()) }
        return playGameToWin(balls.drop(1), boards, balls.first())
    }

    fun part1(balls: List<Int>, boards: List<BingoBoard>): Int {
        val (lastBall, winner) = playGameToWin(balls, boards)
        return winner.sum * lastBall
    }

    private tailrec fun playGameToLose(
        balls: List<Int>,
        boards: List<BingoBoard>,
        lastBall: Int = -1,
        lastWinningBoards: List<BingoBoard> = listOf()
    ): Pair<Int, BingoBoard> {
        if (boards.isEmpty()) {
            return lastBall to lastWinningBoards.first() // problem statement claims there will be only one
        } else if (balls.isEmpty() || boards.isEmpty()) {
            throw Exception("Could not find a winner.")
        }

        boards.forEach { it.recordBall(balls.first()) }

        val boardsByStatus = boards.groupBy { it.hasWon }
        val winners = boardsByStatus[true] ?: listOf()
        val losers = boardsByStatus[false] ?: listOf()

        return playGameToLose(
            balls.drop(1),
            losers,
            balls.first(),
            winners
        )
    }

    fun part2(balls: List<Int>, boards: List<BingoBoard>): Int {
        val (lastBall, winner) = playGameToLose(balls, boards)
        return winner.sum * lastBall
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = inputFileLines.toList()
        val balls = input.extractBalls()
        val boards = input.extractBingoBoards()

        println("Day 4")
        println("\tPart 1: ${part1(balls, boards)}")
        println("\tPart 2: ${part2(balls, boards)}")
    }
}
