package day10

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day10Spec extends Specification {

    def input = new File(getClass().getResource('/input/day10.txt').toURI()).getText()

    @Subject
    Day10 day10 = new Day10()

    def part1() {
        expect:
        day10.part1(input) == 1885
    }

    def part2() {
        expect:
        day10.part2(input) == 2024782584832
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

    @Unroll('#numbers has #distinctValidArrangements distinct valid arrangements')
    def countDistinctValidArrangements(List<Integer> numbers, long distinctValidArrangements) {
        expect:
        day10.countDistinctValidArrangements(numbers) == distinctValidArrangements

        where:
        numbers                                                                                                                      | distinctValidArrangements
        [0, 16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4, 22]                                                                               | 8
        [0, 28, 33, 18, 42, 31, 14, 46, 20, 48, 47, 24, 23, 49, 45, 19, 38, 39, 11, 1, 32, 25, 35, 8, 17, 7, 9, 4, 2, 34, 10, 3, 52] | 19208
    }

    def 'numbers to differences'() {
        expect:
        day10.toDifferences([0, 16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4, 22]) == [1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 3, 3]
    }

    def 'split numbers'() {
        expect:
        day10.split([1, 1, 3, 1, 3, 3, 1, 1, 1], 3) == [[1, 1], [1], [], [1, 1, 1]]
    }

    @Unroll('#ones has #options options')
    def 'number of options in chunk of ones'(List<Integer> ones, long options) {
        expect:
        day10.numberOfOptions(ones.size()) == options

        where:
        ones            | options
        [1]             | 1
        [1, 1]          | 2
        [1, 1, 1]       | 4
        [1, 1, 1, 1]    | 7
        [1, 1, 1, 1, 1] | 13
    }

}
