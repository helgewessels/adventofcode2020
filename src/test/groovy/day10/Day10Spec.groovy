package day10

import spock.lang.Specification
import spock.lang.Subject

class Day10Spec extends Specification {

    def input = new File(getClass().getResource('/input/day10.txt').toURI()).getText()

    @Subject
    Day10 day10 = new Day10()

    def part1() {
        expect:
        day10.part1(input) == 1885
    }

    def readNumbers() {
        given:
        def input = '''
            35
            20
            15
        '''

        when:
        def numbers = day10.readNumbers(input)

        then:
        numbers == [35, 20, 15]
    }

    def withStart() {
        given:
        def numbers = [16, 10]

        expect:
        day10.withStart(numbers, 0) == [16, 10, 0]
    }

    def withEnd() {
        given:
        def numbers = [16, 10]

        expect:
        day10.withEnd(numbers, 3) == [16, 10, 19]
    }

    def accumulateDifferences() {
        given:
        def numbers = [16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4]

        when:
        def differences = day10.accumulateDifferences(numbers)

        then:
        differences[1] == 6
        differences[3] == 4
    }

}
