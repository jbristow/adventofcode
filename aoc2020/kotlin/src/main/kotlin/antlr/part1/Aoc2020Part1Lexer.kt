package antlr.part1

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.RuntimeMetaData
import org.antlr.v4.runtime.Vocabulary
import org.antlr.v4.runtime.VocabularyImpl
import org.antlr.v4.runtime.atn.ATN
import org.antlr.v4.runtime.atn.ATNDeserializer
import org.antlr.v4.runtime.atn.LexerATNSimulator
import org.antlr.v4.runtime.atn.PredictionContextCache
import org.antlr.v4.runtime.dfa.DFA

// Generated from Aoc2020.g4 by ANTLR 4.9
class Aoc2020Part1Lexer(input: CharStream?) : Lexer(input) {
    companion object {
        private val sharedContextCache = PredictionContextCache()
        const val T__0 = 1
        const val T__1 = 2
        var channelNames = arrayOf("DEFAULT_TOKEN_CHANNEL", "HIDDEN")
        var modeNames = arrayOf("DEFAULT_MODE")

        val ruleNames = arrayOf("T__0", "T__1")
        private val LITERAL_NAMES = arrayOf(null, "'b'", "'a'")
        private val SYMBOLIC_NAMES = arrayOf<String>()
        val VOCABULARY: Vocabulary = VocabularyImpl(LITERAL_NAMES, SYMBOLIC_NAMES)

        @Deprecated("Use {@link #VOCABULARY} instead.")
        val tokenNames: Array<String> =
            Array(
                size = SYMBOLIC_NAMES.size,
                init = {
                    when (val literal = VOCABULARY.getLiteralName(it)) {
                        null -> when (val symbolic = VOCABULARY.getSymbolicName(it)) {
                            null -> "<INVALID>"
                            else -> symbolic
                        }
                        else -> literal
                    }
                }
            )
        private const val serializedATN =
            "\u0003\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\u0002\u0004\u000b\b\u0001\u0004\u0002\t\u0002" +
                "\u0004\u0003\t\u0003\u0003\u0002\u0003\u0002\u0003\u0003\u0003\u0003\u0002\u0002\u0004\u0003\u0003\u0005\u0004\u0003\u0002\u0002\u0002\n\u0002\u0003\u0003\u0002\u0002\u0002\u0002\u0005\u0003\u0002\u0002" +
                "\u0002\u0003\u0007\u0003\u0002\u0002\u0002\u0005\t\u0003\u0002\u0002\u0002\u0007\b\u0007d\u0002\u0002\b\u0004\u0003\u0002\u0002\u0002\t\n\u0007c\u0002\u0002\n\u0006\u0003\u0002\u0002" +
                "\u0002\u0003\u0002\u0002"
        private val ATN = ATNDeserializer().deserialize(serializedATN.toCharArray())
        private val decisionToDFA: Array<DFA?> =
            Array(
                size = ATN.numberOfDecisions,
                init = { DFA(ATN.getDecisionState(it), it) }
            )

        init {
            RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION)
        }
    }

    @Deprecated("")
    override fun getTokenNames(): Array<String> = Companion.tokenNames

    override fun getVocabulary(): Vocabulary = VOCABULARY

    override fun getGrammarFileName(): String = "antlr/part1/Aoc2020.g4"

    override fun getRuleNames(): Array<String> = Companion.ruleNames

    override fun getSerializedATN(): String = Companion.serializedATN

    override fun getChannelNames(): Array<String> = Companion.channelNames

    override fun getModeNames(): Array<String> = Companion.modeNames

    override fun getATN(): ATN = ATN

    init {
        _interp = LexerATNSimulator(this, ATN, decisionToDFA, sharedContextCache)
    }
}
