package comet2casl2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Casl2MacroLexer extends Casl2Lexer {
	private boolean macroSave = false;
	private boolean mainSave = false;
	private boolean macro = false;
	List<Iterator<Integer>> writeMacro = new ArrayList<>();
	List<Integer> saveMacro = new ArrayList<>();
	List<Integer> unUseMacro = new ArrayList<>();
	StringBuilder output;
	int mIndex;
	int mSize=0;
	private int macroPeekC;

	public Casl2MacroLexer(Path path,String charset, ErrorTable errorTable) {
		super(path,charset,errorTable);
	}
	public Casl2MacroLexer(String r, ErrorTable errorTable) {
		super(r, errorTable);
	}
	@Override
	int read(){
		int c;
		if(macro){
			Iterator<Integer> iterator = writeMacro.get(mSize);
			if(iterator.hasNext()){
				c = iterator.next();
			}else{
				writeMacro.remove(writeMacro.size()-1);
				unUseMacro.remove(unUseMacro.size()-1);
				mSize--;
				if(writeMacro.isEmpty()){
					macro = false;
				}
				return this.read();
			}
		}else{
			c = super.read();
		}
		if(macroSave){
			saveMacro.add(mIndex, c);
			mIndex++;
		}
		if(mainSave){ write(c);}
		macroPeekC = c;
		return  c;
	}
	@Override
	protected LexerState OTHER(int c) {
		switch(c){
			case '.':peekc = super.read(); return STATE = LexerState.DOT;
			case '$':peekc = super.read(); return STATE = LexerState.DOLL;
			default:
				return ERROR(c);
		}
	}

	void write(int c){
		if(c !=-1) {
			output.append((char) c);
		}
	}
	public void delete(int start,int end){
		output.delete(start,end);
	}

	void readMacroStart(){
		macroSave = true;
		mIndex = 0;
	}
	void stopRead(){
		macroSave = false;
		mIndex = 0;
		saveMacro = new ArrayList<>();
	}
	List<Integer> getCodeBlock(){return saveMacro;}

	public boolean insertMacro(List<Integer> mBlock,int mid){
		if(unUseMacro.contains(mid)){
			return false;
		}else {
			Iterator<Integer> iterator = mBlock.iterator();
			writeMacro.add(iterator);
			unUseMacro.add(mid);
			mSize = writeMacro.size() - 1;
			macro = true;
			return true;
		}
	}
	public int getNest(){
		return writeMacro.size();
	}

	public void saveCode(){
		this.output =new StringBuilder(super.getSval());
		output.append((char)macroPeekC);
		mainSave = true;
	}
	public StringBuilder getOutput(){return output;}
	public void stopSaveMainCode(){
		mainSave = false;
	}
	public void resumeSaveMainCode(){
		mainSave = true;
	}
}
