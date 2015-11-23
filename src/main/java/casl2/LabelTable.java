package casl2;

import assembler.Token;

import java.util.ArrayList;
import java.util.List;


public class LabelTable {
	private List<LabelSymbol> labelTable = new ArrayList<>();
	private List<ImmediateData> imDatas = new ArrayList<>();

	public void addRef(String symbol,int refLocation){
		for(LabelSymbol ls:labelTable){
			if(ls.getName().equals(symbol)){
				ls.addRefineLocation(refLocation);
				return;
			}
		}
		labelTable.add(new LabelSymbol(symbol, -1, refLocation));
	}
	public boolean addDef(String symbol,int defLocation){
		for(LabelSymbol ls:labelTable){
			if(ls.getName().equals(symbol)){
				return false;
			}
		}
		LabelSymbol defSymbol = new LabelSymbol(symbol, defLocation);
		labelTable.add(defSymbol);
		checkedUnresolvedReference(defSymbol);
		return true;
	}

	public void addLit(int lc, Token op) {
		imDatas.add(new ImmediateData(lc, op));
	}

	public void checkedUnresolvedReference(LabelSymbol defSymbol){
		for(LabelSymbol searchSymbol: labelTable){
			if(searchSymbol.getName().equals(defSymbol.getName())){
				for(Integer refLoc:searchSymbol.getRefineLocation())
					defSymbol.addRefineLocation(refLoc);
			}
		}
	}

	public List<LabelSymbol> getLabelTable(){
		return labelTable;
	}
	public void clear(){
		labelTable.clear();
		imDatas.clear();
	}
	public List<ImmediateData> getImmediateData(){
		return imDatas;
	}

	public void addLit(int refLocation){labelTable.add(new LabelSymbol("=", -1, refLocation));}

	private class ImmediateData{
		private final int refineLocation;
		private final Token immediateValue;
		int getRefineLocation() {
			return refineLocation;
		}
		Token getImmediateValue() {
			return immediateValue;
		}
		ImmediateData(int refineLocation, Token immediateValue) {
			this.refineLocation = refineLocation;
			this.immediateValue = immediateValue;
		}

	}
}
