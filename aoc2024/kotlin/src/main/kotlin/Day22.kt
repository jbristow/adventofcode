import util.AdventOfCode

object Day22 : AdventOfCode() {

    const val CHOP = 16777216L - 1

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 22")
//        println("\tPart 1: ${part1(inputFileLongs)}")
//        println("\tPart 2: ${part2(inputFileLongs)}")

        part1(listOf(1, 2, 3))
    }

    private fun part1(numbers: List<Long>): Long =
        numbers.asSequence().sumOf {
            (0 until 2000).fold(it) { acc, _ -> nextSecret(acc) }
        }

    private fun nextSecret(acc: Long): Long {
        val step1 = ((acc shl 6) xor acc) and CHOP
        val step2 = ((step1 shr 5) xor step1) and CHOP
        val step3 = ((step2 shl 11) xor step2) and CHOP
        println("%06x %8d".format(step3, step3))
        return step3
    }

    private fun part2(numbers: List<Long>): Long? =
        numbers.asSequence()
            .map {
                (0 until 2000).asSequence()
                    .runningFold(it) { acc, _ -> nextSecret(acc) }
                    .map { it % 10 }
            }.map { prices ->
                prices.zipWithNext { a, b ->
                    b to (b - a)
                }
            }.flatMap { priceWithDelta ->
                priceWithDelta.windowed(4, 1)
                    .map { seq ->
                        seq.joinToString(",") { it.second.toString() } to seq.last().first
                    }.groupBy({ (quad, _) -> quad }, { (_, price) -> price })
                    .map { (quad, prices) -> quad to prices.first() }
            }.groupBy({ (quad, _) -> quad }, { (_, price) -> price })
            .maxOf { (_, prices) -> prices.sum() }
}
