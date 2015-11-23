package casl2;

import assembler.Lexer;
import assembler.Token;
import static casl2.Casl2Symbol.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Casl2Lexer implements Lexer{

	private String input; // input string
	private int position = 0;    // index into input of current character
	private char c;       // current character
	private int line = 1;
	public static final char EOF = (char)-1; //  represent end of file char

	public Casl2Lexer(String input) {
		this.input = input;
		c = input.charAt(position); // prime lookahead
	}

	public Token nextToken(){
		while ( c!= EOF) {
			switch ( c ) {
			case ' ' : case '\t': case '\r': WS(); continue;
			case ';' : skipToNextLine();
			case '\n': consume() ; return new Casl2Token(EOL,"\n",line++);
			case ',' : consume() ; return new Casl2Token(COMMA,",",line);
			case '=' : consume() ; return new Casl2Token(EQUAL,"=",line);
			case '\'':			   return STR_CONST();
			case '#' :			   return HEX();
			default:
				if ( isNUMBER() ) return NUMBER();
				if ( isKEYWORD()) return KEYWORD();
				Token token =  new Casl2Token(ERROR, String.valueOf(c), line);
				advance();
				return token;
			}
		}
		return new Casl2Token(Casl2Symbol.EOF,String.valueOf(c),line);
	}

	private void skipToNextLine() {
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
	public void match(char x) {
		if ( c == x) consume();
		else throw new Error("expecting "+x+"; found "+c);
	}

	/** WS : (' '|'\t'|'\r')* ; // ignore any whitespace */
	private void WS() { while (isSPACE()) advance(); }

	private boolean isNUMBER() {return '0'<=c && c<='9';}
	private boolean isHEX(){return isNUMBER()||(c>='A' && c<='F');}
	private boolean isKEYWORD(){return (c>='A'&&c<='Z')|| isNUMBER();}
	private boolean isSPACE(){return c==' ' || c=='\t' || c=='\r';}
	private boolean isMOJITEISU(String cand){
		Pattern pattern = Pattern.compile("[\\x21-\\x7E\\xA1-\\xDF]+");
		Matcher matcher = pattern.matcher(cand);
		return matcher.matches();
	}
/*
	private String readBuf(boolean condition){
		StringBuilder buf = new StringBuilder();

		do{buf.append(c);advance();} while(!condition);
		return buf.toString();
	}*/

	private Token HEX() {
		StringBuilder buf = new StringBuilder();
		do{buf.append(c);advance();} while(isHEX());
		String value = buf.toString();
		consume();
		Integer numerical = Integer.parseInt(value.substring(1,value.length()),16);
		if(numerical<=65535)
			return new Casl2Token(NUM_CONST , numerical.toString() , line);
		return new Casl2Token(ERROR, String.valueOf(c), line);
	}

	private Token STR_CONST(){
		advance();
		StringBuilder buf = new StringBuilder();
		while(c!=EOF){
			while(c!='\''){buf.append(c);advance();}
			advance();
			if(c!='\''){break;}
			else{buf.append(c);advance();}
		}
		String candidate = buf.toString();
		if(isMOJITEISU(candidate)) {return new Casl2Token(STR_CONST,buf.toString(),line);}
		return new Casl2Token(ERROR, buf.toString(), line);
	}

	private Token KEYWORD() {
		StringBuilder buf = new StringBuilder();
		do{buf.append(c);advance();} while(isKEYWORD());
		String keyword = buf.toString();
		Casl2Symbol symbol = SymbolTable.searchSymbol(keyword);
		return new Casl2Token(symbol,keyword,line);
	}

	private Token NUMBER() {
		StringBuilder buf = new StringBuilder();
		do{buf.append(c);advance();} while(isNUMBER());
		String value = buf.toString();
		Integer numerical = Integer.parseInt(value);
		if(numerical<=65535)
			return new Casl2Token(NUM_CONST , numerical.toString() , line);
		return new Casl2Token(ERROR, String.valueOf(c), line);
	}
}