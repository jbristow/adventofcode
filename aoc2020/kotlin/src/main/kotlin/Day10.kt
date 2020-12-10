import java.nio.file.Files
import java.nio.file.Paths
import java.util.PriorityQueue

object Day10 {
    const val FILENAME = "src/main/resources/day10.txt"
    val fileData = Files.readAllLines(Paths.get(FILENAME)).map(String::toInt)

    data class Node(val value: List<Int>) : Comparable<Node> {
        constructor(vararg values: Int) : this(values.toList())

        override fun compareTo(other: Node) =
            when (val comp = this.size.compareTo(other.size)) {
                0 -> this.last().compareTo(other.last())
                else -> -1 * comp
            }
    }

    private fun Node.last() = value.last()
    private operator fun Node.contains(it: Int) = it in value
    private operator fun Node.plus(it: Int): Node = Node(value + it)
    private operator fun PriorityQueue<Node>.plus(it: List<Node>) = this.apply { addAll(it) }
    private val Node.size: Int
        get() = value.size

    fun part1(data: List<Int>) {
        val temp = data.map { curr ->
            curr to data.filter { it != curr && it in (curr + 1)..(curr + 3) }
        }.sortedBy { (a, _) -> a }.toMap().toMutableMap()

        temp[0] = data.filter { it <= 3 }
        val biggest = data.maxOrNull()!!
        temp[biggest] = listOf(biggest + 3)

        tailrec fun bfs(toConsider: PriorityQueue<Node>, count: Int = 0): Node? {
            if (toConsider.isEmpty()) {
                return null
            }
            val curr = toConsider.remove()
            val nextChildren: List<Int>? = temp[curr.last()]?.filter { it !in curr }
            return when {
                curr.size == (data.size + 2) -> curr
                nextChildren == null -> bfs(toConsider, count + 1)
                else -> bfs(toConsider + nextChildren.map { curr + it }, count + 1)
            }
        }

        val pq = PriorityQueue<Node>()
        pq.add(Node(0))
        val answer1 = bfs(pq)!!.value

        println(answer1.windowed(2, 1).fold(0 to 0) { (ones, threes), (a, b) ->
            when (b - a) {
                1 -> (ones + 1) to threes
                3 -> ones to (threes + 1)
                else -> ones to threes
            }
        }.let { (o, t) ->
            o * t
        })
    }

    tailrec fun Map<Int, List<Int>>.countPossible(
        toConsider: List<Int>,
        totals: Map<Int, Long> = mapOf(0 to 1L)
    ): Long {
        val current = toConsider.first()
        val childCounts = sumOfChildCounts(current, totals)
        return when {
            toConsider.size > 1 -> countPossible(
                toConsider.drop(1),
                totals.plus(current to childCounts)
            )
            else -> childCounts
        }
    }

    private fun Map<Int, List<Int>>.sumOfChildCounts(
        current: Int,
        totals: Map<Int, Long>
    ) = this[current]?.fold(0L) { a, it -> a + (totals[it] ?: 0L) } ?: 0

    fun part2(data: List<Int>) {
        val fullData = data + 0
        val temp = fullData.map { curr ->
            curr to fullData.filter { it != curr && it in (curr - 3) until curr }
        }.sortedBy { (a, _) -> a }.toMap().toMutableMap()

        val answer2 = temp.countPossible(fullData.sorted().drop(1))

        println("$answer2")
    }
}

fun main() {
    val testdata = "16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4".lines().map(String::toInt)
    val testdata2 =
        "28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3".lines()
            .map(String::toInt)

    println("=== PART 1 === ")
    Day10.part1(testdata)
    Day10.part1(testdata2)
    Day10.part1(Day10.fileData)
    println("=== PART 2 === ")
    Day10.part2(testdata)
    Day10.part2(testdata2)
    Day10.part2(Day10.fileData)
}




