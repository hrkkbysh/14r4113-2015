package comet2casl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
	private static final Map<String, Casl2Symbol> _allSymbol = new HashMap<>();
	static{
		for (Casl2Symbol symbol: Casl2Symbol.values())
			_allSymbol.put(symbol.toString(), symbol);
		for(RegMember reg: RegMember.values()){
			_allSymbol.put(reg.toString(),Casl2Symbol.REGISTER);
		}
		_allSymbol.remove("REGISTER");
		_allSymbol.remove("MACRO_ARG");
		_allSymbol.remove("MACRO_INST");
	}
	private List<String> _allLabel = new ArrayList<>();
	private List<LabelSymbol> lbltbl = new ArrayList<>();
	private List<Integer> _allMacro = new ArrayList<>();
	private int current_id;
	private int lbl_id;

	public SymbolTable(){
		current_id = 0;
	}
	public void setSymbolTable(AsmMode mode){
		switch (mode){
			case NORMAL:
				_allSymbol.remove("PR");
				_allSymbol.remove("SP");
				break;
			case EXTEND:
				_allSymbol.putIfAbsent("PR", Casl2Symbol.REGISTER);
				_allSymbol.putIfAbsent("SP", Casl2Symbol.REGISTER);
				break;
		}
	}

	public Casl2Symbol searchSymbol(String cand){
		Casl2Symbol symbol = _allSymbol.getOrDefault(cand, Casl2Symbol.LABEL);
		if(symbol == Casl2Symbol.LABEL){
			return registerLabel(cand);
		}else {return symbol;}
	}

	Casl2Symbol registerLabel(String cand){
		if(!_allLabel.contains(cand)) {
			_allLabel.add(current_id, cand);
			current_id++;
			lbl_id = current_id -1;
		}else{
			lbl_id = _allLabel.indexOf(cand);
			if(_allMacro.contains(lbl_id)){
				return Casl2Symbol.MACRO_INST;
			}
		}
		return Casl2Symbol.LABEL;
	}

	public String getLabel(int id){
		if(id>=0 && id<_allLabel.size())
		return _allLabel.get(id);
		else return "";
	}
	public int getLabelID(){
		return lbl_id;
	}

	public boolean addLblDefLoc(int symbolID, int lblDefLoc,int proDefLoc) {
		for(LabelSymbol ls:lbltbl){
			if(ls.getID()==symbolID) {
				if (ls.getDefineLocation() != -1) {
					return false;
				} else {
					ls.setDefineLocation(lblDefLoc,proDefLoc);
					return true;
				}
			}
		}
		LabelSymbol defSymbol = new LabelSymbol(symbolID, lblDefLoc);
		lbltbl.add(defSymbol);
		return true;
	}

	public void addLblRefLoc(int symbolID, int lblRefLoc,int proRefLoc) {
		for(LabelSymbol ls:lbltbl){
			if(ls.getID() == symbolID){
				ls.addRefineLocations(lblRefLoc, proRefLoc);
				return ;
			}
		}
		lbltbl.add(new LabelSymbol(symbolID, -1, lblRefLoc));
	}
	public List<LabelSymbol> getLblTbl() {
		return lbltbl;
	}
	public LabelSymbol searchLbltbl(int id){
		for(LabelSymbol ls: lbltbl){
			if(ls.getID()==id)
				return ls;
		}
		return null;
	}

	public void clear() {
		lbltbl.clear();
		_allLabel.clear();
		_allMacro.clear();
		current_id = 0;
	}

	public void addMacroSymbol(int instID) {
		_allMacro.add(instID);
	}
}