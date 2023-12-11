package util

import java.util.LinkedList

abstract class PathfindingAlgorithm {
    tailrec fun <T> reconstructPath(
        cameFrom: Map<T, T>,
        current: T,
        soFar: LinkedList<T> = LinkedList(),
    ): List<T> {
        val previous = cameFrom[current] ?: return soFar
        soFar.addFirst(current)
        return reconstructPath(cameFrom, previous, soFar)
    }
}
