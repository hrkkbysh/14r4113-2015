package casl2;

import java.io.*;
import java.nio.file.Files;
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
	private boolean err;
	private List<MacroData> macroInst = new ArrayList<>();

	public MacroAssembler(BufferedReader reader) {
		symbolTable = new SymbolTable();
		symbolTable.setSymbolTable(AsmMode.EXTEND);
		errorTable = new ErrorTable();
		lexer = new Casl2MacroLexer(reader, symbolTable, errorTable);
	}

	public boolean checkMacro(Path path) {
		err = false;
		token = lexer.nextToken();
		for (skipToNext(); token ==Casl2Symbol.MACRO;skipToNext()){
			token = lexer.nextToken();
			if(token ==Casl2Symbol.EOL){
				token = lexer.nextToken();
				checkHeader();
			}else{
				errorTable.printError(lexer.getLine(),20);
			}
		}
		if(!err) {
			StringBuilder buf = new StringBuilder();
			lexer.saveCode(buf);
			boolean res = insertMacroToMain();
			if (res){
				try {
					BufferedWriter writer = Files.newBufferedWriter(path);
					writer.write(lexer.getOutput().toString());
					writer.close();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}


	private void checkHeader() {
		skipToNext();
		int lblID=-1;
		if(token == Casl2Symbol.MACRO_ARG){
			lblID = lexer.getNval();
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
							break;
						default:
							err = true;
							errorTable.printError(lexer.getLine(), 21, token.toString());//invalid args
							break;
					}
					if (err) break;
					token = lexer.nextToken();
				} while (token == Casl2Symbol.COMMA);
				if(token !=Casl2Symbol.EOL){
					errorTable.printError(lexer.getLine(),28);
					err = true;
				}
				if (!err) {
					MacroData md = new MacroData(lblID,instID,argIDs);
					lexer.readMacroStart();
					while(true){
						token = lexer.nextToken();
						switch(token){
							case MEND:
								token = lexer.nextToken();
								if(token == Casl2Symbol.EOL) {
									List<Integer> codeBlock = lexer.getCodeBlock();
									int codeBlockSize = codeBlock.size() - 6;
									for (int i = codeBlock.size()-1; i >= codeBlockSize; i--){
										codeBlock.remove(i);
									}
									md.setMBlock(lexer.getCodeBlock());
									lexer.stopRead();
									macroInst.add(md);
									symbolTable.addMacroSymbol(md.getInstID());
								}else{
									errorTable.printError(lexer.getLine(),22);
								}
								return;
							case MACRO_ARG:
								if(!argIDs.contains(lexer.getNval())){
									errorTable.printError(lexer.getLine(),23,"$"+lexer.getSval());
								}
								break;
							case START:
							case END:
							case EOF:
								lexer.stopRead();
								md.setMBlock(lexer.getCodeBlock());
								macroInst.add(md);
								err =true;
								errorTable.printError(lexer.getLine(),24);
								return;
							default:
								break;
						}
					}
				}
				break;
			case NUM_CONST:
			case DS_CONST:
				errorTable.printError(lexer.getLine(),25,lexer.getNval());
				break;
			case EQUAL:
				errorTable.printError(lexer.getLine(),25,"=");
				break;
			case COMMA:
				errorTable.printError(lexer.getLine(),25,",");
				break;
			case STR_CONST:
			default:
				errorTable.printError(lexer.getLine(),25,lexer.getSval());
		}
		err = true;
	}
	public boolean insertMacroToMain(){
		StringBuilder buf = lexer.getOutput();
		token = lexer.nextToken();
		for (skipToNext(); token != Casl2Symbol.EOF; skipToNext()) {
			int labelid = -1;
			switch(token){
				case LABEL:
					labelid = symbolTable.getLabelID();
					token = lexer.nextToken();
			}
			switch (token){
				case MACRO_INST:
					int mid = lexer.getNval();
					for(MacroData cand :macroInst){
						if(cand.getInstID() ==mid){
							buf.delete(buf.length()-lexer.getSval().length()-1, buf.length());
							boolean res = insertMacroCode(labelid,cand);
							if(!res) return false;
							break;
						}
					}
					break;
				default: skipToEOL();
					break;
			}
		}
		System.out.println(buf.toString());
		return  true;
	}

	private boolean insertMacroCode(int labelID,MacroData md) {
		lexer.stopSaveMainCode();
		List<Integer> codeBlock = md.getmBlock();
		StringBuilder sb = new StringBuilder();
		codeBlock.forEach(sb::appendCodePoint);
		String macro = sb.toString();
		if(labelID!=-1){
			CharSequence arg = "$" + symbolTable.getLabel(labelID);
			macro = macro.replace(arg,lexer.getSval());
		}
		int index = 0;
		token = lexer.nextToken();
		for (int i : md.getArgIDs()) {
			CharSequence arg = "$" + symbolTable.getLabel(i);
			macro = macro.replace(arg,lexer.getSval());
			token = lexer.nextToken();
			if(token == Casl2Symbol.COMMA){
				token = lexer.nextToken();
				index++;
			}
			if(token ==Casl2Symbol.EOL || token == Casl2Symbol.EOF) break;
		}
		if(index!=md.getArgIDs().size()-1) {
			errorTable.printError(lexer.getLine(), 15);
			return false;
		}
		if(token == Casl2Symbol.EOL) {
			byte[] codeBytes = macro.getBytes();
			List<Integer> c = new ArrayList<>();
			for (byte codeByte : codeBytes) {
				c.add(0x00FFFF & codeByte);
			}
			if(lexer.insertMacro(c,md.getInstID())) {
				lexer.resumeSaveMainCode();
			}else{
				errorTable.printError(lexer.getLine(),27);
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
			if(token != Casl2Symbol.EOF && token != Casl2Symbol.EOL){
				token = lexer.nextToken();
			}else break;
		}
	}

	void skipToNext(){
		while(true){
			if(token == Casl2Symbol.EOL){
				token = lexer.nextToken();
			}else break;
		}
	}
	public boolean hasError(){
		return errorTable.hasError();
	}
}
