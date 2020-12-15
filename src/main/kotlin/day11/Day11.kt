package day11

class Day11 {
    fun part1(input: String) =
            readField(input).countLastOccupiedSeats(SeatStrategy.Part1())

    fun part2(input: String) =
            readField(input).countLastOccupiedSeats(SeatStrategy.Part2())

    fun readField(input: String): List<List<Cell>> = input.trimIndent()
            .split("\n")
            .mapIndexed { row: Int, line: String ->
                line.mapIndexed { column: Int, char: Char ->
                    when (char) {
                        '.' -> Cell.Floor(row, column)
                        'L' -> Cell.EmptySeat(row, column)
                        '#' -> Cell.OccupiedSeat(row, column)
                        else -> throw IllegalArgumentException("input must contain only '.', 'L', '#'")
                    }
                }
            }

    fun List<List<Cell>>.countLastOccupiedSeats(seatStrategy: SeatStrategy): Int =
            applyRulesUntilEqual(seatStrategy).flatten().count { it is Cell.OccupiedSeat }

    private fun List<List<Cell>>.applyRulesUntilEqual(seatStrategy: SeatStrategy): List<List<Cell>> {
        var lastField: List<List<Cell>>? = null
        var field = this
        while (field != lastField) {
            lastField = field
            field = field.applyRules(seatStrategy)
        }
        return field
    }

    private fun List<List<Cell>>.applyRules(seatStrategy: SeatStrategy): List<List<Cell>> =
            map { cells -> cells.map { cell -> cell.applyRules(this, seatStrategy) } }

}

sealed class Cell(open val row: Int, open val column: Int) {
    abstract fun applyRules(field: List<List<Cell>>, seatStrategy: SeatStrategy): Cell

    data class Floor(override val row: Int, override val column: Int) : Cell(row, column) {
        override fun applyRules(field: List<List<Cell>>, seatStrategy: SeatStrategy): Cell = this
    }

    data class EmptySeat(override val row: Int, override val column: Int) : Cell(row, column) {
        override fun applyRules(field: List<List<Cell>>, seatStrategy: SeatStrategy): Cell =
                if (seatStrategy.findAdjacentSeats(this, field).none { cell -> cell is OccupiedSeat }) OccupiedSeat(row, column) else this
    }

    data class OccupiedSeat(override val row: Int, override val column: Int) : Cell(row, column) {
        override fun applyRules(field: List<List<Cell>>, seatStrategy: SeatStrategy): Cell =
                if (seatStrategy.findAdjacentSeats(this, field).count { cell -> cell is OccupiedSeat } > seatStrategy.maxAdjacentOccupiedSeats) EmptySeat(row, column) else this
    }
}

sealed class SeatStrategy(val maxAdjacentOccupiedSeats: Int) {
    abstract fun findAdjacentSeats(cell: Cell, field: List<List<Cell>>): List<Cell>

    protected fun List<List<Cell>>.exists(row: Int, column: Int): Boolean =
            row in indices && column in get(row).indices

    class Part1 : SeatStrategy(3) {
        override fun findAdjacentSeats(cell: Cell, field: List<List<Cell>>): List<Cell> =
                (cell.row - 1..cell.row + 1)
                        .flatMap { row ->
                            (cell.column - 1..cell.column + 1)
                                    .filter { column -> field.exists(row, column) }
                                    .filterNot { column -> row == cell.row && column == cell.column }
                                    .map { column -> field[row][column] }
                        }
                        .filterNot { it is Cell.Floor }
    }

    class Part2 : SeatStrategy(4) {
        override fun findAdjacentSeats(cell: Cell, field: List<List<Cell>>): List<Cell> =
                listOfNotNull(
                        cell.nextSeatOrNull(-1, -1, field),
                        cell.nextSeatOrNull(-1, 0, field),
                        cell.nextSeatOrNull(-1, 1, field),
                        cell.nextSeatOrNull(0, -1, field),
                        cell.nextSeatOrNull(0, 1, field),
                        cell.nextSeatOrNull(1, -1, field),
                        cell.nextSeatOrNull(1, 0, field),
                        cell.nextSeatOrNull(1, 1, field)
                )

        private fun Cell.nextSeatOrNull(rowStep: Int, columnStep: Int, field: List<List<Cell>>): Cell? {
            var cell: Cell? = this
            do {
                val nextRow = cell!!.row + rowStep
                val nextColumn = cell.column + columnStep
                cell = if (field.exists(nextRow, nextColumn)) field[nextRow][nextColumn] else null
            } while (cell is Cell.Floor)
            return cell
        }
    }
}
