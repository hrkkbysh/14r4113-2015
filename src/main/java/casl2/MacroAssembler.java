package casl2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
		for (skiptoNext(); token != Casl2Symbol.EOF && token ==Casl2Symbol.MACRO;skiptoNext()) {
			if (token == Casl2Symbol.MACRO) {
				token = lexer.nextToken();
				if(token ==Casl2Symbol.EOL){
					token = lexer.nextToken();
					checkHeader();
				}else{
					//invalid macro header
				}
			}else{
				//invalid macro header
			}
		}
		if(!err){
			//checkMacros();
		}
		if(!err){
			StringBuilder buf = insertMacroToMain();
			BufferedWriter writer = null;
			try {
				writer = Files.newBufferedWriter(path);
				writer.write(buf.toString());
				writer.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}


	private void checkHeader() {
		skiptoNext();
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
							errorTable.writeError(lexer.getLine(), 9, token.toString());//no args
							break;
					}
					if (err) break;
					token = lexer.nextToken();
				} while (token == Casl2Symbol.COMMA);
				if(token !=Casl2Symbol.EOL) err = true;
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
									lexer.stopSave();
									macroInst.add(md);
									symbolTable.addMacroSymbol(md.getInstID());
								}else{
									errorTable.writeError(1,1,"");
								}
								return;
							case MACRO_ARG:
								if(!argIDs.contains(lexer.getNval())){
									errorTable.writeError(1,1,"");
								}
								break;
							case START:
							case END:
							case EOF:
								lexer.stopSave();
								md.setMBlock(lexer.getCodeBlock());
								macroInst.add(md);
								errorTable.writeError(1,1,"");
								return;
							default:
								break;
						}
					}
				}
				break;
			default:
				err = true;
		}
	}
	public StringBuilder insertMacroToMain(){
		StringBuilder buf = new StringBuilder();
		lexer.saveCode(buf);
		token = lexer.nextToken();
		for (skiptoNext(); token != Casl2Symbol.EOF;skiptoNext()) {
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
							buf.delete(buf.length()-lexer.getSval().length()-1,
									buf.length());
							lexer.stopSaveMainCode();
							insertMacroCode(buf, cand);
							break;
						}
					}
					break;
				default: skiptoEOL();
					break;
			}
		}
		System.out.println(buf.toString());
		return buf;
	}

	private void insertMacroCode(StringBuilder buf,MacroData md) {
		List<Integer> codeBlock = md.getmBlock();
		StringBuilder sb = new StringBuilder();
		for(int i = 0,size = codeBlock.size(); i<size;i++){
			sb.appendCodePoint(codeBlock.get(i));
		}
		String code = sb.toString();
		int index = 0;
		token = lexer.nextToken();
		for (int i : md.getArgIDs()) {
			CharSequence arg = "$" + symbolTable.getLabel(i);
			code = code.replace(arg,lexer.getSval());

			token = lexer.nextToken();
			if(token == Casl2Symbol.COMMA){
				token = lexer.nextToken();
				index++;
			}else if(index!=md.getArgIDs().size()) {
				errorTable.writeError(lexer.getLine(), 15);
				break;
			}
		}
		if(token == Casl2Symbol.EOL) {
			buf.append(code);
			lexer.resumeSaveMainCode();
		}
	}

	void skiptoEOL(){
		while(true){
			if(token != Casl2Symbol.EOF && token != Casl2Symbol.EOL){
				token = lexer.nextToken();
			}else break;
		}
	}

	void skiptoNext(){
		while(true){
			if(token == Casl2Symbol.EOL){
				token = lexer.nextToken();
			}else break;
		}
	}
}
