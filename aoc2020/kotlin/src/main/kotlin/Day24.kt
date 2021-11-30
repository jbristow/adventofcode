import util.AdventOfCode

object Day24 : AdventOfCode() {

    data class Hex(val q: Int, val r: Int, val s: Int) {
        constructor(q: Int, r: Int) : this(q, r, 0 - q - r)
    }

    operator fun Hex.plus(b: Hex) = Hex(q + b.q, r + b.r, s + b.s)
    operator fun Hex.times(n: Int) = Hex(q * n, r * n, s * n)
    operator fun Int.times(h: Hex) = h * this

    val NW = Hex(0, -1, 1)
    val NE = Hex(1, -1, 0)
    val W = Hex(-1, 0, 1)
    val E = Hex(1, 0, -1)
    val SW = Hex(-1, 1, 0)
    val SE = Hex(0, 1, -1)

    fun Hex.neighbors() = sequenceOf(NW, NE, W, E, SW, SE).map { this + it }

    fun Map<String, Int>.toHex(): Hex {
        val e = this.getOrDefault("e", 0) * E
        val se = this.getOrDefault("se", 0) * SE
        val sw = this.getOrDefault("sw", 0) * SW
        val w = this.getOrDefault("w", 0) * W
        val nw = this.getOrDefault("nw", 0) * NW
        val ne = this.getOrDefault("ne", 0) * NE

        return e + se + sw + w + nw + ne
    }

    fun String.toHex() =
        """e|se|sw|w|nw|ne""".toRegex()
            .findAll(this)
            .groupBy { it.groupValues[0] }
            .mapValues { (_, v) -> v.count() }
            .toHex()

    fun Set<Hex>.qRange() = this.extractingRange { q }
    fun Set<Hex>.rRange() = this.extractingRange { r }
    fun Set<Hex>.sRange() = this.extractingRange { s }

    fun <T> Iterable<T>.extractingRange(fn: T.() -> Int) =
        this.map(fn).let {
            when {
                it.isEmpty() -> 0..0
                else -> (it.minOrNull()!!)..(it.maxOrNull()!!)
            }
        }

    fun IntRange.expand() = (this.first - 1)..(this.last + 1)

    tailrec fun dailyTileFlip(
        map: Set<Hex>, maxCount: Int, count: Int = 0,
        qRange: IntRange = map.qRange().expand(),
        rRange: IntRange = map.rRange().expand(),
        sRange: IntRange = map.sRange().expand(),
    ): Set<Hex> {
        if (count >= maxCount) {
            return map
        }
        val newMap = qRange.asSequence()
            .flatMap { q -> rRange.asSequence().map { r -> Hex(q, r) } }
            .mapNotNull { h: Hex ->
                val neighbors = h.neighbors().filter { it in map }.count()
                when (h) {
                    in map -> when (neighbors) {
                        1, 2 -> h
                        else -> null
                    }
                    else -> when (neighbors) {
                        2 -> h
                        else -> null
                    }

                }
            }.toSet()
        return dailyTileFlip(newMap, maxCount, count + 1, qRange.expand(), rRange.expand(), sRange.expand())
    }

    fun List<String>.toBlackTiles() =
        this.map { it.toHex() }.fold(emptySet<Hex>()) { m, tile ->
            when (tile) {
                in m -> m - tile
                else -> m + tile
            }
        }

    fun part1(data: List<String>) = data.toBlackTiles().count()
    fun part2(data: List<String>) = dailyTileFlip(data.toBlackTiles(), 100).count()

    @JvmStatic
    fun main(args: Array<String>) {

        println("Day 24")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}