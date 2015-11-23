package assembler;

import casl2.Comet2Instruction;
import casl2.MacroInstruction;

public interface MachineInstructionTable {

	/*命令表のsize*/
	int getSize();

	/*Mnemonic→instruction. 
	 *  getOrDefaultは平均してO(1)でinstructionを見つける。
	 *  */
	int findFromMachineInst(Comet2Instruction mnemonic);

	/*opcode→instruction
	 * 
	 * getは平均してO(1)でinstructionを見つける。
	 * 比較は高々2回行われる。
	 * */
	InstructionData findFromOpCode(int candidate);

	Integer[] findFromMacroInst(MacroInstruction macro);

}