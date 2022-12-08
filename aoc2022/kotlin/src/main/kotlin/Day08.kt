import util.AdventOfCode
import util.Point2d
import util.Point2dRange

object Day08 : AdventOfCode() {

    data class Forest(val trees: Map<Point2d, Int>, val width: Int, val height: Int) {
        operator fun get(point: Point2d): Int? {
            return trees[point]
        }

        val points: Point2dRange = Point2dRange(xRange = 1 until width - 1, yRange = 1 until height - 1)

        fun treesToTheRightOf(point: Point2d) =
            ((point.x + 1) until width).mapNotNull { x -> trees[point.copy(x = x)] }

        fun treesToTheLeftOf(point: Point2d) =
            (0 until point.x).reversed().mapNotNull { x -> trees[point.copy(x = x)] }

        fun treesAbove(point: Point2d) = (0 until point.y).reversed().mapNotNull { y -> trees[point.copy(y = y)] }
        fun treesBelow(point: Point2d) = ((point.y + 1) until height).mapNotNull { y -> trees[point.copy(y = y)] }
    }

    private fun plantTrees(input: List<String>): Forest {
        val width = input.first().length
        val height = input.size

        val trees = Point2dRange(
            xRange = 0 until width,
            yRange = 0 until height
        ).associateWith { input[it.y][it.x].digitToInt() }
        return Forest(trees, width, height)
    }

    private fun part1(input: List<String>): Int {
        val forest = plantTrees(input)
        val visible = forest.points.filter { point ->
            val currTree = forest[point]!!
            forest.treesToTheRightOf(point).all { it < currTree } ||
                forest.treesToTheLeftOf(point).all { it < currTree } ||
                forest.treesAbove(point).all { it < currTree } ||
                forest.treesBelow(point).all { it < currTree }
        }
        return visible.count() + 2 * (forest.height + forest.width - 2)
    }

    private tailrec fun countVisible(treeHouse: Int, trees: List<Int>, count: Int = 0): Int {
        if (trees.isEmpty()) {
            return count
        }
        val head = trees.first()
        if (head >= treeHouse) {
            return count + 1
        }
        return countVisible(treeHouse, trees.drop(1), count + 1)
    }

    private fun part2(input: List<String>): Int {
        val forest = plantTrees(input)
        return forest.points.maxOf { point ->
            val treeHouse = forest[point]!!
            val countRight = countVisible(treeHouse, forest.treesToTheRightOf(point))
            val countLeft = countVisible(treeHouse, forest.treesToTheLeftOf(point))
            val countUp = countVisible(treeHouse, forest.treesAbove(point))
            val countDown = countVisible(treeHouse, forest.treesBelow(point))
            countRight * countLeft * countUp * countDown
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
