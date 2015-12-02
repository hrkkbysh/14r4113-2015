package casl2.casl2ex;

import casl2.ErrorTable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static casl2.casl2ex.Casl2Symbol.*;

public class Casl2LexerA {

    private ErrorTable errorTable;
    private String sval;
    private int nval;///////
    private int line = 1;
    private InputStreamReader input;

    /*Unicode専用.
    * @param 入力ストリーム*/
    public Casl2LexerA(InputStreamReader is){
        if(is == null){
            throw new NullPointerException();
        }
        this.input = is;
        errorTable = ErrorTable.getInstance();
    }

    /** Move one character; detect "end of file" */
    private int read() throws IOException {
        return input.read();
    }

    public Casl2Symbol nextToken(){
        try {
            boolean neg = false;
            int c = read();
            switch ( c ) {
                case  -1 : return EOF;
                case ' ' : case '\t': case '\r': return nextToken();
                case ';' : skipToEOL();
                case '\n': line++; return EOL;
                case ',' : return COMMA;
                case '=' : return EQUAL;
                case '\'': return STR_CONST();
                case '#' : return HEX();
                case '-' : neg = true; c = read(); if(!isNUMBER(c)) return ERROR;
                case '0' : case '1' : case '2' : case '3' : case '4' : case '5' :
                case '6' : case '7' : case '8' : case '9' : return NUM(c,neg);
                case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' : case 'G' :
                case 'H' : case 'I' : case 'J' : case 'K' : case 'L' : case 'M' : case 'N' :
                case 'O' : case 'P' : case 'Q' : case 'R' : case 'S' : case 'T' : case 'U' :
                case 'V' : case 'W' : case 'X' : case 'Y' : case 'Z' : return KEYWORD(c);
                default:
                    errorTable.writeError(line, 0, (char) c);//"有効な識別子ではありません。"
                    skipToEOL();
                    read();
                    return ERROR;
            }
        } catch (IOException e) {
            errorTable.writeError(line,-1,"I/O EXCEPTION occurred.");//例外
            return EOF;
        }
    }

    private boolean isNUMBER(int c) {return '0'<=c && c<='9';}
    private  boolean isHEX(int c){return (c>='A' && c<='F');}
    private boolean isKEYWORD(int c){return (c>='A'&&c<='Z');}
    private boolean isSTRING(String candidate){
        Pattern pattern = Pattern.compile("[\\x00-\\x7F\\xA1-\\xDF]{1,65535}");
        Matcher matcher = pattern.matcher(candidate);
        return matcher.matches();
    }

    private Casl2Symbol HEX() throws IOException {
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
        if(0<=v && v<=65535) {
            nval = v;
            return NUM_CONST;
        }else {
            errorTable.writeError(line,1,v);//"16進定数hは 0000<=h<=FFFFの範囲で記述する必要があります。""
            skipToEOL();
            read();
            return ERROR;
        }
    }

    private Casl2Symbol STR_CONST() throws IOException {
        int c = read();
        StringBuilder buf = new StringBuilder();
        do{
            while (c != '\'') {
                if(c=='\n'){
                    sval = buf.toString();
                    return ERROR;
                }
                buf.append((char)c);
                c =read();
            }
            c = read();
            if (c != '\'') break;
            else {
                buf.append((char)c);
                c =read();
            }
        }while(true);
        String candidate = buf.toString();
        if(isSTRING(candidate)) {
            sval = candidate;
            return STR_CONST;
        }else {
            errorTable.writeError(line, 0,candidate);// "有効な識別子ではありません。"
            skipToEOL();
            read();
            return ERROR;
        }
    }

    private Casl2Symbol NUM(int c,boolean neg) throws IOException {
        int v = 0;
        while(true){
            if(isNUMBER(c))
                v = v * 10 + (c - '0');
            else break;
            c = read();
        }
        nval = neg ? -v : v;
        if(!(-32768<=v && v<=65535)) {
            nval = v & 0x0000FFFF;
            errorTable.writeWarning(line,0, nval);// "数値が16bitに切り捨てられました。"
        }else if(nval>=0) {
            return DS_CONST;
        }
        return NUM_CONST;
    }

    private Casl2Symbol KEYWORD(int c) throws IOException {
        StringBuilder buf = new StringBuilder();
        do{
            if(isKEYWORD(c)|| isNUMBER(c)){
                buf.append((char)c);
            }else break;
            c = read();
        }while(true);
        sval = buf.toString();
        return SymbolTable.searchSymbol(sval);
    }

    public int getLine() {return line;}
    public int getNval() {return nval;}
    public String getSval(){return sval;}

    private void skipToEOL() throws IOException {
        int c = read();
        while(c!='\n')
            c =read();

    }
}