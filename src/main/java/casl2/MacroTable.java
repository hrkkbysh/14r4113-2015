package casl2;

import java.util.HashMap;
import java.util.Map;

public class MacroTable {
	Map<Integer, String> macroMap = new HashMap<>();
	public MacroTable(){}
	public boolean regMacro(Integer macroID, String codes){
		if(macroMap.get(macroID)==null) {
			macroMap.putIfAbsent(macroID, codes);
			return true;
		}
		return false;
	}
	public String getCodes(Integer macro){
		return macroMap.getOrDefault(macro,null);
	}
}