package day12

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day12Spec extends Specification {
    def input = new File(getClass().getResource('/input/day12.txt').toURI()).getText()

    @Subject
    Day12 day12 = new Day12()

    def part1() {
        expect:
        day12.part1(input) == 759
    }

    def part2() {
        expect:
        day12.part2(input) == 45763
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
        instructions[0] instanceof Instruction.Forward
        instructions[0].value == 10
        instructions[1] instanceof Instruction.North
        instructions[1].value == 3
        instructions[2] instanceof Instruction.Forward
        instructions[2].value == 7
        instructions[3] instanceof Instruction.Right
        instructions[3].value == 90
        instructions[4] instanceof Instruction.Forward
        instructions[4].value == 11
    }

    def 'execute instructions with MoveShip navigation strategy'() {
        given:
        def instructions = day12.readInstructions('''
            F10
            N3
            F7
            R90
            F11
        ''')
        def startPosition = new Position(0, 0)
        def startDirection = new Position(1, 0)
        def startShip = new Ship(startPosition, startDirection, new Ship.NavigationStrategy.MoveShip())

        when:
        def ship = day12.execute(instructions, startShip)

        then:
        ship.position == new Position(17, 8)
        ship.position.manhattanDistance(startPosition) == 25
    }

    def 'execute instructions with MoveWaypoint navigation strategy'() {
        given:
        def instructions = day12.readInstructions('''
            F10
            N3
            F7
            R90
            F11
        ''')
        def startPosition = new Position(0, 0)
        def startWaypoint = new Position(10, -1)
        def startShip = new Ship(startPosition, startWaypoint, new Ship.NavigationStrategy.MoveWaypoint())

        when:
        def ship = day12.execute(instructions, startShip)

        then:
        ship.position == new Position(214, 72)
        ship.position.manhattanDistance(startPosition) == 286
    }

    @Unroll('rotate east #east, south #south by #degrees degrees results in east #expectedEast, south #expectedSouth')
    def rotate(int east, int south, int degrees, int expectedEast, int expectedSouth) {
        expect:
        new Position(east, south).rotate(degrees) == new Position(expectedEast, expectedSouth)

        where:
        east | south | degrees || expectedEast | expectedSouth
        1    | 1     | 0       || 1            | 1
        1    | 1     | 90      || -1           | 1
        1    | 1     | -90     || 1            | -1
        1    | 1     | 270     || 1            | -1
        1    | 1     | 180     || -1           | -1
    }
}
