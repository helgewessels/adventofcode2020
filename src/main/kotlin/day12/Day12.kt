package day12

import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Day12 {
    fun part1(input: String) =
            readInstructions(input)
                    .execute(Ship(position = ORIGIN, direction = EAST, navigationStrategy = Ship.NavigationStrategy.MoveShip()))
                    .position.manhattanDistance(ORIGIN)

    fun part2(input: String) =
            readInstructions(input)
                    .execute(Ship(position = ORIGIN, direction = Direction(10, -1), navigationStrategy = Ship.NavigationStrategy.MoveWaypoint()))
                    .position.manhattanDistance(ORIGIN)

    fun readInstructions(input: String): List<Instruction> = input.trimIndent()
            .split("\n")
            .map { line -> Instruction.fromString(line) }

    fun List<Instruction>.execute(startShip: Ship): Ship =
            fold(startShip) { ship, instruction -> ship.execute(instruction) }
}

sealed class Instruction(open val value: Int) {
    companion object {
        fun fromString(string: String): Instruction = when (string.firstOrNull()) {
            'N' -> North(string.drop(1).toInt())
            'S' -> South(string.drop(1).toInt())
            'E' -> East(string.drop(1).toInt())
            'W' -> West(string.drop(1).toInt())
            'L' -> Left(string.drop(1).toInt())
            'R' -> Right(string.drop(1).toInt())
            'F' -> Forward(string.drop(1).toInt())
            else -> throw IllegalArgumentException("invalid instruction")
        }
    }

    class North(override val value: Int) : Instruction(value)
    class South(override val value: Int) : Instruction(value)
    class East(override val value: Int) : Instruction(value)
    class West(override val value: Int) : Instruction(value)
    class Left(override val value: Int) : Instruction(value)
    class Right(override val value: Int) : Instruction(value)
    class Forward(override val value: Int) : Instruction(value)
}

data class Ship(val position: Position, val direction: Direction, private val navigationStrategy: NavigationStrategy) {
    fun execute(instruction: Instruction): Ship = when (instruction) {
        is Instruction.Left -> copy(direction = direction.rotate(-instruction.value))
        is Instruction.Right -> copy(direction = direction.rotate(instruction.value))
        is Instruction.Forward -> copy(position = position.move(direction, instruction.value))
        is Instruction.North -> navigationStrategy.move(this, NORTH, instruction.value)
        is Instruction.South -> navigationStrategy.move(this, SOUTH, instruction.value)
        is Instruction.East -> navigationStrategy.move(this, EAST, instruction.value)
        is Instruction.West -> navigationStrategy.move(this, WEST, instruction.value)
    }

    abstract class NavigationStrategy {
        abstract fun move(ship: Ship, direction: Direction, distance: Int): Ship

        class MoveShip : NavigationStrategy() {
            override fun move(ship: Ship, direction: Direction, distance: Int) = ship.copy(position = ship.position.move(direction, distance))
        }

        class MoveWaypoint : NavigationStrategy() {
            override fun move(ship: Ship, direction: Direction, distance: Int) = ship.copy(direction = ship.direction.move(direction, distance))
        }
    }
}

data class Position(val east: Int, val south: Int) {
    fun move(direction: Direction, distance: Int) =
            Position(
                    east = east + direction.east * distance,
                    south = south + direction.south * distance
            )

    fun rotate(degrees: Int): Position {
        val radians = Math.toRadians(degrees.toDouble())
        return Position(
                east = (east * cos(radians) - south * sin(radians)).roundToInt(),
                south = (east * sin(radians) + south * cos(radians)).roundToInt()
        )
    }

    fun manhattanDistance(origin: Position): Int =
            (east - origin.east).absoluteValue + (south - origin.south).absoluteValue
}

val ORIGIN = Position(0, 0)

typealias Direction = Position

val NORTH = Direction(0, -1)
val SOUTH = Direction(0, 1)
val EAST = Direction(1, 0)
val WEST = Direction(-1, 0)
