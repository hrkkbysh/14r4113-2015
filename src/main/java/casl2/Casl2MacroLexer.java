package casl2;

import java.io.BufferedReader;

public class Casl2MacroLexer extends Casl2Lexer{
    private boolean macro;
    private boolean usemacro;
    StringBuilder buf;
    public Casl2MacroLexer(BufferedReader r, SymbolTable symbolTable, ErrorTable errorTable) {
        super(r, symbolTable, errorTable);
    }
    @Override
    int read(){
        int c = super.read();
        if(macro){buf.append(c);}
        return c;
    }
    void saveCodeBlock(){
        buf = new StringBuilder();
        macro = true;
    }
    void stopSave(){
        macro = false;
    }
    StringBuilder getCodeBlock(){return buf;}
    public void insertMacro(){

    }
}
