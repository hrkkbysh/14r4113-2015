package assembler;

import casl2.Comet2BG;
import casl2.Comet2Instruction;
import casl2.Comet2Register;
import casl2.MacroInstruction;

import java.util.Collection;

/**
 * Created by Haruki on 2015/11/23.
 */
public interface BinaryGenerator {
	void setPath(String filepath);

	/*  */
	Collection<QuasiWord> getMachineCode();

	/*  */
	void setStartAdr(String startLabel);

	void setStartAdr();

	/*  */
	void setEndAdr();

	/**/
	void genFile();

	/*  */
	void genSingleWordCode(Comet2Instruction mnemonic, Comet2Register r1, Comet2Register r2, Comet2BG.AddressingMode mode);

	/*  */
	void genRegStackCode(Comet2Instruction mnemonic, Comet2Register r1);

	/*  */
	void genNoOpCode(Comet2Instruction mnemonic);

	/*  */
	void genAdrCode(Token adr, Comet2BG.Immediate mode);

	/* */
	void genMacroBlock(MacroInstruction macro, String bufLabel, String lenLabel);

	/* */
	void genMacroBlock(MacroInstruction macro);

	/* */
	void genDSArea(int dataSize);

	/*
         *  */
	void defineLabel(String symbolName);

	void setProgramName(String proName);
}
