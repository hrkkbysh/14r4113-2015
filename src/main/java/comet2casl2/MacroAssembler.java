package comet2casl2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Haruki on 2015/12/12.
 */

public class MacroAssembler {
	private Casl2MacroLexer lexer;
	private SymbolTable symbolTable;
	private ErrorTable errorTable;
	private Casl2Symbol token;
	private List<MacroData> macroInst = new ArrayList<>();
	int macroSize;


	public MacroAssembler(Path path,String charset) {
		symbolTable = new SymbolTable();
		symbolTable.setSymbolTable(AsmMode.EXTEND);
		errorTable = new ErrorTable();
		lexer = new Casl2MacroLexer(path,charset, errorTable);
	}

	public String parse() {
		lexer.nextToken();
		for (nextLine(); token != Casl2Symbol.MACRO; nextLine()){
			lexer.nextToken();
			if(Casl2Lexer.STATE == LexerState.EOL){
				lexer.nextToken();
				checkHeader();
			}else{
				errorTable.printError(lexer.getLine(),20);
			}
		}
		if(!errorTable.hasError()) {
			macroSize = lexer.getLine();
			lexer.saveCode();
			return insertMacroToMain();
		}
		return null;
	}


	private void checkHeader() {
		nextLine();
		int lblID = -1;
		lblID = checkMacroArg();
		int instID = -1;
		List<Integer> argIDs = new ArrayList<>();
		for (int ms = 0; Casl2Lexer.STATE != LexerState.EOF; ) {
			switch (ms) {
				case -1:
					errorTable.printError(lexer.getLine(), 21, token.toString());//invalid args
					return;
				case 0:
					if (Casl2Lexer.STATE == LexerState.KEYWORD) {
						instID = symbolTable.getLabelID();
						lexer.nextToken();
						ms = 1;
						break;
					} else {
						//error
						ms = -1;
						break;
					}
				case 1:
					int id = checkMacroArg();
					if (id != -1) {
						argIDs.add(id);
						ms = 2;
					} else {
						errorTable.printError(lexer.getLine(), 21, token.toString());//invalid args
						ms = -1;
					}
					break;
				case 2:
					if (Casl2Lexer.STATE == LexerState.COMMA) {
						ms = 1;
						break;
					}
					if (Casl2Lexer.STATE != LexerState.EOL) {
						errorTable.printError(lexer.getLine(), 28);
						ms = -1;
						break;
					}
					MacroData md = new MacroData(lblID, instID, argIDs);
					lexer.readMacroStart();
					while (Casl2Lexer.STATE !=LexerState.EOF) {
						lexer.nextToken();
						token = symbolTable.searchSymbol(lexer.getSval());
						switch (token) {
							case MEND:
								List<Integer> codeBlock = lexer.getCodeBlock();
								int codeBlockSize = codeBlock.size() - 6;
								for (int i = codeBlock.size() - 1; i >= codeBlockSize; i--) {
									codeBlock.remove(i);
								}
								md.setMBlock(lexer.getCodeBlock());
								lexer.stopRead();
								macroInst.add(md);
								symbolTable.addMacroSymbol(md.getInstID());
								lexer.nextToken();
								if (Casl2Lexer.STATE != LexerState.EOL){
									errorTable.printError(lexer.getLine(), 22);
								}
								return;
							case MACRO_ARG:
								if (!argIDs.contains(lexer.getNval())) {
									errorTable.printError(lexer.getLine(), 23, "$" + lexer.getSval());
								}
								break;
							case START:
							case END:
								lexer.stopRead();
								md.setMBlock(lexer.getCodeBlock());
								macroInst.add(md);
								errorTable.printError(lexer.getLine(), 24);
								return;
							default:
								break;
						}
					}
			}
		}
	}
	public String insertMacroToMain(){
		StringBuilder buf = lexer.getOutput();
		for (nextLine(); Casl2Lexer.STATE == LexerState.EOF; nextLine()) {
			int lblID = -1;
			if(Casl2Lexer.STATE == LexerState.KEYWORD) {
				token = symbolTable.searchSymbol(lexer.getSval());

				switch (token) {
					case LABEL:
						lblID = symbolTable.getLabelID();
						lexer.nextToken();
						token = symbolTable.searchSymbol(lexer.getSval());
					case MACRO_INST:
						int mid = symbolTable.getLabelID();
						for (MacroData cand : macroInst) {
							if (cand.getInstID() == mid) {
								lexer.delete(buf.length() - lexer.getSval().length() - 1, buf.length());
								cand.setProLc(lexer.getLine(),lexer.getNest());
								boolean res = insertMacroCode(lblID, cand);
								if (!res){
									cand.setProLc(lexer.getLine(),lexer.getNest());
									return null;
								}
								break;
							}
						}
						break;
					default:
						skipToEOL();
						break;
				}
			}else{
				skipToEOL();
			}
		}
		return  buf.toString();
	}

	private boolean insertMacroCode(int labelID, MacroData md) {
		lexer.stopSaveMainCode();
		List<Integer> codeBlock = md.getmBlock();
		StringBuilder sb = new StringBuilder();
		codeBlock.forEach(sb::appendCodePoint);
		String macro = sb.toString();
		if(labelID!=-1){
			CharSequence fromLbl = symbolTable.getLabel(md.getLblID());
			CharSequence toLbl = symbolTable.getLabel(labelID);
			macro = macro.replace(fromLbl + "\\.", toLbl);
			macro = macro.replace(fromLbl, toLbl);
		}
		int index = 0;
		lexer.nextToken();
		for (int i : md.getArgIDs()) {
			CharSequence arg = symbolTable.getLabel(i);
			macro = macro.replace(arg, lexer.getSval());
			lexer.nextToken();
			if(Casl2Lexer.STATE == LexerState.COMMA){
				lexer.nextToken();
				index++;
			}
			if(Casl2Lexer.STATE == LexerState.EOL || Casl2Lexer.STATE == LexerState.EOF) break;
		}
		if(index!=md.getArgIDs().size()-1) {
			errorTable.printError(lexer.getLine(), 15);
			return false;
		}
		if(Casl2Lexer.STATE == LexerState.EOL) {
			byte[] codeBytes = macro.getBytes();
			List<Integer> c = new ArrayList<>();
			for (byte codeByte : codeBytes) {
				c.add(0x00FFFF & codeByte);
			}
			if(lexer.insertMacro(c,md.getInstID())) {
				lexer.resumeSaveMainCode();
			}else{
				errorTable.printError(lexer.getLine(),27);//呼び出しループが発生
				return  false;
			}
		}else{
			errorTable.printError(lexer.getLine(),26);
			lexer.resumeSaveMainCode();
		}
		return true;
	}

	void skipToEOL(){
		while(true){
			if(Casl2Lexer.STATE == LexerState.EOF && Casl2Lexer.STATE != LexerState.EOL){
				lexer.nextToken();
			}else break;
		}
	}

	void nextLine(){
		while(true){
			if(Casl2Lexer.STATE == LexerState.EOL){
				lexer.nextToken();
			}else{
				token = symbolTable.searchSymbol(lexer.getSval());
				break;
			}
		}
	}
	public boolean hasError(){
		return errorTable.hasError();
	}
	//helper class
	public int checkMacroArg(){
		if(Casl2Lexer.STATE == LexerState.DOLL){
			lexer.nextToken();
			switch(Casl2Lexer.STATE){
				case NUMBER:
					token = symbolTable.searchSymbol("$"+String.valueOf(lexer.getNval()));
					return symbolTable.getLabelID();
				case KEYWORD:
					token = symbolTable.searchSymbol("$"+lexer.getSval());
					if(token == Casl2Symbol.LABEL){
						return symbolTable.getLabelID();
					}else{
						//error
					}
			}
			lexer.nextToken();
		}
		return -1;
	}
}