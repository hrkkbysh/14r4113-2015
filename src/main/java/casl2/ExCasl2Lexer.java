package casl2;

import java.io.InputStreamReader;

public class ExCasl2Lexer extends Casl2Lexer{
	private int peekc;
	private int line = 1;
	private ArgTbl argTbl;

	/*Unicode専用.
* @param 入力ストリーム*/
	public ExCasl2Lexer(InputStreamReader is, ExSymbolTable symbolTable, ErrorTable errorTable,ArgTbl argTbl){
		super(is,  symbolTable, errorTable);
		this.argTbl = argTbl;
	}

	@Override
	public Casl2Symbol nextToken(){
		boolean neg = false;
		int c = peekc;
		switch (c) {
			case  -1 : return Casl2Symbol.EOF;
			case ' ' : case '\t': case '\r': peekc = read();return nextToken();
			case ';' : skipToEOL();
			case '\n': peekc = read();line++; return Casl2Symbol.EOL;
			case ',' : peekc = read();return Casl2Symbol.COMMA;
			case '=' : peekc = read();return Casl2Symbol.EQUAL;
			case '\'': return STR_CONST();
			case '#' : return HEX();
			case '-' : neg = true;  c = read(); if(!isNUMBER(c)) return Casl2Symbol.ERROR;
			case '0' : case '1' : case '2' : case '3' : case '4' : case '5' :
			case '6' : case '7' : case '8' : case '9' : return NUM(c,neg);
			case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' : case 'G' :
			case 'H' : case 'I' : case 'J' : case 'K' : case 'L' : case 'M' : case 'N' :
			case 'O' : case 'P' : case 'Q' : case 'R' : case 'S' : case 'T' : case 'U' :
			case 'V' : case 'W' : case 'X' : case 'Y' : case 'Z' : return KEYWORD(c);

			case '$' : return MACRO_ARG(c);

			default:

				getErrTbl().writeError(line, 1, Character.getName(c));//"有効な識別子ではありません。"
				peekc = read();
				skipToEOL();
				return Casl2Symbol.ERROR;
		}
	}

	Casl2Symbol MACRO_ARG(int c) {
		StringBuilder buf = new StringBuilder();
		buf.append((char)c);
		c = read();
		while(true){
			if(isUPPER_CASE_LETTER(c)|| isNUMBER(c)){
				buf.append((char)c);
			}else if(c=='_'){
				buf.append((char)c);
			}else if(isLOWER_CASE_LETTER(c)){
				c = 'A' + (c - 'a');
				buf.append((char)c);
			}else break;
			c = read();
		}
		super.setSval(buf.toString());
		peekc = c;
		if(getSval().length()>8) super.getErrTbl().writeWarning(line, 4, getSval());
		super.setNval(argTbl.registerAndGetArgID(getSval()));

		return Casl2Symbol.MACRO_ARG;
	}
}