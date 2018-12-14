import utility.scan

object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val bm = 71307
        val ps = 458
        println("answer 1: ${this.answer1(bm, ps)}")
        println("answer 2: ${this.answer2(bm, ps)}")
    }

    fun answer2(biggestMarble: Int, players: Int) =
        answer1(biggestMarble * 100, players)


    fun answer1(biggestMarble: Int, players: Int) =
        takeTurn(
            biggestMarble = biggestMarble,
            players = players,
            scores = Array(players) { 0L }
        ).max()


    class CircularCache<E>(initial: E) {
        private val init: Node<E>
        var first = Node(initial)
        var last = first
        var size = 0

        init {
            first = Node(initial)
            last = first
            first.next = first
            first.prev = first
            init = first
        }

        fun linkLast(e: E) {
            val l = last
            val newNode = Node(l, e, first)
            last = newNode
            l.next = newNode
            size++
        }

        fun rotateLeft(n: Int) = repeat(n) { rotateLeft() }

        private fun rotateRight() {
            val l = last
            l.next = first
            first = l
            last = l.prev
        }

        fun rotateRight(n: Int) = repeat(n) { rotateRight() }

        fun unlinkLast(): E {
            // assert f == first && f != null;
            val element = last
            val next = last.prev
            last = next
            first.prev = last
            last.next = first
            size--

            return element.item
        }

        private fun rotateLeft() {
            val f = first
            f.prev = last
            last = f
            first = f.next
            first.prev = f
            last.next = first
        }

        override fun toString(): String {
            return (0 until size).asSequence()
                .scan(init) { x, _ -> x.next }
                .joinToString()
        }
    }

    class Node<E>(val item: E) {

        var next: Node<E> = this
        var prev: Node<E> = this

        constructor(p: Node<E>, e: E, n: Node<E>) : this(item = e) {
            prev = p
            next = n
        }

        override fun toString(): String {
            return item.toString()
        }
    }
}

tailrec fun takeTurn(
    lastTurn: Int = 0,
    player: Int = 0,
    circle: Day09.CircularCache<Int> = Day09.CircularCache(0),
    scores: Array<Long>,
    players: Int,
    biggestMarble: Int
): Array<Long> {
    val turn = lastTurn + 1
    if (turn > biggestMarble) {
        return scores
    }
    if (turn % 23 == 0) {
        circle.rotateRight(7)

        val marble = circle.unlinkLast()
        scores[player] += (turn + marble).toLong()
        circle.rotateLeft(1)
    } else {
        circle.rotateLeft(1)
        circle.linkLast(turn)
    }

    return takeTurn(
        turn,
        (player + 1) % players,
        circle,
        scores,
        players,
        biggestMarble
    )
}

