package day18

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class Day18Spec extends Specification {
    def input = new File(getClass().getResource('/input/day18.txt').toURI()).getText()

    @Subject
    Day18 day18 = new Day18()

    def part1() {
        expect:
        day18.part1(input) == 45840336521334
    }

    def part2() {
        expect:
        day18.part2(input) == 328920644404583
    }

    @Unroll('#input = #value')
    def 'evaluate expression with precedence addition before multiplication'(String input, long value) {
        given:
        def tokens = day18.scanTokens(input)
        def parser = new Parser.Part2(tokens)
        def expression = parser.parse()

        expect:
        expression.evaluate() == value

        where:
        input                                             | value
        '1 + 2 * 3 + 4 * 5 + 6'                           | 231
        '1 + (2 * 3) + (4 * (5 + 6))'                     | 51
        '2 * 3 + (4 * 5)'                                 | 46
        '5 + (8 * 3 + 9 + 3 * 4 * 3)'                     | 1445
        '5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))'       | 669060
        '((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2' | 23340
    }

    def 'parse addition'() {
        given:
        def input = '1 + 2'
        def tokens = day18.scanTokens(input)
        def parser = new Parser.Part1(tokens)

        when:
        def expression = parser.parse()

        then:
        expression instanceof Expression.Binary
        with(expression as Expression.Binary) {
            left instanceof Expression.Literal
            (left as Expression.Literal).value == 1
            operator == '+' as char
            right instanceof Expression.Literal
            (right as Expression.Literal).value == 2
        }
    }

    def 'parse multiplication'() {
        given:
        def input = '2 * 3'
        def tokens = day18.scanTokens(input)
        def parser = new Parser.Part1(tokens)

        when:
        def expression = parser.parse()

        then:
        expression instanceof Expression.Binary
        with(expression as Expression.Binary) {
            left instanceof Expression.Literal
            (left as Expression.Literal).value == 2
            operator == '*' as char
            right instanceof Expression.Literal
            (right as Expression.Literal).value == 3
        }
    }

    def 'parse term with grouping'() {
        given:
        def input = '1 + (2 * 3)'
        def tokens = day18.scanTokens(input)
        def parser = new Parser.Part1(tokens)

        when:
        def expression = parser.parse()

        then:
        (expression as Expression.Binary) instanceof Expression.Binary
        (expression as Expression.Binary).left instanceof Expression.Literal
        ((expression as Expression.Binary).left as Expression.Literal).value == 1
        (expression as Expression.Binary).operator == '+' as char
        (expression as Expression.Binary).right instanceof Expression.Grouping
        ((expression as Expression.Binary).right as Expression.Grouping).expression instanceof Expression.Binary
        (((expression as Expression.Binary).right as Expression.Grouping).expression as Expression.Binary).left instanceof Expression.Literal
        ((((expression as Expression.Binary).right as Expression.Grouping).expression as Expression.Binary).left as Expression.Literal).value == 2
        (((expression as Expression.Binary).right as Expression.Grouping).expression as Expression.Binary).operator == '*' as char
        (((expression as Expression.Binary).right as Expression.Grouping).expression as Expression.Binary).right instanceof Expression.Literal
        ((((expression as Expression.Binary).right as Expression.Grouping).expression as Expression.Binary).right as Expression.Literal).value == 3
    }

    def 'parse term with precedence addition before multiplication'() {
        given:
        def input = '1 * 2 + 3'
        def tokens = day18.scanTokens(input)
        def parser = new Parser.Part2(tokens)

        when:
        def expression = parser.parse()

        then:
        (expression as Expression.Binary) instanceof Expression.Binary
        ((expression as Expression.Binary) as Expression.Binary).operator == '*' as char
        ((expression as Expression.Binary) as Expression.Binary).left instanceof Expression.Literal
        (((expression as Expression.Binary) as Expression.Binary).left as Expression.Literal).value == 1
        ((expression as Expression.Binary) as Expression.Binary).right instanceof Expression.Binary
        (((expression as Expression.Binary) as Expression.Binary).right as Expression.Binary).operator == '+' as char
        (((expression as Expression.Binary) as Expression.Binary).right as Expression.Binary).left instanceof Expression.Literal
        ((((expression as Expression.Binary) as Expression.Binary).right as Expression.Binary).left as Expression.Literal).value == 2
        (((expression as Expression.Binary) as Expression.Binary).right as Expression.Binary).right instanceof Expression.Literal
        ((((expression as Expression.Binary) as Expression.Binary).right as Expression.Binary).right as Expression.Literal).value == 3
    }
}
