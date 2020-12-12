import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day12Spec extends Specification {
    def input = new File(getClass().getResource('/input/day12.txt').toURI()).getText()

    @Subject
    Day12 day12 = new Day12()

    def part1() {
        when:
        def result = day12.part1(input)

        then:
        println("Part 1: $result")
        result == 759
    }

    def readInstructions() {
        given:
        def input = '''
            F10
            N3
            F7
            R90
            F11
        '''

        when:
        def instructions = day12.readInstructions(input)

        then:
        instructions.size() == 5
        instructions[0] == new Instruction.Forward(10)
        instructions[1] == new Instruction.North(3)
        instructions[2] == new Instruction.Forward(7)
        instructions[3] == new Instruction.Right(90)
        instructions[4] == new Instruction.Forward(11)
    }

    def 'execute instructions'() {
        given:
        def instructions = day12.readInstructions('''
            F10
            N3
            F7
            R90
            F11
        ''')
        def startShipState = new ShipState(0, 0, 90)

        when:
        def shipState = day12.execute(instructions, startShipState)

        then:
        shipState.eastPosition == 17
        shipState.southPosition == 8
        shipState.manhattanDistance == 25
    }

    @Unroll('move forward by #value in direction #direction results in position east #eastPosition, south #southPosition')
    def 'forward instruction'(int direction, int value, int eastPosition, int southPosition) {
        given:
        def shipState = new ShipState(0, 0, direction)
        def instruction = new Instruction.Forward(value)

        when:
        def resultingShipState = instruction.execute(shipState)

        then:
        resultingShipState.direction == shipState.direction
        resultingShipState.eastPosition == eastPosition
        resultingShipState.southPosition == southPosition

        where:
        direction | value || eastPosition | southPosition
        0         | 0     || 0            | 0
        0         | 1     || 0            | -1
        90        | 2     || 2            | 0
        180       | 2     || 0            | 2
        270       | 2     || -2           | 0
        360       | 3     || 0            | -3
        450       | 3     || 3            | 0
        540       | 3     || 0            | 3
        630       | 3     || -3           | 0
        -90       | 4     || -4           | 0
        -180      | 4     || 0            | 4
        -270      | 4     || 4            | 0
    }
}
