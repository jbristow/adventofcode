import util.AdventOfCode

object Day09 : AdventOfCode() {
    private fun Array<Int?>.checksum(): Long = mapIndexedNotNull { index, value -> value?.let { index.toLong() * it.toLong() } }.sum()

    sealed class DiskSpace(
        val range: IntRange,
    ) {
        val size: Int = range.last - range.first + 1
        abstract val checksum: Long

        class FileSpace(
            val id: Int,
            range: IntRange,
        ) : DiskSpace(range) {
            override val checksum: Long get() = range.sumOf { it * id.toLong() }

            override fun toString(): String = id.toString().repeat(size)
        }

        class FreeSpace(
            range: IntRange,
        ) : DiskSpace(range) {
            override val checksum = 0L

            override fun toString(): String = ".".repeat(size)
        }
    }

    fun part1(input: String): Long {
        val filesize = input.sumOf { c -> c.digitToInt() }
        return arrayOfNulls<Int>(filesize)
            .populate(input.map { it.digitToInt() })
            .defrag()
            .checksum()
    }

    private tailrec fun Array<Int?>.defrag(
        frontIndex: Int = 0,
        backIndex: Int = this.lastIndex,
    ): Array<Int?> {
        if (frontIndex >= backIndex) {
            return this
        }
        if (this[backIndex] == null) {
            return this.defrag(frontIndex, backIndex - 1)
        }
        if (this[frontIndex] != null) {
            return this.defrag(frontIndex + 1, backIndex)
        }
        this[frontIndex] = this[backIndex]
        this[backIndex] = null
        return this.defrag(frontIndex + 1, backIndex - 1)
    }

    private tailrec fun Array<Int?>.populate(
        ints: List<Int>,
        free: Boolean = false,
        index: Int = 0,
        nextId: Int = 0,
    ): Array<Int?> {
        if (ints.isEmpty()) {
            return this
        }
        val nextSpace = ints.first()
        return if (free) {
            this.populate(ints.drop(1), index = index + nextSpace, nextId = nextId)
        } else {
            for (i in index until index + nextSpace) {
                this[i] = nextId
            }
            this.populate(ints.drop(1), true, index + nextSpace, nextId + 1)
        }
    }

    fun part2(input: String): Long {
        val files =
            input
                .mapIndexed { index, ch ->
                    if (index % 2 == 0) {
                        (index / 2)
                    } else {
                        null
                    } to ch.digitToInt()
                }.fold(0 to mutableListOf<DiskSpace>()) { (currIndex, list), (spaceIndex, spaceSize) ->
                    if (spaceIndex == null) {
                        list.add(DiskSpace.FreeSpace(currIndex until currIndex + spaceSize))
                        (currIndex + spaceSize) to list
                    } else {
                        list.add(DiskSpace.FileSpace(spaceIndex, currIndex until currIndex + spaceSize))
                        (currIndex + spaceSize) to list
                    }
                }.second
                .toList()

        return files
            .complexDefrag(
                files
                    .filter {
                        it is DiskSpace.FileSpace
                    }.map { it as DiskSpace.FileSpace }
                    .sortedBy { it.id },
            ).sumOf { it.checksum }
    }

    tailrec fun List<DiskSpace>.complexDefrag(files: List<DiskSpace.FileSpace>): List<DiskSpace> {
        if (files.isEmpty()) {
            return this
        }
        val fileToMove = files.last()
        val indexOfMoving = indexOf(fileToMove)
        val indexOfFree = withIndex().find { (_, ds) -> ds is DiskSpace.FreeSpace && ds.size >= fileToMove.size }?.index ?: -1
        if (indexOfFree < 0 || indexOfFree > indexOfMoving) {
            return this.complexDefrag(files.dropLast(1))
        }

        val chunk1 = take(indexOfFree)
        val freeSpace = this[indexOfFree]
        val chunk2 = drop(indexOfFree + 1).take(indexOfMoving - indexOfFree - 1)
        val chunk3 = drop(indexOfMoving + 1)

        val nextDisk =
            chunk1 +
                if (freeSpace.size > fileToMove.size) {
                    val newFile = DiskSpace.FileSpace(fileToMove.id, freeSpace.range.first until (freeSpace.range.first + fileToMove.size))
                    val newSpace = DiskSpace.FreeSpace((freeSpace.range.first + fileToMove.size)..freeSpace.range.last)
                    listOf(newFile, newSpace)
                } else {
                    val newFile = DiskSpace.FileSpace(fileToMove.id, freeSpace.range)
                    listOf(newFile)
                } + chunk2 + DiskSpace.FreeSpace(fileToMove.range) + chunk3
        return nextDisk.complexDefrag(files.dropLast(1))
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 9")
        println("\tPart 1: ${part1(inputFileString)}")
        println("\tPart 2: ${part2(inputFileString)}")
    }
}
