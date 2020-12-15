package day15

import org.apache.commons.lang3.time.StopWatch
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day15Spec extends Specification {
    def input = new File(getClass().getResource('/input/day15.txt').toURI()).getText()

    @Subject
    def day15 = new Day15()

    def part1() {
        expect:
        day15.part1(input) == 1015
    }

    def part2() {
        given:
        def stopWatch = StopWatch.createStarted()

        when:
        def result = day15.part2(input)
        stopWatch.stop()

        then:
        println("Part 2: $result (Runtime: ${stopWatch.formatTime()})")
        result == 201
    }

    def parseNumbers() {
        expect:
        day15.parseNumbers('0,3,6') == [0, 3, 6]
    }

    @Unroll('starting with #numbers, the 2020th number is #lastNumber')
    def 'play part until 2020'(List<Integer> numbers, lastNumber) {
        expect:
        day15.play(numbers, 2020).last() == lastNumber

        where:
        numbers   | lastNumber
        [0, 3, 6] | 436
        [1, 3, 2] | 1
        [2, 1, 3] | 10
        [1, 2, 3] | 27
        [2, 3, 1] | 78
        [3, 2, 1] | 438
        [3, 1, 2] | 1836
    }

    @Unroll('starting with #numbers, the 30000000th number is #lastNumber')
    def 'play part until 30000000'(List<Integer> numbers, lastNumber) {
        expect:
        day15.play(numbers, 30000000).last() == lastNumber

        where:
        numbers   | lastNumber
        [0, 3, 6] | 175594
        [1, 3, 2] | 2578
        [2, 1, 3] | 3544142
        [1, 2, 3] | 261214
        [2, 3, 1] | 6895259
        [3, 2, 1] | 18
        [3, 1, 2] | 362
    }

    @Unroll('next round of #numbers is #nextNumbers')
    def play(List<Integer> numbers, List<Integer> nextNumbers) {
        expect:
        day15.play(numbers, numbers.size() + 1) == nextNumbers

        where:
        numbers                     | nextNumbers
        [0, 3, 6]                   | [0, 3, 6, 0]
        [0, 3, 6, 0]                | [0, 3, 6, 0, 3]
        [0, 3, 6, 0, 3]             | [0, 3, 6, 0, 3, 3]
        [0, 3, 6, 0, 3, 3]          | [0, 3, 6, 0, 3, 3, 1]
        [0, 3, 6, 0, 3, 3, 1]       | [0, 3, 6, 0, 3, 3, 1, 0]
        [0, 3, 6, 0, 3, 3, 1, 0]    | [0, 3, 6, 0, 3, 3, 1, 0, 4]
        [0, 3, 6, 0, 3, 3, 1, 0, 4] | [0, 3, 6, 0, 3, 3, 1, 0, 4, 0]
    }

    @Unroll('next number of #numbers is #nextNumber')
    def nextNumber(List<Integer> numbers, int nextNumber) {
        given:
        Map<Integer, Integer> lastIndices = numbers.dropRight(1).withIndex().collectEntries { number, index -> [number, index] }

        expect:
        day15.nextNumber(numbers.last(), numbers.size() - 1, lastIndices) == nextNumber

        where:
        numbers                     | nextNumber
        [0, 3, 6]                   | 0
        [0, 3, 6, 0]                | 3
        [0, 3, 6, 0, 3]             | 3
        [0, 3, 6, 0, 3, 3]          | 1
        [0, 3, 6, 0, 3, 3, 1]       | 0
        [0, 3, 6, 0, 3, 3, 1, 0]    | 4
        [0, 3, 6, 0, 3, 3, 1, 0, 4] | 0
    }
}
