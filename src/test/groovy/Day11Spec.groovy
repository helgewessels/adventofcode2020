import spock.lang.Specification

class Day11Spec extends Specification {

    def input = new File(getClass().getResource('/input/day11.txt').toURI()).getText()

    Day11 day11 = new Day11()
    SeatStrategy part1SeatStrategy = new SeatStrategy.Part1()
    SeatStrategy part2SeatStrategy = new SeatStrategy.Part2()

    def part1() {
        when:
        def result = day11.part1(input)

        then:
        println("Part 1: $result")
        result == 2476
    }

    def part2() {
        when:
        def result = day11.part2(input)

        then:
        println("Part 2: $result")
        result == 2257
    }

    def readField() {
        given:
        def input = '''
            L.L
            L#L
            LLL
        '''

        when:
        def field = day11.readField(input)

        then:
        field.size() == 3
        field == [
                [
                        new Cell.EmptySeat(0, 0),
                        new Cell.Floor(0, 1),
                        new Cell.EmptySeat(0, 2)
                ],
                [
                        new Cell.EmptySeat(1, 0),
                        new Cell.OccupiedSeat(1, 1),
                        new Cell.EmptySeat(1, 2)
                ],
                [
                        new Cell.EmptySeat(2, 0),
                        new Cell.EmptySeat(2, 1),
                        new Cell.EmptySeat(2, 2)
                ]
        ]
    }

    def 'Part 1 countLastOccupiedSeats'() {
        def input = '''
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
        '''
        def field = day11.readField(input)

        expect:
        day11.countLastOccupiedSeats(field, part1SeatStrategy) == 37
    }

    def 'Part 2 countLastOccupiedSeats'() {
        def input = '''
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
        '''
        def field = day11.readField(input)

        expect:
        day11.countLastOccupiedSeats(field, part2SeatStrategy) == 26
    }

    def 'Part 1 findAdjacentSeats'() {
        given:
        def field = emptySeats(4, 4)
        def cell = new Cell.EmptySeat(1, 1)

        when:
        def adjacentCells = part1SeatStrategy.findAdjacentSeats(cell, field)

        then:
        adjacentCells.size() == 8
        adjacentCells.contains(new Cell.EmptySeat(0, 0))
        adjacentCells.contains(new Cell.EmptySeat(0, 1))
        adjacentCells.contains(new Cell.EmptySeat(0, 2))
        adjacentCells.contains(new Cell.EmptySeat(1, 0))
        adjacentCells.contains(new Cell.EmptySeat(1, 2))
        adjacentCells.contains(new Cell.EmptySeat(2, 0))
        adjacentCells.contains(new Cell.EmptySeat(2, 1))
        adjacentCells.contains(new Cell.EmptySeat(2, 2))
    }

    def 'Part 1 findAdjacentSeats in corner'() {
        given:
        def field = emptySeats(3, 3)
        def cell = new Cell.EmptySeat(0, 0)

        when:
        def adjacentCells = part1SeatStrategy.findAdjacentSeats(cell, field)

        then:
        adjacentCells.size() == 3
        adjacentCells.contains(new Cell.EmptySeat(0, 1))
        adjacentCells.contains(new Cell.EmptySeat(1, 0))
        adjacentCells.contains(new Cell.EmptySeat(1, 1))
    }

    def 'Part 1 empty seat becomes occupied if there are no occupied seats adjacent to it'() {
        given:
        def field = emptySeats(3, 3)
        def emptySeat = new Cell.EmptySeat(1, 1)

        expect:
        emptySeat.applyRules(field, part1SeatStrategy) == new Cell.OccupiedSeat(1, 1)
    }

    def 'Part 1 empty seat does not change if there is an occupied seats adjacent to it'() {
        given:
        def field = [
                [
                        new Cell.OccupiedSeat(0, 0),
                        new Cell.EmptySeat(0, 1),
                        new Cell.EmptySeat(0, 2)
                ],
                [
                        new Cell.EmptySeat(1, 0),
                        new Cell.EmptySeat(1, 1),
                        new Cell.EmptySeat(1, 2)],
                [
                        new Cell.EmptySeat(2, 0),
                        new Cell.EmptySeat(2, 1),
                        new Cell.EmptySeat(2, 2)
                ]
        ]
        def emptySeat = new Cell.EmptySeat(1, 1)

        expect:
        emptySeat.applyRules(field, part1SeatStrategy) == emptySeat
    }

    def 'Part 1 occupied seat does becomes empty if there are 4 occupied seats adjacent to it'() {
        given:
        def field = [
                [
                        new Cell.OccupiedSeat(0, 0),
                        new Cell.OccupiedSeat(0, 1),
                        new Cell.OccupiedSeat(0, 2)
                ],
                [
                        new Cell.OccupiedSeat(1, 0),
                        new Cell.OccupiedSeat(1, 1),
                        new Cell.EmptySeat(1, 2)
                ],
                [
                        new Cell.EmptySeat(2, 0),
                        new Cell.EmptySeat(2, 1),
                        new Cell.EmptySeat(2, 2)
                ]
        ]
        def occupiedSeat = new Cell.OccupiedSeat(1, 1)

        expect:
        occupiedSeat.applyRules(field, part1SeatStrategy) == new Cell.EmptySeat(1, 1)
    }

    def 'Part 1 occupied seat does not change if there are less than 4 occupied seats adjacent to it'() {
        given:
        def field = [
                [
                        new Cell.OccupiedSeat(0, 0),
                        new Cell.OccupiedSeat(0, 1),
                        new Cell.OccupiedSeat(0, 2)
                ],
                [
                        new Cell.EmptySeat(1, 0),
                        new Cell.OccupiedSeat(1, 1),
                        new Cell.EmptySeat(1, 2)
                ],
                [
                        new Cell.EmptySeat(2, 0),
                        new Cell.EmptySeat(2, 1),
                        new Cell.EmptySeat(2, 2)
                ]
        ]
        def occupiedSeat = new Cell.OccupiedSeat(1, 1)

        expect:
        occupiedSeat.applyRules(field, part1SeatStrategy) == occupiedSeat
    }

    def 'Part 2 findAdjacentSeats sees only next non floor cell in any direction'() {
        given:
        def field = day11.readField('''
            .............
            .L.L.#.#.#.#.
            .............
        ''')
        def cell = new Cell.EmptySeat(1, 1)

        expect:
        part2SeatStrategy.findAdjacentSeats(cell, field) == [new Cell.EmptySeat(1, 3)]
    }

    def 'Part 2 findAdjacentSeats does not see floor cells'() {
        given:
        def field = day11.readField('''
            .##.##.
            #.#.#.#
            ##...##
            ...L...
            ##...##
            #.#.#.#
            .##.##.
        ''')
        def cell = new Cell.EmptySeat(3, 3)

        expect:
        part2SeatStrategy.findAdjacentSeats(cell, field) == []
    }

    private static List<List<Cell.EmptySeat>> emptySeats(int rows, int columns) {
        return (0..rows - 1).collect { row -> (0..columns - 1).collect { column -> new Cell.EmptySeat(row, column) } }
    }
}
