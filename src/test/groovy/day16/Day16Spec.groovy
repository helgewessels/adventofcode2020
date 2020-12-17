package day16

import kotlin.ranges.IntRange
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day16Spec extends Specification {
    def input = new File(getClass().getResource('/input/day16.txt').toURI()).getText()

    @Subject
    Day16 day16 = new Day16()

    def part1() {
        expect:
        day16.part1(input) == 21071
    }

    def part2() {
        expect:
        day16.part2(input) == 3429967441937L
    }

    def 'part 1'() {
        given:
        def input = '''
            class: 1-3 or 5-7
            row: 6-11 or 33-44
            seat: 13-40 or 45-50
            
            your ticket:
            7,1,14
            
            nearby tickets:
            7,3,47
            40,4,50
            55,2,20
            38,6,12
        '''

        expect:
        day16.part1(input) == 71
    }

    def 'parse input'() {
        given:
        def input = '''
            class: 1-3 or 5-7
            row: 6-11 or 33-44
            seat: 13-40 or 45-50
            departure time: 1-2 or 2-3 
            
            your ticket:
            7,1,14
            
            nearby tickets:
            7,3,47
            40,4,50
            55,2,20
            38,6,12
        '''

        when:
        def notes = day16.parseNotes(input)

        then:
        notes.fields.size() == 4
        notes.fields[0] == new Field('class', [new IntRange(1, 3), new IntRange(5, 7)])
        notes.fields[1] == new Field('row', [new IntRange(6, 11), new IntRange(33, 44)])
        notes.fields[2] == new Field('seat', [new IntRange(13, 40), new IntRange(45, 50)])
        notes.fields[3] == new Field('departure time', [new IntRange(1, 2), new IntRange(2, 3)])

        and:
        notes.ticket == [7, 1, 14]

        and:
        notes.nearbyTickets.size() == 4
        notes.nearbyTickets[0] == [7, 3, 47]
        notes.nearbyTickets[1] == [40, 4, 50]
        notes.nearbyTickets[2] == [55, 2, 20]
        notes.nearbyTickets[3] == [38, 6, 12]
    }

    @Unroll('invalid field values of ticket #ticket are #invalidFieldValues')
    def findInvalidFieldValues(List<Integer> ticket, List<Integer> invalidFieldValues) {
        given:
        def fields = [
                new Field('class', [new IntRange(1, 3), new IntRange(5, 7)]),
                new Field('row', [new IntRange(6, 11), new IntRange(33, 44)]),
                new Field('seat', [new IntRange(13, 40), new IntRange(45, 50)])
        ]

        expect:
        day16.findInvalidFieldValues(ticket, fields) == invalidFieldValues

        where:
        ticket      | invalidFieldValues
        [7, 3, 47]  | []
        [40, 4, 50] | [4]
        [55, 2, 20] | [55]
        [38, 6, 12] | [12]
    }

    def findValidFields() {
        given:
        def fields = [
                new Field('class', [new IntRange(0, 1), new IntRange(4, 19)]),
                new Field('row', [new IntRange(0, 5), new IntRange(8, 19)]),
                new Field('seat', [new IntRange(0, 13), new IntRange(16, 19)])
        ]
        def tickets = [
                [3, 9, 18],
                [15, 1, 5],
                [5, 14, 9]
        ]

        when:
        def validFields = day16.findValidFields(tickets, fields)

        then:
        validFields[0].name == 'row'
        validFields[1].name == 'class'
        validFields[2].name == 'seat'
    }

    @Unroll('position #position is valid for #fieldNames')
    def 'find valid fields'(int position, List<String> fieldNames) {
        given:
        def fields = [
                new Field('class', [new IntRange(0, 1), new IntRange(4, 19)]),
                new Field('row', [new IntRange(0, 5), new IntRange(8, 19)]),
                new Field('seat', [new IntRange(0, 13), new IntRange(16, 19)])
        ]
        def tickets = [
                [3, 9, 18],
                [15, 1, 5],
                [5, 14, 9]
        ]

        when:
        def validatingFields = day16.findValidatingFields(tickets.collect { it[position] }, fields)

        then:
        validatingFields.size() == fieldNames.size()
        validatingFields.collect { it.name } == fieldNames

        where:
        position | fieldNames
        0        | ['row']
        1        | ['class', 'row']
        2        | ['class', 'row', 'seat']
    }
}
