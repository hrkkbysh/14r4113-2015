package casl2;

import java.util.HashMap;
import java.util.Map;

public class ExSymbolTable extends SymbolTable{
    private static Map<String, Casl2Symbol> symbolMap = new HashMap<>();
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
	}
    public ExSymbolTable(){
	    super();
    }

	@Override
	public Casl2Symbol searchSymbol(String cand){
		Casl2Symbol symbol = symbolMap.getOrDefault(cand, Casl2Symbol.LABEL);
		if(symbol == Casl2Symbol.LABEL){
			return registerLabel(cand);
		}else {return symbol;}
	}
}