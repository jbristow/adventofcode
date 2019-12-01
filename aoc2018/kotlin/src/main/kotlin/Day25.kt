import utility.Point4d
import utility.cartesianForEach
import utility.manhattanDistance
import java.io.File

object Day25 {

    @Suppress("unused")
    val sampleInput =
        "0,0,0,0\n3,0,0,0\n0,3,0,0\n0,0,3,0\n0,0,0,3\n0,0,0,6\n9,0,0,0\n12,0,0,0".lines()

    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day25.txt").readLines()


        val points = input.map {
            val ns = it.split(",").map(String::toLong)
            Point4d(ns[0], ns[1], ns[2], ns[3])

        }
        println("Answer 1: ${Day25.answer1(points)}")
    }

    data class DJSNode(
        var value: Point4d,
        var rank: Int = 0,
        var size: Int = 1
    ) {
        var parent = this
    }

    private fun union(x: DJSNode, y: DJSNode) {


        val xRoot = x.find()
        println("root for ${x.value} is $xRoot")

        val yRoot = y.find()

        if (xRoot != yRoot) {
            when {
                xRoot.size < yRoot.size -> {
                    xRoot.parent = yRoot
                    yRoot.size = yRoot.size + xRoot.size
                }
                else -> {
                    yRoot.parent = xRoot
                    xRoot.size = xRoot.size + yRoot.size
                }
            }

        }
    }

    private fun DJSNode.find(): DJSNode {
        val x = this
        if (x.parent != x) {
            x.parent = x.parent.find()
        }
        return x.parent
    }

    fun answer1(points: List<Point4d>): Int? {

        val map = points.map { it to DJSNode(it) }.toMap()
        points.cartesianForEach { a, b ->
            val dist = a.manhattanDistance(b)
            if (dist <= 3) {
                union(map[a]!!, map[b]!!)
            }
        }
        val roots = map.values.filter { it.parent == it }
        println("roots:\n${roots.joinToString("\n")}")
        return roots.count()
    }
}