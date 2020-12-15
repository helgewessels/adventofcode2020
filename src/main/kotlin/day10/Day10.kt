package day10

class Day10 {

    fun part1(input: String): Int {
        val differences = accumulateDifferences(readNumbers(input).withStart(0).withEnd(3))
        return differences.getOrDefault(1, 0) * differences.getOrDefault(3, 0)
    }

    fun readNumbers(input: String) = input.trimIndent()
            .split("\n")
            .map { it.toInt() }

    fun List<Int>.withStart(start: Int): List<Int> = this + start

    fun List<Int>.withEnd(difference: Int): List<Int> = this + listOf((this.maxOrNull() ?: 0) + difference)

    fun accumulateDifferences(numbers: List<Int>): MutableMap<Int, Int> =
            numbers.sorted().windowed(2).fold(mutableMapOf()) { acc, window ->
                val difference = window.last() - window.first()
                acc[difference] = acc.getOrDefault(difference, 0) + 1
                acc
            }

}
