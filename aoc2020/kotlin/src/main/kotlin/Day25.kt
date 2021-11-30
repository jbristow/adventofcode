import util.AdventOfCode
import java.math.BigInteger

object Day25 : AdventOfCode() {
    @JvmStatic
    fun main(args: Array<String>) {

        val data = inputFileLines
        val cardPk = data[0].toBigInteger()
        val doorPk = data[1].toBigInteger()

        println(cardPk)

        val cardLoop = 1050835
        val doorLoop = 8912970
        // println(7.toBigInteger().pow(cardLoop).mod(20201227.toBigInteger()) == cardPk)
        // println(7.toBigInteger().pow(doorLoop).mod(20201227.toBigInteger()) == doorPk)


        println("door:"+doorPk.pow(cardLoop).mod(20201227.toBigInteger()))
        println("card:"+cardPk.pow(doorLoop).mod(20201227.toBigInteger()))
        // println((1..100).asSequence().map {
        //     transform(7.toBigInteger(), it) to 7.toBigInteger().pow(it) % 20201227.toBigInteger()
        // }.joinToString("\n"))
    }

    tailrec fun transform(
        subject: BigInteger,
        loopSize: Int,
        value: BigInteger = BigInteger.ONE,
        count: Int = 0,
    ): BigInteger {
        if (count == loopSize) {
            return value
        }
        return transform(subject, loopSize, (subject * value) % 20201227.toBigInteger(), count + 1)
    }
}


// 1526110  - 2-5-101-1511
// 20175123 - 3-439-15319