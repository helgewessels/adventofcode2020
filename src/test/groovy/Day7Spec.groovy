import kotlin.Pair
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day7Spec extends Specification {

    def bag = 'shiny gold'
    def input = new File(getClass().getResource('/input/day7.txt').toURI()).getText()

    @Subject
    Day7 day7 = new Day7()

    def part1() {
        when:
        def result = day7.part1(bag, input)

        then:
        println("Part 1: $result")
        result == 121
    }

    def part2() {
        when:
        def result = day7.part2(bag, input)

        then:
        println("Part 2: $result")
        result == 3805
    }

    def readRules() {
        given:
        def input = '''
            light red bags contain 1 bright white bag, 2 muted yellow bags.
            bright white bags contain 1 shiny gold bag.
            dotted black bags contain no other bags.
        '''

        when:
        def rules = day7.readRules(input)

        then:
        rules.size() == 3

        rules['light red'].size() == 2
        rules['light red'][0].first == 1
        rules['light red'][0].second == 'bright white'
        rules['light red'][1].first == 2
        rules['light red'][1].second == 'muted yellow'

        rules['bright white'].size() == 1
        rules['bright white'][0].first == 1
        rules['bright white'][0].second == 'shiny gold'

        rules['dotted black'].empty
    }

    @Unroll('#input = #bag')
    def readBag(String input, String bag) {
        expect:
        day7.readBag(input) == bag

        where:
        input               | bag
        'light red bags'    | 'light red'
        'bright white bag'  | 'bright white'
        'muted yellow bags' | 'muted yellow'
    }

    @Unroll('#input = #bag')
    def readAmountAndBag(String input, Pair<Integer, String> bag) {
        expect:
        day7.readAmountAndBag(input) == bag

        where:
        input                 | bag
        '1 bright white bag'  | new Pair<>(1, 'bright white')
        '2 muted yellow bags' | new Pair<>(2, 'muted yellow')
        'no other bags'       | null
    }

    @Unroll('#parentBag contains a shiny gold bag: #result')
    def containsBag(String parentBag, boolean result) {
        given:
        def rules = [
                'light red'   : [new Pair<>(1, 'bright white'), new Pair<>(2, 'muted yellow')],
                'dark orange' : [new Pair<>(3, 'bright white'), new Pair<>(4, 'muted yellow')],
                'bright white': [new Pair<>(1, 'shiny gold')],
                'muted yellow': [new Pair<>(2, 'shiny gold'), new Pair<>(9, 'faded blue')],
                'shiny gold'  : [new Pair<>(1, 'dark olive'), new Pair<>(2, 'vibrant plum')],
                'dark olive'  : [new Pair<>(3, 'faded blue'), new Pair<>(4, 'dotted black')],
                'vibrant plum': [new Pair<>(5, 'faded blue'), new Pair<>(6, 'dotted black')],
                'faded blue'  : [],
                'dotted black': []
        ]

        expect:
        day7.containsBag(parentBag, 'shiny gold', rules) == result

        where:
        parentBag      | result
        'light red'    | true
        'dark orange'  | true
        'bright white' | true
        'muted yellow' | true
        'shiny gold'   | false
        'dark olive'   | false
        'vibrant plum' | false
    }

    def countBagsThatContain() {
        given:
        def rules = [
                'light red'   : [new Pair<>(1, 'bright white'), new Pair<>(2, 'muted yellow')],
                'dark orange' : [new Pair<>(3, 'bright white'), new Pair<>(4, 'muted yellow')],
                'bright white': [new Pair<>(1, 'shiny gold')],
                'muted yellow': [new Pair<>(2, 'shiny gold'), new Pair<>(9, 'faded blue')],
                'shiny gold'  : [new Pair<>(1, 'dark olive'), new Pair<>(2, 'vibrant plum')],
                'dark olive'  : [new Pair<>(3, 'faded blue'), new Pair<>(4, 'dotted black')],
                'vibrant plum': [new Pair<>(5, 'faded blue'), new Pair<>(6, 'dotted black')],
                'faded blue'  : [],
                'dotted black': []
        ]

        expect:
        day7.countBagsThatContain('shiny gold', rules) == 4
    }

    def countAllIncludedBags() {
        given:
        def rules = [
                'light red'   : [new Pair<>(1, 'bright white'), new Pair<>(2, 'muted yellow')],
                'dark orange' : [new Pair<>(3, 'bright white'), new Pair<>(4, 'muted yellow')],
                'bright white': [new Pair<>(1, 'shiny gold')],
                'muted yellow': [new Pair<>(2, 'shiny gold'), new Pair<>(9, 'faded blue')],
                'shiny gold'  : [new Pair<>(1, 'dark olive'), new Pair<>(2, 'vibrant plum')],
                'dark olive'  : [new Pair<>(3, 'faded blue'), new Pair<>(4, 'dotted black')],
                'vibrant plum': [new Pair<>(5, 'faded blue'), new Pair<>(6, 'dotted black')],
                'faded blue'  : [],
                'dotted black': []
        ]

        expect:
        day7.countAllIncludedBags('shiny gold', rules) == 32
    }
}
