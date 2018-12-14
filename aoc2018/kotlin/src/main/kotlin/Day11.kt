import utility.Point

object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {

        val input = 9005
        println("answer1: ${Day11.answer1(input)}")
        println("answer2: ${Day11.answer2(input)}")
    }

    fun powerLevel(serial: Int, point: Point): Int {
        return ((point.x + 10) * point.y + serial) * (point.x + 10) / 100 % 10 - 5
    }

    private fun answer1(input: Int): Point {
        return (2..299).map { x ->
            (2..299).map { y ->
                Point(x - 1, y - 1) to powerLevel(input, Point(x - 1, y - 1)) +
                        powerLevel(input, Point(x - 1, y)) +
                        powerLevel(input, Point(x - 1, y + 1)) +
                        powerLevel(input, Point(x, y - 1)) +
                        powerLevel(input, Point(x, y)) +
                        powerLevel(input, Point(x, y + 1)) +
                        powerLevel(input, Point(x + 1, y - 1)) +
                        powerLevel(input, Point(x + 1, y)) +
                        powerLevel(input, Point(x + 1, y + 1))
            }.maxBy {
                it.second
            }!!
        }.maxBy { it.second }!!.first
    }

    private tailrec fun findBox(
        size: Int,
        maxSize: Int,
        maxLoc: Point,
        maxPower: Int,
        maxPowerSize: Int,
        grid: List<List<Int>>
    ): List<Int> {
        if (size > maxSize) {
            return listOf(maxLoc.x, maxLoc.y, maxPowerSize, maxPower)
        }
        println("findBox: $size, $maxSize, $maxLoc, $maxPower, $maxPowerSize")
        val biggest = (1..(301 - size)).map { x ->
            (1..(301 - size)).map { y ->
                Point(x, y) to grid.drop(y - 1).take(size)
                    .sumBy { it.drop(x - 1).take(size).sum() }
            }.maxBy {
                it.second
            }!!
        }.maxBy { it.second }!!
        return if (biggest.second > maxPower) {
            findBox(
                size + 1,
                maxSize,
                biggest.first,
                biggest.second,
                size,
                grid
            )
        } else {
            findBox(size + 1, maxSize, maxLoc, maxPower, maxPowerSize, grid)
        }
    }

    private fun answer2(input: Int): List<Int> {
        return findBox(1, 300, Point(-1, -1), -6, -1, fillGrid(input))
    }

    fun fillGrid(input: Int): List<List<Int>> {
        return (1..300).map { y ->
            (1..300).map { x ->
                powerLevel(input, Point(x, y))
            }
        }
    }
}