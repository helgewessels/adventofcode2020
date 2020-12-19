package day17

import kotlin.math.absoluteValue

class Day17 {
    fun part1(input: String): Int =
            parseActiveCubes(input, 3).executeCycles(6).count()

    fun part2(input: String): Int =
            parseActiveCubes(input, 4).executeCycles(6).count()

    fun parseActiveCubes(input: String, dimensions: Int): Set<Cube> =
            input.trimIndent().split("\n")
                    .flatMapIndexed { x: Int, line: String ->
                        line.mapIndexedNotNull { y, char ->
                            if (char == '#') listOf(x, y) + List(dimensions - 2) { 0 } else null
                        }
                    }
                    .toSet()

    private fun Set<Cube>.executeCycles(cycles: Int): Set<Cube> =
            generateSequence(this) { it.nextActiveCubes() }.drop(cycles).first()

    private fun Set<Cube>.nextActiveCubes(): Set<Cube> =
            mapNotNull { it.nextActive(this) }.toSet() + flatMap { it.nextActiveNeighbors(this) }.toSet()

    private fun Cube.nextActive(cubes: Set<Cube>): Cube? =
            if (countNeighbors(cubes) in (2..3)) this else null

    private fun Cube.nextActiveNeighbors(cubes: Set<Cube>): Set<Cube> =
            findNeighbors()
                    .filterNot { cubes.contains(it) }
                    .filter { it.countNeighbors(cubes) == 3 }
                    .toSet()

    private fun Cube.findNeighbors(): List<Cube> =
            fold(mutableListOf(mutableListOf())) { acc, coordinate ->
                (coordinate - 1..coordinate + 1).flatMap { neighborCoordinate ->
                    acc.map { it + listOf(neighborCoordinate) }
                }
            }

    private fun Cube.countNeighbors(cubes: Set<Cube>): Int =
            cubes.count { it.isNeighbor(this) }

    private fun Cube.isNeighbor(cell: Cube): Boolean =
            cell != this && indices.all { dimension ->
                (this[dimension] - cell[dimension]).absoluteValue < 2
            }
}

typealias Cube = List<Int>
