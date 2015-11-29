package assembler;

import casl2.*;

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
	void genAdrCode(Casl2Token adr,int proLC);

	/*  */
	void genImmediateCode(Casl2Token adr,int proLC);

	/* */
	void genMacroBlock(MacroInstruction macro, String bufLabel, String lenLabel,int proLC);

	/* */
	void genMacroBlock(MacroInstruction macro);

	/* */
	void genDSArea(int dataSize);

	/*
         *  */
	boolean defineLabel(String symbolName);

	void setProgramName(String proName);
}
