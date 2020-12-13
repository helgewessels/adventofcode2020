class Day13 {
    fun part1(input: String) =
            readStartAndBusIds(input).let { startAndBusIds ->
                val start = startAndBusIds.first
                val busIds = startAndBusIds.second
                val earliestDeparture = busIds.findEarliestDeparture(start)
                earliestDeparture.first * (earliestDeparture.second - start)
            }

    fun readStartAndBusIds(input: String): Pair<Long, List<Long>> = input.trimIndent()
            .split("\n")
            .let { lines ->
                val start = lines.first().toLong()
                val busIds = lines.last().split(",").mapNotNull { it.toLongOrNull() }
                Pair(start, busIds)
            }

    fun Long.findNextDeparture(start: Long): Long =
            if (start % this == 0L) {
                start / this * this
            } else {
                start / this * this + this
            }

    fun List<Long>.findEarliestDeparture(start: Long): Pair<Long, Long> {
        val busId = minByOrNull { it.findNextDeparture(start) }!!
        val departure = busId.findNextDeparture(start)
        return Pair(busId, departure)
    }
}
