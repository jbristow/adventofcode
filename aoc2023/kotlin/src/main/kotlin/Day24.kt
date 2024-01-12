import Day11.cartesian
import util.AdventOfCode
import util.Point3dBigDecimal
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat

// Precision is important.
private val mc = MathContext(100, RoundingMode.HALF_EVEN)

object Day24 : AdventOfCode() {
    private fun Point3dBigDecimal.cross() =
        arrayOf(
            arrayOf(BigDecimal.ZERO, -z, y),
            arrayOf(z, BigDecimal.ZERO, -x),
            arrayOf(-y, x, BigDecimal.ZERO),
        )

    private fun gaussianElimination(matrix: Array<Array<BigDecimal>>) {
        matrix.indices.forEach { i ->

            var maxRow = i
            ((i + 1)..<matrix.size).forEach { j ->
                if (matrix[j][i].abs() > matrix[maxRow][i].abs()) {
                    maxRow = j
                }
            }

            (i..<(matrix.size + 1)).forEach { k ->
                val tmp = matrix[maxRow][k]
                matrix[maxRow][k] = matrix[i][k]
                matrix[i][k] = tmp
            }

            ((i + 1)..<matrix.size).forEach { j ->
                val c =
                    try {
                        matrix[j][i].divide(matrix[i][i], mc)
                    } catch (e: ArithmeticException) {
                        println("tried to divide ${matrix[j][i]} by ${matrix[i][i]}")
                        (matrix[j][i].toDouble() / matrix[i][i].toDouble()).toBigDecimal()
                    }
                (i..<(matrix.size + 1)).forEach { k ->
                    if (i == k) {
                        matrix[j][k] = BigDecimal.ZERO
                    } else {
                        matrix[j][k] -= c * matrix[i][k]
                    }
                }
            }
        }

        matrix.indices.reversed().forEach { i ->
            matrix[i][matrix.size] =
                try {
                    matrix[i][matrix.size].divide(matrix[i][i], mc)
                } catch (e: ArithmeticException) {
                    println("tried to divide ${matrix[i][matrix.size]} by ${matrix[i][i]}")
                    (matrix[i][matrix.size].toDouble() / matrix[i][i].toDouble()).toBigDecimal()
                }
            matrix[i][i] = BigDecimal.ONE
            (0..<i).forEach { j ->
                matrix[j][matrix.size] = matrix[j][matrix.size] - (matrix[j][i] * matrix[i][matrix.size])
                matrix[j][i] = BigDecimal.ZERO
            }
        }
    }

    operator fun Array<Array<BigDecimal>>.times(b: Point3dBigDecimal): Array<BigDecimal> {
        val c = Array(3) { BigDecimal.ZERO }
        (0..<3).forEach { i ->
            (0..<3).forEach { j ->
                c[i] = c[i] + this[i][j] * b[j]
            }
        }
        return c
    }

    data class Hail(val position: Point3dBigDecimal, val velocity: Point3dBigDecimal) {
        override fun toString() = "Hailstone: ${position.x}, ${position.y}, ${position.z} @ ${velocity.x}, ${velocity.y}, ${velocity.z}"
    }

    private fun List<String>.toHail(): List<Hail> {
        return this.map { line ->
            line.split("""\s+@\s+""".toRegex())
                .map { point ->
                    point.split(""",\s+""".toRegex())
                        .map { it.toLong() }
                        .let { (x, y, z) -> Point3dBigDecimal(x.toBigDecimal(), y.toBigDecimal(), z.toBigDecimal()) }
                }
        }.map { (position, velocity) ->
            Hail(position, velocity)
        }
    }

    operator fun Point3dBigDecimal.div(other: Point3dBigDecimal): Point3dBigDecimal {
        return Point3dBigDecimal(
            this.x.divide(other.x, mc),
            this.y.divide(other.y, mc),
            this.z.divide(other.z, mc),
        )
    }

    val df = DecimalFormat("0.000")

    private fun Hail.intersects2d(other: Hail): Point3dBigDecimal? {
        val dLessC = other.position + other.velocity - other.position
        val bLessA = position + velocity - position

        val de = bLessA.x * dLessC.y - bLessA.y * dLessC.x

        if (de == BigDecimal.ZERO) {
            return null
        }

        // if (de > -tor && de < tor) return false; //line is in parallel
        val aLessC = position - other.position
        val r = ((aLessC.y * dLessC.x) - (aLessC.x * dLessC.y)).divide(de, mc)
        val s = ((aLessC.y * bLessA.x) - (aLessC.x * bLessA.y)).divide(de, mc)

        return if (r > BigDecimal.ZERO && s > BigDecimal.ZERO) {
            position + bLessA * r
        } else {
            null
        }
    }

    private fun part1(input: List<String>): Int {
        val hail =
            input.toHail()

        val testArea = 200000000000000.toBigDecimal()..400000000000000.toBigDecimal()
        return hail.cartesian()
            .mapNotNull { (a, b) ->
                a.intersects2d(b)
            }.count { (x, y) -> (x in testArea) && (y in testArea) }
    }

    private fun part2(input: List<String>): BigDecimal {
        val hail =
            input.toHail().sortedBy {
                it.position.manhattanDistance()
            }

        val dp0x = (hail[1].position - hail[0].position).cross()
        val dp1x = (hail[2].position - hail[1].position).cross()
        val dv0x = (hail[1].velocity - hail[0].velocity).cross()
        val dv1x = (hail[2].velocity - hail[1].velocity).cross()
        val coeffs = Array(6) { Array(7) { BigDecimal.ZERO } }
        (0..<3).forEach { i ->
            (0..<3).forEach { j ->
                coeffs[i][j] = -dv0x[i][j]
                coeffs[i][j + 3] = dp0x[i][j]
                coeffs[i + 3][j] = -dv1x[i][j]
                coeffs[i + 3][j + 3] = dp1x[i][j]
            }
        }
        val p0x = hail[0].position.cross()
        val p1x = hail[1].position.cross()
        val p2x = hail[2].position.cross()
        val p0xv0 = p0x * hail[0].velocity
        val p1xv1 = p1x * hail[1].velocity
        val p2xv2 = p2x * hail[2].velocity
        (0..<3).forEach { i ->
            coeffs[i][6] = (p1xv1[i] - p0xv0[i])
            coeffs[i + 3][6] = (p2xv2[i] - p1xv1[i])
        }
        gaussianElimination(coeffs)
        return coeffs[0][6].toBigInteger().toBigDecimal() +
            coeffs[1][6].toBigInteger().toBigDecimal() +
            coeffs[2][6].toBigInteger().toBigDecimal()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 24")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
