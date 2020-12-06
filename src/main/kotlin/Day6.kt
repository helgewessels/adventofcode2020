class Day6 {

    fun part1(input: String): Int =
        countInAnyGroup(mapInput(input))

    fun part2(input: String): Int =
        countInEveryGroup(mapInput(input))

    fun mapInput(input: String): List<List<String>> =
        input.trimIndent().split("\n\n").map { it.split("\n") }

    fun countInAnyGroup(groups: List<List<String>>): Int =
        groups.sumBy { countInAnyString(it) }

    fun countInEveryGroup(groups: List<List<String>>): Int =
        groups.sumBy { countInEveryString(it) }

    fun countInAnyString(group: List<String>): Int =
        group.distinctChars().count()

    fun countInEveryString(group: List<String>): Int =
        group.distinctChars().filter { char -> group.all { it.contains(char) } }.count()

    private fun List<String>.distinctChars(): List<Char> =
        flatMap { it.toCharArray().toList() }.distinct()
}
