package day15

class Day15 {
    fun part1(input: String) = parseNumbers(input).play(2020).last()
    fun part2(input: String) = parseNumbers(input).play(30000000).last()

    fun parseNumbers(input: String) = input.trimIndent().split(",").map { it.toInt() }

    fun List<Int>.play(maxSize: Int): List<Int> {
        val list = dropLast(1).toMutableList()
        val lastIndices = list.mapIndexed { index, number -> number to index }.toMap(mutableMapOf())
        var lastNumber = last()
        while (list.size < maxSize) {
            val nextNumber = lastNumber.nextNumber(list.size, lastIndices)
            lastIndices[lastNumber] = list.size
            list.add(lastNumber)
            lastNumber = nextNumber
        }
        return list
    }

    private fun Int.nextNumber(index: Int, lastIndices: Map<Int, Int>): Int =
            if (lastIndices.containsKey(this)) {
                index - lastIndices.getValue(this)
            } else {
                0
            }
}
