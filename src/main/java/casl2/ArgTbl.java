package casl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgTbl {

	private Map<Integer,String> _allArg = new HashMap<>();
	private List<LabelSymbol> argtbl = new ArrayList<>();
	private int mid = 0;
	public int registerAndGetArgID(String cand) {
		if(!_allArg.containsValue(cand)) {
			_allArg.put(mid, cand);
			mid++;
		}
		return mid -1;
	}
	public String getArg(int id){
		return _allArg.getOrDefault(id,null);
	}
	public boolean addArgDefLoc(int symbolID, int lblDefLoc) {
		for(LabelSymbol ls:argtbl){
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
		argtbl.add(defSymbol);
		return true;
	}

	public void addArgRefLoc(int symbolID, int lblRefLoc,int proRefLoc) {
		for(LabelSymbol ls:argtbl){
			if(ls.getID() == symbolID){
				ls.addRefineLocations(lblRefLoc, proRefLoc);
				return ;
			}
		}
		argtbl.add(new LabelSymbol(symbolID, -1, lblRefLoc));
	}
	public List<LabelSymbol> getArgTbl() {
		return argtbl;
	}
	public LabelSymbol searchArgtbl(int id){
		for(LabelSymbol ls: argtbl){
			if(ls.getID()==id)
				return ls;
		}
		return null;
	}
}
