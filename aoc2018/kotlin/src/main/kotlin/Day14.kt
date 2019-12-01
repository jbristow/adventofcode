object Day14 {
    @JvmStatic
    fun main(args: Array<String>) {
        val input = 760221
        println(Day14.answer1(input))
        println(Day14.answer2(input))

    }

    private fun answer2(input: Int): Any? {

        val elf1 = 0
        val elf2 = 1
        val recipes = mutableListOf(3, 7)
        return findRecipes2(
            2,
            elf1,
            elf2,
            recipes,
            input.toString().map { it.toDigit() }
        )
    }

    private fun answer1(input: Int): Any? {
        val elf1 = 0
        val elf2 = 1
        val recipes = mutableListOf(3, 7)
        return findRecipes(2, elf1, elf2, recipes, input)

    }

    private tailrec fun findRecipes2(
        n: Int,
        elf1: Int,
        elf2: Int,
        recipes: MutableList<Int>,
        check: List<Int>
    ): Int {
        val ccount = check.count()
        if ((n > ccount) && (n - ccount until n).map { recipes[it] } == check) {
            return n - ccount
        } else if ((n > ccount) && (n - ccount until n).map { recipes[it - 1] } == check) {
            return n - ccount - 1
        }

        val re1 = recipes[elf1]
        val re2 = recipes[elf2]

        val newRecipes = when {
            re1 + re2 > 9 -> mutableListOf(1, (re1 + re2) % 10)
            else -> mutableListOf((re1 + re2) % 10)
        }
        val nextN = n + newRecipes.count()
        recipes.addAll(newRecipes)
        return findRecipes2(
            nextN,
            (elf1 + re1 + 1) % nextN,
            (elf2 + re2 + 1) % nextN,
            recipes,
            check
        )

    }


    private tailrec fun findRecipes(
        n: Int,
        elf1: Int,
        elf2: Int,
        recipes: MutableList<Int>,
        limit: Int
    ): String {
//        println("$n, $elf1, $elf2, $recipes")

        if (n > limit + 10) {
            return recipes.drop(limit).take(10).joinToString("")
        }
        val re1 = recipes[elf1]
        val re2 = recipes[elf2]
        val newRecipes = when {
            re1 + re2 > 9 -> mutableListOf(1, (re1 + re2) % 10)
            else -> mutableListOf((re1 + re2) % 10)
        }
        val nextN = n + newRecipes.count()

        recipes.addAll(newRecipes)
        return findRecipes(
            nextN,
            (elf1 + re1 + 1) % nextN,
            (elf2 + re2 + 1) % nextN,
            recipes,
            limit
        )

    }
}


private fun Char.toDigit(): Int {
    return toInt() - '0'.toInt()
}
