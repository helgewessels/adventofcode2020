package day16

class Day16 {
    fun part1(input: String): Int =
            parseNotes(input).let { notes: Notes ->
                notes.nearbyTickets
                        .map { it.findInvalidFieldValues(notes.fields) }
                        .flatten()
                        .sum()
            }

    fun part2(input: String): Long =
            parseNotes(input).let { notes: Notes ->
                notes.nearbyTickets
                        .filter { it.isValid(notes.fields) }
                        .findValidFields(notes.fields)
                        .filterValues { field -> field.name.startsWith("departure") }
                        .keys
                        .fold(1L) { acc, index -> acc * notes.ticket[index] }
            }

    fun parseNotes(input: String): Notes =
            input.trimIndent().split("\n\n").let { blocks ->
                Notes(
                        fields = parseFields(blocks[0]),
                        ticket = parseTickets(blocks[1]).first(),
                        nearbyTickets = parseTickets(blocks[2])
                )
            }

    private fun parseFields(input: String): List<Field> =
            input.split("\n").map { line ->
                val groupValues = """([\w ]+):\s*(\d+)-(\d+)\s*or\s*(\d+)-(\d+)""".toRegex().find(line)!!.groupValues
                Field(
                        name = groupValues[1],
                        valueRanges = listOf(groupValues[2].toInt()..groupValues[3].toInt(), groupValues[4].toInt()..groupValues[5].toInt())
                )
            }

    private fun parseTickets(input: String): List<Ticket> =
            input.split("\n").drop(1).map { line -> line.parseNumbers() }

    private fun String.parseNumbers(): List<Int> =
            split(",").mapNotNull { it.toIntOrNull() }

    fun List<Int>.findInvalidFieldValues(fields: List<Field>): List<Int> =
            filterNot { value: Int -> fields.any { it.validates(value) } }

    fun List<Ticket>.findValidFields(fields: List<Field>): Map<Int, Field> {
        val openFields = fields.toMutableList()
        val openIndices = first().indices.toMutableList()
        val fieldsByIndex = mutableMapOf<Int, Field>()

        while (openIndices.isNotEmpty()) {
            openIndices
                    .map { index ->
                        index to findValidatingFields(values = getValuesByIndex(index), fields = openFields)
                    }
                    .filterNot { it.second.isEmpty() }
                    .minByOrNull { it.second.size }
                    ?.let { (index, validatingFields) ->
                        fieldsByIndex[index] = validatingFields.first()
                        openIndices.remove(index)
                        openFields.remove(validatingFields.first())
                    } ?: return fieldsByIndex // terminate when no fields left
        }
        return fieldsByIndex
    }

    private fun List<Ticket>.getValuesByIndex(index: Int): List<Int> =
            map { ticket -> ticket[index] }

    private fun List<Int>.isValid(fields: List<Field>): Boolean =
            all { value -> fields.any { field -> field.validates(value) } }

    fun findValidatingFields(values: List<Int>, fields: List<Field>): List<Field> =
            fields.filter { it.validatesAll(values) }
}

data class Notes(
        val fields: List<Field>,
        val ticket: Ticket,
        val nearbyTickets: List<Ticket>
)

data class Field(val name: String, val valueRanges: List<IntRange>) {
    fun validates(value: Int): Boolean = valueRanges.any { value in it }
    fun validatesAll(values: List<Int>): Boolean = values.all { value -> validates(value) }
}

typealias Ticket = List<Int>
