package casl2;

import assembler.BinaryGenerator;
import assembler.Parser;
import static casl2.Casl2Symbol.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Casl2Parser implements Parser {
	private Casl2Lexer lexer;
	private BinaryGenerator bg;
	private ErrorTable errorTable;
	private AtomicInteger asmLC;
	private Casl2Token token;

	public Casl2Parser(Casl2Lexer lexer, BinaryGenerator bg) {
		this.lexer = lexer;
		this.bg = bg;
		asmLC = new AtomicInteger(0);
		errorTable = ErrorTable.getInstance();
	}

	@Override
	public void enter(){
		errorTable.clear();
		for(token = lexer.nextToken();token.getSymbol()!=EOF;token = lexer.nextToken()){
			if(token.getSymbol()==LABEL){
				bg.setProgramName(token.getToken());
				token = lexer.nextToken();
				if(token.getSymbol()==ASSEMBLERINST){
					Casl2Instruction cai = Casl2Instruction.valueOf(token.getToken());
					if(cai==Casl2Instruction.START){
						token = lexer.nextToken();
						if(token.getSymbol()==LABEL){
							bg.setStartAdr(token.getToken());
							token = lexer.nextToken();
						}
						if(token.getSymbol()== Casl2Symbol.EOL){
							asmLC.getAndIncrement();
							bg.setStartAdr();
							content();
							bg.genFile();
							continue;
						}else{
							errorTable.writeTemp(asmLC.get(),token,"START命令のシンタックスエラー");
						}
					}
				}else{
					errorTable.writeTemp(asmLC.get(),token,"最初にSTART命令を記述してください");
				}
			}else{
				token = lexer.nextToken();
				if(token.getSymbol()==ASSEMBLERINST) {
					Casl2Instruction cai = Casl2Instruction.valueOf(token.getToken());
					if (cai == Casl2Instruction.START) {
						errorTable.writeTemp(asmLC.get(), token, "START命令のシンタックスエラー(最初のラベルがない)");
					}
				}else{
					errorTable.writeTemp(asmLC.get(), token, "最初の命令はSTART命令を記述する必要があります。");
				}
			}
			asmLC.getAndIncrement();
			setNextLine(token);
			content();
		}
	}


	private void content() {
		for(token = lexer.nextToken();token.getSymbol()!=EOF;token = lexer.nextToken()){
			if(token.getSymbol() == LABEL){
				if(!bg.defineLabel(token.getToken())){
					errorTable.writeTemp(asmLC.get(), token, "二重定義");
				}
				token = lexer.nextToken();
			}

			switch(token.getSymbol()){
				case ASSEMBLERINST:
					Casl2Instruction cai = Casl2Instruction.valueOf(token.getToken());
					switch(cai){
						case DC:
							token = lexer.nextToken();
							if(checkConstant(token.getSymbol())){
								bg.genAdrCode(token, asmLC.get());
								for(token = lexer.nextToken();token.getSymbol()==COMMA;token = lexer.nextToken()){
									if(checkConstant(token.getSymbol())) {
										bg.genAdrCode(token, asmLC.get());
									}else{
										errorTable.writeTemp(asmLC.get(),token,"DC シンタックスエラー");
									}
								}
							}else{
								errorTable.writeTemp(asmLC.get(),token,"DC シンタックスエラー");
							}
							break;
						case DS:
							token = lexer.nextToken();
							if(token.getSymbol()== Casl2Symbol.NUM_CONST){
								bg.genDSArea(Integer.parseInt(token.getToken()));
							}else{
								errorTable.writeTemp(asmLC.get(),token,"DS シンタックスエラー");
							}
							break;
						case START:
							errorTable.writeTemp(asmLC.get(),token,"START命令の位置が不適切です。");
							break;
						case END:
							bg.setEndAdr();
							token = lexer.nextToken();
							checkEOL();
							return;
					}
					break;
				case MACHINEINST:
					Comet2Instruction coi = Comet2Instruction.valueOf(token.getToken());
					switch(coi.getSyntaxType()){
						case 5:
							token = lexer.nextToken();
							if(token.getSymbol()==REGISTER){
								Comet2Register r1 = Comet2Register.valueOf(token.getToken());
								if(lexer.nextToken().getSymbol()==COMMA){
									token = lexer.nextToken();
									if(token.getSymbol()==REGISTER){
										Comet2Register r2 = Comet2Register.valueOf(token.getToken());
										bg.genSingleWordCode(coi, r1, r2, Comet2BG.AddressingMode.REGISTER);
										token = lexer.nextToken();
									}else{
										adr_x(coi, r1);
									}
								}else{
									errorTable.writeTemp(asmLC.get(),token,"カンマが不足しています");
								}
							}else{
								errorTable.writeTemp(asmLC.get(),token,"第一オペランドがレジスタではありません。");
							}
							break;
						case 3:
							token = lexer.nextToken();
							if(token.getSymbol()==REGISTER){
								Comet2Register r = Comet2Register.valueOf(token.getToken());
								if(lexer.nextToken().getSymbol()==COMMA){
									token = lexer.nextToken();
									adr_x(coi, r);
								}else{
									errorTable.writeTemp(asmLC.get(),token,"カンマが不足しています");
								}
							}else{
								errorTable.writeTemp(asmLC.get(),token,"第一オペランドがレジスタではありません。");
							}
							break;
						case 2:
							token = lexer.nextToken();
							adr_x(coi, Comet2Register.GR0);
							break;
						case 1:
							token = lexer.nextToken();
							if(token.getSymbol()==REGISTER){
								Comet2Register r = Comet2Register.valueOf(token.getToken());
								bg.genRegStackCode(coi, r);
								token = lexer.nextToken();
							}else{
								errorTable.writeTemp(asmLC.get(),token,"第一オペランドがレジスタではありません。");
							}
							break;
						case 0:
							bg.genNoOpCode(coi);
							token =lexer.nextToken();
							break;
					}
					break;
				case MACROINST:
					MacroInstruction mai = MacroInstruction.valueOf(token.getToken());
					switch(mai){
						case RPOP:
						case RPUSH:
							bg.genMacroBlock(mai);
							token = lexer.nextToken();
							break;
						case IN:
						case OUT:
							token = lexer.nextToken();
							if(token.getSymbol()==LABEL){
								String bufLabel = token.getToken();
								if(lexer.nextToken().getSymbol()==COMMA){
									token = lexer.nextToken();
									if(token.getSymbol()==LABEL){
										String lenLabel =token.getToken();
										bg.genMacroBlock(mai, bufLabel, lenLabel, asmLC.get());
										token = lexer.nextToken();
									}else{
										errorTable.writeTemp(asmLC.get(),token,"第二オペランドにラベルを記述する必要があります。");
									}
								}else{
									errorTable.writeTemp(asmLC.get(),token,"カンマが不足しています");
								}
							}else{
								errorTable.writeTemp(asmLC.get(),token,"第一オペランドにラベルを記述する必要があります。");
							}
							break;
					}
					break;
				case LABEL:
					break;
				case REGISTER:
					break;
				case EOL:
					break;
				case NUM_CONST:
					break;
				case STR_CONST:
					break;
				case EQUAL:
					break;
				case COMMA:
					break;
				case EOF:
					break;
				case ERROR:
					break;
				default:
			}
			checkEOL();
		}
	}

	private void checkEOL() {
		if(!(token.getSymbol()==EOL)){
			errorTable.writeTemp(asmLC.get(),token,"not EOL");
			setNextLine(token);
		}
		asmLC.incrementAndGet();
	}
	private void setNextLine(Casl2Token token){
		while (token.getSymbol()!=EOL) {
			token = lexer.nextToken();
		}
	}

	private void adr_x(Comet2Instruction coi, Comet2Register r1) {
		Casl2Token adr = new Casl2Token(token.getSymbol(),token.getToken());
		if(checkAddress(token.getSymbol())){
			token = lexer.nextToken();
			if(token.getSymbol()==COMMA){
				token=lexer.nextToken();
				if(token.getSymbol() == REGISTER){
					Comet2Register x = Comet2Register.valueOf(token.getToken());
					if(x.getCode()!=0){
						bg.genSingleWordCode(coi, r1, x, Comet2BG.AddressingMode.INDEX);
						bg.genAdrCode(adr, asmLC.get());
						token = lexer.nextToken();
					}else{
						errorTable.writeTemp(asmLC.get(),token,"GR0はインデックスレジスタとして使えません。");
					}
				}else{
					errorTable.writeTemp(asmLC.get(),token,"不適切なオペランドです。第三オペランドはインデックスレジスタを指定してください。");
				}
			}else{
				bg.genSingleWordCode(coi, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
				bg.genAdrCode(adr, asmLC.get());
			}
		}else if(token.getSymbol()==EQUAL){
			token = lexer.nextToken();
			if(checkLit(token.getSymbol())) {
				adr = new Casl2Token(token.getSymbol(), token.getToken());
				token = lexer.nextToken();
				if (token.getSymbol() == COMMA) {
					token = lexer.nextToken();
					if (token.getSymbol() == REGISTER) {
						Comet2Register x = Comet2Register.valueOf(token.getToken());
						if (x.getCode() != 0) {
							bg.genSingleWordCode(coi, r1, x, Comet2BG.AddressingMode.INDEX);
							bg.genImmediateCode(adr, asmLC.get());
							token = lexer.nextToken();
						} else {
							errorTable.writeTemp(asmLC.get(), token, "GR0はインデックスレジスタとして使えません。");
						}
					} else {
						errorTable.writeTemp(asmLC.get(), token, "不適切なオペランドです。第三オペランドはインデックスレジスタを指定してください。");
					}
				} else {
					bg.genSingleWordCode(coi, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
					bg.genImmediateCode(adr, asmLC.get());
				}
			}else{
				errorTable.writeTemp(asmLC.get(),token,"リテラルの記述が間違っています。");
			}
		}else{
			errorTable.writeTemp(asmLC.get(),token,"アドレスの記述が間違っています。");

		}
	}


	private boolean checkAddress(Casl2Symbol symbol){
		return symbol==LABEL|| symbol==NUM_CONST || symbol == STR_CONST;
	}

	private boolean checkConstant(Casl2Symbol symbol) {
		return symbol == NUM_CONST || symbol == STR_CONST || symbol == LABEL;
	}

	private boolean checkLit(Casl2Symbol symbol){
		return (symbol==NUM_CONST||symbol==STR_CONST);
	}
}