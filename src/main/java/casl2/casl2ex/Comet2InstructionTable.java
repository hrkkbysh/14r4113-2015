package casl2.casl2ex;

import java.util.EnumMap;
import java.util.Map;
import static casl2.casl2ex.Casl2Symbol.*;

public class Comet2InstructionTable {
	private static final Map<Casl2Symbol, Comet2InstructionData> instructionMap =
			new EnumMap<>(Casl2Symbol.class);
	private int size=-1;
	private final void initInstruction
	(Casl2Symbol mnemonic,int opcode){
		Comet2InstructionData instruction = new Comet2InstructionData(mnemonic,opcode);
		instructionMap.put(mnemonic, instruction);
		size++;
	}
	public Comet2InstructionTable(){
		/*				mnemonic,group,opcode	*/
		initInstruction( NOP , 0x00);
		initInstruction( LD  , 0x10);
		initInstruction( ST  , 0x11);
		initInstruction( LAD , 0x12);
		initInstruction( ADDA, 0x20);
		initInstruction( SUBA, 0x21);
		initInstruction( ADDL, 0x22);
		initInstruction( SUBL, 0x23);
		initInstruction( AND , 0x30);
		initInstruction( OR  , 0x31);
		initInstruction( XOR , 0x32);
		initInstruction( CPA , 0x40);
		initInstruction( CPL , 0x41);
		initInstruction( SLA , 0x50);
		initInstruction( SRA , 0x51);
		initInstruction( SLL , 0x52);
		initInstruction( SRL , 0x53);
		initInstruction( JMI , 0x61);
		initInstruction( JNZ , 0x62);
		initInstruction( JZE , 0x63);
		initInstruction( JUMP, 0x64);
		initInstruction( JPL , 0x65);
		initInstruction( JOV , 0x66);
		initInstruction( PUSH, 0x70);
		initInstruction( POP , 0x71);
		initInstruction( CALL, 0x80);
		initInstruction( RET , 0x81);
		initInstruction( SVC , 0xF0);

		initInstruction(Casl2Symbol.ERROR, 0);
	}

	/*命令表のsize*/
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.MachineInstructionTable#getSize()
	 */
	public int getSize(){
		return size;
	}


	/*Mnemonic→instruction.
	 *  getOrDefaultは平均してO(1)でinstructionを見つける。
	 *  */
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.MachineInstructionTable#findFromMachineInst(hosei.wadalab.compiler.casl2.Casl2Symbol)
	 */
	public int findFromMachineInst(Casl2Symbol mnemonic){
		Comet2InstructionData instruction = instructionMap.getOrDefault(mnemonic, instructionMap.get(Casl2Symbol.ERROR));
		return instruction.getOpCode();
	}


	/*opcode→instruction
	 *
	 * getは平均してO(1)でinstructionを見つける。
	 * 比較は高々2回行われる。
	 * */
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.MachineInstructionTable#findFromOpCode(int)
	 */
	public Comet2InstructionData findFromOpCode(int candidate){
		switch ((candidate & 0xF0)>>4) {
			case 1:
				switch (candidate & 0x0F) {
					case 0:
					case 4: return instructionMap.get(LD);
					case 1: return instructionMap.get(ST);
					case 2: return instructionMap.get(LAD);
					default:
						break;
				}
			case 2:
				switch (candidate & 0x0F){
					case 0:
					case 4: return instructionMap.get(ADDA);
					case 1:
					case 5: return instructionMap.get(SUBA);
					case 2:
					case 6: return instructionMap.get(ADDL);
					case 3:
					case 7: return instructionMap.get(SUBL);
					default:
						break;
				}
			case 3:
				switch (candidate & 0x0F){
					case 0:
					case 4: return instructionMap.get(AND);
					case 1:
					case 5: return instructionMap.get(OR);
					case 2:
					case 6: return instructionMap.get(XOR);
					default:
						break;
				}
			case 4:
				switch (candidate & 0x0F){
					case 0:
					case 4: return instructionMap.get(CPA);
					case 1:
					case 5: return instructionMap.get(CPL);
				}
				break;
			case 5:
				switch (candidate & 0x0F) {
					case 0: return instructionMap.get(SLA);
					case 1: return instructionMap.get(SRA);
					case 2: return instructionMap.get(SLL);
					case 3: return instructionMap.get(SRL);
					default:
						break;
				}
			case 6:
				switch (candidate & 0x0F) {
					case 1: return instructionMap.get(JMI);
					case 2: return instructionMap.get(JNZ);
					case 3: return instructionMap.get(JZE);
					case 4: return instructionMap.get(JUMP);
					case 5: return instructionMap.get(JPL);
					case 6: return instructionMap.get(JOV);
					default:
						break;
				}
			case 7:
				switch (candidate & 0x0F) {
					case 0: return instructionMap.get(PUSH);
					case 1: return instructionMap.get(POP);
					default:
						break;
				}
			case 8:
				switch (candidate & 0x0F) {
					case 0: return instructionMap.get(CALL);
					case 1: return instructionMap.get(RET);
					default:
						break;
				}
			case 0:
				if((candidate & 0x0F) == 0) return instructionMap.get(NOP);
				break;
			case 0xF:
				if((candidate & 0x0F) == 0) return instructionMap.get(SVC);
				break;
			default:
				break;
		}
		return instructionMap.get(Casl2Symbol.ERROR);
	}

	private static final int BUF = -1;
	private static final int LEN = -1;
	private static final Map<Casl2Symbol,Integer[]> macroInstMap = new EnumMap<>(Casl2Symbol.class);
	static{
		Integer[] INBLOCK={28673,0,
				28674,0,
				4609,BUF,
				4610,LEN,
				61440,1,
				28960,28944,
		},OUTBLOCK={28673,0,
				28674,0,
				4609,BUF,
				4610,LEN,
				61440,2,
				28960,28944
		},RPOPBLOCK= {29040,
				29024,
				29008,
				28992,
				28976,
				28960,
				28944
		},RPUSHBLOCK={28673,0,
				28674,0,
				28675,0,
				28676,0,
				28677,0,
				28678,0,
				28679,0},ERROR={-1};;
		macroInstMap.put(IN, INBLOCK);
		macroInstMap.put(OUT, OUTBLOCK);
		macroInstMap.put(RPOP, RPOPBLOCK);
		macroInstMap.put(RPUSH, RPUSHBLOCK);
	}
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.MachineInstructionTable#findFromMacroInst(hosei.wadalab.compiler.casl2.Casl2Symbol)
	 */
	public Integer[] findFromMacroInst(Casl2Symbol macro){
		return macroInstMap.getOrDefault(macro, macroInstMap.get(Casl2Symbol.ERROR));
	}
}