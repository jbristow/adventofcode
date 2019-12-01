import utility.Point
import utility.manhattanDistance
import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.text.contains

interface Fighter : Comparable<Fighter> {
    var loc: Point
    var hp: Int
    val attackPower: Int
    val type: String
    val id: Long
}

abstract class DefaultFighter(override var loc: Point) : Fighter {
    override var hp = 200
    override val attackPower = 3
    override fun toString() =
        "$type[hp=$hp, loc=$loc]"

    override val id = System.currentTimeMillis()

    override fun compareTo(other: Fighter): Int =

        when (val compy = y.compareTo(other.y)) {
            0 -> x.compareTo(other.x)
            else -> compy
        }
}

val Fighter.x get() = loc.x
val Fighter.y get() = loc.y

class Goblin(loc: Point) : DefaultFighter(loc) {
    override val type = "Goblin"
}

class Elf(loc: Point, attack: Int) : DefaultFighter(loc) {
    constructor(loc: Point) : this(loc, 3)

    override val type: String = "Elf"
    override val attackPower = attack
}

fun Point.neighboringSq(): Set<Point> {
    return setOf(
        Point(x, y - 1),
        Point(x - 1, y),
        Point(x + 1, y),
        Point(x, y + 1)
    )
}


object Day15 {

    @Suppress("unused")
    const val inputB =
        "#######\n\n#.G...#\n#...EG#\n#.#.#G#\n#..G#E#\n#.....#\n#######"

    @Suppress("unused")
    const val inputC =
        "#######\n#.E...#\n#.#..G#\n#.###.#\n#E#G#G#\n#...#G#\n#######"

    @Suppress("unused")
    const val inputD =
        "#########\n#G......#\n#.E.#...#\n#..##..G#\n#...##..#\n#...#...#\n#.G...G.#\n#.....G.#\n#########"

    @Suppress("unused")
    const val inputE =
        "#########\n#G..G..G#\n#.......#\n#.......#\n#G..E..G#\n#.......#\n#.......#\n#G..G..G#\n#########"

    @Suppress("unused")
    const val inputF =
        "#########\n#G......#\n#.E.#...#\n#..##..G#\n#...##..#\n#...#...#\n#.G...G.#\n#.....G.#\n#########"

    @JvmStatic
    fun main(args: Array<String>) {

        val input = File("src/main/resources/day15.txt").readLines()

        println("Answer 1: ${Day15.answer1(input)}")
        println("Answer 2: ${Day15.answer2(input)}")
    }

    private fun answer2(input: List<String>): Int {
        val spaces = input.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    in "GE." -> Point(x, y)
                    else -> null
                }
            }
        }.flatten().toSet()
        val outcome = generateSequence(4) { it + 1 }.map {
            val fighters = input.mapIndexed { y, row ->
                row.mapIndexedNotNull { x, c ->
                    when (c) {
                        'G' -> Goblin(Point(x, y))
                        'E' -> Elf(Point(x, y), it)
                        else -> null

                    }
                }
            }.flatten()
            val outcome = round(0, fighters, spaces)
            val winner = outcome.second.first().type
            val noElvesDied =
                winner == "Elf" && fighters.filter { f -> f.type == winner }.count() == outcome.second.count()
            noElvesDied to (outcome.first * outcome.second.sumBy { f -> f.hp })
        }.dropWhile {
            !it.first
        }.first()
        return outcome.second
    }

    private fun answer1(
        input: List<String>
    ): Int {
        val spaces = input.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    in "GE." -> Point(x, y)
                    else -> null
                }
            }
        }.flatten().toSet()
        val fighters = input.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    'G' -> Goblin(Point(x, y))
                    'E' -> Elf(Point(x, y))
                    else -> null

                }
            }
        }.flatten()
        val outcome = round(0, fighters, spaces)
        return outcome.first * outcome.second.sumBy { it.hp }

    }

    private tailrec fun round(
        n: Int,
        fighters: List<Fighter>,
        spaces: Set<Point>
    ): Pair<Int, List<Fighter>> {

        if (fighters.isEmpty() || fighters.none { f -> f.type != fighters.first().type }) {
            val winnerType = fighters.first().type
            val hpLeft = fighters.sumBy { it.hp }
            println("$winnerType win! ${n - 1} $hpLeft")
            return (n - 1) to fighters
        }

        val remaining = fighterTurn(n, fighters.sorted(), emptySet(), spaces)
        return round(n + 1, remaining, spaces)
    }


}

private fun fighterTurn(
    n: Int,
    remaining: List<Fighter>,
    done: Set<Fighter>,
    spaces: Set<Point>
): List<Fighter> {

    if (remaining.isEmpty()) {
        return done.toList()
    }

    val current = remaining.first()
    val nextRemaining = remaining.drop(1)

    val (enemies, friends) = (remaining + done).splitBy { current.type == it.type }
    val enemyLocs = enemies.map { it.loc }
    val friendLocs = friends.map { it.loc }
    // identify if already in range
    val currentNeigboringSq =
        current.loc.neighboringSq().filter { it in spaces && it !in friendLocs }
    val alreadyInRange = enemies.any { it.loc in currentNeigboringSq }

    if (!alreadyInRange) {
        // try to move...

        // identify target spaces
        val potentialTargetSq =
            enemies.flatMap { it.loc.neighboringSq() }.distinct()
                .filter { it in spaces && it !in friendLocs && it !in enemyLocs }
                .sorted()

        if (potentialTargetSq.isEmpty()) {
            // nowhere to go...
            return fighterTurn(n, nextRemaining, done + current, spaces)
        }
        val unblocked =
            spaces.filter { it !in friendLocs && it !in enemyLocs }.toSet()
        val shortestPaths =
            potentialTargetSq.flatMap {
                currentNeigboringSq.mapNotNull { p ->
                    p.shortestPathTo(it, unblocked)
                        .ifEmpty { null }
                }
            }.filter { it.isNotEmpty() }

        if (shortestPaths.isEmpty()) {
            // nowhere to go
            return fighterTurn(n, nextRemaining, done + current, spaces)
        }
        val move =
            shortestPaths.groupBy { it.count() }.toSortedMap().toList()
                .first().second.sortedBy { it.last() }.first().first()
        current.loc = move

    }

    // NOW FIGHT!

    val neigboringSq =
        current.loc.neighboringSq().filter { it in spaces }
    val attackable =
        enemies.filter { it.loc in neigboringSq }.sortedBy { it.hp }

    if (attackable.isEmpty()) {
        // nothing to do...
        return fighterTurn(n, nextRemaining, done + current, spaces)
    }

    val target = attackable.groupBy { it.hp }.toSortedMap().toList().first()
        .second.sorted().first()
    target.hp -= current.attackPower
    return fighterTurn(
        n,
        nextRemaining.filter { it.hp > 0 },
        (done.filter { it.hp > 0 } + current).toSet(),
        spaces
    )

}

object AStarSearch {
    private fun reconstructPath(
        cameFrom: Map<Point, Point>,
        curr: Point
    ): MutableList<Point> {
        var current = curr
        val totalPath = mutableListOf<Point>()

        totalPath.add(current)


        while (current in cameFrom) {
            current = cameFrom[current]!!
            totalPath.add(0, current)
        }
        return totalPath
    }

    internal tailrec fun aStarInner(
        goal: Point,
        openSet: MutableSet<Point>,
        closedSet: MutableSet<Point>,
        cameFrom: MutableMap<Point, Point>,
        gScore: MutableMap<Point, Int>,
        fScore: MutableMap<Point, Int>,
        spaces: Set<Point>
    ): List<Point> {
        if (openSet.isEmpty()) {
            return emptyList()
        }

        val current =
            fScore.filterKeys { it in openSet }.minBy { (_, v) -> v }!!.key

        if (current == goal) {
            return reconstructPath(cameFrom, current)
        }

        openSet.remove(current)
        closedSet.add(current)

        val neighbors = current.neighboringSq()
            .filter { it !in closedSet && it in spaces }


        for (neighbor in neighbors) {
            // The distance from start to a neighbor
            val tentativeGScore = gScore[current]!! + 1

            if (neighbor !in openSet) {    // Discover a new node
                openSet.add(neighbor)
            }
            if (gScore[neighbor] == null || tentativeGScore < gScore[neighbor]!!) {

                // This path is the best until now. Record it!
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = gScore[neighbor]!! +
                        neighbor.heuristicCostEstimate(goal)
            }
        }
        return aStarInner(
            goal,
            openSet,
            closedSet,
            cameFrom,
            gScore,
            fScore,
            spaces
        )
    }

}

fun Point.shortestPathTo(
    goal: Point,
    spaces: Set<Point>
): List<Point> {

    return AStarSearch.aStarInner(
        goal,
        mutableSetOf(this),
        mutableSetOf(),
        mutableMapOf(),
        mutableMapOf(this to 0),
        mutableMapOf(this to this.heuristicCostEstimate(goal)),
        spaces
    )
}

private fun Point.heuristicCostEstimate(goal: Point) = manhattanDistance(goal)

