class Day7 {

    fun part1(bag: String, input: String): Int =
            countBagsThatContain(bag, readRules(input))

    fun part2(bag: String, input: String): Int =
            countAllIncludedBags(bag, readRules(input))

    fun readRules(input: String): Map<String, List<Pair<Int, String>>> =
            input.trimIndent()
                    .split("\n")
                    .associate { line ->
                        val keyAndValue = line.split(" contain ")
                        val parentBag = readBag(keyAndValue[0])
                        val children = keyAndValue[1].replace(".", "").split(",").mapNotNull { readAmountAndBag(it) }
                        parentBag to children
                    }

    fun readBag(input: String): String =
            """([a-zA-Z]+ [a-zA-Z]+) bags?""".toRegex().find(input)!!.groupValues[1]

    fun readAmountAndBag(input: String): Pair<Int, String>? {
        if (input.contains("no other bag")) {
            return null
        }
        val match = """(\d+) ([a-zA-Z]+ [a-zA-Z]+) bags?""".toRegex().find(input)!!
        return Pair(match.groupValues[1].toInt(), match.groupValues[2])
    }

    fun countBagsThatContain(bag: String, rules: Map<String, List<Pair<Int, String>>>): Int =
            rules.filterKeys { containsBag(it, bag, rules) }.count()

    fun containsBag(parentBag: String, bag: String, rules: Map<String, List<Pair<Int, String>>>): Boolean =
            rules[parentBag]?.any { it.second == bag || containsBag(it.second, bag, rules) } ?: false

    fun countAllIncludedBags(bag: String, rules: Map<String, List<Pair<Int, String>>>): Int =
            rules[bag]?.sumOf { child -> child.first + child.first * countAllIncludedBags(child.second, rules) } ?: 0

}
