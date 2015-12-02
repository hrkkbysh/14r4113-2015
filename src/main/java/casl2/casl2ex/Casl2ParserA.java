package casl2.casl2ex;

import casl2.Comet2Register;
import casl2.ErrorTable;

import static casl2.casl2ex.Casl2Symbol.*;

/**
 * Created by Haruki on 2015/12/02.
 */
public class Casl2ParserA{
    private Casl2LexerA lexer;
    private Comet2BGA bg;
    private ErrorTable errorTable;
    private String sval;
    private int nval;


    public Casl2ParserA(Casl2LexerA lexer, Comet2BGA bg) {
        this.lexer = lexer;
        this.bg = bg;
        errorTable = ErrorTable.getInstance();
    }

    public void enter() {
        errorTable.clear();
        for (Casl2Symbol token = lexer.nextToken(); token != EOF; token = lexer.nextToken()) {
            if (token == LABEL) {
                bg.setProgramName(lexer.getSval());
                token = lexer.nextToken();
                if (token == START) {
                    token = lexer.nextToken();
                    if (token == LABEL) {
                        bg.setStartAdr(lexer.getSval());
                        token = lexer.nextToken();
                    }
                    if (token == EOL) {
                        bg.setStartAdr();
                        content();
                        bg.genFile();
                        continue;
                    } else {
                        errorTable.writeError(lexer.getLine(), 1, lexer.getSval()); // "START命令のシンタックスエラー"
                    }
                } else {
                    errorTable.writeError(lexer.getLine(), 1, lexer.getSval());//   "最初にSTART命令を記述してください"
                }
            } else {
                token = lexer.nextToken();
                if (token == START) {
                    errorTable.writeError(lexer.getLine(), 1, lexer.getSval());// "START命令のシンタックスエラー(最初のラベルがない)"
                } else {
                    errorTable.writeError(lexer.getLine(), 1, lexer.getSval());//"最初の命令はSTART命令を記述する必要があります。"
                }
                setNextLine();
                content();
            }
        }
    }


    private void content() {
        for(Casl2Symbol token = lexer.nextToken();token!=EOF; checkEOL(token)){
            boolean error = false;
            switch(token) {
                case END:
                    bg.setEndAdr();
                    checkEOL(lexer.nextToken());
                    return;
                case LABEL:
                    if (!bg.defineLabel(lexer.getSval())) {
                        errorTable.writeError(lexer.getLine(), 1, lexer.getSval());//"二重定義"
                    }
                    token = lexer.nextToken();
            }
            Casl2Symbol mnemonic;
            boolean equal = false;
            switch(token){
                case DC:
                    do {
                        token = lexer.nextToken();
                        switch (token) {
                            case NUM_CONST:
                            case DS_CONST:
                                bg.genAdrCode(lexer.getNval());
                                break;
                            case STR_CONST:
                                bg.genAdrCode(lexer.getSval());
                                break;
                            case LABEL:
                                bg.genAdrCode(lexer.getSval(), lexer.getLine());
                                break;
                            default:
                                errorTable.writeError(lexer.getLine(), 1, token.toString());//no constant
                                return;
                        }
                        token = lexer.nextToken();
                    }while(token ==COMMA);
                    break;
                case DS:
                    token = lexer.nextToken();
                    if(token == DS_CONST){
                        bg.genDSArea(lexer.getNval());
                    }else{
                        errorTable.writeError(lexer.getLine(), 1, token.toString());
                    }
                    break;
                case START:
                    errorTable.writeError(lexer.getLine(), 1);//START命令の位置が不適切です。
                    break;
                case ADDA: case ADDL: case AND:
                case CPA: case CPL: case LD:
                case OR: case SUBA: case SUBL:
                    mnemonic = token;
                    if(lexer.nextToken()==GR){
                        Comet2Register r1 = Comet2Register.valueOf(lexer.getSval());
                        if(lexer.nextToken()==COMMA){
                            token = lexer.nextToken();
                            switch(token) {
                                case GR:
                                    Comet2Register r2 = Comet2Register.valueOf(lexer.getSval());
                                    bg.genSingleWordCode(mnemonic, r1, r2, Comet2BGA.AddressingMode.REGISTER);
                                    break;
                                case EQUAL:
                                    equal = true;
                                    lexer.nextToken();
                            }
                            switch(token){
                                case LABEL:
                                    sval = lexer.getSval();
                                    if (lexer.nextToken() == COMMA) {
                                        if(lexer.nextToken() == GR) {
                                            Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                            if (x.getCode() != 0) {
                                                bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                                if(equal) {
                                                    errorTable.writeWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
                                                    bg.genImmCode(sval, lexer.getLine());
                                                }else {
                                                    bg.genAdrCode(sval, lexer.getLine());
                                                }
                                            } else {
                                                errorTable.writeError(lexer.getLine(), 1, "カンマが不足しています");
                                            }
                                        } else {
                                            errorTable.writeError(lexer.getLine(), 1, "第一オペランドがレジスタではありません。");
                                        }
                                    } else {
                                        bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            errorTable.writeWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
                                            bg.genAdrCode(sval, lexer.getLine());
                                        }else {
                                            bg.genAdrCode(sval, lexer.getLine());
                                        }
                                    }
                                case NUM_CONST:
                                case DS_CONST:
                                    nval = lexer.getNval();
                                    if (lexer.nextToken() == COMMA) {
                                        if (lexer.nextToken() == GR) {
                                            Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                            if (x.getCode() != 0) {
                                                bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                                if(equal) {
                                                    bg.genImmCode(nval);
                                                }else {
                                                    bg.genAdrCode(nval);
                                                }
                                            } else {
                                                errorTable.writeError(lexer.getLine(), 1, "GR0��?C���?f�?b�?N�?X���?W�?X�?^��?��??g����?����B");
                                            }
                                        } else {
                                            errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                                    "第三オペランドはインデックスレジスタを指定してください。");
                                        }
                                    } else {
                                        bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            bg.genImmCode(nval);
                                        }else {
                                            bg.genAdrCode(nval);
                                        }
                                    }
                                case STR_CONST:
                                    sval = lexer.getSval();
                                    if (lexer.nextToken() == COMMA) {
                                        if (lexer.nextToken() == GR) {
                                            Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                            if (x.getCode() != 0) {
                                                bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                                bg.genAdrCode(sval);
                                            } else {
                                                errorTable.writeError(lexer.getLine(), 1, "GR0��?C���?f�?b�?N�?X���?W�?X�?^��?��??g����?����B");
                                            }
                                        } else {
                                            errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                                    "第三オペランドはインデックスレジスタを指定してください。");
                                        }
                                    } else {
                                        bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            bg.genImmCode(sval);
                                        }else {
                                            bg.genAdrCode(sval);
                                        }
                                    }
                                default:
                                    errorTable.writeError(lexer.getLine(), 1);//illegal address;
                            }
                        } else {
                            errorTable.writeError(lexer.getLine(), 1, "カンマが不足しています");
                        }
                    } else {
                        errorTable.writeError(lexer.getLine(), 1, "第一オペランドがレジスタではありません。");
                    }
                    break;

                case LAD:case SLA:case SLL:
                case SRA:case SRL:case ST:
                    mnemonic = token;
                    if(lexer.nextToken()==GR){
                        Comet2Register r1 = Comet2Register.valueOf(lexer.getSval());
                        if(lexer.nextToken()==COMMA){
                            token = lexer.nextToken();
                            switch(token) {
                                case EQUAL:
                                    equal = true;
                                    lexer.nextToken();
                            }
                            switch(token){
                                case LABEL:
                                    sval = lexer.getSval();
                                    if (lexer.nextToken() == COMMA) {
                                        if(lexer.nextToken() == GR) {
                                            Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                            if (x.getCode() != 0) {
                                                bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                                if(equal) {
                                                    errorTable.writeWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
                                                    bg.genImmCode(sval, lexer.getLine());
                                                }else {
                                                    bg.genAdrCode(sval, lexer.getLine());
                                                }
                                            } else {
                                                errorTable.writeError(lexer.getLine(), 1, "GR0はインデックスレジスタとして使えません。");
                                            }
                                        } else {
                                            errorTable.writeError(lexer.getLine(), 1,"不適切なオペランドです。" +
                                                    "第三オペランドはインデックスレジスタを指定してください。");
                                        }
                                    } else {
                                        bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            errorTable.writeWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
                                            bg.genAdrCode(sval, lexer.getLine());
                                        }else {
                                            bg.genAdrCode(sval, lexer.getLine());
                                        }
                                    }
                                case NUM_CONST:
                                case DS_CONST:
                                    nval = lexer.getNval();
                                    if (lexer.nextToken() == COMMA) {
                                        if (lexer.nextToken() == GR) {
                                            Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                            if (x.getCode() != 0) {
                                                bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                                if(equal) {
                                                    bg.genImmCode(nval);
                                                }else {
                                                    bg.genAdrCode(nval);
                                                }
                                            } else {
                                                errorTable.writeError(lexer.getLine(), 1,"GR0はインデックスレジスタとして使えません。");
                                            }
                                        } else {
                                            errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                                    "第三オペランドはインデックスレジスタを指定してください。");
                                        }
                                    } else {
                                        bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            bg.genImmCode(nval);
                                        }else {
                                            bg.genAdrCode(nval);
                                        }
                                    }
                                case STR_CONST:
                                    sval = lexer.getSval();
                                    if (lexer.nextToken() == COMMA) {
                                        if (lexer.nextToken() == GR) {
                                            Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                            if (x.getCode() != 0) {
                                                bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                                bg.genAdrCode(sval);
                                            } else {
                                                errorTable.writeError(lexer.getLine(), 1, "GR0はインデックスレジスタとして使えません。");
                                            }
                                        } else {
                                            errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                                    "第三オペランドはインデックスレジスタを指定してください。");
                                        }
                                    } else {
                                        bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            bg.genImmCode(sval);
                                        }else {
                                            bg.genAdrCode(sval);
                                        }
                                    }
                                default:
                                    errorTable.writeError(lexer.getLine(), 1);//illegal address;
                            }
                        } else {
                            errorTable.writeError(lexer.getLine(), 1, "カンマが不足しています");
                        }
                    } else {
                        errorTable.writeError(lexer.getLine(), 1, "第一オペランドがレジスタではありません。");
                    }
                    break;
                case CALL:case JMI:case JNZ:
                case JOV:case JPL: case JUMP:
                case JZE: case PUSH:case SVC:
                    mnemonic = token;
                    Comet2Register r1 = Comet2Register.GR0;
                    switch(token) {
                        case EQUAL:
                            equal = true;
                            lexer.nextToken();
                    }
                    switch(token){
                        case LABEL:
                            sval = lexer.getSval();
                            if (lexer.nextToken() == COMMA) {
                                if(lexer.nextToken() == GR) {
                                    Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                    if (x.getCode() != 0) {
                                        bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            errorTable.writeWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
                                            bg.genImmCode(sval, lexer.getLine());
                                        }else {
                                            bg.genAdrCode(sval, lexer.getLine());
                                        }
                                    } else {
                                        errorTable.writeError(lexer.getLine(), 1, "GR0はインデックスレジスタとして使えません。");
                                    }
                                } else {
                                    errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                            "第三オペランドはインデックスレジスタを指定してください。");
                                }
                            } else {
                                bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                if(equal) {
                                    errorTable.writeWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
                                    bg.genAdrCode(sval, lexer.getLine());
                                }else {
                                    bg.genAdrCode(sval, lexer.getLine());
                                }
                            }
                        case NUM_CONST:
                        case DS_CONST:
                            nval = lexer.getNval();
                            if (lexer.nextToken() == COMMA) {
                                if (lexer.nextToken() == GR) {
                                    Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                    if (x.getCode() != 0) {
                                        bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                        if(equal) {
                                            bg.genImmCode(nval);
                                        }else {
                                            bg.genAdrCode(nval);
                                        }
                                    } else {
                                        errorTable.writeError(lexer.getLine(), 1, "GR0はインデックスレジスタとして使えません。");
                                    }
                                } else {
                                    errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                            "第三オペランドはインデックスレジスタを指定してください。");
                                }
                            } else {
                                bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                if(equal) {
                                    bg.genImmCode(nval);
                                }else {
                                    bg.genAdrCode(nval);
                                }
                            }
                        case STR_CONST:
                            sval = lexer.getSval();
                            if (lexer.nextToken() == COMMA) {
                                if (lexer.nextToken() == GR) {
                                    Comet2Register x = Comet2Register.valueOf(lexer.getSval());
                                    if (x.getCode() != 0) {
                                        bg.genSingleWordCode(mnemonic, r1, x, Comet2BGA.AddressingMode.INDEX);
                                        bg.genAdrCode(sval);
                                    } else {
                                        errorTable.writeError(lexer.getLine(), 1, "GR0はインデックスレジスタとして使えません。");
                                    }
                                } else {
                                    errorTable.writeError(lexer.getLine(), 1, "不適切なオペランドです。" +
                                            "第三オペランドはインデックスレジスタを指定してください。");
                                }
                            } else {
                                bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BGA.AddressingMode.INDEX);
                                if(equal) {
                                    bg.genImmCode(sval);
                                }else {
                                    bg.genAdrCode(sval);
                                }
                            }
                        default:
                            errorTable.writeError(lexer.getLine(), 1);//illegal address;
                    }
                break;
                case POP:
                    token = lexer.nextToken();
                    if(token==GR){
                        Comet2Register r = Comet2Register.valueOf(lexer.getSval());
                        bg.genRegStackCode(POP, r);
                    }else{
                        errorTable.writeError(lexer.getLine(), 1 ,"第一オペランドがレジスタではありません。");
                    }
                    break;
                case RET:case NOP:
                    bg.genNoOpCode(token);
                    break;
                case RPOP:
                case RPUSH:
                    bg.genMacroBlock(token);
                    break;
                case IN:
                case OUT:
                    token = lexer.nextToken();
                    if(token==LABEL){
                        String bufLabel = lexer.getSval();
                        if(lexer.nextToken()==COMMA){
                            token = lexer.nextToken();
                            if(token==LABEL){
                                String lenLabel =lexer.getSval();
                                bg.genMacroBlock(token, bufLabel, lenLabel, lexer.getLine());
                                token = lexer.nextToken();
                            }else{
                                errorTable.writeError(lexer.getLine(), 1, "第二オペランドにラベルを記述する必要があります。");
                            }
                        }else{
                            errorTable.writeError(lexer.getLine(), 1,"カンマが不足しています");
                        }
                    }else{
                        errorTable.writeError(lexer.getLine(), 1, "第一オペランドにラベルを記述する必要があります。");
                    }
                    break;
            }
        }
    }

    private void checkEOL(Casl2Symbol token) {
        if(!(token==EOL)){
            errorTable.writeError(lexer.getLine(), 1, "not EOL");
            setNextLine();
        }
    }
    private void setNextLine() {
        while (lexer.nextToken()!=EOL){}
        lexer.nextToken();
    }
}