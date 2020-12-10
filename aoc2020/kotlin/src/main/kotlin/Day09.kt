import java.nio.file.Files
import java.nio.file.Paths
import java.util.Optional

object Day09 {
    const val FILENAME = "src/main/resources/day09.txt"
    tailrec fun findTwoNumbersThatSumTo(
        target: Long,
        current: Long,
        remaining: List<Long>
    ): Pair<Long, Long>? {
        if (current >= target || remaining.isEmpty()) {
            return null
        }
        val answer = current + remaining.last()
        return when {
            answer > target -> findTwoNumbersThatSumTo(target, current, remaining.dropLast(1))
            answer == target -> current to remaining.last()
            else -> findTwoNumbersThatSumTo(target, remaining.first(), remaining.drop(1))
        }
    }

    fun isValid(p: Pair<Long, List<Long>>): Boolean {
        val list = p.second.sorted()
        return findTwoNumbersThatSumTo(p.first, list.first(), list.drop(1)) != null
    }

    fun part01(data: List<Long>, stepSize: Int = 25): Optional<Long>? {
        return data.windowed(stepSize + 1, 1).map { it.last() to it.dropLast(1) }
            .parallelStream()
            .filter { !isValid(it) }.findFirst()
            .map { it.first }
    }

    tailrec fun findSpan(
        target: Long,
        stack: ArrayDeque<Long>,
        sum: Long,
        remaining: ArrayDeque<Long>,
        answer: ArrayDeque<Long>
    ): ArrayDeque<Long> {
        if (remaining.isEmpty() || answer.size > remaining.size) {
            return answer
        }

        val head = remaining.first()
        return when {
            head + sum == target && (stack.size + 1) > answer.size -> {
                answer.clear()
                answer.addAll(stack)
                answer.add(head)
                stack.clear()
                remaining.removeFirst()
                findSpan(target, stack, 0, remaining, answer)
            }
            head + sum == target -> {
                stack.clear()
                remaining.removeFirst()
                findSpan(target, stack, 0, remaining, answer)
            }
            head + sum >= target && !stack.isEmpty() -> {
                val jettisoned = stack.removeFirst()
                findSpan(target, stack, sum - jettisoned, remaining, answer)
            }
            head + sum >= target -> {
                remaining.removeFirst()
                findSpan(target, stack, sum, remaining, answer)
            }
            else -> {
                remaining.removeFirst()
                stack.add(head)
                findSpan(target, stack, sum + head, remaining, answer)
            }
        }
    }

    fun part02(data: List<Long>, target: Long): Long {
        val list = ArrayDeque<Long>()
        list.addAll(data)
        val answer = findSpan(target, ArrayDeque(), 0, list, ArrayDeque())
        return answer.minOrNull()!! + answer.maxOrNull()!!
    }
}

fun main() {
    val data = Files.readAllLines(Paths.get(Day09.FILENAME)).map(String::toLong)
    val part1Answer = Day09.part01(data)!!

    part1Answer.ifPresent {
        println("Part 1: $it")
        println("Part 2: ${Day09.part02(data, it)}")
    }
}



