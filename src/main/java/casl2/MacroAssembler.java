package casl2;

import editor.MacroInstruction;

import javax.crypto.Mac;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MacroAssembler {
    private ExCasl2Lexer lexer;
    private ExSymbolTable symbolTable;
    private ErrorTable errorTable;
    private Casl2Symbol token;
    private boolean err;

    public MacroAssembler(InputStreamReader is) {
        symbolTable = new ExSymbolTable();
        errorTable = new ErrorTable();
        lexer = new ExCasl2Lexer(is, symbolTable, errorTable);
    }

    public void checkMacro() {
        err = false;
        for (token = lexer.nextToken(); token != Casl2Symbol.EOF && token != Casl2Symbol.START; token = lexer.nextToken()) {
            while(true){
                if(token ==Casl2Symbol.MACROSTART) break;
                else if(token ==Casl2Symbol.EOL)
                    token = lexer.nextToken();
                else if(token == Casl2Symbol.START) return;
                else {err = true; token = lexer.nextToken();}
            }
            if (!err) {
                token = lexer.nextToken();
                if(token ==Casl2Symbol.EOL){
                    checkHeader();
                }else{
                    //invalid macro header
                }
            }else{
                //invalid macro header
            }
        }
    }

    private StringBuilder checkHeader() {
        for (token = lexer.nextToken(); token != Casl2Symbol.EOF && token != Casl2Symbol.START; token = lexer.nextToken()) {
            int candID;
            if(token == Casl2Symbol.MACRO_ARG){
                candID = lexer.getNval();
                token = lexer.nextToken();
            }
            int instID;
            List<Integer> argIDs = new ArrayList<>();
            switch(token) {
                case LABEL:
                    instID = lexer.getNval();
                    do {
                        token = lexer.nextToken();
                        switch (token) {
                            case MACRO_ARG:
                                argIDs.add(lexer.getNval());
                            default:
                                err = true;
                                errorTable.writeError(lexer.getLine(), 9, token.toString());//no args
                                break;
                        }
                        if (err) break;
                        token = lexer.nextToken();
                    } while (token == Casl2Symbol.COMMA);
                    if (!err) {

                    }
                    break;
                default:
                    err = true;
            }

        }
    }
}