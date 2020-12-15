package day8

import kotlin.Pair
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day8Spec extends Specification {

    def input = new File(getClass().getResource('/input/day8.txt').toURI()).getText()

    @Subject
    Day8 day8 = new Day8()

    def part1() {
        expect:
        day8.part1(input) == 2014
    }

    def part2() {
        expect:
        day8.part2(input) == 2251
    }

    def readInstructions() {
        given:
        def input = '''
            nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6
        '''

        when:
        def instructions = day8.readInstructions(input)

        then:
        instructions.size() == 9
        instructions[0] == new Pair('nop', 0)
        instructions[1] == new Pair('acc', 1)
        instructions[2] == new Pair('jmp', 4)
        instructions[3] == new Pair('acc', 3)
        instructions[4] == new Pair('jmp', -3)
        instructions[5] == new Pair('acc', -99)
        instructions[6] == new Pair('acc', 1)
        instructions[7] == new Pair('jmp', -4)
        instructions[8] == new Pair('acc', 6)
    }

    def execute() {
        given:
        def instructions = [
                new Pair('nop', 0),
                new Pair('acc', 1),
                new Pair('jmp', 4),
                new Pair('acc', 3),
                new Pair('jmp', -3),
                new Pair('acc', -99),
                new Pair('acc', 1),
                new Pair('jmp', -4),
                new Pair('acc', 6)
        ]

        when:
        def state = day8.execute(instructions)

        then:
        state.index == 1
        state.accumulator == 5
    }

    @Unroll('executing "#instruction #argument" with index #index and accumulator #accumulator results in index #expectedIndex and accumulator #expectedAccumulator')
    def executeInstruction(String instruction, int argument, int index, int accumulator, int expectedIndex, int expectedAccumulator) {
        when:
        def state = day8.executeInstruction(new Pair<>(instruction, argument), new State(index, [], accumulator))

        then:
        state.executed == [index]
        state.index == expectedIndex
        state.accumulator == expectedAccumulator

        where:
        instruction | argument | index | accumulator || expectedIndex | expectedAccumulator
        'nop'       | 0        | 0     | 0           || 1             | 0
        'nop'       | 0        | 0     | 42          || 1             | 42
        'acc'       | 1        | 1     | 0           || 2             | 1
        'acc'       | 1        | 1     | 42          || 2             | 43
        'jmp'       | 4        | 2     | 0           || 6             | 0
        'jmp'       | 4        | 2     | 42          || 6             | 42
        'jmp'       | -1       | 2     | 0           || 1             | 0
        'jmp'       | -1       | 2     | 42          || 1             | 42
    }

}
