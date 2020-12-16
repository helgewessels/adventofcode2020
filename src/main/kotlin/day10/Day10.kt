package day10

import kotlin.math.pow
import kotlin.math.roundToLong

class Day10 {

    fun part1(input: String): Int {
        val differences = accumulateDifferences(readNumbers(input).withStart(0).withEnd(3))
        return differences.getOrDefault(1, 0) * differences.getOrDefault(3, 0)
    }

    fun part2(input: String): Long =
            countDistinctValidArrangements(readNumbers(input).withStart(0).withEnd(3))

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

    fun countDistinctValidArrangements(numbers: List<Int>): Long =
            numbers.toDifferences()
                    .split(3)
                    .map { chunk -> numberOfOptions(chunk.size) }
                    .reduce { acc, options -> acc * options }

    fun List<Int>.toDifferences() =
            sorted().windowed(2).map { window -> window.last() - window.first() }

    fun List<Int>.split(delimiter: Int) =
            joinToString("") { "$it" }
                    .split(delimiter.toString())
                    .map { chunk -> chunk.map { Character.getNumericValue(it) } }

    fun numberOfOptions(size: Int): Long =
            numberOfCombinations(size) - numberOfInvalidCombinations(size)

    /**
     * Counts all combinations with the constraint that the last has to be 1,
     * because it's followed by a 3 (chunk in the middle) or it's the last one at all (last chunk).
     */
    private fun numberOfCombinations(size: Int): Long =
            2.0.pow(size - 1).roundToLong()

    /**
     * Invalid combinations are 3 or more in a row, i.e. 000, 0000 etc.
     * Combinations ending with 0 are not counted at all, so we can exclude the last invalid chunks here.
     * E.g.
     * - for 11111 there are 2 chunks of 000 (not counting the last), which is size - 3
     * - for 11111 there is 1 chunk of 0000 (not counting the last), which is size - 4
     */
    private fun numberOfInvalidCombinations(size: Int): Long =
            if (size < 3) 0
            else (3 until size).sumBy { size - it }.toLong()
}
