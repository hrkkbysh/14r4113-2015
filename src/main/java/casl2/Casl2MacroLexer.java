package casl2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Casl2MacroLexer extends Casl2Lexer{
	private boolean macroSave = false;
	private boolean mainSave = false;
	private boolean usemacro = false;
	List<Integer> macroCode = new ArrayList<>();
	int mindex;
	int mLength;
	private StringBuilder output;

	public Casl2MacroLexer(BufferedReader r, SymbolTable symbolTable, ErrorTable errorTable) {
		super(r, symbolTable, errorTable);
	}
	@Override
	int read(){
		int c = -1;
		if(!usemacro) {
			c = super.read();
		}else{
			if(mLength<mindex) {
				c = macroCode.get(mindex);
				mindex++;
			}else{
				usemacro = false;
				return super.read();
			}
		}
		if(macroSave){
			macroCode.add(mindex, c);
			mindex++;
		}
		if(mainSave){ write(c);}
		return  c;
	}

	void write(int c){
		if(c !=-1)
		output.appendCodePoint(c);
	}


	void readMacroStart(){
		macroSave = true;
		mindex = 0;
	}
	void stopSave(){
		macroSave = false;
		mindex = 0;
		macroCode = new ArrayList<>();
	}
	List<Integer> getCodeBlock(){return macroCode;}
	public void insertMacro(List<Integer> mblock){
		macroCode.clear();
		macroCode.addAll(mblock);
		mLength = macroCode.size();
		usemacro = true;
	}

	public void saveCode(StringBuilder w){
		this.output = w;
		mainSave = true;
	}
	public void stopSaveMainCode(){
		mainSave = false;
	}
	public void resumeSaveMainCode(){
		mainSave = true;
	}
}
