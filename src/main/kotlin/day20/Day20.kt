package day20

class Day20 {
    fun part1(input: String): Long =
            parseTiles(input).findCornerTiles().map { it.id.toLong() }.reduce { acc, id -> acc * id }

    fun part2(input: String): Long = 0

    fun parseTiles(input: String): List<Tile> =
            input.trimIndent().split("\n\n").map { parseTile(it) }

    private fun parseTile(input: String): Tile =
            input.trimIndent().split("\n").let { lines ->
                Tile(
                        id = """(\d+)""".toRegex().find(lines.first())!!.groupValues[1].toInt(),
                        top = lines[1],
                        bottom = lines.last(),
                        right = lines.indices.drop(1).map { lines[it].last() }.joinToString(""),
                        left = lines.indices.drop(1).map { lines[it].first() }.joinToString("")
                )
            }

    private fun List<Tile>.findCornerTiles(): List<Tile> {
        val expandedTiles = flatMap { it.expand() }
        return filter { it.isCornerTile(expandedTiles) }
    }

    private fun Tile.isCornerTile(tiles: List<Tile>): Boolean {
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

    private fun Iterable<Tile>.findMatchingTiles(border: String) =
            filter { it.top == border || it.right == border || it.bottom == border || it.left == border }
}


data class Tile(
        val id: Int,
        val top: String,
        val right: String,
        val bottom: String,
        val left: String) {

    fun expand(): Set<Tile> =
            (0..3).map { rotateTimes(it) }
                    .flatMap { tile -> listOf(tile, tile.flip()) }
                    .toSet()

    private fun rotateTimes(n: Int) =
            generateSequence(this) { it.rotate() }.drop(n).first()

    private fun rotate(): Tile = copy(
            right = top,
            bottom = right.reversed(),
            left = bottom,
            top = left.reversed()
    )

    private fun flip(): Tile = copy(
            top = bottom,
            bottom = top,
            left = left.reversed(),
            right = right.reversed()
    )
}
