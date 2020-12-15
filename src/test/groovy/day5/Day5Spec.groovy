package day5

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day5Spec extends Specification {

    def input = new File(getClass().getResource('/input/day5.txt').toURI()).getText()

    @Subject
    Day5 day5 = new Day5()

    def part1() {
        expect:
        day5.part1(input) == 906
    }

    def part2() {
        expect:
        day5.part2(input) == 519
    }

    @Unroll('#seat: ID #id')
    def seatId(String seat, int id) {
        expect:
        day5.getSeatId(seat) == id

        where:
        seat         || id
        'FBFBBFFRLR' || 357
        'BFFFBBFRRR' || 567
        'FFFBBBFRRR' || 119
        'BBFFBBFRLL' || 820
    }

    @Unroll('#seat: row #row')
    def row(String seat, int row) {
        expect:
        day5.getRow(seat) == row

        where:
        seat         || row
        'FBFBBFFRLR' || 44
        'BFFFBBFRRR' || 70
        'FFFBBBFRRR' || 14
        'BBFFBBFRLL' || 102
    }

    @Unroll('#seat: column #column')
    def column(String seat, int column) {
        expect:
        day5.getColumn(seat) == column

        where:
        seat         || column
        'FBFBBFFRLR' || 5
        'BFFFBBFRRR' || 7
        'FFFBBBFRRR' || 7
        'BBFFBBFRLL' || 4
    }
}
