package casl2;

import assembler.BinaryGenerator;
import assembler.Lexer;
import assembler.Parser;
import assembler.Token;

import static casl2.Casl2Symbol.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class Casl2Parser implements Parser {
	private Lexer lexer;
	private BinaryGenerator bg;
	private ErrorTable errorTable;
	private boolean hasError=false;
	private AtomicInteger asmlc;
	{
		try {
			errorTable = new ErrorTable(Casl2Parser.class.getResource("ErrorDictionary.txt"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}


	public Casl2Parser(Lexer lexer, BinaryGenerator bg) {
		this.lexer = lexer;
		this.bg = bg;
		asmlc = new AtomicInteger(0);
	}

	@Override
	public void program(){
		for(Token token = lexer.nextToken();token.getSymbol()!=EOF;token = lexer.nextToken()){
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
						}else{
							bg.setStartAdr();
						}
						if(token.getSymbol()==Casl2Symbol.EOL){
							content();
							if(!hasError) {
								bg.genFile();
							}
						}
					}
				}
			}
		}
	}


	private void content() {
		for(Token token = lexer.nextToken();token.getSymbol()==EOF;token = lexer.nextToken()){
			if(token.getSymbol() == LABEL){
				bg.defineLabel(token.getToken());
				token = lexer.nextToken();
			}

			switch(token.getSymbol()){
				case ASSEMBLERINST:
					Casl2Instruction cai = Casl2Instruction.valueOf(token.getToken());
					switch(cai){
						case DC:
							token = lexer.nextToken();
							label:if(checkConstant(token)){
								bg.genAdrCode(token, Comet2BG.Immediate.NO);
								if(lexer.nextToken().getSymbol()==COMMA){
									token = lexer.nextToken();
									break label;
								}
							}
							break;
						case DS:
							token = lexer.nextToken();
							if(token.getSymbol()==Casl2Symbol.NUM_CONST){
								bg.genDSArea(Integer.parseInt(token.getToken()));
							}
							break;
						case START:
							//errorTable.write(token);
						case END:
							bg.setEndAdr();
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
										if(lexer.nextToken().getSymbol()==EOL){
											bg.genSingleWordCode(coi, r1, r2, Comet2BG.AddressingMode.REGISTER);
										}
									}else if(token.getSymbol()==LABEL){
										adr_x(coi, r1, token);
									}
								}
							}
							break;
						case 3:
							token = lexer.nextToken();
							if(token.getSymbol()==REGISTER){
								Comet2Register r = Comet2Register.valueOf(token.getToken());
								if(lexer.nextToken().getSymbol()==COMMA){
									token = lexer.nextToken();
									adr_x(coi, r, token);
								}
							}
							break;
						case 2:
							token = lexer.nextToken();
							adr_x(coi, Comet2Register.GR0,token);
							break;
						case 1:
							token = lexer.nextToken();
							if(token.getSymbol()==REGISTER){
								Comet2Register r = Comet2Register.valueOf(token.getToken());
								if(lexer.nextToken().getSymbol()==EOL){
									bg.genRegStackCode(coi, r);
								}
							}
							break;
						case 0:
							if(lexer.nextToken().getSymbol()==EOL){
								bg.genNoOpCode(coi);
							}
							break;
					}
					break;
				case MACROINST:
					MacroInstruction mai = MacroInstruction.valueOf(token.getToken());
					switch(mai){
						case RPOP:
						case RPUSH:
							if(lexer.nextToken().getSymbol()==EOL){
								bg.genMacroBlock(mai);
							}
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
										if(lexer.nextToken().getSymbol()==EOL){
											bg.genMacroBlock(mai,bufLabel,lenLabel);
										}
									}
								}
							}
							break;
						default: break;
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
					//	errorTable.write(token);
			}

		}
	}

	private void adr_x(Comet2Instruction coi, Comet2Register r1,Token token) {
		Token adr = Casl2Token.newInstance(token);
		if(checkAddress(token.getSymbol())){
			if(lexer.nextToken().getSymbol()==COMMA){
				token=lexer.nextToken();
				if(token.getSymbol() == REGISTER){
					Comet2Register x = Comet2Register.valueOf(token.getToken());
					if(x.getCode()!=0){
						if(lexer.nextToken().getSymbol()==EOL){
							bg.genSingleWordCode(coi, r1, x, Comet2BG.AddressingMode.INDEX);
							bg.genAdrCode(adr, Comet2BG.Immediate.NO);
						}
					}
				}
			}else{
				if(lexer.nextToken().getSymbol()==EOL){
					bg.genSingleWordCode(coi, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
					bg.genAdrCode(adr, Comet2BG.Immediate.NO);
				}
			}

		}else if(checkLit(token)){
			adr = Casl2Token.newInstance(token);
			if(lexer.nextToken().getSymbol()==COMMA){
				token=lexer.nextToken();
				if(token.getSymbol() == REGISTER){
					Comet2Register x = Comet2Register.valueOf(token.getToken());
					if(x.getCode()!=0){
						if(lexer.nextToken().getSymbol()==EOL){
							bg.genSingleWordCode(coi, r1, x, Comet2BG.AddressingMode.INDEX);
							bg.genAdrCode(adr, Comet2BG.Immediate.YES);
						}
					}
				}
			}else{
				if(lexer.nextToken().getSymbol()==EOL){
					bg.genSingleWordCode(coi, r1, Comet2Register.GR0, Comet2BG.AddressingMode.INDEX);
					bg.genAdrCode(adr, Comet2BG.Immediate.YES);
				}
			}
		}
	}

	private boolean checkAddress(Casl2Symbol symbol){
		return symbol==LABEL|| symbol==NUM_CONST || symbol == STR_CONST;
	}

	private boolean checkConstant(Token token) {
		Casl2Symbol symbol = token.getSymbol();
		return symbol == NUM_CONST || symbol == STR_CONST || symbol == LABEL;
	}

	private boolean checkLit(Token token){
		if(token.getSymbol()==EQUAL){
			token = lexer.nextToken();
			Casl2Symbol constCand = token.getSymbol();
			if(constCand==NUM_CONST||constCand==STR_CONST)
				return true;
		}
		return false;
	}
}
