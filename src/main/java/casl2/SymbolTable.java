package casl2;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

	private static final Map<String, Casl2Symbol> symbolMap = new HashMap<>();
	static {
		for (Comet2Instruction coi : Comet2Instruction.values())
			symbolMap.put(coi.toString(), Casl2Symbol.MACHINEINST);
		for (Casl2Instruction cai : Casl2Instruction.values())
			symbolMap.put(cai.toString(), Casl2Symbol.ASSEMBLERINST);
		for (MacroInstruction mi : MacroInstruction.values())
			symbolMap.put(mi.toString(), Casl2Symbol.MACROINST);
		for (Comet2Register cr : Comet2Register.values()){
			symbolMap.put(cr.toString(), Casl2Symbol.REGISTER);
		}
	}

	public static Casl2Symbol searchSymbol(String cand){
		return symbolMap.getOrDefault(cand, Casl2Symbol.LABEL);
	}
}
