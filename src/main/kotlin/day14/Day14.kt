package day14

import kotlin.math.pow

class Day14 {
    fun part1(input: String) = parseInstructions(input).execute(Instruction.WriteMemoryStrategy.WriteMaskedValue()).sum
    fun part2(input: String) = parseInstructions(input).execute(Instruction.WriteMemoryStrategy.WriteToMaskedAddresses()).sum

    fun parseInstructions(input: String) = input.trimIndent()
            .split("\n")
            .map { line -> Instruction.parse(line) }

    fun List<Instruction>.execute(writeMemoryStrategy: Instruction.WriteMemoryStrategy) =
            fold(State()) { state, instruction -> instruction.execute(state, writeMemoryStrategy) }
}

data class State(var mask: String = "", val memory: MutableMap<Long, Long> = mutableMapOf()) {
    val sum: Long
        get() = memory.values.sum()

    fun setMask(mask: String): State {
        this.mask = mask
        return this
    }

    fun setMemory(index: Long, value: Long): State {
        memory[index] = value
        return this
    }
}

sealed class Instruction {
    companion object {
        @JvmStatic
        fun parse(string: String): Instruction = when {
            string.startsWith("mask") -> WriteMask("""mask\s*=\s*([X10]+)""".toRegex().find(string)!!.groupValues[1])
            string.startsWith("mem") -> {
                val groupValues = """mem\[(\d+)]\s*=\s*(\d+)""".toRegex().find(string)!!.groupValues
                WriteMemory(index = groupValues[1].toLong(), value = groupValues[2].toLong())
            }
            else -> throw IllegalArgumentException("invalid instruction")
        }
    }

    abstract fun execute(state: State, writeMemoryStrategy: WriteMemoryStrategy): State

    class WriteMask(val mask: String) : Instruction() {
        override fun execute(state: State, writeMemoryStrategy: WriteMemoryStrategy) = state.setMask(mask)
    }

    class WriteMemory(val index: Long, val value: Long) : Instruction() {
        override fun execute(state: State, writeMemoryStrategy: WriteMemoryStrategy) = writeMemoryStrategy.execute(index, value, state)
    }

    abstract class WriteMemoryStrategy {
        abstract fun execute(index: Long, value: Long, state: State): State

        class WriteMaskedValue : WriteMemoryStrategy() {
            override fun execute(index: Long, value: Long, state: State): State {
                return state.setMemory(index, state.mask.apply(value))
            }

            private fun String.apply(value: Long): Long {
                val bitmask = replace('1', '0').replace('X', '1').toLong(2)
                val summand = replace('X', '0').toLong(2)
                return bitmask.and(value) + summand
            }
        }

        class WriteToMaskedAddresses : WriteMemoryStrategy() {
            override fun execute(index: Long, value: Long, state: State): State {
                val baseValue = baseValue(index, state.mask)
                return summands(state.mask)
                        .map { summand -> baseValue + summand }
                        .fold(state) { acc: State, address: Long -> acc.setMemory(address, value) }
            }

            private fun baseValue(value: Long, mask: String): Long {
                val bitmask = mask.map { if (it == '0') '1' else '0' }.joinToString("").toLong(2)
                val summand = mask.replace('X', '0').toLong(2)
                return bitmask.and(value) + summand
            }

            private fun summands(mask: String): List<Long> =
                    mask.floatingBits()
                            .fold(emptyList()) { summands: List<Long>, bit: Int ->
                                if (summands.isEmpty()) {
                                    listOf(0L, 2.0.pow(bit).toLong())
                                } else {
                                    summands.flatMap { listOf(it, it + 2.0.pow(bit).toLong()) }
                                }
                            }

            private fun String.floatingBits(): List<Int> =
                    reversed().mapIndexedNotNull { index, char -> if (char == 'X') index else null }
        }
    }
}
