package casl2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Casl2Lexer {

	private ErrorTable errorTable;//エラー出力用
	private SymbolTable symbolTable;//シンボルテーブル
	private String sval;//切り出したトークン(文字列)を格納する変数
	private int nval;//切り出したトークン(数字)を格納する変数
	private int line = 1;//現在読み込んでいるトークンの位置(行)
	private BufferedReader input;//入力ストリーム。ここからプログラムをトークンに切り出していく
	private int peekc;//現在検査している文字

	/*Unicode専用.
* @param 入力ストリーム*/
	public Casl2Lexer(BufferedReader r,SymbolTable symbolTable,ErrorTable errorTable){
		this.symbolTable = symbolTable;
		this.errorTable = errorTable;
		init(r);
	}

	/*入力ストリームを閉じる*/
	public void reset(){
		try {
			input.reset();
		} catch (IOException e) {
			errorTable.printError(line, -1, "I/O EXCEPTION occurred.");//例外
		}
	}
	/*入力ストリームから最初の文字を読み込む*/
	public void init(BufferedReader r){
		if(r == null){
			throw new NullPointerException();
		}
		this.input = r;
		peekc = read();
	}

	/** Move one character; detect "end of file" */
	int read(){
		try {
			return input.read();
		} catch (IOException e) {
			errorTable.printError(line, -1, "I/O EXCEPTION occurred.");//例外
			return -1;
		}
	}
/*トークンの切り出しを行う。切り出したトークンはnvalかsvalに格納される*/
	public Casl2Symbol nextToken(){
		boolean neg = false;
		boolean arg = false;
		int c = peekc;
		switch (c) {
			case  -1 : return Casl2Symbol.EOF;
			case ' ' : case '\t': case '\r': peekc = read(); return nextToken();
			case ';' : skipToEOL();
			case '\n': peekc = read();line++; return Casl2Symbol.EOL;
			case ',' : peekc = read();return Casl2Symbol.COMMA;
			case '=' : peekc = read();return Casl2Symbol.EQUAL;
			case '\'': return STR_CONST();
			case '#' : return HEX();
			case '-' : neg = true; c = read(); if(!isNUMBER(c)) return ERROR('-',c);
			case '0' : case '1' : case '2' : case '3' : case '4' : case '5' :
			case '6' : case '7' : case '8' : case '9' : return NUM(c,neg);
			case '$' : arg = true; c = read(); if(!isLETTERorNUMBER(c)) return  ERROR('$',c);
			case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' : case 'G' :
			case 'H' : case 'I' : case 'J' : case 'K' : case 'L' : case 'M' : case 'N' :
			case 'O' : case 'P' : case 'Q' : case 'R' : case 'S' : case 'T' : case 'U' :
			case 'V' : case 'W' : case 'X' : case 'Y' : case 'Z' : return KEYWORD(c,arg);
			default:
				return ERROR(c);
		}
	}
	/*以下、文字の種類を判定するメソッド群*/
	boolean isNUMBER(int c) {return '0'<=c && c<='9';}
	boolean isHEX(int c){return (c>='A' && c<='F');}
	boolean isUPPER_CASE_LETTER(int c){return (c>='A'&&c<='Z');}
	boolean isLOWER_CASE_LETTER(int c){return (c>='a'&&c<='z');}
	boolean isSTRING(String candidate){
		Pattern pattern = Pattern.compile("[\\x00-\\x7F\\xA1-\\xDF]{1,65535}");
		Matcher matcher = pattern.matcher(candidate);
		return matcher.matches();
	}
	boolean isLETTERorNUMBER(int c){
		return isNUMBER(c)||isUPPER_CASE_LETTER(c)||isLOWER_CASE_LETTER(c);
	}
/*16進数*/
	Casl2Symbol HEX(){
		int v = 0;
		int c = read();
		while(true){
			if(isNUMBER(c))
				v = v * 16 + (c - '0');
			else if(isHEX(c))
				v = v * 16 + (c - 'A' + 10);
			else break;
			c = read();
		}
		nval = v;
		peekc = c;
		if(0<=v && v<=65535) {
			nval = v & 0x0000FFFF;
			errorTable.printWarning(line, 1,nval);//数値%nvalは1wordに収まらないため，16bit以降は切り捨てられました。
		}
		return Casl2Symbol.NUM_CONST;
	}

	/* 文字列 */
	Casl2Symbol STR_CONST() {
		int c = read();
		StringBuilder buf = new StringBuilder();
		do{
			while (c != '\'') {
				if (c == '\n') {
					sval = buf.toString();
					return ERROR(sval.codePoints().toArray());
				}
				buf.append((char) c);
				c = read();
			}
			c = read();

			//エスケープ文字(')の検査
			if (c != '\'') break;
			else {
				buf.append((char) c);
				c = read();
			}
		}while(true);
		String candidate = buf.toString();
		peekc = c;
		if(isSTRING(candidate)) {
			sval = candidate;
			return Casl2Symbol.STR_CONST;
		}else {
			errorTable.printError(line, 3,candidate);/// ",JIS X 0201に対応していない文字が含まれています。
			skipToEOL();
			return Casl2Symbol.ERROR;
		}
	}
/*数字*/
	Casl2Symbol NUM(int c,boolean neg) {
		int v = 0;
		while(true){
			if(isNUMBER(c))
				v = v * 10 + (c - '0');
			else break;
			c = read();
		}
		nval = neg ? -v : v;
		peekc = c;
		if(!(-32768<=v && v<=65535)) {
			nval = v & 0x0000FFFF;
			errorTable.printWarning(line,0, nval);// 数値%nvalは1wordに収まらないため，16bit以降は切り捨てられました。
		}else if(nval>=0) {
			return Casl2Symbol.DS_CONST;
		}
		return Casl2Symbol.NUM_CONST;
	}
/*命令やラベル*/
	Casl2Symbol KEYWORD(int c,boolean arg) {
		StringBuilder buf = new StringBuilder();
		buf.append((char)c);
		c = read();
		boolean warn = false;
		while(true){
			if(isUPPER_CASE_LETTER(c)|| isNUMBER(c)){
				buf.append((char)c);
			}else if(c=='_'){
				warn = true;
				buf.append((char)c);
			}else if(isLOWER_CASE_LETTER(c)){
				c = 'A' + (c - 'a');
				warn = true;
				buf.append((char)c);
			}else break;
			c = read();
		}
		sval = buf.toString();
		peekc = c;
		Casl2Symbol symbol = symbolTable.searchSymbol(sval);
		if(symbol!= Casl2Symbol.LABEL && symbol != Casl2Symbol.MACRO_INST) return symbol;
		if(warn) errorTable.printWarning(line,3,sval);
		if(sval.length()>8) errorTable.printWarning(line,4,sval);
		nval = symbolTable.getLabelID();
		return arg ? Casl2Symbol.MACRO_ARG:symbol;
	}

	/*以下、getter*/
	public int getLine() {return line;}
	public int getNval() {return nval;}
	public String getSval(){return sval;}

	/*改行まで読み飛ばし*/
	void skipToEOL(){
		while(peekc!='\n' && peekc != -1)
			peekc = read();
	}
	/*エラー*/
	Casl2Symbol ERROR(int ...c){
		StringBuilder buf = new StringBuilder();
		for(int index = 0; index < c.length; index++){
			buf.append((char)index);
		}
		errorTable.printError(line, 1,buf.toString());//"有効な識別子ではありません。"
		peekc = read();
		skipToEOL();
		return Casl2Symbol.ERROR;
	}
}