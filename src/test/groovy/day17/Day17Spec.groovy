package day17

import org.apache.commons.lang3.time.StopWatch
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day17Spec extends Specification {
    def input = new File(getClass().getResource('/input/day17.txt').toURI()).getText()

    @Subject
    Day17 day17 = new Day17()

    def part1() {
        expect:
        day17.part1(input) == 213
    }

    def part2() {
        given:
        def stopWatch = StopWatch.createStarted()

        when:
        def result = day17.part2(input)
        stopWatch.stop()

        then:
        println("Part 2: $result (Runtime: ${stopWatch.formatTime()})")
        result == 1624
    }

    def 'part 1: 3 dimensions'() {
        given:
        def input = '''
            .#.
            ..#
            ###
        '''

        expect:
        day17.part1(input) == 112
    }

    def 'part 2: 4 dimensions'() {
        given:
        def input = '''
            .#.
            ..#
            ###
        '''

        expect:
        day17.part2(input) == 848
    }

    def parseActiveCubes() {
        given:
        def input = '''
            .#.
            ..#
            ###
        '''

        when:
        def activeCubes = day17.parseActiveCubes(input, 3)

        then:
        activeCubes.size() == 5
        activeCubes.contains([0, 1, 0])
        activeCubes.contains([1, 2, 0])
        activeCubes.contains([2, 0, 0])
        activeCubes.contains([2, 1, 0])
        activeCubes.contains([2, 2, 0])
    }

    def 'inactive neighbors with exactly 3 active neighbors become active'() {
        given:
        def activeCubes = day17.parseActiveCubes('''
            .#.
            ###
        ''', 2)

        when:
        def nextActiveNeighbors = day17.nextActiveNeighbors([0, 1], activeCubes)

        then:
        nextActiveNeighbors.size() == 2
        nextActiveNeighbors.contains([0, 0])
        nextActiveNeighbors.contains([0, 2])
    }

    def 'find neighbors in 1 dimension'() {
        when:
        def neighbors = day17.findNeighbors([1])

        then:
        neighbors.contains([0])
        neighbors.contains([1])
        neighbors.contains([2])
    }

    def 'find neighbors in 2 dimensions'() {
        when:
        def neighbors = day17.findNeighbors([1, 1])

        then:
        neighbors.contains([0, 0])
        neighbors.contains([0, 1])
        neighbors.contains([0, 2])
        neighbors.contains([1, 0])
        neighbors.contains([1, 1])
        neighbors.contains([1, 2])
        neighbors.contains([2, 0])
        neighbors.contains([2, 1])
        neighbors.contains([2, 2])
    }

    @Unroll('#cube2 is neighbor of #cube1: #neighbor')
    def 'cube neighbors'(List<Integer> cube1, List<Integer> cube2, boolean neighbor) {
        expect:
        day17.isNeighbor(cube1, cube2) == neighbor

        where:
        cube1     | cube2     || neighbor
        [1, 2, 3] | [2, 2, 2] || true
        [1, 2, 3] | [0, 2, 3] || true
        [1, 2, 3] | [1, 2, 5] || false
        [1, 2, 3] | [1, 2, 3] || false
    }
}
