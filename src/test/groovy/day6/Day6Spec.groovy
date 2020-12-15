package day6

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day6Spec extends Specification {

    def input = new File(getClass().getResource('/input/day6.txt').toURI()).getText()

    @Subject
    Day6 day6 = new Day6()

    def part1() {
        expect:
        day6.part1(input) == 6742
    }

    def part2() {
        expect:
        day6.part2(input) == 3447
    }

    def mapInput() {
        given:
        def input = '''
            abc
            
            a
            b
            c
            
            ab
            ac
            
            a
            a
            a
            a
            
            b
        '''

        when:
        def result = day6.mapInput(input)

        then:
        result.size() == 5

        result[0].size() == 1
        result[0][0] == 'abc'

        result[1].size() == 3
        result[1][0] == 'a'
        result[1][1] == 'b'
        result[1][2] == 'c'

        result[2].size() == 2
        result[2][0] == 'ab'
        result[2][1] == 'ac'

        result[3].size() == 4
        result[3][0] == 'a'
        result[3][1] == 'a'
        result[3][2] == 'a'
        result[3][3] == 'a'

        result[4].size() == 1
        result[4][0] == 'b'
    }

    def countInAnyGroup() {
        given:
        def groups = [['abc'], ['a', 'b', 'c'], ['ab', 'ac'], ['a', 'a', 'a', 'a'], ['b']]

        expect:
        day6.countInAnyGroup(groups) == 11
    }

    @Unroll('countInAnyString #group: #result')
    def countInAnyString(List<String> group, int result) {
        expect:
        day6.countInAnyString(group) == result

        where:
        group                | result
        ['abc']              | 3
        ['a', 'b', 'c']      | 3
        ['ab', 'ac']         | 3
        ['a', 'a', 'a', 'a'] | 1
        ['b']                | 1
    }

    def countInEveryGroup() {
        given:
        def groups = [['abc'], ['a', 'b', 'c'], ['ab', 'ac'], ['a', 'a', 'a', 'a'], ['b']]

        expect:
        day6.countInEveryGroup(groups) == 6
    }

    @Unroll('countInEveryString #group: #result')
    def countInEveryString(List<String> group, int result) {
        expect:
        day6.countInEveryString(group) == result

        where:
        group                | result
        ['abc']              | 3
        ['a', 'b', 'c']      | 0
        ['ab', 'ac']         | 1
        ['a', 'a', 'a', 'a'] | 1
        ['b']                | 1
    }
}
