import kotlin.math.pow

class Day5 {

    fun part1(input: String): Int? =
        input.getSeatIds().maxOrNull()

    fun part2(input: String): Int? {
        val sortedSeatIds = input.getSeatIds().sorted()

        return sortedSeatIds
            .drop(1)
            .filterIndexed { index, id -> sortedSeatIds[index] == id - 2 }
            .map { it - 1 }
            .firstOrNull()
    }

    private fun String.getSeatIds() = trimIndent().lines().map { getSeatId(it) }

    fun getSeatId(seat: String): Int =
        getRow(seat) * 8 + getColumn(seat)

    fun getRow(seat: String): Int =
        seat.filter { it in "FB" }.reversed().foldIndexed(0, { index: Int, acc: Int, char: Char ->
            val take = if (char == 'F') 0 else 1
            acc + take * 2.0.pow(index).toInt()
        })

    fun getColumn(seat: String): Int =
        seat.filter { it in "LR" }.reversed().foldIndexed(0, { index: Int, acc: Int, char: Char ->
            val take = if (char == 'L') 0 else 1
            acc + take * 2.0.pow(index).toInt()
        })

}
