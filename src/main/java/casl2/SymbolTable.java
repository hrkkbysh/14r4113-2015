package casl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
	private Map<String, Casl2Symbol> symbolMap = new HashMap<>();
	private List<String> _allLabel = new ArrayList<>();
	private List<LabelSymbol> lbltbl = new ArrayList<>();
	private List<Integer> macroIDs = new ArrayList<>();
	private int current_id;
	private int lbl_id;
	public SymbolTable(){
		current_id = 0;
		for (Casl2Symbol symbol: Casl2Symbol.values())
			symbolMap.put(symbol.toString(), symbol);
		for(Comet2Register cr : Comet2Register.values()){
			symbolMap.put(cr.toString(),Casl2Symbol.GR);
		}
		symbolMap.remove("GR");
		symbolMap.remove("EOL");
		symbolMap.remove("NUM_CONST");
		symbolMap.remove("DS_CONST");
		symbolMap.remove("STR_CONST");
		symbolMap.remove("EQUAL");
		symbolMap.remove("COMMA");
		symbolMap.remove("EOF");
		symbolMap.remove("ERROR");
		symbolMap.remove("MACRO_ARG");
	}

	public void setSymbolTable(AsmMode mode){
		switch (mode){
			case NORMAL:
				symbolMap.remove("PR");
				symbolMap.remove("SP");
				break;
			case EXTEND:
				symbolMap.putIfAbsent("PR",Casl2Symbol.GR);
				symbolMap.putIfAbsent("SP",Casl2Symbol.GR);
				break;
		}
	}

	public Casl2Symbol searchSymbol(String cand){
		Casl2Symbol symbol = symbolMap.getOrDefault(cand, Casl2Symbol.LABEL);
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
			if(macroIDs.contains(lbl_id)){
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
		macroIDs.clear();
		current_id = 0;
	}

	public void addMacroSymbol(int instID) {
		macroIDs.add(instID);
	}
}