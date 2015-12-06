package casl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private static Map<String, Casl2Symbol> symbolMap = new HashMap<>();
    private Map<Integer, String> _allLabel = new HashMap<>();
    private List<LabelSymbol> lbltbl = new ArrayList<>();
    private int id;
	static{
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
		symbolMap.remove("MACRO_START");
		symbolMap.remove("MACRO_END");
		symbolMap.remove("PR");
		symbolMap.remove("SP");
	}
    public SymbolTable(){
	    id = 0;
    }
    public Casl2Symbol searchSymbol(String cand){
        Casl2Symbol symbol = symbolMap.getOrDefault(cand, Casl2Symbol.LABEL);
        if(symbol == Casl2Symbol.LABEL){
            return registerLabel(cand);
        }else {return symbol;}
    }

     Casl2Symbol registerLabel(String cand){
		if(!_allLabel.containsValue(cand)) {
			_allLabel.put(id, cand);
			id++;
		}
        return Casl2Symbol.LABEL;
    }
    public String getLabel(int id){
        return _allLabel.getOrDefault(id,null);
    }
    public int getLabelID(){return id-1;}

    public boolean addLblDefLoc(int symbolID, int lblDefLoc) {
        for(LabelSymbol ls:lbltbl){
            if(ls.getID()==symbolID) {
                if (ls.getDefineLocation() != -1) {
                    return false;
                } else {
                    ls.setDefineLocation(lblDefLoc);
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
		id = 0;
	}
}