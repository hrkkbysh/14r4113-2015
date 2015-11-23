package casl2;


import assembler.InstructionData;

public class Comet2InstructionData implements InstructionData {
	private final Comet2Instruction mnemonic;
	private final int opcode;

	Comet2InstructionData
	(Comet2Instruction mnemonic,int opcode){
		this.mnemonic = mnemonic;
		this.opcode	  = opcode;
	}

	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.InstructionData#getMnemonic()
	 */
	@Override
	public Comet2Instruction getMnemonic() { return mnemonic; }

	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.InstructionData#getOpCode()
	 */
	@Override
	public int getOpCode() { return opcode; }
}