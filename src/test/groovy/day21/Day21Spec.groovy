package day21

import spock.lang.Specification
import spock.lang.Subject

class Day21Spec extends Specification {
    def input = new File(getClass().getResource('/input/day21.txt').toURI()).getText()

    @Subject
    Day21 day21 = new Day21()

    def part1() {
        expect:
        day21.part1(input) == 2461
    }

    def part2() {
        expect:
        day21.part2(input) == 'ltbj,nrfmm,pvhcsn,jxbnb,chpdjkf,jtqt,zzkq,jqnhd'
    }

    def 'part 1 for 4 foods'() {
        given:
        def input = '''
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
        '''

        expect:
        day21.part1(input) == 5
    }

    def 'part 2 for 4 foods'() {
        given:
        def input = '''
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
        '''

        expect:
        day21.part2(input) == 'mxmxvkd,sqjhc,fvjkl'
    }

    def 'parse food'() {
        given:
        def input = '''
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
        '''

        when:
        def foods = day21.parseFoods(input)

        then:
        foods.size() == 4
        foods.contains(new Food(['mxmxvkd', 'kfcds', 'sqjhc', 'nhms'] as Set, ['dairy', 'fish'] as Set))
        foods.contains(new Food(['trh', 'fvjkl', 'sbzzf', 'mxmxvkd'] as Set, ['dairy'] as Set))
        foods.contains(new Food(['sqjhc', 'fvjkl'] as Set, ['soy'] as Set))
        foods.contains(new Food(['sqjhc', 'mxmxvkd', 'sbzzf'] as Set, ['fish'] as Set))
    }

    def 'find ingredients without allergen'() {
        given:
        def input = '''
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
        '''
        def foods = day21.parseFoods(input)

        when:
        def ingredients = day21.findIngredientsWithoutAllergen(foods)

        then:
        ingredients.size() == 4
        ingredients.contains('kfcds')
        ingredients.contains('nhms')
        ingredients.contains('sbzzf')
        ingredients.contains('trh')
    }

    def 'find ingredients with allergen'() {
        given:
        def input = '''
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
        '''
        def foods = day21.parseFoods(input)

        when:
        def ingredients = day21.findIngredientsWithAllergen(foods)

        then:
        ingredients.size() == 3
        ingredients['mxmxvkd'] == 'dairy'
        ingredients['sqjhc'] == 'fish'
        ingredients['fvjkl'] == 'soy'
    }

    def 'find allergens with ingredients'() {
        given:
        def input = '''
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
        '''
        def foods = day21.parseFoods(input)

        when:
        def allergens = day21.findAllergens(foods)

        then:
        allergens.size() > 0

        allergens['dairy'].size() == 1
        allergens['dairy'].contains('mxmxvkd')

        allergens['fish'].size() == 2
        allergens['fish'].contains('sqjhc')
        allergens['fish'].contains('mxmxvkd')

        allergens['soy'].size() == 2
        allergens['soy'].contains('sqjhc')
        allergens['soy'].contains('fvjkl')
    }
}
