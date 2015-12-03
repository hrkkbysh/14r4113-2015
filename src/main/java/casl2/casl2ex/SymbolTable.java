package casl2.casl2ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private static final Map<String, Casl2Symbol> symbolMap = new HashMap<>();
    private static final Map<Integer, String> _allLabel = new HashMap<>();
    private static final List<LabelSymbol> lbltbl = new ArrayList<>();
    private static final int MAX_LABEL = 65535;
    private static int id = 0;
    static {
        for (Casl2Symbol symbol: Casl2Symbol.values())
            symbolMap.put(symbol.toString(), symbol);
    }
    public static Casl2Symbol searchSymbol(String cand){
        Casl2Symbol symbol = symbolMap.getOrDefault(cand, Casl2Symbol.LABEL);
        return symbol == Casl2Symbol.LABEL ? registerLabel(cand) : symbol;
    }

    private static Casl2Symbol registerLabel(String cand){
        _allLabel.putIfAbsent(id, cand);
        return Casl2Symbol.LABEL;
    }
    public static String getLabel(int id){
        return _allLabel.getOrDefault(id,null);
    }
    public static int getLabelID(){return id;}

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
}