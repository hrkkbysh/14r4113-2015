package casl2;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.List;

public class Casl2Parser {
	private Casl2Lexer lexer;
	private Comet2BG bg;
	private ErrorTable errorTable;
	private SymbolTable symbolTable;
	private Casl2Symbol token;
	boolean er;

	public Casl2Parser(BufferedReader r,AsmMode asmMode) {
		symbolTable = new SymbolTable();
		symbolTable.setSymbolTable(asmMode);
		errorTable = new ErrorTable();
		lexer = new Casl2Lexer(r,symbolTable,errorTable);
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


	public void enter(String filepath) {
		errorTable.clear();
		for (token = lexer.nextToken(); token != Casl2Symbol.EOF; setNextLine()) {
			if (token == Casl2Symbol.LABEL) {
				bg.setProgramName(lexer.getSval());
				token = lexer.nextToken();
				if (token == Casl2Symbol.START) {
					token = lexer.nextToken();
					if (token == Casl2Symbol.LABEL) {
						bg.setStartAdr(lexer.getNval());
						token = lexer.nextToken();
					}
					if (token == Casl2Symbol.EOL) {
						bg.setStartAdr();
						content();
						bg.genFile(filepath);
					} else {
						errorTable.printError(lexer.getLine(), 4, lexer.getSval()); // "START命令のシンタックスエラー"
					}
				} else {
					errorTable.printError(lexer.getLine(), 5, lexer.getSval());//   "最初にSTART命令を記述してください"
					//label inst....
				}
			} else if (token == Casl2Symbol.START){
				errorTable.printError(lexer.getLine(), 6, lexer.getSval());// "START命令のシンタックスエラー(最初のラベルがない)"
			} else {
				errorTable.printError(lexer.getLine(), 4, lexer.getSval()); // "START命令のシンタックスエラー"
			}
			if(token == Casl2Symbol.EOF) break;
		}
	}

	private void content() {
		for(token = lexer.nextToken(),er = false ;token!= Casl2Symbol.EOF; setNextLine()){
			switch(token) {
				case END:
					bg.setEndAdr();
					token = lexer.nextToken();
					checkEOL();
					return;
				case LABEL:
					if (!bg.defineLabel(lexer.getNval(),lexer.getLine())) {
						errorTable.printError(lexer.getLine(), 8, lexer.getSval());//"二重定義"
					}
					token = lexer.nextToken();
			}
			Casl2Symbol mnemonic;
			boolean equal = false;
			int nval;
			String sval;
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
								bg.genAdrCode(lexer.getNval(), lexer.getLine());
								break;
							default:
								er = true;
								errorTable.printError(lexer.getLine(), 9, token.toString());//no constant
								break;
						}
						if(er) break;
						token = lexer.nextToken();
					}while(token == Casl2Symbol.COMMA);
					break;
				case DS:
					token = lexer.nextToken();
					if(token == Casl2Symbol.DS_CONST){
						bg.genDSArea(lexer.getNval());
						token = lexer.nextToken();
					}else{
						er = true;
						errorTable.printError(lexer.getLine(), 10, token.toString());//DSシンタックスエラー．
					}
					break;
				case START:
					errorTable.printError(lexer.getLine(), 11);//START命令の位置が不適切です。
					er = true;
					break;
				case ADDA: case ADDL: case AND:
				case CPA: case CPL: case LD:
				case OR: case SUBA: case SUBL:
					mnemonic = token;
					if(lexer.nextToken()== Casl2Symbol.GR){
						Comet2Register r1 = Comet2Register.valueOf(lexer.getSval());
						token = lexer.nextToken();
						if(token == Casl2Symbol.COMMA){
							token = lexer.nextToken();
							if(token == Casl2Symbol.GR){
								Comet2Register r2 = Comet2Register.valueOf(lexer.getSval());
								bg.genSingleWordCode(mnemonic, r1, r2, Comet2BG.AddressingMode.REGISTER);
								token = lexer.nextToken();
							}else{
								adr_x(mnemonic,r1);
							}
						} else {
							errorTable.printError(lexer.getLine(), 15);//カンマが不足しています。
							er = true;
						}
					} else {
						errorTable.printError(lexer.getLine(), 16, mnemonic.toString()); //第一オペランドがレジスタではありません。
						er = true;
					}
					break;
				case LAD:case SLA:case SLL:
				case SRA:case SRL:case ST:
					mnemonic = token;
					if(lexer.nextToken()== Casl2Symbol.GR){
						Comet2Register r1 = Comet2Register.valueOf(lexer.getSval());
						if(lexer.nextToken()== Casl2Symbol.COMMA){
							token = lexer.nextToken();
							adr_x(mnemonic,r1);
						} else {
							errorTable.printError(lexer.getLine(), 15);
							er = true;
						}
					} else {
						errorTable.printError(lexer.getLine(), 16, mnemonic.toString());
						er = true;
					}
					break;
				case CALL:case JMI:case JNZ:
				case JOV:case JPL: case JUMP:
				case JZE: case PUSH:case SVC:
					mnemonic = token;
					Comet2Register r1 = Comet2Register.GR0;
					token = lexer.nextToken();
					adr_x(mnemonic,r1);
					break;
				case POP:
					token = lexer.nextToken();
					if(token== Casl2Symbol.GR){
						Comet2Register r = Comet2Register.valueOf(lexer.getSval());
						bg.genRegStackCode(Casl2Symbol.POP, r);
						token = lexer.nextToken();
					}else{
						errorTable.printError(lexer.getLine(), 12);
						er = true;
					}
					break;
				case RET:case NOP:
					bg.genNoOpCode(token);
					token = lexer.nextToken();
					break;
				case RPOP:
				case RPUSH:
					bg.genMacroBlock(token);
					token = lexer.nextToken();
					break;
				case IN:
				case OUT:
					mnemonic = token;
					token = lexer.nextToken();
					if(token== Casl2Symbol.LABEL){
						int bufLabel = lexer.getNval();
						if(lexer.nextToken()== Casl2Symbol.COMMA){
							token = lexer.nextToken();
							if(token== Casl2Symbol.LABEL){
								int lenLabel =lexer.getNval();
								bg.genMacroBlock(mnemonic, bufLabel, lenLabel, lexer.getLine());
								token = lexer.nextToken();
							}else{
								errorTable.printError(lexer.getLine(), 17, 2);
								er = true;
							}
						}else{
							errorTable.printError(lexer.getLine(), 15);
							er = true;
						}
					}else{
						errorTable.printError(lexer.getLine(), 17, 1);
						er = true;
					}
					break;
				default:
					er = true;
			}
			checkEOL();
		}
	}

	private void checkEOL() {
		if(er) return;
		if(!(token== Casl2Symbol.EOL)){
			errorTable.printError(lexer.getLine(), 18);
		}
	}
	private void setNextLine() {
		while (token!= Casl2Symbol.EOL){
			token = lexer.nextToken();
		}
		er = false;
		token = lexer.nextToken();
	}

	private void adr_x(Casl2Symbol mnemonic,Comet2Register r1){
		if(token == Casl2Symbol.EQUAL){
			token = lexer.nextToken();
			adr_x(mnemonic, r1,true);
		}else {adr_x(mnemonic,r1,false);}
	}
	private void adr_x(Casl2Symbol mnemonic,Comet2Register r1,boolean equal){
		switch(token){
			case LABEL:
				label(mnemonic,r1,lexer.getNval(),equal);
				break;
			case NUM_CONST:
			case DS_CONST:
				num(mnemonic, r1, lexer.getNval(), equal);
				break;
			case STR_CONST:
				str(mnemonic, r1, lexer.getSval(), equal);
				break;
			default:
				errorTable.printError(lexer.getLine(), 14, mnemonic.toString(), 2, token.toString());//illegal address;
				er = true;
		}
	}


	private void label(Casl2Symbol mnemonic,Comet2Register r1,int nval,boolean equal){
		token = lexer.nextToken();
		if (token == Casl2Symbol.COMMA) {
			if(lexer.nextToken() == Casl2Symbol.GR) {
				Comet2Register x = Comet2Register.valueOf(lexer.getSval());
				if (x.getCode() != 0) {
					bg.genSingleWordCode(mnemonic, r1, x, Comet2BG.AddressingMode.INDEX);
					if(equal) {
						errorTable.printWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
						bg.genImmCode(nval, lexer.getLine());
					}else {
						bg.genAdrCode(nval, lexer.getLine());
					}
					token = lexer.nextToken();
				} else {
					errorTable.printError(lexer.getLine(), 12);
					er = true;
				}
			} else {
				errorTable.printError(lexer.getLine(), 13, 1);
				er = true;
			}
		} else {
			bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
			if(equal) {
				errorTable.printWarning(lexer.getLine(), 2);//第二オペランドの値が不安定です。
				bg.genAdrCode(nval, lexer.getLine());
			}else {
				bg.genAdrCode(nval, lexer.getLine());
			}
		}
	}
	private void num(Casl2Symbol mnemonic,Comet2Register r1,int nval,boolean equal){
		token = lexer.nextToken();
		if (token == Casl2Symbol.COMMA) {
			if (lexer.nextToken() == Casl2Symbol.GR) {
				Comet2Register x = Comet2Register.valueOf(lexer.getSval());
				if (x.getCode() != 0) {
					bg.genSingleWordCode(mnemonic, r1, x, Comet2BG.AddressingMode.INDEX);
					if(equal) {
						bg.genImmCode(nval);
					}else {
						bg.genAdrCode(nval);
					}
					token = lexer.nextToken();
				} else {
					errorTable.printError(lexer.getLine(), 12);
					er = true;
				}
			} else {
				errorTable.printError(lexer.getLine(), 13, 2);
				er = true;
			}
		} else {
			bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
			if(equal) {
				bg.genImmCode(nval);
			}else {
				bg.genAdrCode(nval);
			}
		}
	}
	private void str(Casl2Symbol mnemonic,Comet2Register r1,String sval,boolean equal){
		token = lexer.nextToken();
		if (token == Casl2Symbol.COMMA) {
			if (lexer.nextToken() == Casl2Symbol.GR) {
				Comet2Register x = Comet2Register.valueOf(lexer.getSval());
				if (x.getCode() != 0) {
					bg.genSingleWordCode(mnemonic, r1, x, Comet2BG.AddressingMode.INDEX);
					bg.genAdrCode(sval);
					token = lexer.nextToken();
				} else {
					errorTable.printError(lexer.getLine(), 12);
					er = true;
				}
			} else {
				errorTable.printError(lexer.getLine(), 13, 3);
				er = true;
			}
		} else {
			bg.genSingleWordCode(mnemonic, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
			if(equal) {
				bg.genImmCode(sval);
			}else {
				bg.genAdrCode(sval);
			}
		}
	}
	public boolean hasError(){
		return errorTable.hasError();
	}
	public boolean hasWarning(){
		return errorTable.hasWarning();
	}

	public List<String> getMessages() {
		return errorTable.getMessages();
	}
}