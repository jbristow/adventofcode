import utility.Point
import utility.allPoints
import utility.manhattanDistance

object Day22 {


    @JvmStatic
    fun main(args: Array<String>) {
        val cs = CaveSystem(10647, Point(7, 770))
//        val cs = CaveSystem(510, Point(10, 10))

        println(cs.riskLevel)
        (0..cs.target.y).forEach { y ->
            println(
                (0..cs.target.x).joinToString("") { x ->
                    when (val p = Point(x, y)) {
                        origin -> "M"
                        cs.target -> "X"
                        else -> cs.type(p).toString()
                    }
                }
            )
        }

        val (score, path) = (cs.bestPath)
//        println(path.joinToString("\n"))
        println(path.windowed(2, 1).fold(0) { sum, (a, b) ->
            sum + if (a.first == b.first) {
                println("Changing tools from ${a.second} to ${b.second}")
                7
            } else {
                println("Moving from ${a.first} to ${b.first}")
                1
            }

        })
        println(score)
    }
}

const val elModulo = 20183
val origin = Point(0, 0)

class CaveSystem(private val depth: Int, val target: Point) {

    private val geologicIndices = mutableMapOf<Point, Long>(
        origin to 0,
        target to 0
    )

    val riskLevel: Long get() = (origin to target).allPoints().flatten().fold(0L) { sum: Long, point: Point -> sum + point.riskLevel() }
    val bestPath by lazy {
        findShortestPath(origin to Tool.Torch, target to Tool.Torch)
    }


    private fun geologicIndex(p: Point): Long {
        if (!geologicIndices.containsKey(p)) {
            geologicIndices[p] = when {
                p == Point(0, 0) -> 0
                p.x == 0 -> p.y * 48271L
                p.y == 0 -> p.x * 16807L
                else -> p.down.erosionLevel() * p.left.erosionLevel()
            }
        }
        return geologicIndices[p]!!
    }

    /**
     * A region's erosion level is its geologic index plus the cave system's depth, all modulo 20183. Then:
     *
     * If the erosion level modulo 3 is 0, the region's type is Rocky.
     * If the erosion level modulo 3 is 1, the region's type is Wet.
     * If the erosion level modulo 3 is 2, the region's type is Narrow.
     */

    private fun Point.erosionLevel() = (geologicIndex(this) + depth) % elModulo

    enum class RegionType {
        Rocky, Wet, Narrow;

        val risk: Long
            get() = when (this) {
                Rocky -> 0
                Wet -> 1
                Narrow -> 2
            }

        override fun toString(): String {
            return when (this) {
                Rocky -> "."
                Wet -> "="
                Narrow -> "|"

            }
        }
    }

    fun type(point: Point) = when (point.erosionLevel() % 3) {
        0L -> RegionType.Rocky
        1L -> RegionType.Wet
        2L -> RegionType.Narrow
        else -> throw Exception("Unknown type. Bad modulo.")
    }

    private fun Point.riskLevel(): Long {
        return type(this).risk
    }


    private fun reconstructPath(
        cameFrom: MutableMap<Pair<Point, Tool>, Pair<Point, Tool>>,
        curr: Pair<Point, Tool>
    ): MutableList<Pair<Point, Tool>> {
        var current = curr
        val totalPath = mutableListOf<Pair<Point, Tool>>()

        totalPath.add(current)


        while (current in cameFrom) {
            current = cameFrom[current]!!
            totalPath.add(0, current)
        }
        return totalPath
    }

    private fun findShortestPath(
        start: Pair<Point, Tool>,
        goal: Pair<Point, Tool>
    ): Pair<Int, MutableList<Pair<Point, Tool>>> {
        // The set of nodes already evaluated
        val closedSet = mutableSetOf<Pair<Point, Tool>>()

        // The set of currently discovered nodes that are not evaluated yet.
        // Initially, only the start node is known.
        val openSet = mutableSetOf(start)

        // For each node, which node it can most efficiently be reached from.
        // If a node can be reached from many nodes, cameFrom will eventually contain the
        // most efficient previous step.
        val cameFrom =
            emptyMap<Pair<Point, Tool>, Pair<Point, Tool>>().toMutableMap()

        // For each node, the cost of getting from the start node to that node

        val gScore = mutableMapOf<Pair<Point, Tool>, Int>()

        // The cost of going from start to start is zero.
        gScore[start] = 0

        // For each node, the total cost of getting from the start node to the goal
        // by passing by that node. That value is partly known, partly heuristic.
        val fScore = mutableMapOf<Pair<Point, Tool>, Int>()

        // For the first node, that value is completely heuristic.
        fScore[start] = heuristicCostEstimate(start, goal)


        while (openSet.isNotEmpty()) {
            val current =
                fScore.filterKeys { it in openSet }.minBy { (k, v) -> v }!!.key
            if (current == goal) {
                return gScore[current]!! to reconstructPath(cameFrom, current)
            }


            openSet.remove(current)
            closedSet.add(current)

            val allNeighbors = current.first.neighboringSq()
                .filter { it.x >= 0 && it.y >= 0 && it.x < target.x + 21 && it.y < target.y + 21 }
                .map { it to current.second }
                .filter { it.second.isAppropriateFor(it.first) }
            val neighbors =
                (allNeighbors + current.switchedTool()).filter { it !in closedSet }


            for (neighbor in neighbors) {
                // The distance from start to a neighbor
                val tentativeGScore =
                    gScore[current]!! + (if (current.first == neighbor.first) 0 else 1) + (if (current.second == neighbor.second) 0 else 7)

                if (neighbor !in openSet) {    // Discover a new node
                    openSet.add(neighbor)
                }
                if (gScore[neighbor] == null || tentativeGScore < gScore[neighbor]!!) {
                    // This path is the best until now. Record it!
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = gScore[neighbor]!! +
                            heuristicCostEstimate(neighbor, goal)
                }
            }
        }
        return 0 to emptyList<Pair<Point, Tool>>().toMutableList()
    }

    enum class Tool {
        Torch, ClimbingGear, Neither;
    }

    private fun RegionType.appropriateTools(): List<Tool> {
        return when (this) {
            RegionType.Rocky -> listOf(Tool.ClimbingGear, Tool.Torch)
            RegionType.Wet -> listOf(Tool.ClimbingGear, Tool.Neither)
            RegionType.Narrow -> listOf(Tool.Torch, Tool.Neither)
        }
    }


    private fun heuristicCostEstimate(
        neighbor: Pair<Point, Tool>,
        goal: Pair<Point, Tool>
    ): Int {
        return neighbor.first.manhattanDistance(goal.first) + if (goal.second == neighbor.second) 0 else 7
    }

    private fun Pair<Point, Tool>.switchedTool(): Pair<Point, Tool> {
        return this.first to type(this.first).appropriateTools().find { it != this.second }!!
    }

    private fun CaveSystem.Tool.isAppropriateFor(first: Point): Boolean {
        return this in type(first).appropriateTools()
    }
}




