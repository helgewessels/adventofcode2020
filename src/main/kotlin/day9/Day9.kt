package day9

class Day9 {

    fun part1(input: String, offset: Int) =
            findFirstInvalid(readNumbers(input), offset)

    fun part2(input: String, offset: Int): Long? {
        val numbers = readNumbers(input)
        return findFirstInvalid(numbers, offset)
                ?.let { findWindowBySum(numbers, it) }
                ?.let { it.minOrNull()!! + it.maxOrNull()!! }
    }

    fun readNumbers(input: String) = input.trimIndent()
            .split("\n")
            .map { it.toLong() }

    fun findFirstInvalid(numbers: List<Long>, offset: Int): Long? =
            numbers.indices
                    .drop(offset)
                    .firstOrNull { !isValid(it, numbers, offset) }
                    ?.let { numbers[it] }

    fun findWindowBySum(numbers: List<Long>, sum: Long): List<Long>? =
            numbers.allWindows().firstOrNull { it.sum() == sum }

    fun isValid(index: Int, numbers: List<Long>, offset: Int): Boolean =
            numbers.subList(index - offset, index).allPairs().any { chunk -> chunk.sum() == numbers[index] }

    fun List<Long>.allPairs(): List<List<Long>> =
            indices.toList().dropLast(1).flatMap { first ->
                indices.toList().drop(first + 1).map { second -> listOf(get(first), get(second)) }
            }

    fun List<Long>.allWindows(): List<List<Long>> =
            indices.toList().dropLast(1).flatMap { start ->
                indices.toList().drop(start + 1).map { end -> subList(start, end + 1) }
            }
}
