import util.AdventOfCode

object Day02 : AdventOfCode() {
    sealed class RpsMove(val points: Int) {
        fun willBeat(mine: RpsMove) = this.beats == mine
        fun willLoseTo(mine: RpsMove) = this.losesTo == mine

        val beats: RpsMove
            get() = when (this) {
                Rock -> Scissors
                Paper -> Rock
                Scissors -> Paper
            }

        val losesTo: RpsMove
            get() = when (this) {
                Rock -> Paper
                Paper -> Scissors
                Scissors -> Rock
            }

        object Rock : RpsMove(1)
        object Paper : RpsMove(2)
        object Scissors : RpsMove(3)
    }

    private fun String.toRpsMove() = when (this) {
        "A", "X" -> RpsMove.Rock
        "B", "Y" -> RpsMove.Paper
        "C", "Z" -> RpsMove.Scissors
        else -> throw IllegalArgumentException("`$this` is not a valid move.")
    }

    private fun score(theirs: RpsMove, mine: RpsMove) =
        mine.points + when {
            theirs.willBeat(mine) -> 0
            theirs.willLoseTo(mine) -> 6
            else -> 3
        }

    private fun scoreByResult(theirs: RpsMove, result: String) =
        when (result) {
            "X" -> theirs.beats.points
            "Y" -> 3 + theirs.points
            "Z" -> 6 + theirs.losesTo.points
            else -> throw IllegalArgumentException()
        }


    fun part1(input: List<String>) =
        input.map { line -> line.split(" ").map { it.toRpsMove() } }
            .sumOf { (theirs, mine) -> score(theirs, mine) }

    fun part2(input: List<String>) =
        input.map { it.split(" ") }
            .sumOf { (theirs, outcome) -> scoreByResult(theirs.toRpsMove(), outcome) }


    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 2")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
