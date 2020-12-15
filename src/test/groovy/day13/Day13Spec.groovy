package day13

import kotlin.Pair
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day13Spec extends Specification {
    def input = new File(getClass().getResource('/input/day13.txt').toURI()).getText()

    @Subject
    Day13 day13 = new Day13()

    def part1() {
        expect:
        day13.part1(input) == 2165L
    }

    def readStartAndBusIds() {
        given:
        def input = '''
            939
            7,13,x,x,59,x,31,19
        '''

        when:
        def startAndBusIds = day13.readStartAndBusIds(input)

        then:
        startAndBusIds.first == 939L
        startAndBusIds.second == [7L, 13L, 59L, 31L, 19L]
    }

    @Unroll('next departure of bus #bus starting #start is #earliestDeparture')
    def findNextDeparture(long bus, long start, long earliestDeparture) {
        expect:
        day13.findNextDeparture(bus, start) == earliestDeparture

        where:
        bus | start || earliestDeparture
        7   | 0     || 0
        13  | 0     || 0
        59  | 59    || 59
        31  | 62    || 62
        19  | 57    || 57
        7   | 939   || 945
        13  | 939   || 949
        59  | 939   || 944
        31  | 939   || 961
        19  | 939   || 950
    }

    def findEarliestDeparture() {
        given:
        def start = 939
        def busIds = [7L, 13L, 59L, 31L, 19L]

        expect:
        day13.findEarliestDeparture(busIds, start) == new Pair(59L, 944L)
    }
}
