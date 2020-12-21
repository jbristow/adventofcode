import util.AdventOfCode

private val <K, V> Map<K, V>.keySet: Set<K>
    get() = this.keys.toSet()

object Day21 : AdventOfCode() {

    val test = """
        mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
        trh fvjkl sbzzf mxmxvkd (contains dairy)
        sqjhc fvjkl (contains soy)
        sqjhc mxmxvkd sbzzf (contains fish)
    """.trimIndent().lines()

    tailrec fun mapIngredientsToAllergens(
        recipes: Set<Recipe>,
        allergens: Set<String>,
        iToA: Map<String, String> = emptyMap(),
    ): Map<String, String> {
        val currentItoA = allergens.asSequence().map { a ->
            val sets = recipes
                .filter { a in it.allergens }
                .map { it.ingredients - iToA.keySet }
            sets.reduce(Set<String>::intersect) to a
        }.filter { it.first.size == 1 }.map {
            it.first.first() to it.second
        }.firstOrNull()

        return when {
            currentItoA != null ->
                mapIngredientsToAllergens(
                    recipes,
                    allergens - currentItoA.second,
                    iToA + currentItoA
                )
            else -> iToA
        }
    }

    fun part1(data: List<String>): Int {
        val recipes = processRecipes(data)
        val iToA = mapIngredientsToAllergens(recipes, recipes.allAllergens)
        return recipes.sumBy { (it.ingredients - iToA.keySet).count() }
    }

    fun part2(data: List<String>): String {
        val recipes = processRecipes(data)
        val iToA = mapIngredientsToAllergens(recipes, recipes.allAllergens)
        return iToA.map { (k, v) -> v to k }
            .sortedBy(Pair<String, String>::first)
            .joinToString(",", transform = Pair<String, String>::second)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 21")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }

    private fun processRecipes(data: List<String>) =
        data.map {
            val m = """(.*) \(contains (.*)\)""".toRegex().matchEntire(it)!!
            Recipe(
                m.groupValues[1].split(" ").toSet(),
                m.groupValues[2].split(", ").toSet()
            )
        }.toSet()

    data class Recipe(val ingredients: Set<String>, val allergens: Set<String>)

    private val Iterable<Recipe>.allAllergens: Set<String>
        get() = this.flatMap { it.allergens }.toSet()
}
