package day9

import spock.lang.Specification
import spock.lang.Subject

class Day9Spec extends Specification {

    def input = new File(getClass().getResource('/input/day9.txt').toURI()).getText()

    @Subject
    Day9 day9 = new Day9()

    def part1() {
        expect:
        day9.part1(input, 25) == 50047984
    }

    def part2() {
        expect:
        day9.part2(input, 25) == 5407707
    }

    def readNumbers() {
        given:
        def input = '''
            35
            20
            15
        '''

        when:
        def numbers = day9.readNumbers(input)

        then:
        numbers == [35, 20, 15] as List<Long>
    }

    def findFirstInvalid() {
        given:
        def offset = 5
        def numbers = [
                35L,
                20L,
                15L,
                25L,
                47L,
                40L,
                62L,
                55L,
                65L,
                95L,
                102L,
                117L,
                150L,
                182L,
                127L,
                219L,
                299L,
                277L,
                309L,
                576L
        ]

        expect:
        day9.findFirstInvalid(numbers, offset) == 127
    }

    def findWindowBySum() {
        def sum = 127L
        def numbers = [
                35L,
                20L,
                15L,
                25L,
                47L,
                40L,
                62L,
                55L,
                65L,
                95L,
                102L,
                117L,
                150L,
                182L,
                127L,
                219L,
                299L,
                277L,
                309L,
                576L
        ]

        expect:
        day9.findWindowBySum(numbers, sum) == [15L, 25L, 47L, 40L]
    }

    def isValid() {
        given:
        def offset = 5
        def numbers = [
                35L,
                20L,
                15L,
                25L,
                47L,
                40L,
                62L,
                55L,
                65L,
                95L,
                102L,
                117L,
                150L,
                182L,
                127L,
                219L,
                299L,
                277L,
                309L,
                576L
        ]

        expect:
        [5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19].each {
            assert day9.isValid(it, numbers, offset)
        }
        !day9.isValid(14, numbers, offset)
    }

    def allPairs() {
        expect:
        day9.allPairs([1L, 2L, 3L]) == [[1L, 2L], [1L, 3L], [2L, 3L]]
    }

    def allWindows() {
        expect:
        day9.allWindows([1L, 2L, 3L]) == [[1L, 2L], [1L, 2L, 3L], [2L, 3L]]
    }
}
