package editor;

import casl2.Comet2Instruction;

import java.util.HashMap;
import java.util.Map;

import static casl2.MacroInstruction.*;
import static casl2.Casl2Instruction.*;

public class InstructionEntry{
	public static final Map<String, Integer> map = new HashMap<>();


	static{
		initAssemblyInstEntry();
		initCPUInstEntry();
		initMacroInstEntry();
	}
	
	private static final void initAssemblyInstEntry(){
		add(START.toString(),-1);
		add(END.toString(),-2);
		add(DS.toString(),-3);
		add(DC.toString(),-4);
	}
	private static final void initMacroInstEntry() {
		add(IN.toString(), 10);
		add(OUT.toString(), 10);
		add(RPUSH.toString(),11);
		add(RPOP.toString(),11);
	}
	private static final void initCPUInstEntry(){
		for (Comet2Instruction is: Comet2Instruction.values()) {
			switch(is.getSyntaxType()){
			case 0:
				add(is.toString(),0);
			case 1:
				add(is.toString(),1);
				break;
			case 2:
				add(is.toString(),2);
				break;
			case 3:
				add(is.toString(),3);
				break;
			case 5:
				add(is.toString(),5);
				break;	
			}
		}
	}
	
	private static final void add(String instruction,int syntaxID){
		map.put(instruction,syntaxID);
	}
}