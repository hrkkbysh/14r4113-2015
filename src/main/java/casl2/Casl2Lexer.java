package casl2;

import assembler.Lexer;
import static casl2.Casl2Symbol.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Casl2Lexer implements Lexer{

	private String input; // input string
	private int position = 0;    // index into input of current character
	private char c;       // current character
	private int line = 1;
	public static final char EOF = (char)-1; //  represent end of file char
	private ErrorTable errorTable;

	/*デフォルトエンコーディングは使わないでください。
	* @param 入力文字列*/
	public Casl2Lexer(String input) {
		this.input = input;
		c = input.charAt(position); // prime lookahead
		errorTable = ErrorTable.getInstance();
	}

	@Override
	public Casl2Token nextToken(){
		while ( c!= EOF) {
			switch ( c ) {
				case ' ' : case '\t': case '\r': WS(); continue;
				case ';' : skipToNextLine();
				case '\n': consume() ; line++; return new Casl2Token(EOL,"");
				case ',' : consume() ; return new Casl2Token(COMMA,"");
				case '=' : consume() ; return new Casl2Token(EQUAL,"");
				case '\'':			   return STR_CONST();
				case '#' :			   return HEX();
				default:
					if ( isKEYWORD()) return KEYWORD();
					if ( isNUMBER() ) return NUMBER();
					Casl2Token token = new Casl2Token(ERROR,String.valueOf(c));
					errorTable.writeTemp(line,token,"有効な識別子ではありません。");
					consume();
					return new Casl2Token(ERROR,"");
			}
		}
		return new Casl2Token(Casl2Symbol.EOF,"");
	}
	/** Move to next line character */
	public void skipToNextLine() {
		while(c!='\n') advance();
	}

	/** Move to next non-whitespace character */
	public void consume() { advance(); WS();}

	/** Move one character; detect "end of file" */
	public void advance() {
		position++;
		if ( position>= input.length() ) c = EOF;
		else c = input.charAt(position);
	}

	/** Ensure x is next character on the input stream */
	public boolean match(char x) {
		if ( c == x){ consume();return true;}
		else {return false;}
	}

	/** WS : (' '|'\t'|'\r')* ; // ignore any whitespace */
	private void WS() { while (isSPACE()) advance(); }

	private boolean isNUMBER() {return '0'<=c && c<='9';}
	private boolean isHEX(){return isNUMBER()||(c>='A' && c<='F');}
	private boolean isKEYWORD(){return (c>='A'&&c<='Z');}
	private boolean isSPACE(){return c==' ' || c=='\t' || c=='\r';}
	private boolean isSTRING(String candidate){
		Pattern pattern = Pattern.compile("[\\x00-\\x7F\\xA1-\\xDF]+");
		Matcher matcher = pattern.matcher(candidate);
		return matcher.matches();
	}
/*
	private String readBuf(boolean condition){
		StringBuilder buf = new StringBuilder();

		do{buf.append(c);advance();} while(!condition);
		return buf.toString();
	}*/

	private Casl2Token HEX() {
		StringBuilder buf = new StringBuilder();
		do{buf.append(c);advance();} while(isHEX());
		String value = buf.toString();
		consume();
		Integer numerical = Integer.parseInt(value.substring(1,value.length()),16);
		if(numerical<=65535)
			return new Casl2Token(NUM_CONST , numerical.toString() );
		Casl2Token token = new Casl2Token(ERROR,value);
		errorTable.writeTemp(line,token,"有効な識別子ではありません。");
		return token;
	}

	private Casl2Token STR_CONST(){
		advance();
		StringBuilder buf = new StringBuilder();
		while(c!=EOF){
			while (c != '\'') {
				if(c=='\n'){ return new Casl2Token(ERROR, buf.toString());}
				buf.append(c);advance();
			}
			advance();
			if (c != '\'') break;
			else {buf.append(c);advance();}
		}
		String candidate = buf.toString();
		if(isSTRING(candidate)) {return new Casl2Token(STR_CONST,buf.toString());}
		Casl2Token token = new Casl2Token(ERROR,candidate);
		errorTable.writeTemp(line,token,"有効な識別子ではありません。");
		return token;
	}

	private Casl2Token KEYWORD() {
		StringBuilder buf = new StringBuilder();
		do{buf.append(c);advance();} while(isKEYWORD() || isNUMBER());
		String keyword = buf.toString();
		Casl2Symbol symbol = SymbolTable.searchSymbol(keyword);
		return new Casl2Token(symbol,keyword);
	}

	private Casl2Token NUMBER() {
		StringBuilder buf = new StringBuilder();
		do{buf.append(c);advance();} while(isNUMBER());
		String value = buf.toString();
		Integer numerical = Integer.parseInt(value);
		if(numerical<=65535)
			return new Casl2Token(NUM_CONST , value );
		Casl2Token token = new Casl2Token(ERROR,value);
		errorTable.writeTemp(line,token,"有効な識別子ではありません。");
		return token;
	}
}