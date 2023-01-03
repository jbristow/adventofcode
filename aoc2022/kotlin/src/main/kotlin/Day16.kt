import util.AdventOfCode
import util.Djikstra
import java.util.PriorityQueue

object Day16 : AdventOfCode() {


    data class VolcanoTube(
        val currentNode: String,
        val closedValves: Map<String, Int>,
        val ppm: Int = 0,
        val minutesToArrive: Int = 0,
        val currentPressure: Int = 0,
    ) : Comparable<VolcanoTube> {
        override fun compareTo(other: VolcanoTube): Int {
            if (minutesToArrive == other.minutesToArrive) {
                return -1 * currentPressure.compareTo(other.currentPressure)
            }
            return minutesToArrive.compareTo(other.minutesToArrive)
        }
    }

    data class ElephantVolcanoTube(
        val myNode: String,
        val elephantNode: String,
        val closedValves: Map<String, Int>,
        val ppm: Int = 0,
        val minutesToArrive: Int = 0,
        val currentPressure: Int = 0,
        val path: List<String> = emptyList()
    ) : Comparable<ElephantVolcanoTube> {
        override fun compareTo(other: ElephantVolcanoTube): Int {
            if (minutesToArrive == other.minutesToArrive) {
                if (currentPressure == other.currentPressure) {
                    return -1 * ppm.compareTo(other.ppm)
                }
                return -1 * currentPressure.compareTo(other.currentPressure)
            }
            return minutesToArrive.compareTo(other.minutesToArrive)
        }
    }

    private tailrec fun pqSearch(
        graph: Map<String, Map<String, Int>>,
        q: PriorityQueue<VolcanoTube>,
        step: Int,
        seen: MutableMap<String, Int>
    ): VolcanoTube? {
        val current = q.remove()

        if (current.minutesToArrive == 30) {
            return current
        }

        val modified = if (current.currentNode in current.closedValves) {
            val newPpm = (current.closedValves[current.currentNode] ?: 0)
            val remainingTime = 30 - current.minutesToArrive
            current.copy(
                ppm = current.ppm + newPpm,
                closedValves = current.closedValves - current.currentNode,
                currentPressure = current.currentPressure + remainingTime * newPpm,
            )
        } else {
            current
        }

        val validEdges = graph[modified.currentNode]?.toList()
            ?.filter { (node, weight) -> node != modified.currentNode && (modified.minutesToArrive + weight) < 30 }
            ?.filter { (node, _) -> node in modified.closedValves }
        if (validEdges == null || validEdges.isEmpty() || modified.closedValves.isEmpty()) {
            val sitInPlace = modified.copy(
                minutesToArrive = 30,
            )
            q.add(sitInPlace)
        } else {
            q.addAll(
                validEdges.map { edge ->
                    modified.copy(
                        currentNode = edge.first,
                        minutesToArrive = modified.minutesToArrive + edge.second + 1,
                    )
                }
            )
        }

        return pqSearch(graph, q, step + 1, seen)
    }

    private tailrec fun pqElephantSearch(
        graph: Map<String, Map<String, Int>>,
        q: PriorityQueue<ElephantVolcanoTube>,
        step: Int,
        seen: MutableMap<String, Int>
    ): ElephantVolcanoTube? {

        val current = q.remove()
        if (current.minutesToArrive == 26) {
            return current
        }

        val seenKey = seenKey(current)
        if ((seen[seenKey] ?: Int.MAX_VALUE) <= current.ppm) {
            return pqElephantSearch(graph, q, step + 1, seen)
        }
        seen[seenKey] = current.ppm

        val minutesLeft = 25 - current.minutesToArrive
        val eKeys = (graph[current.elephantNode] ?: emptyMap()).keys
        val mKeys = (graph[current.myNode] ?: emptyMap()).keys

        val nextValves = current.closedValves.toMutableMap()
        val newNodes = mutableListOf<ElephantVolcanoTube>()

        if (current.myNode != current.elephantNode && current.myNode in current.closedValves && current.elephantNode in current.closedValves) {
            val newPpm = (nextValves.remove(current.myNode) ?: 0) + (nextValves.remove(current.elephantNode) ?: 0)
            newNodes.add(
                current.copy(
                    minutesToArrive = current.minutesToArrive + 1,
                    currentPressure = current.currentPressure + minutesLeft * newPpm,
                    ppm = current.ppm + newPpm,
                    closedValves = nextValves,
                    path = current.path + "u=open(${current.myNode}),e=open(${current.elephantNode}),+(${minutesLeft * newPpm})"
                )
            )

        } else if (current.myNode in current.closedValves) {
            val newPpm = nextValves.remove(current.myNode) ?: 0
            newNodes.addAll(eKeys.map { elephant ->
                current.copy(
                    minutesToArrive = current.minutesToArrive + 1,
                    currentPressure = current.currentPressure + minutesLeft * newPpm,
                    ppm = current.ppm + newPpm,
                    closedValves = nextValves,
                    elephantNode = elephant,
                    path = current.path + "u=open(${current.myNode}),e=$elephant,+(${minutesLeft * newPpm})"
                )
            })
        } else if (current.elephantNode in current.closedValves) {
            val newPpm = nextValves.remove(current.elephantNode) ?: 0
            newNodes.addAll(mKeys.map { my ->
                current.copy(
                    minutesToArrive = current.minutesToArrive + 1,
                    currentPressure = current.currentPressure + minutesLeft * newPpm,
                    ppm = current.ppm + newPpm,
                    closedValves = nextValves,
                    myNode = my,
                    path = current.path + "u=$my,e=open(${current.elephantNode}),+(${minutesLeft * newPpm})"
                )
            })
        }
        newNodes.addAll(
            eKeys.flatMap { elephant ->
                mKeys.map { my ->
                    current.copy(
                        minutesToArrive = current.minutesToArrive + 1,
                        myNode = my,
                        elephantNode = elephant,
                        path = current.path + "u=${my},e=${elephant},+(0)"
                    )
                }
            }
        )

        q.addAll(newNodes.filter { (seen[seenKey(it)] ?: Int.MAX_VALUE) > it.ppm })


        return pqElephantSearch(graph, q, step + 1, seen)
    }

    private fun seenKey(it: ElephantVolcanoTube) = "${it.myNode}-${it.elephantNode}-${it.minutesToArrive}"


    private fun toEdgeMap(lines: List<Pair<Pair<String, Int>, List<String>>>) =
        lines.flatMap { (node, edges) -> edges.sorted().map { edge -> node.first to edge } }.groupBy { it.first }
            .mapValues { (_, v) -> v.associateBy({ it.second }, { 1 }) }

    private fun parseLines(input: List<String>) = input.map {
        val splitItems = it.split(" ", "; ", ", ")
        ((splitItems[1] to splitItems[4].split("=")[1].toInt()) to splitItems.drop(9))
    }

    private fun part1(input: List<String>): Int? {
        val lines = parseLines(input)

        val allValves = lines.associate { (node, _) -> node }

        val closedValves = allValves.filter { (k, v) -> k == "AA" || v > 0 }
        val edgeMap = toEdgeMap(lines)

        val simpleEdges = closedValves.keys.asSequence().map { f ->
            f to closedValves.keys.asSequence().map { t ->
                t to Djikstra.djikstra(
                    f,
                    { it == t },
                    allValves.keys,
                    { n -> edgeMap[n]?.map { it.key } ?: emptyList() },
                    { a, b -> edgeMap[a]?.get(b) ?: throw Exception("Could not find distance for $a to $b") }
                )!!.first
            }.toMap()
        }.toMap()

        return pqSearch(
            simpleEdges,
            PriorityQueue(listOf(VolcanoTube("AA", closedValves - "AA"))),
            0,
            mutableMapOf()
        )?.currentPressure
    }

    private fun part2(input: List<String>): Int? {
        val lines = parseLines(input)

        val closedValves = lines.associate { (node, _) -> node }.filter { (k, v) -> k == "AA" || v > 0 }
        val edgeMap = toEdgeMap(lines)

        return pqElephantSearch(
            edgeMap,
            PriorityQueue(listOf(ElephantVolcanoTube("AA", "AA", closedValves - "AA"))),
            0,
            mutableMapOf()
        )?.currentPressure
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
