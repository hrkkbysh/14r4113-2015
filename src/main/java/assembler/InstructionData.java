package assembler;

import casl2.Comet2Instruction;

public interface InstructionData {

	Comet2Instruction getMnemonic();

	int getOpCode();

}