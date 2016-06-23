package casl2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Casl2MacroLexer extends Casl2Lexer{
	private boolean macroSave = false;//マクロ命令ブロック保存用
	private boolean mainSave = false;//メインプログラム保存用
	private boolean useMacro = false;//trueの時
	List<Iterator<Integer>> insMacroCodes = new ArrayList<>();
	List<Integer> savMacroCode = new ArrayList<>();
	List<Integer> unUseMacro = new ArrayList<>();
	int mIndex;
	int mSize=0;
	private StringBuilder output;
	private int macroPeekC;

	public Casl2MacroLexer(BufferedReader r, SymbolTable symbolTable, ErrorTable errorTable) {
		super(r, symbolTable, errorTable);
	}
	@Override
	int read(){
		int c;
		if(!useMacro) {
			c = super.read();
		}else{
			Iterator<Integer> iterator = insMacroCodes.get(mSize);
			if(iterator.hasNext()){
				c = iterator.next();
			}else{
				insMacroCodes.remove(insMacroCodes.size()-1);
				unUseMacro.remove(unUseMacro.size()-1);
				mSize--;
				if(insMacroCodes.isEmpty()){
					useMacro = false;
				}
				return this.read();
			}
		}
		if(macroSave){
			savMacroCode.add(mIndex, c);
			mIndex++;
		}
		if(mainSave){ write(c);}
		macroPeekC = c;
		return  c;
	}

	void write(int c){
		if(c !=-1)
		output.appendCodePoint(c);
	}


	void readMacroStart(){
		macroSave = true;
		mIndex = 0;
	}
	void stopRead(){
		macroSave = false;
		mIndex = 0;
		savMacroCode = new ArrayList<>();
	}
	List<Integer> getCodeBlock(){return savMacroCode;}

	public boolean insertMacro(List<Integer> mBlock,int mid){
		if(unUseMacro.contains(mid)){
			return false;
		}else {
			Iterator<Integer> iterator = mBlock.iterator();
			insMacroCodes.add(iterator);
			unUseMacro.add(mid);
			mSize = insMacroCodes.size() - 1;
			useMacro = true;
			return true;
		}
	}

	public void saveCode(StringBuilder w){
		this.output = w;
		output.append(super.getSval());
		output.appendCodePoint(macroPeekC);
		mainSave = true;
	}
	public StringBuilder getOutput(){
		return output;
	}
	public void stopSaveMainCode(){
		mainSave = false;
	}
	public void resumeSaveMainCode(){
		mainSave = true;
	}
}
