import util.AdventOfCode

object Day21 : AdventOfCode() {
    enum class Op(val symbol: String, val fn: Long.(Long) -> Long) : (Long, Long) -> Long {
        Plus("+", Long::plus),
        Divide("/", Long::div),
        Times("*", Long::times),
        Minus("-", Long::minus),
        Eq("==", { b -> if (this == b) 1 else 0 });

        override fun invoke(p1: Long, p2: Long): Long {
            return p1.fn(p2)
        }
    }

    private fun String.op(): Op {
        return when (this) {
            "+" -> Op.Plus
            "/" -> Op.Divide
            "*" -> Op.Times
            "-" -> Op.Minus
            "==" -> Op.Eq
            else -> throw IllegalArgumentException("Bad Operator: $this")
        }
    }

    sealed interface MonkeyTree {
        data class Leaf(val value: Long) : MonkeyTree
        data class Node(val left: String, val right: String, val operator: Op) : MonkeyTree
    }

    private tailrec fun unwrap(tree: MutableMap<String, MonkeyTree>, rootLabel: String): String {
        val root = tree[rootLabel] as MonkeyTree.Node
        when (val leftNode = tree[root.left]) {
            null -> return rootLabel
            is MonkeyTree.Node -> {
                when (tree[leftNode.right]) {
                    is MonkeyTree.Leaf -> {
                        tree[rootLabel] = root.copy(left = leftNode.left, right = root.left)
                        tree[root.left] = rearrangeOp(root.right, leftNode)
                    }
                    else -> {
                        tree[rootLabel] = root.copy(left = leftNode.right, right = root.left)
                        tree[root.left] = rearrangeOpR(root.right, leftNode)
                    }
                }
            }
            is MonkeyTree.Leaf -> tree[rootLabel] = root.copy(left = root.right, right = root.left)
        }
        return unwrap(tree, rootLabel)
    }

    private fun rearrangeOp(right: String, node: MonkeyTree.Node): MonkeyTree.Node {
        return MonkeyTree.Node(
            right,
            node.right,
            when (node.operator) {
                Op.Times -> Op.Divide
                Op.Plus -> Op.Minus
                Op.Divide -> Op.Times
                Op.Minus -> Op.Plus
                Op.Eq -> throw IllegalArgumentException("Cant do equality here")
            }
        )
    }

    private fun rearrangeOpR(right: String, node: MonkeyTree.Node): MonkeyTree.Node {
        return when (node.operator) {
            Op.Times -> MonkeyTree.Node(right, node.left, Op.Divide)
            Op.Plus -> MonkeyTree.Node(right, node.left, Op.Minus)
            Op.Divide -> MonkeyTree.Node(right, node.left, Op.Divide)
            Op.Minus -> MonkeyTree.Node(node.left, right, Op.Minus)
            Op.Eq -> throw IllegalArgumentException("Cant do equality here")
        }
    }

    private fun simplify(monkeys: Map<String, MonkeyTree>): Map<String, MonkeyTree> {
        val output = monkeys.toMutableMap()
        var prev: Map<String, MonkeyTree>? = null
        while (output != prev) {
            prev = output.toMap()
            output.putAll(
                output.filterValues { it is MonkeyTree.Node && output[it.left] is MonkeyTree.Leaf && output[it.right] is MonkeyTree.Leaf }
                    .mapValues { (_, v) ->
                        val node = v as MonkeyTree.Node
                        val left = output[v.left]!! as MonkeyTree.Leaf
                        val right = output[v.right]!! as MonkeyTree.Leaf
                        MonkeyTree.Leaf(node.operator(left.value, right.value))
                    }
            )
        }
        return output
    }

    private fun part1(input: List<String>): Long {
        val monkeys = input.map {
            val (name, value) = it.split(": ")
            val rhs = value.split(" ")

            name to when (rhs.size) {
                1 -> MonkeyTree.Leaf(value.toLong())
                else -> MonkeyTree.Node(rhs[0], rhs[2], rhs[1].op())
            }
        }.toMap()

        val simplified = simplify(monkeys)
        return (simplified["root"] as MonkeyTree.Leaf).value
    }

    private fun part2(input: List<String>): Long {
        val tree: Map<String, MonkeyTree> = input.mapNotNull {
            val (name, value) = it.split(": ")
            val rhs = value.split(" ")

            when {
                name == "humn" -> null
                name == "root" -> name to MonkeyTree.Node(rhs[0], rhs[2], Op.Eq)
                rhs.size == 1 -> name to MonkeyTree.Leaf(value.toLong())
                else -> name to MonkeyTree.Node(rhs[0], rhs[2], rhs[1].op())
            }
        }.toMap()

        val mTree = simplify(tree).toMutableMap()
        unwrap(mTree, "root")

        val simplified = simplify(mTree)
        val newRoot = simplified["root"] as MonkeyTree.Node

        return if (newRoot.left == "humn") {
            (simplified[newRoot.right] as MonkeyTree.Leaf).value
        } else {
            (simplified[newRoot.left] as MonkeyTree.Leaf).value
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 21")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
