package day18

class Day18 {
    fun part1(input: String): Long =
            input.trimIndent().split("\n")
                    .map { Parser.Part1(it.scanTokens()).parse().evaluate() }
                    .sum()

    fun part2(input: String): Long =
            input.trimIndent().split("\n")
                    .map { Parser.Part2(it.scanTokens()).parse().evaluate() }
                    .sum()

    private fun String.scanTokens(): CharSequence =
            trim().replace(" ", "")
}

abstract class Parser(open val tokens: CharSequence) {
    private var current = 0

    abstract fun parse(): Expression

    protected fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    protected fun peek(): Char = tokens[current]

    protected fun previous(): Char = tokens[current - 1]

    protected fun consume(type: TokenType, message: String): Char {
        if (check(type)) return advance()
        throw IllegalArgumentException("Parse error at ${peek()}: $message")
    }

    private fun check(type: TokenType): Boolean =
            !isAtEnd() && when (type) {
                TokenType.LEFT_PARENTHESIS -> peek() == '('
                TokenType.RIGHT_PARENTHESIS -> peek() == ')'
                TokenType.PLUS -> peek() == '+'
                TokenType.STAR -> peek() == '*'
                TokenType.NUMBER -> peek().toString().toLongOrNull() != null
            }

    private fun advance(): Char {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean = current >= tokens.length

    class Part1(override val tokens: CharSequence) : Parser(tokens) {
        override fun parse(): Expression = term()

        private fun term(): Expression {
            var expression = primary()
            while (match(TokenType.PLUS, TokenType.STAR)) {
                val operator = previous()
                val right = primary()
                expression = Expression.Binary(expression, operator, right)
            }
            return expression
        }

        private fun primary(): Expression {
            return when {
                match(TokenType.NUMBER) -> {
                    Expression.Literal(previous().toString().toLong())
                }
                match(TokenType.LEFT_PARENTHESIS) -> {
                    val expression = term()
                    consume(TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression.")
                    Expression.Grouping(expression)
                }
                else -> {
                    throw IllegalArgumentException("Parse error at ${peek()}: '(' or number.")
                }
            }
        }
    }

    class Part2(override val tokens: CharSequence) : Parser(tokens) {
        override fun parse(): Expression = multiplication()

        private fun multiplication(): Expression {
            var expression = addition()
            while (match(TokenType.STAR)) {
                val operator = previous()
                val right = addition()
                expression = Expression.Binary(expression, operator, right)
            }
            return expression
        }

        private fun addition(): Expression {
            var expression = primary()
            while (match(TokenType.PLUS)) {
                val operator = previous()
                val right = primary()
                expression = Expression.Binary(expression, operator, right)
            }
            return expression
        }

        private fun primary(): Expression {
            return when {
                match(TokenType.NUMBER) -> {
                    Expression.Literal(previous().toString().toLong())
                }
                match(TokenType.LEFT_PARENTHESIS) -> {
                    val expression = multiplication()
                    consume(TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression.")
                    Expression.Grouping(expression)
                }
                else -> {
                    throw IllegalArgumentException("Parse error at ${peek()}: Expect '(' or number.")
                }
            }
        }
    }
}

sealed class Expression {
    abstract fun evaluate(): Long

    data class Literal(val value: Long) : Expression() {
        override fun evaluate(): Long = value
    }

    data class Grouping(val expression: Expression) : Expression() {
        override fun evaluate(): Long = expression.evaluate()
    }

    data class Binary(val left: Expression, val operator: Char, val right: Expression) : Expression() {
        override fun evaluate(): Long = when (operator) {
            '+' -> left.evaluate() + right.evaluate()
            '*' -> left.evaluate() * right.evaluate()
            else -> throw IllegalArgumentException("invalid operator: $operator")
        }
    }
}

enum class TokenType {
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    PLUS,
    STAR,
    NUMBER
}
