package comet2casl2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Casl2Lexer {
	private ErrorTable errorTable;
	private SymbolTable symbolTable;
	public int nval;
	public String sval;
	private int line = 1;
	private BufferedReader input;
	private int peekc;
	private StringBuilder buf = new StringBuilder();

	/*
* @param 入力ストリーム,記号表,エラーテーブル*/
	public Casl2Lexer(Path path,String charset, SymbolTable symbolTable, ErrorTable errorTable) {
		try {
			BufferedReader r = Files.newBufferedReader(path, Charset.forName(charset));
			init(r);
		} catch (IOException e) {
			errorTable.printError(line, -1, "I/O EXCEPTION occurred.");//例外
		}
		this.symbolTable = symbolTable;
		this.errorTable = errorTable;
	}
	/*
* @param 入力文字列,記号表,エラーテーブル*/
	public Casl2Lexer(String input, SymbolTable symbolTable, ErrorTable errorTable) {
		StringReader sr = new StringReader(input);
		BufferedReader r = new BufferedReader(sr);
		init(r);
		this.symbolTable = symbolTable;
		this.errorTable = errorTable;
	}

	public void reset() {
		try {
			input.reset();
			line = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(BufferedReader r) {
		if (r == null) {
			throw new NullPointerException();
		}
		this.input = r;
		peekc = read();
	}

	/**
	 * Move one character; detect "end of file"
	 */
	int read() {
		try {
			return input.read();
		} catch (IOException e) {
			errorTable.printError(line, -1, "I/O EXCEPTION occurred.");//例外
			return -1;
		}
	}

	public Casl2Symbol nextToken() {
		boolean neg = false;
		int c = peekc;
		switch (c) {
			case  -1 :  try {input.close(); return Casl2Symbol.EOF;}
			catch (IOException e) {return Casl2Symbol.ERROR;}
			case ' ' : case '\t': case '\r': peekc = read();return nextToken();
			case ';' : skipToEOL();
			case '\n': peekc = read();line++; return Casl2Symbol.EOL;
			case ',' : peekc = read();return Casl2Symbol.COMMA;
			case '=' : peekc = read();return Casl2Symbol.EQUAL;
			case '\'': return STR_CONST();
			case '#' : return HEX();
			case '-' : neg = true;  c = read();
				if(!isNUMBER(c)) {
					errorTable.printError(line,1,String.valueOf((char)c));
					return Casl2Symbol.ERROR;
				}
			case '0' : case '1' : case '2' : case '3' : case '4' : case '5' :
			case '6' : case '7' : case '8' : case '9' : return NUM(c,neg);
			case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' : case 'G' :
			case 'H' : case 'I' : case 'J' : case 'K' : case 'L' : case 'M' : case 'N' :
			case 'O' : case 'P' : case 'Q' : case 'R' : case 'S' : case 'T' : case 'U' :
			case 'V' : case 'W' : case 'X' : case 'Y' : case 'Z' : return KEYWORD(c);
			default:
				errorTable.printError(line,1,String.valueOf((char) c)); //"有効な識別子ではありません。"
				skipToEOL();
				return Casl2Symbol.ERROR;
		}
	}

	boolean isNUMBER(int c) {return '0' <= c && c <= '9';}
	boolean isHEX(int c) {return (c >= 'A' && c <= 'F');}

	boolean isUPPER_CASE_LETTER(int c) {return (c >= 'A' && c <= 'Z');}

	boolean isLOWER_CASE_LETTER(int c) {return (c >= 'a' && c <= 'z');}

	boolean isSTRING(int c) {return (c >= 0x00 && c <= 0x7F)||(c >= 0xA1 && c <= 0xDF);}

	Casl2Symbol HEX() {
		int v = 0;
		int c = read();
		while (true) {
			if (isNUMBER(c))
				v = v * 16 + (c - '0');
			else if (isHEX(c))
				v = v * 16 + (c - 'A' + 10);
			else break;
			c = read();
		}
		nval = v;
		peekc = c;
		if (0 <= v && v <= 65535) {
			nval = v & 0x0000FFFF;
			errorTable.printWarning(line, 1, nval);//数値%nvalは1wordに収まらないため，16bit以降は切り捨てられました。
		}
		return Casl2Symbol.NUM_CONST;
	}

	Casl2Symbol STR_CONST() {
		buf.setLength(0);
		int c = read();
		while (true) {
			if(c =='\n' || c==-1){
				buf.insert(0,'\'');
				errorTable.printError(line,1,buf.toString());
				return Casl2Symbol.ERROR;
			}
			while (isSTRING(c)) {
				if (c != '\'') {
					buf.appendCodePoint(c);
				} else break;
				c = read();
			}
			c= read();
			if(c == '\''){
				buf.appendCodePoint(c);
			}else break;
		}
		sval = buf.toString();
		peekc = c;
		return Casl2Symbol.STR_CONST;
	}

	Casl2Symbol NUM(int c, boolean neg) {
		int v = 0;
		while (true) {
			if (isNUMBER(c))
				v = v * 10 + (c - '0');
			else break;
			c = read();
		}
		nval = neg ? -v : v;
		peekc = c;
		if (!(-32768 <= v && v <= 65535)) {
			nval = v & 0x0000FFFF;
			errorTable.printWarning(line, 0, nval);// 数値%nvalは1wordに収まらないため，16bit以降は切り捨てられました。
		}
		return Casl2Symbol.NUM_CONST;
	}

	Casl2Symbol KEYWORD(int c) {
		buf.setLength(0);
		buf.append((char) c);
		c = read();
		boolean warn = false;
		while (true) {
			if (isUPPER_CASE_LETTER(c) || isNUMBER(c)) {
				buf.append((char) c);
			} else if (c == '_') {
				warn = true;
				buf.append((char) c);
			} else if (isLOWER_CASE_LETTER(c)) {
				c = 'A' + (c - 'a');
				warn = true;
				buf.append((char) c);
			} else break;
			c = read();
		}
		sval = buf.toString();
		peekc = c;
		Casl2Symbol symbol = symbolTable.searchSymbol(sval);
		if (symbol != Casl2Symbol.LABEL){
			/*if (symbol == Casl2Symbol.GR) {
				char ch = sval.charAt(sval.length()-1);
				nval = ch- '0' ;
			}*/
			return symbol;
		}
		if (warn) errorTable.printWarning(line, 3, sval);
		if (sval.length() > 8) errorTable.printWarning(line, 4, sval);
		nval = symbolTable.getLabelID();
		return symbol;
	}

	public int getLine() {return line;}

	void skipToEOL() {
		while (peekc != '\n' && peekc != -1)
			peekc = read();
	}
}