package day14

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day14Spec extends Specification {
    def input = new File(getClass().getResource('/input/day14.txt').toURI()).getText()

    @Subject
    Day14 day14 = new Day14()

    def part1() {
        expect:
        day14.part1(input) == 12408060320841
    }

    def 'execute program'() {
        given:
        def program = day14.parseInstructions('''
            mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
            mem[8] = 11
            mem[7] = 101
            mem[8] = 0
        ''')

        when:
        def state = day14.execute(program)

        then:
        state.memory[7] == 101
        state.memory[8] == 64
        state.sum == 165
    }

    def 'parse WriteMask instruction'() {
        given:
        def input = 'mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X'

        when:
        def instruction = Instruction.parse(input)

        then:
        instruction instanceof Instruction.WriteMask
        (instruction as Instruction.WriteMask).mask == new Mask(68719476669, 64)
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

    def 'parse Mask'() {
        expect:
        Mask.parse('XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X') == new Mask(68719476669, 64)
    }

    @Unroll('apply mask to #value results in #result')
    def 'apply mask'(Long value, Long result) {
        given:
        def mask = Mask.parse('XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X')

        expect:
        mask.apply(value) == result

        where:
        value | result
        11    | 73
        101   | 101
        0     | 64
    }
}
