package day14

class Day14 {
    fun part1(input: String) = parseInstructions(input).execute().sum

    fun parseInstructions(input: String) = input.trimIndent()
            .split("\n")
            .map { line -> Instruction.parse(line) }

    fun List<Instruction>.execute() =
            fold(State()) { state, instruction -> instruction.execute(state) }
}

data class State(val mask: Mask = Mask(0, 0), val memory: Map<Int, Long> = emptyMap()) {
    val sum: Long
        get() = memory.values.sum()

    fun setMask(mask: Mask) = copy(mask = mask)
    fun setMemory(index: Int, value: Long) = copy(memory = memory + mapOf(index to value))
}

sealed class Instruction {
    companion object {
        @JvmStatic
        fun parse(string: String): Instruction = when {
            string.startsWith("mask") -> WriteMask(Mask.parse("""mask\s*=\s*([X10]+)""".toRegex().find(string)!!.groupValues[1]))
            string.startsWith("mem") -> {
                val groupValues = """mem\[(\d+)]\s*=\s*(\d+)""".toRegex().find(string)!!.groupValues
                WriteMemory(index = groupValues[1].toInt(), value = groupValues[2].toLong())
            }
            else -> throw IllegalArgumentException("invalid instruction")
        }
    }

    abstract fun execute(state: State): State

    class WriteMask(val mask: Mask) : Instruction() {
        override fun execute(state: State) = state.setMask(mask)
    }

    class WriteMemory(val index: Int, val value: Long) : Instruction() {
        override fun execute(state: State) = state.setMemory(index, state.mask.apply(value))
    }

}

data class Mask(val bitmask: Long, val summand: Long) {
    companion object {
        @JvmStatic
        fun parse(string: String) = Mask(
                bitmask = string.replace('1', '0').replace('X', '1').toLong(2),
                summand = string.replace('X', '0').toLong(2)
        )
    }

    fun apply(value: Long) = bitmask.and(value) + summand
}
