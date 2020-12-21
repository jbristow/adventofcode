// Generated from Aoc2020Part2.g4 by ANTLR 4.9
package antlr.part2

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

class Aoc2020Part2Lexer(input: CharStream?) : Lexer(input) {
    companion object {
        private val _decisionToDFA: Array<DFA?>
        private val _sharedContextCache = PredictionContextCache()
        const val T__0 = 1
        const val T__1 = 2
        var channelNames = arrayOf(
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
        )
        var modeNames = arrayOf(
            "DEFAULT_MODE"
        )

        private fun makeRuleNames(): Array<String> {
            return arrayOf(
                "T__0", "T__1"
            )
        }

        val ruleNames = makeRuleNames()
        private fun makeLiteralNames(): Array<String?> {
            return arrayOf(
                null, "'b'", "'a'"
            )
        }

        private val _LITERAL_NAMES = makeLiteralNames()
        private fun makeSymbolicNames(): Array<String> {
            return arrayOf()
        }

        private val _SYMBOLIC_NAMES = makeSymbolicNames()
        val VOCABULARY: Vocabulary = VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES)

        @Deprecated("Use {@link #VOCABULARY} instead.")
        val tokenNames: Array<String?> = arrayOfNulls(_SYMBOLIC_NAMES.size)
        const val _serializedATN =
            "\u0003\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\u0002\u0004\u000b\b\u0001\u0004\u0002\t\u0002" +
                "\u0004\u0003\t\u0003\u0003\u0002\u0003\u0002\u0003\u0003\u0003\u0003\u0002\u0002\u0004\u0003\u0003\u0005\u0004\u0003\u0002\u0002\u0002\n\u0002\u0003\u0003\u0002\u0002\u0002\u0002\u0005\u0003\u0002\u0002" +
                "\u0002\u0003\u0007\u0003\u0002\u0002\u0002\u0005\t\u0003\u0002\u0002\u0002\u0007\b\u0007d\u0002\u0002\b\u0004\u0003\u0002\u0002\u0002\t\n\u0007c\u0002\u0002\n\u0006\u0003\u0002\u0002" +
                "\u0002\u0003\u0002\u0002"
        val _ATN: ATN = ATNDeserializer().deserialize(_serializedATN.toCharArray())

        init {
            RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION)
        }

        init {
            tokenNames.indices.forEach { i ->
                tokenNames[i] = VOCABULARY.getLiteralName(i)
                if (tokenNames[i] == null) {
                    tokenNames[i] = VOCABULARY.getSymbolicName(i)
                }
                if (tokenNames[i] == null) {
                    tokenNames[i] = "<INVALID>"
                }
            }
        }

        init {
            _decisionToDFA = arrayOfNulls(_ATN.numberOfDecisions)
            for (i in 0 until _ATN.numberOfDecisions) {
                _decisionToDFA[i] = DFA(_ATN.getDecisionState(i), i)
            }
        }
    }

    override fun getVocabulary(): Vocabulary {
        return VOCABULARY
    }

    override fun getGrammarFileName(): String {
        return "Aoc2020Part2.g4"
    }

    override fun getRuleNames(): Array<String> {
        return Companion.ruleNames
    }

    override fun getSerializedATN(): String {
        return _serializedATN
    }

    override fun getChannelNames(): Array<String> {
        return Companion.channelNames
    }

    override fun getModeNames(): Array<String> {
        return Companion.modeNames
    }

    override fun getATN(): ATN {
        return _ATN
    }

    init {
        _interp = LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache)
    }
}
