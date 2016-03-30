package comet2casl2;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.List;

public class Casl2Parser {
	private Casl2Lexer lexer;
	private Comet2BG bg;
	private ErrorTable errorTable;
	private SymbolTable symbolTable;
	boolean er;

	public Casl2Parser(Path path,String charset,AsmMode asmMode) {
		init(asmMode);
		lexer = new Casl2Lexer(path,charset,errorTable);
	}
	public Casl2Parser(String input,AsmMode asmMode) {
		init(asmMode);
		lexer = new Casl2Lexer(input,errorTable);
	}
	//for simulator
	public Casl2Parser(MachineObserver mo){
		symbolTable = mo.symTbl;
		bg = new Comet2BG(mo);
	}

	public void init(AsmMode asmMode){
		symbolTable = new SymbolTable();
		symbolTable.setSymbolTable(asmMode);
		errorTable = new ErrorTable();
		bg = new Comet2BG(new Comet2InstructionTable(),symbolTable,errorTable);
	}
	public void init(BufferedReader r){
		errorTable.clear();
		symbolTable.clear();
		bg.init();
		lexer.init(r);
	}

	public void setMode(AsmMode parseMode){
		symbolTable.setSymbolTable(parseMode);
	}

	public boolean decode(Path path) {
		errorTable.clear();
		Casl2Symbol symbol;
		lexer.nextToken();
		for (byte ps = 0; Casl2Lexer.STATE != LexerState.EOF; ) {
			switch (ps) {
				case 0:
					if (Casl2Lexer.STATE == LexerState.KEYWORD) {
						symbol = symbolTable.searchSymbol(lexer.getSval());
						if (symbol != Casl2Symbol.REGISTER) {
							bg.setProgramName(lexer.getSval());
							lexer.nextToken();
							ps = 1;
						}
					} else {
						symbol = symbolTable.searchSymbol(lexer.getSval());
						if (symbol == Casl2Symbol.START) {
							errorTable.printError(lexer.getLine(), 6, lexer.getSval());// "START命令のシンタックスエラー(最初のラベルがない)"
						} else {
							errorTable.printError(lexer.getLine(), 4, lexer.getSval()); // "START命令のシンタックスエラー"
						}
						return false;
					}
					break;
				case 1:
					if (Casl2Lexer.STATE == LexerState.KEYWORD) {
						symbol = symbolTable.searchSymbol(lexer.getSval());
						if (symbol == Casl2Symbol.START) {
							lexer.nextToken();
							ps = 2;
						} else {
							errorTable.printError(lexer.getLine(), 5, lexer.getSval());//   "最初にSTART命令を記述してください"
							ps = 4;
						}
					} else {
						errorTable.printError(lexer.getLine(), 5, lexer.getSval());//   "最初にSTART命令を記述してください"
						ps = 4;
					}
					break;
				case 2:
					switch (Casl2Lexer.STATE) {
						case KEYWORD:
							symbol = symbolTable.searchSymbol(lexer.getSval());
							if (symbol == Casl2Symbol.LABEL) {
								bg.setStartAdr(symbolTable.getLabelID());
								lexer.nextToken();
							}
						case EOL:
							ps = decodeContent();
							break;
						default:
							errorTable.printError(lexer.getLine(), 4, lexer.getSval()); // "START命令のシンタックスエラー"
					}
					break;
				case 3://parsing content is complete
					bg.genFile(path);
					break;
				case 4://failed
					break;
			}
		}
		return false;
	}

	byte decodeContent() {
		Casl2Symbol symbol,mnemonic = Casl2Symbol.LABEL;
		RegMember r1 = RegMember.GR0;
		for(byte ps = 2;Casl2Lexer.STATE != LexerState.EOF;) {
			switch (ps) {
				case -1://error
					nextLine();
					ps = 2;
					break;
				case 0://correct(check eol)
					checkEOL();
					ps = 2;
					break;
				case 2:
					lexer.nextToken();
					mnemonic = symbolTable.searchSymbol(lexer.getSval());
					switch (mnemonic) {
						case END:
							bg.setEndAdr();
							lexer.nextToken();
							checkEOL();
							return 3;
						case LABEL:
							if (!bg.defineLabel(symbolTable.getLabelID(), lexer.getLine())) {
								errorTable.printError(lexer.getLine(), 8, lexer.getSval());//"二重定義"
							}
							lexer.nextToken();
							mnemonic = symbolTable.searchSymbol(lexer.getSval());
					}
					switch (mnemonic) {
						case DC:
							ps = 3;
							lexer.nextToken();
							break;
						case DS:
							ps = 5;
							lexer.nextToken();
							break;
						case START:
						case END:
							errorTable.printError(lexer.getLine(), 11, lexer.getSval());//START命令の位置が不適切です。
							ps = -1;
							break;
						case ADDA: case ADDL: case AND:
						case CPA: case CPL: case LD:
						case OR: case SUBA: case SUBL:
							lexer.nextToken();
							ps = 6;
							break;
						case LAD: case SLA: case SLL:
						case SRA: case SRL: case ST:
							lexer.nextToken();
							ps = 8;
							break;
						case CALL: case JMI: case JNZ:
						case JOV: case JPL: case JUMP:
						case JZE: case PUSH: case SVC:
							lexer.nextToken();
							ps = adr_x(mnemonic, RegMember.GR0);
							break;
						case POP:
							symbol = symbolTable.searchSymbol(lexer.getSval());
							if (symbol == Casl2Symbol.REGISTER) {
								RegMember r = RegMember.valueOf(lexer.getSval());
								bg.genRegStackCode(mnemonic, r);
								lexer.nextToken();
								ps = 0;
							} else {
								errorTable.printError(lexer.getLine(), 12);
								ps = -1;
							}
							break;
						case RET: case NOP:
							bg.genNoOpCode(mnemonic);
							lexer.nextToken();
							ps = 0;
							break;
						case RPOP: case RPUSH:
							bg.genMacroBlock(mnemonic);
							lexer.nextToken();
							ps = 0;
							break;
						case IN: case OUT:
							lexer.nextToken();
							ps = 9;
							break;
						default:
							errorTable.printError(lexer.getLine(), -999, lexer.getSval());
							ps = -1;
					}
					break;
				case 3://DC
					switch (Casl2Lexer.STATE) {
						case NUMBER:
							bg.genAdrCode(lexer.getNval());
							ps = 4;
							break;
						case STRING:
							bg.genAdrCode(lexer.getSval());
							ps = 4;
							break;
						case KEYWORD:
							symbol = symbolTable.searchSymbol(lexer.getSval());
							if (symbol != Casl2Symbol.REGISTER)
								bg.genAdrCode(lexer.getNval(), lexer.getLine());
							ps = 4;
							break;
						default:
							er = true;
							errorTable.printError(lexer.getLine(), 9, lexer.getSval());//no constant
							ps = -1;
							break;
					}
					break;
				case 4:
					if (Casl2Lexer.STATE == LexerState.COMMA) {
						ps = 3;
					} else {
						ps = 0;
					}
				case 5://DS
					if (Casl2Lexer.STATE == LexerState.NUMBER) {
						if (lexer.getNval() >= 0)
							bg.genDSArea(lexer.getNval());
					} else {
						errorTable.printError(lexer.getLine(), 10, lexer.getSval());//DSシンタックスエラー．
					}
					break;
				case 6:
				/*case ADDA: case ADDL: case AND:
				case CPA: case CPL: case LD:
				case OR:case SUBA: case SUBL:*/
					symbol = symbolTable.searchSymbol(lexer.getSval());
					if (symbol == Casl2Symbol.REGISTER) {
						r1 = RegMember.valueOf(lexer.getSval());
						lexer.nextToken();
						if (Casl2Lexer.STATE == LexerState.COMMA) {
							lexer.nextToken();
							ps = 7;
						} else {
							errorTable.printError(lexer.getLine(), 15);//カンマが不足しています。
							ps = -1;
						}
					} else {
						errorTable.printError(lexer.getLine(), 16, lexer.getSval()); //第一オペランドがレジスタではありません。
						ps = -1;
					}
					break;
				case 7:
					symbol = symbolTable.searchSymbol(lexer.getSval());
					if (symbol == Casl2Symbol.REGISTER) {
						bg.genSingleWordCode(symbol, r1, RegMember.valueOf(lexer.getSval()), 4);
						ps = 0;
						lexer.nextToken();
					} else {
						ps = adr_x(mnemonic, r1);
					}
					break;
				case 8:
/*				case LAD: case SLA: case SLL:
				case SRA: case SRL: case ST:*/
					symbol = symbolTable.searchSymbol(lexer.getSval());
					if (symbol == Casl2Symbol.REGISTER) {
						r1 = RegMember.valueOf(lexer.getSval());
						lexer.nextToken();
						if (Casl2Lexer.STATE == LexerState.COMMA) {
							lexer.nextToken();
							ps = adr_x(mnemonic, r1);
						} else {
							errorTable.printError(lexer.getLine(), 15);//カンマが不足しています。
							ps = -1;
						}
					} else {
						errorTable.printError(lexer.getLine(), 16, lexer.getSval()); //第一オペランドがレジスタではありません。
						ps = -1;
					}
					break;
				case 9:
	/*				case IN:
					case OUT:*/
					symbol = symbolTable.searchSymbol(lexer.getSval());
					if (symbol == Casl2Symbol.LABEL) {
						int buf = symbolTable.getLabelID();
						if (lexer.nextToken() == LexerState.COMMA) {
							lexer.nextToken();
							symbol = symbolTable.searchSymbol(lexer.getSval());
							if (symbol == Casl2Symbol.LABEL) {
								int len =symbolTable.getLabelID();
								bg.genMacroBlock(mnemonic, buf, len, lexer.getLine());
								lexer.nextToken();
							} else {
								errorTable.printError(lexer.getLine(), 17, 2);
								ps = -1;
							}
						} else {
							errorTable.printError(lexer.getLine(), 15);
							ps = -1;
						}
					} else {
						errorTable.printError(lexer.getLine(), 17, 1);
						ps = -1;
					}
					break;
				default:
					return 3;
			}
		}
		return 3;
	}

	void nextLine() {
		while (Casl2Lexer.STATE != LexerState.EOL){
			lexer.nextToken();
		}
		lexer.nextToken();
	}

	private void checkEOL() {
		lexer.nextToken();
		if(Casl2Lexer.STATE != LexerState.EOL){
			errorTable.printError(lexer.getLine(), 18);
		}
	}
	byte adr_x(Casl2Symbol mnemonic, RegMember r1){
		switch(Casl2Lexer.STATE){
			case EQUAL:
				return adr_x(mnemonic,r1,true);
			case KEYWORD:
			case NUMBER:
			case STRING:
				return adr_x(mnemonic,r1,false);
			default:
				return -1;
		}
	}
	byte adr_x(Casl2Symbol mnemonic, RegMember r1,boolean equal){
		Casl2Symbol symbol = symbolTable.searchSymbol(lexer.getSval());
		switch(Casl2Lexer.STATE){
			case KEYWORD:
				return address(mnemonic, r1, symbolTable.getLabelID(), equal);
			case NUMBER:
				return num(mnemonic, r1, lexer.getNval(), equal);
			case STRING:
				return STRING(mnemonic, r1, lexer.getSval(), equal);
			default:
				errorTable.printError(lexer.getLine(), 14, symbol.toString(), 2, lexer.getSval());//illegal address;
				return -1;
		}
	}


	byte address(Casl2Symbol mnemonic, RegMember r1, int nval, boolean equal){
		lexer.nextToken();
		if (Casl2Lexer.STATE == LexerState.COMMA) {
			Casl2Symbol symbol = symbolTable.searchSymbol(lexer.getSval());
			if(symbol == Casl2Symbol.REGISTER) {
				RegMember x = RegMember.valueOf(lexer.getSval());
				if (x.getCode() != 0) {
					bg.genSingleWordCode(mnemonic, r1, x, 0);
					if(equal) {
						errorTable.printWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
						bg.genImmCode(nval, lexer.getLine());
					}else {
						bg.genAdrCode(nval, lexer.getLine());
					}
					lexer.nextToken();
					return 0;
				} else {
					errorTable.printError(lexer.getLine(), 12);
					return  -1;
				}
			} else {
				errorTable.printError(lexer.getLine(), 13, 1);
				return  -1;
			}
		} else {
			bg.genSingleWordCode(mnemonic, r1, RegMember.GR0, 0);
			if(equal) {
				errorTable.printWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
				bg.genAdrCode(nval, lexer.getLine());
			}else {
				bg.genAdrCode(nval, lexer.getLine());
			}
			return 0;
		}
	}
	byte num(Casl2Symbol mnemonic, RegMember r1,int nval,boolean equal){
		lexer.nextToken();
		if (Casl2Lexer.STATE == LexerState.COMMA) {
			Casl2Symbol symbol = symbolTable.searchSymbol(lexer.getSval());
			if (symbol == Casl2Symbol.REGISTER) {
				RegMember x = RegMember.valueOf(lexer.getSval());
				if (x.getCode() != 0) {
					bg.genSingleWordCode(mnemonic, r1, x, 0);
					if(equal) {
						bg.genImmCode(nval);
					}else {
						bg.genAdrCode(nval);
					}
					lexer.nextToken();
					return 0;
				} else {
					errorTable.printError(lexer.getLine(), 12);
					return -1;
				}
			} else {
				errorTable.printError(lexer.getLine(), 13, 2);
				return -1;
			}
		} else {
			bg.genSingleWordCode(mnemonic, r1, RegMember.GR0, 0);
			if(equal) {
				bg.genImmCode(nval);
			}else {
				bg.genAdrCode(nval);
			}
			return 0;
		}
	}
	byte STRING(Casl2Symbol mnemonic, RegMember r1, String sval, boolean equal){
		lexer.nextToken();
		if (Casl2Lexer.STATE == LexerState.COMMA) {
			lexer.nextToken();
			Casl2Symbol symbol = symbolTable.searchSymbol(lexer.getSval());
			if (symbol == Casl2Symbol.REGISTER) {
				RegMember x = RegMember.valueOf(lexer.getSval());
				if (x.getCode() != 0) {
					bg.genSingleWordCode(mnemonic, r1, x,0);
					bg.genAdrCode(sval);
					lexer.nextToken();
					return 0;
				} else {
					errorTable.printError(lexer.getLine(), 12);
					return -1;
				}
			} else {
				errorTable.printError(lexer.getLine(), 13, 3);
				return -1;
			}
		} else {
			bg.genSingleWordCode(mnemonic, r1, RegMember.GR0, 0);
			if(equal) {
				bg.genImmCode(sval);
			}else {
				bg.genAdrCode(sval);
			}
			return 0;
		}
	}

	public boolean hasError() {
		return errorTable.hasError();
	}

	public boolean hasWarning() {
		return errorTable.hasWarning();
	}

	public List<String> getMessages() {
		return errorTable.getMessages();
	}
}