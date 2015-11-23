package comet2;

import casl2.LabelSymbol;

import java.util.List;

public class Comet2Debugger {
	private List<Comet2Simulator> history;
	private List<LabelSymbol> symbolTable;
	private int unit;
	private List<LabelSymbol> stopSymbols;
	private List<Integer> bpList;

	public Comet2Debugger() {}

	public List<Comet2Simulator> getHistory() {return history;}
	public void setHistory(Comet2Simulator comet2) {this.history.add(comet2);}

	public List<LabelSymbol> getSymbolTable() {return symbolTable;}
	public void setSymbolTable(List<LabelSymbol> symbolTable) {this.symbolTable = symbolTable;}

	public int getUnit() {return unit;}
	public void setUnit(int unit) {this.unit = unit;}

	public List<LabelSymbol> getStopSymbols() {return stopSymbols;}
	public void setStopSymbol(LabelSymbol stopSymbol) {this.stopSymbols.add(stopSymbol);}
	
	public List<Integer> getBpList() {return bpList;}
	public void setBpList(Integer bp) {this.bpList.add(bp);}
	
	private boolean checkCondition(Comet2Simulator cs) {return false;}
	public void exec(Comet2Simulator cs){
		 for(int i = 0 ; i < unit && checkCondition(cs) ; i++){
			 cs.singleStepExec();
		 }
	}
	
	public void back(){}
	public void showTraceView(Comet2Simulator cs){}
	public void showTraceView(){}
}