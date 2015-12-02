package casl2.casl2ex;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private static final Map<String, Casl2Symbol> symbolMap = new HashMap<>();
    static {
        for (Casl2Symbol symbol: Casl2Symbol.values())
            symbolMap.put(symbol.toString(), symbol);
    }

    public static Casl2Symbol searchSymbol(String cand){
        return symbolMap.getOrDefault(cand, Casl2Symbol.LABEL);
    }
}
