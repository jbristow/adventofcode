import antlr.part1.Aoc2020Part1Lexer
import antlr.part1.Aoc2020Part1Parser
import antlr.part2.Aoc2020Part2Lexer
import antlr.part2.Aoc2020Part2Parser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import util.AdventOfCode

object Day19 : AdventOfCode() {

    fun part1(): Int {

        return inputFileString.split("\n\n")[1].trim()
            .lineSequence()
            .filter {
                val cs = CharStreams.fromString(it)
                val lexer = Aoc2020Part1Lexer(cs)
                val tokens = CommonTokenStream(lexer)
                tokens.fill()
                val parser = Aoc2020Part1Parser(tokens)
                parser.removeErrorListeners()
                // parser.start()
                parser.numberOfSyntaxErrors == 0 && parser.currentToken.text == "<EOF>"
            }.count()
    }

    fun part2(): Int {
        val x = inputFileString.split("\n\n")[1].trim()
            .lineSequence()
            .filter {
                val cs = CharStreams.fromString(it)
                val lexer = Aoc2020Part2Lexer(cs)
                val tokens = CommonTokenStream(lexer)

                tokens.fill()

                val parser = Aoc2020Part2Parser(tokens)
                parser.removeErrorListeners()
                // parser.start()
                parser.numberOfSyntaxErrors == 0 && parser.currentToken.text == "<EOF>"
            }

        return x.count()
    }

    @JvmStatic
    fun main(args: Array<String>) {

        println("Day 19")
        println("\tPart 1: ${part1()}")
        println("\tPart 2: ${part2()}")
    }
}
