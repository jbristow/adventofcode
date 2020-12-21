// Generated from Sample.g4 by ANTLR 4.9
package alt;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SampleParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2;
	public static final int
		RULE_r0 = 0, RULE_r1 = 1, RULE_r2 = 2, RULE_r3 = 3, RULE_r4 = 4, RULE_r5 = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"r0", "r1", "r2", "r3", "r4", "r5"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'a'", "'b'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Sample.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SampleParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class R0Context extends ParserRuleContext {
		public R4Context r4() {
			return getRuleContext(R4Context.class,0);
		}
		public R1Context r1() {
			return getRuleContext(R1Context.class,0);
		}
		public R5Context r5() {
			return getRuleContext(R5Context.class,0);
		}
		public R0Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r0; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).enterR0(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).exitR0(this);
		}
	}

	public final R0Context r0() throws RecognitionException {
		R0Context _localctx = new R0Context(_ctx, getState());
		enterRule(_localctx, 0, RULE_r0);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			r4();
			setState(13);
			r1();
			setState(14);
			r5();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class R1Context extends ParserRuleContext {
		public R2Context r2() {
			return getRuleContext(R2Context.class,0);
		}
		public R3Context r3() {
			return getRuleContext(R3Context.class,0);
		}
		public R1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).enterR1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).exitR1(this);
		}
	}

	public final R1Context r1() throws RecognitionException {
		R1Context _localctx = new R1Context(_ctx, getState());
		enterRule(_localctx, 2, RULE_r1);
		try {
			setState(22);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(16);
				r2();
				setState(17);
				r3();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(19);
				r3();
				setState(20);
				r2();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class R2Context extends ParserRuleContext {
		public List<R4Context> r4() {
			return getRuleContexts(R4Context.class);
		}
		public R4Context r4(int i) {
			return getRuleContext(R4Context.class,i);
		}
		public List<R5Context> r5() {
			return getRuleContexts(R5Context.class);
		}
		public R5Context r5(int i) {
			return getRuleContext(R5Context.class,i);
		}
		public R2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).enterR2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).exitR2(this);
		}
	}

	public final R2Context r2() throws RecognitionException {
		R2Context _localctx = new R2Context(_ctx, getState());
		enterRule(_localctx, 4, RULE_r2);
		try {
			setState(30);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				r4();
				setState(25);
				r4();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				r5();
				setState(28);
				r5();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class R3Context extends ParserRuleContext {
		public R4Context r4() {
			return getRuleContext(R4Context.class,0);
		}
		public R5Context r5() {
			return getRuleContext(R5Context.class,0);
		}
		public R3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).enterR3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).exitR3(this);
		}
	}

	public final R3Context r3() throws RecognitionException {
		R3Context _localctx = new R3Context(_ctx, getState());
		enterRule(_localctx, 6, RULE_r3);
		try {
			setState(38);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(32);
				r4();
				setState(33);
				r5();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(35);
				r5();
				setState(36);
				r4();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class R4Context extends ParserRuleContext {
		public R4Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r4; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).enterR4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).exitR4(this);
		}
	}

	public final R4Context r4() throws RecognitionException {
		R4Context _localctx = new R4Context(_ctx, getState());
		enterRule(_localctx, 8, RULE_r4);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class R5Context extends ParserRuleContext {
		public R5Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r5; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).enterR5(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SampleListener ) ((SampleListener)listener).exitR5(this);
		}
	}

	public final R5Context r5() throws RecognitionException {
		R5Context _localctx = new R5Context(_ctx, getState());
		enterRule(_localctx, 10, RULE_r5);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\4/\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\5\3\31\n\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4!\n\4\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\5\5)\n\5\3\6\3\6\3\7\3\7\3\7\2\2\b\2\4\6\b\n\f\2\2\2+\2\16\3\2\2\2"+
		"\4\30\3\2\2\2\6 \3\2\2\2\b(\3\2\2\2\n*\3\2\2\2\f,\3\2\2\2\16\17\5\n\6"+
		"\2\17\20\5\4\3\2\20\21\5\f\7\2\21\3\3\2\2\2\22\23\5\6\4\2\23\24\5\b\5"+
		"\2\24\31\3\2\2\2\25\26\5\b\5\2\26\27\5\6\4\2\27\31\3\2\2\2\30\22\3\2\2"+
		"\2\30\25\3\2\2\2\31\5\3\2\2\2\32\33\5\n\6\2\33\34\5\n\6\2\34!\3\2\2\2"+
		"\35\36\5\f\7\2\36\37\5\f\7\2\37!\3\2\2\2 \32\3\2\2\2 \35\3\2\2\2!\7\3"+
		"\2\2\2\"#\5\n\6\2#$\5\f\7\2$)\3\2\2\2%&\5\f\7\2&\'\5\n\6\2\')\3\2\2\2"+
		"(\"\3\2\2\2(%\3\2\2\2)\t\3\2\2\2*+\7\3\2\2+\13\3\2\2\2,-\7\4\2\2-\r\3"+
		"\2\2\2\5\30 (";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}