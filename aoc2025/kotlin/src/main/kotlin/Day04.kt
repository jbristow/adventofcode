import util.AdventOfCode
import util.Point2d

object Day04 : AdventOfCode() {
    private const val EASILY_ACCESSIBLE = 4

    private fun List<String>.toPaperInventory() =
        flatMapIndexed { y, row -> row.mapIndexed { x, c -> Point2d(x, y) to c } }
            .filter { (_, c) -> c == '@' }
            .map { (p, _) -> p }
            .toSet()

    private fun Point2d.isAccessible(inventory: Set<Point2d>) = neighbors.count { it in inventory } < EASILY_ACCESSIBLE

    private fun part1(input: List<String>): Int {
        val inventory = input.toPaperInventory()
        return inventory.count { it.isAccessible(inventory) }
    }

    private tailrec fun removeAccessible(
        inventory: Set<Point2d>,
        removed: Long = 0,
    ): Long {
        val currentCount = inventory.size
        val nextInventory = inventory.filterNot { it.isAccessible(inventory) }.toSet()

        val nextRemoved = currentCount - nextInventory.size.toLong()
        if (nextRemoved == 0L) {
            return removed
        }
        return removeAccessible(nextInventory, removed + nextRemoved)
    }

    private fun part2(input: List<String>) = removeAccessible(input.toPaperInventory())

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 4")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
