package util

object AStarSearch : PathfindingAlgorithm() {
    fun orthogonalShortestPath(
        start: Point2d,
        end: Point2d,
        spaces: Set<Point2d>,
        neighbors: (Point2d) -> Set<Point2d> = Point2d::orthoNeighbors,
    ): List<Point2d> = shortestPath(start, spaces, { it == end }, { it.manhattanDistance(end) }, neighbors)

    fun <T> shortestPath(
        start: T,
        spaces: Set<T>,
        isGoal: (T) -> Boolean,
        heuristicCostEstimate: (T) -> Long,
        neighbors: (T) -> Set<T>,
    ): List<T> {
        val openSet = mutableSetOf(start)
        val closedSet = mutableSetOf<T>()
        val cameFrom = mutableMapOf<T, T>()
        val gScore = mutableMapOf(start to 0)
        val fScore = mutableMapOf(start to heuristicCostEstimate(start))

        tailrec fun aStarInner(spaces: Set<T>): List<T> {
            if (openSet.isEmpty()) {
                return emptyList()
            }

            val current = fScore.filterKeys { it in openSet }.minBy { (_, v) -> v }.key

            if (isGoal(current)) {
                return reconstructPath(cameFrom, current)
            }

            openSet.remove(current)
            closedSet.add(current)

            val newNeighbors = neighbors(current).filter { it !in closedSet && it in spaces }

            for (neighbor in newNeighbors) {
                // The distance from start to a neighbor
                val tentativeGScore = gScore[current]!! + 1

                if (neighbor !in openSet) { // Discover a new node
                    openSet.add(neighbor)
                }
                if (gScore[neighbor] == null || tentativeGScore < gScore[neighbor]!!) {
                    // This path is the best until now. Record it!
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = gScore[neighbor]!! + heuristicCostEstimate(neighbor)
                }
            }
            return aStarInner(spaces)
        }
        return aStarInner(spaces)
    }
}
