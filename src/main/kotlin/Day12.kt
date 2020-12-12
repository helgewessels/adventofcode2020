import kotlin.math.absoluteValue

class Day12 {
    fun part1(input: String) =
            readInstructions(input).execute(ShipState(0, 0, 90)).manhattanDistance

    fun readInstructions(input: String): List<Instruction> = input.trimIndent()
            .split("\n")
            .map { line -> Instruction.fromString(line) }

    fun List<Instruction>.execute(startShipState: ShipState): ShipState =
            fold(startShipState) { shipState, instruction -> instruction.execute(shipState) }
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

    abstract fun execute(shipState: ShipState): ShipState

    data class North(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = ShipState(shipState.eastPosition, shipState.southPosition - value, shipState.direction)
    }

    data class South(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = ShipState(shipState.eastPosition, shipState.southPosition + value, shipState.direction)
    }

    data class East(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = ShipState(shipState.eastPosition + value, shipState.southPosition, shipState.direction)
    }

    data class West(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = ShipState(shipState.eastPosition - value, shipState.southPosition, shipState.direction)
    }

    data class Left(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = ShipState(shipState.eastPosition, shipState.southPosition, shipState.direction - value)
    }

    data class Right(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = ShipState(shipState.eastPosition, shipState.southPosition, shipState.direction + value)
    }

    data class Forward(override val value: Int) : Instruction(value) {
        override fun execute(shipState: ShipState): ShipState = when (shipState.direction.toNormalizedDegrees()) {
            0 -> ShipState(shipState.eastPosition, shipState.southPosition - value, shipState.direction)
            90 -> ShipState(shipState.eastPosition + value, shipState.southPosition, shipState.direction)
            180 -> ShipState(shipState.eastPosition, shipState.southPosition + value, shipState.direction)
            270 -> ShipState(shipState.eastPosition - value, shipState.southPosition, shipState.direction)
            else -> throw IllegalArgumentException("invalid direction: ${shipState.direction} (${shipState.direction.toNormalizedDegrees()})")
        }

        private fun Int.toNormalizedDegrees(): Int {
            var degrees = this % 360
            while (degrees < 0) {
                degrees += 360
            }
            return degrees
        }
    }
}

data class ShipState(val eastPosition: Int, val southPosition: Int, val direction: Int) {
    val manhattanDistance: Int
        get() = eastPosition.absoluteValue + southPosition.absoluteValue
}
