import java.io.File
import java.util.*


object Day08 {
    interface Node

    data class Parent(val children: Int, val metadata: Int) : Node {
        constructor(list: List<Int>) : this(list[0], list[1])
    }

    data class Child(val value: Int) : Node

    fun answer1(input: List<Int>) = readNode(0, input)

    private tailrec fun readNode(
        seenMetadata: Int,
        input: List<Int>
    ): Int {
        val (allMetadata, remainder) = input.windowed(2, 2).let { winput ->
            val ws = winput.takeWhile { it[0] != 0 }
            val current = winput.drop(ws.count()).first()
            val rest = input.drop(2 * ws.count() + 2)
            val currentMetadata = rest.take(current[1]).asSequence()
            currentMetadata to
                    if (ws.isNotEmpty()) {
                        ws.dropLast(1).flatten() + ws.last().let {
                            listOf(
                                it[0] - 1,
                                it[1]
                            ) + rest.drop(current[1])
                        }
                    } else {
                        emptyList()
                    }

        }

        return if (remainder.isEmpty()) {
            seenMetadata + allMetadata.sum()
        } else {
            readNode(seenMetadata + allMetadata.sum(), remainder)
        }
    }

    fun answer2(input: List<Int>) = readNode2(input, LinkedList())

    private tailrec fun readNode2(
        input: List<Int>,
        seen: LinkedList<Node>
    ): Int {
        if (input.isEmpty()) {
            return (seen.pop() as Child).value
        }

        val current = input.take(2)
        if (current[0] != 0) {
            seen.push(Parent(current))
            return readNode2(input.drop(2), seen)
        }

        seen.push(Child(input.drop(2).take(current[1]).sum()))
        val currentInput = input.drop(2 + current[1])
        val childrenCount = seen.takeWhile { it is Child }.count()
        return when {
            seen.isEmpty() -> readNode2(currentInput, seen)
            (seen[childrenCount] as Parent).children > childrenCount ->
                readNode2(currentInput, seen)
            else -> {
                val (remaining, currentSeen) =
                        collapseChildren(seen, currentInput)
                readNode2(remaining, currentSeen)
            }

        }

    }

    private tailrec fun collapseChildren(
        seen: LinkedList<Node>,
        currentInput: List<Int>
    ): Pair<List<Int>, LinkedList<Node>> {
        val childrenCount = seen.takeWhile { it is Child }.count()
        if (seen.isEmpty() || seen.count() == childrenCount || (seen[childrenCount] as Parent).children > childrenCount) {
            return currentInput to seen
        }

        val children = seen.popAllChildren()
        val parent = seen.pop() as Parent
        seen.push(
            Child(
                currentInput
                    .take((seen.pop() as Parent).metadata)
                    .filter { it in 1..childrenCount }
                    .sumBy { children[it - 1].value }
            )
        )
        return collapseChildren(
            seen,
            currentInput
                .drop(parent.metadata)
        )
    }

    private fun LinkedList<Node>.popAllChildren(): LinkedList<Child> {
        val children = LinkedList<Child>()
        while (peek() is Child) {
            children.push(pop() as Child)
        }
        return children
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            File("src/main/resources/day08.txt").readLines().first()
                .split(" ")
                .map(String::toInt)

//        val inputB =
//            "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2".split(" ")
//                .map(String::toInt)

        println("answer 1: ${this.answer1(input)}")
        println("answer 2: ${this.answer2(input)}")
    }
}






