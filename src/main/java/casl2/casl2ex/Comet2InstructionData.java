package casl2.casl2ex;

public class Comet2InstructionData{
	private final Casl2Symbol mnemonic;
	private final int opcode;

	Comet2InstructionData
	(Casl2Symbol mnemonic,int opcode){
		this.mnemonic = mnemonic;
		this.opcode	  = opcode;
	}

	public Casl2Symbol getMnemonic() { return mnemonic; }
	
	public int getOpCode() { return opcode; }
}