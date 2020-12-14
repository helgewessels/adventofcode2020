package day14

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day14Spec extends Specification {
    def input = new File(getClass().getResource('/input/day14.txt').toURI()).getText()

    @Subject
    def day14 = new Day14()
    def writeMaskedValueStrategy = new Instruction.WriteMemoryStrategy.WriteMaskedValue()
    def writeToMaskedAddressesStrategy = new Instruction.WriteMemoryStrategy.WriteToMaskedAddresses()

    def part1() {
        expect:
        day14.part1(input) == 12408060320841
    }

    def part2() {
        expect:
        day14.part2(input) == 4466434626828
    }

    def 'execute program with WriteMaskedValue strategy'() {
        given:
        def program = day14.parseInstructions('''
            mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
            mem[8] = 11
            mem[7] = 101
            mem[8] = 0
        ''')

        when:
        def state = day14.execute(program, writeMaskedValueStrategy)

        then:
        state.memory[7L] == 101
        state.memory[8L] == 64
        state.sum == 165
    }

    def 'execute program with WriteToMaskedAddresses strategy'() {
        given:
        def program = day14.parseInstructions('''
            mask = 000000000000000000000000000000X1001X
            mem[42] = 100
            mask = 00000000000000000000000000000000X0XX
            mem[26] = 1
        ''')

        when:
        def state = day14.execute(program, writeToMaskedAddressesStrategy)

        then:
        state.sum == 208
    }

    def 'parse WriteMask instruction'() {
        given:
        def input = 'mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X'

        when:
        def instruction = Instruction.parse(input)

        then:
        instruction instanceof Instruction.WriteMask
        (instruction as Instruction.WriteMask).mask == 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X'
    }

    def 'parse WriteMemory instruction'() {
        given:
        def input = 'mem[8] = 11'

        when:
        def instruction = Instruction.parse(input)

        then:
        instruction instanceof Instruction.WriteMemory
        (instruction as Instruction.WriteMemory).index == 8
        (instruction as Instruction.WriteMemory).value == 11
    }

    @Unroll('apply mask to #value results in #result')
    def 'WriteMaskedValue: apply mask to value'(Long value, Long result) {
        given:
        def state = new State('XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X', [:])

        expect:
        writeMaskedValueStrategy.execute(0, value, state).memory[0L] == result

        where:
        value | result
        11    | 73
        101   | 101
        0     | 64
    }

    def 'WriteToMaskedAddressesStrategy: apply mask to address'() {
        given:
        def state = new State('000000000000000000000000000000X1001X', [:])

        when:
        def result = writeToMaskedAddressesStrategy.execute(42, 100, state)

        then:
        result.memory[26L] == 100
        result.memory[27L] == 100
        result.memory[58L] == 100
        result.memory[59L] == 100
    }

    @Unroll('base value of #value with mask #mask is #baseValue')
    def 'WriteToMaskedAddressesStrategy: base value'(long value, String mask, long baseValue) {
        expect:
        writeToMaskedAddressesStrategy.baseValue(value, mask) == baseValue

        where:
        value | mask                                   || baseValue
        42    | '000000000000000000000000000000X1001X' || 2 + 8 + 16
        26    | '00000000000000000000000000000000X0XX' || 16
    }

    @Unroll('summands of mask #mask are #summands')
    def 'WriteToMaskedAddressesStrategy: summands'(String mask, List<Long> summands) {
        expect:
        writeToMaskedAddressesStrategy.summands(mask).sort() == summands

        where:
        mask                                   | summands
        '000000000000000000000000000000X1001X' | [0, 1, 32, 33]
        '00000000000000000000000000000000X0XX' | [0, 1, 2, 3, 8, 9, 10, 11]
    }

    @Unroll('floating bits of mask #mask are #floatingBits')
    def 'WriteToMaskedAddressesStrategy: floatingBits'(String mask, List<Integer> floatingBits) {
        expect:
        writeToMaskedAddressesStrategy.floatingBits(mask) == floatingBits

        where:
        mask                                   | floatingBits
        '000000000000000000000000000000X1001X' | [0, 5]
        '00000000000000000000000000000000X0XX' | [0, 1, 3]
    }
}
