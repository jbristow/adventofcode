import util.AdventOfCode
import java.time.Instant
import java.util.LinkedList

object Day23 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Part 1: ${part1(inputFileString)}")
        // println("Part 2: ${part2("389125467")}")
        println("Part 2: ${part2(inputFileString)}")
    }

    fun LinkedList<Int>.answer1(): String {
        while (first() != 1) {
            addLast(removeFirst())
        }
        return drop(1).take(8).joinToString("")
    }

    fun LinkedList<Int>.answer2(): Long {
        val index = indexOf(1)
        val index1 = (index + 1) % size
        val index2 = (index + 2) % size
        return get(index1).toLong() * get(index2).toLong()
    }

    tailrec fun move1(cq: LinkedList<Int>, maxMoves: Int, size: Int = cq.size, moveN: Int = 0): LinkedList<Int> {
        if (moveN >= maxMoves) {
            return cq
        }
        if (moveN % 100_000 == 0) {
            println("move $moveN ${Instant.now()} ${cq.answer2()}")
        }

        val current = cq.removeFirst()
        val next3 = listOf(cq.removeFirst(), cq.removeFirst(), cq.removeFirst())
        cq.addFirst(current)
        var nextLabel = when {
            (current - 1) < 1 -> size
            else -> current - 1
        }
        while (nextLabel in next3) {
            nextLabel -= 1
            if (nextLabel < 1) {
                nextLabel = size
            }
        }
        var rotation = 0
        while (cq.first() != nextLabel) {
            cq.addFirst(cq.removeLast())
            rotation += 1
        }

        cq.addLast(cq.removeFirst())
        cq.addFirst(next3[2])
        cq.addFirst(next3[1])
        cq.addFirst(next3[0])

        while (cq.first() != current) {
            cq.addLast(cq.removeFirst())
        }
        cq.addLast(cq.removeFirst())
        //cq.addLast(cq.removeFirst())
        return move1(cq, maxMoves, size, moveN + 1)
    }

    tailrec fun move2(cups: MutableMap<Int, Int>, cur: Int, maxMoves: Int = 10_000_000, moveN: Int = 0): MutableMap<Int, Int> {
        if (moveN >= maxMoves) {
            return cups
        }
        val remove1 = cups[cur]!!
        val remove2 = cups[remove1]!!
        val remove3 = cups[remove2]!!
        cups[cur] = cups[remove3]!!

        val removed = listOf(cur, remove1, remove2, remove3)
        var cval = cur
        while (cval in removed) {
            cval -= 1
            if (cval == 0) {
                cval = 1000000
            }
        }
        val targetLoc = cval
        val afterTarget = cups[targetLoc]!!
        cups[cval] = remove1
        cups[remove3] = afterTarget
        return move2(cups, cups[cur]!!, maxMoves, moveN + 1)
    }

    private fun part1(input: String) =
        move1(LinkedList(input.map { it.toString().toInt() }), 100).answer1()

    private fun part2(input: String): Long {
        val nums = LinkedList(input.map { it.toString().toInt() })

        val cups = initializeCupMap(nums)
        move2(cups, nums[0], maxMoves = 10_000_000)

        val remove1 = cups[1]!!
        val remove2 = cups[remove1]!!
        return remove1.toLong() * remove2.toLong()
    }

    private fun initializeCupMap(nums: LinkedList<Int>): MutableMap<Int, Int> {
        val cups = mutableMapOf<Int, Int>()
        var prev = -1
        ((nums.size - 1) downTo 0).forEach { i ->
            cups[nums[i]] = prev
            prev = nums[i]
        }
        (1_000_000 downTo 10).forEach { i ->
            cups[i] = prev
            prev = i
        }
        cups[nums.last()] = 10
        return cups
    }
}