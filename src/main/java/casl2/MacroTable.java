package casl2;

import java.util.HashMap;
import java.util.Map;

public class MacroTable {
	Map<Integer, Comet2BG.ObjCode[]> macroMap = new HashMap<>();
	public MacroTable(){}
	public boolean regMacro(Integer macro, Comet2BG.ObjCode[] codes){
		if(macroMap.get(macro)==null) {
			macroMap.putIfAbsent(macro, codes);
			return true;
		}
		return false;
	}
	public Comet2BG.ObjCode[] getCodes(Integer macro){
		return macroMap.getOrDefault(macro,null);
	}
}