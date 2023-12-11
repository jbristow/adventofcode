import util.AdventOfCode
import util.Maths

object Day08 : AdventOfCode() {
    sealed interface Turn {
        data object Left : Turn

        data object Right : Turn
    }

    data class Node(val label: String, val left: String, val right: String)

    data class Network(val turns: List<Turn>, private val nodes: Map<String, Node>) {
        operator fun get(nodeLabel: String) = nodes.getValue(nodeLabel)

        val keys: Set<String>
            get() = nodes.keys
    }

    private fun List<String>.toNetwork(): Network {
        val turns = this[0].map { it.toTurn() }

        val nodes =
            this.drop(2).map { line ->
                line.split(" = ").let { (label, nodes) ->
                    nodes.split(", ").let { (l, r) -> Node(label, l.drop(1), r.dropLast(1)) }
                }
            }

        return Network(turns, nodes.associateBy { it.label })
    }

    private tailrec fun Network.navigate(
        currNode: Node,
        isEnd: (Node) -> Boolean = { it.label == "ZZZ" },
        directions: List<Turn> = this.turns,
        steps: Long = 0,
    ): Long {
        if (isEnd(currNode)) {
            return steps
        }

        val currDirection = directions.first()
        val remainingDirections = directions.drop(1).ifEmpty { this.turns }

        val nextNodeLabel =
            when (currDirection) {
                Turn.Left -> currNode.left
                Turn.Right -> currNode.right
            }
        return this.navigate(
            this[nextNodeLabel],
            isEnd,
            remainingDirections,
            steps + 1,
        )
    }

    private fun Char.toTurn() =
        when (this) {
            'R' -> Turn.Right
            'L' -> Turn.Left
            else -> throw Exception("Illegal Turn: $this")
        }

    private tailrec fun Network.runTilRepeat(
        node: Node,
        turns: List<Turn>,
        seen: MutableMap<String, Long> = mutableMapOf(),
        steps: Long = 0,
        maxSteps: Long = this.turns.size * 100L,
    ): MutableMap<String, Long> {
        if (maxSteps < steps) {
            return seen
        }
        if (node.label !in seen) {
            seen[node.label] = steps
        }
        val currDir = turns.first()
        val nextDirs = turns.drop(1).ifEmpty { this.turns }

        val nextNode =
            when (currDir) {
                Turn.Left -> node.left
                Turn.Right -> node.right
            }
        return this.runTilRepeat(this[nextNode], nextDirs, seen, steps + 1)
    }

    private fun part1(input: List<String>): Long {
        val network = input.toNetwork()
        return network.navigate(network["AAA"])
    }

    private fun part2(input: List<String>): Long {
        val network = input.toNetwork()
        val aNodes = network.keys.filter { it.endsWith("A") }.map { network[it] }
        val loops =
            aNodes.map { aNode -> network.runTilRepeat(aNode, network.turns) }
                .map { loop -> loop.filterKeys { it.endsWith("Z") } }

        // huh... it's an infinite loop...
        // and they only ever land on one...
        // At least for the first 100 times through the loop
        return loops.map { it.values.first() }.reduce { a, b -> Maths.lcm(a, b) }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 8")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
