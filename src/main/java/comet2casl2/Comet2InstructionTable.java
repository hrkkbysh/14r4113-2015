package comet2casl2;

import java.util.EnumMap;
import java.util.Map;

import static comet2casl2.Casl2Symbol.*;

public class Comet2InstructionTable {
	private static final Map<Casl2Symbol, Integer> instructionMap =
			new EnumMap<>(Casl2Symbol.class);
	private int size=-1;
	private  void initInstruction
	(Casl2Symbol mnemonic,int opcode){
		instructionMap.put(mnemonic, opcode);
		size++;
	}
	public Comet2InstructionTable(){
		/*				mnemonic,opcode	*/
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

		initInstruction(ERROR, 0);


		Integer[] IN_BLOCK={28673,0,
				28674,0,
				4609,BUF,
				4610,LEN,
				61440,1,
				28960,28944,
		},OUT_BLOCK={28673,0,
				28674,0,
				4609,BUF,
				4610,LEN,
				61440,2,
				28960,28944
		},RPOP_BLOCK= {29040,
				29024,
				29008,
				28992,
				28976,
				28960,
				28944
		},RPUSH_BLOCK={28673,0,
				28674,0,
				28675,0,
				28676,0,
				28677,0,
				28678,0,
				28679,0},
		ERROR_BLOCK={0};
		macroInstMap.put(IN, IN_BLOCK);
		macroInstMap.put(OUT, OUT_BLOCK);
		macroInstMap.put(RPOP, RPOP_BLOCK);
		macroInstMap.put(RPUSH, RPUSH_BLOCK);
		macroInstMap.put(ERROR, ERROR_BLOCK);

	}

	/*命令表のsize*/
	public int getSize(){
		return size;
	}


	/*Mnemonic→instruction.
	 *  getOrDefaultは平均してO(1)でinstructionを見つける。
	 *  */
	public int findFromMachineInst(Casl2Symbol mnemonic){
		return instructionMap.getOrDefault(mnemonic, -1);
	}


	/*opcode→instruction
	 *
	 * getは平均してO(1)でinstructionを見つける。
	 * 比較は高々2回行われる。
	 * */
	public Casl2Symbol findFromOpCode(int candidate){
		switch ((candidate & 0xF0)>>4) {
			case 1:
				switch (candidate & 0x0F) {
					case 0: word = 2;
					case 4: return LD;
					case 1: return ST;
					case 2: return LAD;
					default:
						break;
				}
			case 2:
				switch (candidate & 0x0F){
					case 0:
					case 4: return ADDA;
					case 1:
					case 5: return SUBA;
					case 2:
					case 6: return ADDL;
					case 3:
					case 7: return SUBL;
					default:
						break;
				}
			case 3:
				switch (candidate & 0x0F){
					case 0:
					case 4: return AND;
					case 1:
					case 5: return OR;
					case 2:
					case 6: return XOR;
					default:
						break;
				}
			case 4:
				switch (candidate & 0x0F){
					case 0:
					case 4: return CPA;
					case 1:
					case 5: return CPL;
				}
				break;
			case 5:
				switch (candidate & 0x0F) {
					case 0: return SLA;
					case 1: return SRA;
					case 2: return SLL;
					case 3: return SRL;
					default:
						break;
				}
			case 6:
				switch (candidate & 0x0F) {
					case 1: return JMI;
					case 2: return JNZ;
					case 3: return JZE;
					case 4: return JUMP;
					case 5: return JPL;
					case 6: return JOV;
					default:
						break;
				}
			case 7:
				switch (candidate & 0x0F) {
					case 0: return PUSH;
					case 1: return POP;
					default:
						break;
				}
			case 8:
				switch (candidate & 0x0F) {
					case 0: return CALL;
					case 1: return RET;
					default:
						break;
				}
			case 0:
				if((candidate & 0x0F) == 0) return NOP;
				break;
			case 0xF:
				if((candidate & 0x0F) == 0) return SVC;
				break;
			default:
				break;
		}
		return ERROR;
	}

	public int word(){ return word;}
	private int word = -1;

	private static final int BUF = -1;
	private static final int LEN = -1;
	private static final Map<Casl2Symbol,Integer[]> macroInstMap =
			new EnumMap<>(Casl2Symbol.class);
	public Integer[] findFromMacroInst(Casl2Symbol macro){
		return macroInstMap.getOrDefault(macro, macroInstMap.get(ERROR));
	}
}