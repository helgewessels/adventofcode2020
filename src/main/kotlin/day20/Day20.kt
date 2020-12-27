package day20

class Day20 {
    fun part1(input: String): Long =
            parseTiles(input)
                    .findCornerTiles()
                    .map { it.id.toLong() }
                    .reduce { acc, id -> acc * id }

    fun part2(input: String): Long =
            parseTiles(input)
                    .assembleImage()
                    .flatten()
                    .findAndMarkSeaMonsters()
                    .toString()
                    .count { it == '#' }
                    .toLong()

    fun parseTiles(input: String): List<Tile<Char>> =
            input.trimIndent().split("\n\n").map { parseTile(it) }

    private fun parseTile(input: String): Tile<Char> =
            input.trimIndent().split("\n").let { lines ->
                Tile(
                        id = """(\d+)""".toRegex().find(lines.first())!!.groupValues[1].toInt(),
                        image = Image(lines.drop(2).dropLast(1).map { line -> line.drop(1).dropLast(1).toList() }),
                        top = lines[1],
                        bottom = lines.last(),
                        right = lines.indices.drop(1).map { lines[it].last() }.joinToString(""),
                        left = lines.indices.drop(1).map { lines[it].first() }.joinToString("")
                )
            }

    private fun List<Tile<Char>>.findCornerTiles(): List<Tile<Char>> {
        val expandedTiles = flatMap { it.expand() }
        return filter { it.isCornerTile(expandedTiles) }
    }

    private fun Tile<Char>.isCornerTile(tiles: List<Tile<Char>>): Boolean {
        val otherTiles = tiles.filterNot { it.id == id }
        val topMatchingTiles = otherTiles.findMatchingTiles(top).count()
        val rightMatchingTiles = otherTiles.findMatchingTiles(right).count()
        val bottomMatchingTiles = otherTiles.findMatchingTiles(bottom).count()
        val leftMatchingTiles = otherTiles.findMatchingTiles(left).count()

        return (topMatchingTiles == 0 && rightMatchingTiles == 0) ||
                (rightMatchingTiles == 0 && bottomMatchingTiles == 0) ||
                (bottomMatchingTiles == 0 && leftMatchingTiles == 0) ||
                (leftMatchingTiles == 0 && topMatchingTiles == 0)
    }

    private fun List<Tile<Char>>.assembleImage(): Image<Tile<Char>> {
        val topLeftTile = findTopLeftCornerTile()
        val unassignedTiles = toMutableList()
        val imageTiles = mutableListOf<List<Tile<Char>>>()

        var leftOfRow: Tile<Char>? = topLeftTile
        while (unassignedTiles.isNotEmpty() && leftOfRow != null) {
            val row = mutableListOf(leftOfRow)
            unassignedTiles.removeId(leftOfRow.id)
            var right = unassignedTiles.findRightMatchingTile(leftOfRow)
            while (right != null) {
                row.add(right)
                unassignedTiles.removeId(right.id)
                right = unassignedTiles.findRightMatchingTile(row.last())
            }

            imageTiles.add(row)
            leftOfRow = unassignedTiles.findBottomMatchingTile(leftOfRow)
        }

        return Image(imageTiles)
    }

    private fun Image<Tile<Char>>.flatten(): Image<Char> =
            Image(toString().split("\n").map { it.toList() })

    private fun Image<Char>.findAndMarkSeaMonsters(): Image<Char> =
            expand().first { it.containsSeaMonster() }.markSeaMonsters()

    private fun Image<Char>.containsSeaMonster(): Boolean =
            rows.indices.windowed(3).any { rowIndices ->
                rows[rowIndices.first()].indices.windowed(20)
                        .any { columnIndices ->
                            SeaMonster.match(rowIndices.map { rows[it].slice(columnIndices) })
                        }
            }

    private fun Image<Char>.markSeaMonsters(): Image<Char> {
        val markedRows = rows.toMutableList()

        rows.indices.windowed(3).forEach { rowIndices ->
            rows[rowIndices.first()].indices.windowed(20)
                    .filter { columnIndices ->
                        SeaMonster.match(rowIndices.map { rows[it].slice(columnIndices) })
                    }.map { seaMonsterColumnIndices ->
                        val markedRowsSlice = SeaMonster.mark(rows = markedRows.slice(rowIndices), offset = seaMonsterColumnIndices.first())
                        markedRows[rowIndices[0]] = markedRowsSlice[0]
                        markedRows[rowIndices[1]] = markedRowsSlice[1]
                        markedRows[rowIndices[2]] = markedRowsSlice[2]
                    }
        }

        return Image(markedRows.toList())
    }

    private fun List<Tile<Char>>.findTopLeftCornerTile(): Tile<Char> {
        val expandedTiles = flatMap { it.expand() }
        return expandedTiles.find { it.isTopLeftCornerTile(expandedTiles) }!!
    }

    private fun List<Tile<Char>>.findBottomMatchingTile(tile: Tile<Char>): Tile<Char>? =
            flatMap { it.expand() }.find { it.matchTop(tile) }

    private fun List<Tile<Char>>.findRightMatchingTile(tile: Tile<Char>): Tile<Char>? =
            flatMap { it.expand() }.find { it.matchLeft(tile) }

    private fun Tile<Char>.isTopLeftCornerTile(tiles: List<Tile<Char>>): Boolean {
        val otherTiles = tiles.filterNot { it.id == id }
        val topMatchingTiles = otherTiles.findMatchingTiles(top).count()
        val leftMatchingTiles = otherTiles.findMatchingTiles(left).count()

        return leftMatchingTiles == 0 && topMatchingTiles == 0
    }

    private fun Iterable<Tile<Char>>.findMatchingTiles(border: String) =
            filter { it.top == border || it.right == border || it.bottom == border || it.left == border }

    private fun MutableList<Tile<Char>>.removeId(id: Int) {
        removeAll { tile -> tile.id == id }
    }
}

object SeaMonster {
    @JvmStatic
    fun match(rows: List<List<Char>>): Boolean =
            rows.size > 2 && pixels.all { (row, columns) ->
                columns.all { column -> rows[row][column] == '#' }
            }

    @JvmStatic
    fun mark(rows: List<List<Char>>, offset: Int): List<List<Char>> =
            rows.mapIndexed { rowIndex, row ->
                row.mapIndexed { pixelIndex, pixel ->
                    if ((pixelIndex - offset) in pixels.getOrDefault(rowIndex, emptyList())) 'O' else pixel
                }
            }

    private val pixels: Map<Int, List<Int>> = mapOf(
            0 to listOf(18),
            1 to listOf(0, 5, 6, 11, 12, 17, 18, 19),
            2 to listOf(1, 4, 7, 10, 13, 16)
    )
}

data class Image<T>(val rows: List<List<T>>) {

    fun expand(): Set<Image<T>> =
            (0..3).map { rotateTimes(it) }
                    .flatMap { tile -> listOf(tile, tile.flip()) }
                    .toSet()

    private fun rotateTimes(n: Int) =
            generateSequence(this) { it.rotate() }.drop(n).first()

    fun rotate(): Image<T> = copy(
            rows = rows.first().indices.map { column -> rows.indices.reversed().map { row -> rows[row][column] } }
    )

    fun flip(): Image<T> = copy(
            rows = rows.reversed()
    )

    override fun toString(): String =
            rows.joinToString("\n") { rowTiles ->
                val tiles = rowTiles.map { it.toString().split("\n") }
                tiles.first().indices.joinToString("\n") { row ->
                    tiles.joinToString("") { tile -> tile[row] }
                }
            }
}

data class Tile<T>(
        val id: Int,
        val image: Image<T>,
        val top: String,
        val right: String,
        val bottom: String,
        val left: String) {

    fun matchLeft(tile: Tile<T>): Boolean =
            left == tile.right

    fun matchTop(tile: Tile<T>): Boolean =
            top == tile.bottom

    fun expand(): Set<Tile<T>> =
            (0..3).map { rotateTimes(it) }
                    .flatMap { tile -> listOf(tile, tile.flip()) }
                    .toSet()

    private fun rotateTimes(n: Int) =
            generateSequence(this) { it.rotate() }.drop(n).first()

    fun rotate(): Tile<T> = copy(
            image = image.rotate(),
            right = top,
            bottom = right.reversed(),
            left = bottom,
            top = left.reversed()
    )

    private fun flip(): Tile<T> = copy(
            image = image.flip(),
            top = bottom,
            bottom = top,
            left = left.reversed(),
            right = right.reversed()
    )

    override fun toString(): String =
            image.toString()
}
