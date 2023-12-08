package util

object Maths {
    fun lcm(a: Long, b: Long): Long {
        return (a * b) / gcd(a, b)
    }


    tailrec fun gcd(a: Long, b: Long): Long {
        return when (val r = a % b) {
            0L -> b
            else -> gcd(b, r)
        }
    }
}