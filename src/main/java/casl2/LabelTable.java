package casl2;

import java.util.ArrayList;
import java.util.List;


public class LabelTable {
	private List<LabelSymbol> labelTable = new ArrayList<>();

	public void addRef(String symbol,int refLocation,int proRefLoc){
		for(LabelSymbol ls:labelTable){
			if(ls.getName().equals(symbol)){
				ls.addRefineLocations(refLocation, proRefLoc);
				return;
			}
		}
		labelTable.add(new LabelSymbol(symbol, -1, refLocation));
	}
	public boolean addDef(String symbol,int defLocation){
		for(LabelSymbol ls:labelTable){
			if(ls.getName().equals(symbol)) if (ls.getDefineLocation() != -1) {
				return false;//ìÒèdíËã`
			} else {
				ls.setName(symbol);
				ls.setDefineLocation(defLocation);
				return true;
			}
		}
		LabelSymbol defSymbol = new LabelSymbol(symbol, defLocation);
		labelTable.add(defSymbol);
		return true;
	}

	public List<LabelSymbol> getLabelTable(){
		return labelTable;
	}
}
