package day8

class Day8 {

    fun part1(input: String): Int =
            execute(readInstructions(input)).accumulator

    fun part2(input: String): Int =
            execute(findRepairedInstructions(readInstructions(input)) ?: emptyList()).accumulator

    fun readInstructions(input: String): List<Pair<String, Int>> =
            input.trimIndent()
                    .split("\n")
                    .map { line: String ->
                        val instruction = line.split(" ")
                        Pair(instruction[0], instruction[1].toInt())
                    }

    fun execute(instructions: List<Pair<String, Int>>): State {
        var state = State()
        do {
            state = executeInstruction(instructions[state.index], state)
        } while (!state.isInfiniteLoop() && state.index in (instructions.indices))
        return state
    }

    fun executeInstruction(instruction: Pair<String, Int>, state: State): State {
        return when (instruction.first) {
            "nop" -> state.next()
            "acc" -> state.accumulate(instruction.second)
            "jmp" -> state.jump(instruction.second)
            else -> state
        }
    }

    private fun findRepairedInstructions(instructions: List<Pair<String, Int>>): List<Pair<String, Int>>? =
            instructions
                    .mapIndexedNotNull { index, instruction -> if (instruction.isRepairable()) index else null }
                    .map { indexToRepair -> instructions.mapIndexed { index, instruction -> if (index == indexToRepair) instruction.repair() else instruction } }
                    .firstOrNull { repairedInstructions -> !execute(repairedInstructions).isInfiniteLoop() }

    private fun Pair<String, Int>.isRepairable() = first in REPAIR_MAP.keys

    private fun Pair<String, Int>.repair() = Pair(REPAIR_MAP.getOrDefault(first, first), second)

    companion object {
        val REPAIR_MAP = mapOf(
                "nop" to "jmp",
                "jmp" to "nop"
        )
    }
}

data class State(
        val index: Int = 0,
        val executed: List<Int> = emptyList(),
        val accumulator: Int = 0
) {
    fun next() = State(index = index + 1, executed = executed + index, accumulator = accumulator)
    fun accumulate(value: Int) = State(index = index + 1, executed = executed + index, accumulator = accumulator + value)
    fun jump(offset: Int) = State(index = index + offset, executed = executed + index, accumulator = accumulator)

    fun isInfiniteLoop(): Boolean = index in executed
}
