package day21

class Day21 {
    fun part1(input: String): Int =
            parseFoods(input).let { foods ->
                foods.findIngredientsWithoutAllergen().fold(0) { sum, ingredient ->
                    sum + foods.count { it.ingredients.contains(ingredient) }
                }
            }

    fun part2(input: String): String =
            parseFoods(input)
                    .findIngredientsWithAllergen().asSequence()
                    .sortedBy { it.value }
                    .map { it.key }
                    .joinToString(",")

    fun parseFoods(input: String): List<Food> =
            input.trimIndent().lines().map { line ->
                val groupValues = """([\w ]+) \(contains ([\w, ]+)\)""".toRegex().find(line)!!.groupValues
                Food(ingredients = groupValues[1].split(" ").toSet(), allergens = groupValues[2].split(", ").toSet())
            }

    fun List<Food>.findIngredientsWithoutAllergen(): Set<String> =
            flatMap { it.ingredients }.toSet() - findAllergens().values.flatten()

    fun List<Food>.findIngredientsWithAllergen(): Map<String, String> {
        val ingredientsWithAllergen = mutableMapOf<String, String>()
        val allergens = findAllergens().toMutableMap()

        while (allergens.isNotEmpty()) {
            allergens.filterValues { ingredients -> ingredients.size == 1 }.forEach { (allergen, ingredients) ->
                val ingredient = ingredients.first()
                ingredientsWithAllergen[ingredient] = allergen
                allergens.remove(allergen)
                allergens.filterValues { it.contains(ingredient) }.keys.forEach { allergens[it] = allergens[it]!!.minus(ingredient) }
            }
        }

        return ingredientsWithAllergen
    }

    fun List<Food>.findAllergens(): Map<String, Set<String>> =
            fold(mutableMapOf()) { allergens: MutableMap<String, Set<String>>, food: Food ->
                food.allergens.forEach { allergen ->
                    allergens[allergen] = allergens.getOrDefault(allergen, food.ingredients).intersect(food.ingredients)
                }
                allergens
            }
}

data class Food(
        val ingredients: Set<String>,
        val allergens: Set<String>
)
