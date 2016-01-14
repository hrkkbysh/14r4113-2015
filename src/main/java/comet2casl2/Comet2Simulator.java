package comet2casl2;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Comet2Simulator{
	private static final int maskR1 = 0x00F0;
	private static final int maskR2 = 0x000F;
	private static final int maskInst = 0xFF00;
	private Comet2Word o1;
	private Comet2Word o2;
	private AddressSpace memory;
	private Register register;
	private Comet2InstructionTable instTbl;
	private ExecState execState;
	public enum DataType {ADDRESS,DATA,FUNCTION_NAME}

	public Comet2Simulator(AddressSpace memory, Register register){
		this.memory = memory;
		this.register = register;
	}
//	public void loadFromBG(BinaryGenerator bg){
//		bg.getMachineCode();
//		bg.getCodeMemorySize();
//		bg.getMainFunction();
//		Comet2 comet2 = new Comet2(bg);
//		DisAssembler disAssembler = new DisAssembler();
//				cps.setDisasm(new DisAssembler());
//		        interp.code = assembler.getMachineCode();
//		        interp.codeSize = assembler.getCodeMemorySize();
//		        interp.constPool = assembler.getConstantPool();
//		        interp.mainFunction = assembler.getMainFunction();
//		        interp.globals = new Object[assembler.getDataSize()];
//		        interp.disasm = new DisAssembler(interp.code,
//		                                         interp.codeSize,
//		                                         interp.constPool);
//		        hasErrors = assembler.getNumberOfSyntaxErrors()>0;
//	}

	public boolean start(Path path,int base){
		try {
			register.setPc(loadFromFile(path,base));
			return register.getPc().isData();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

	//return start address
	public int loadFromFile(Path path,int base) throws IOException, URISyntaxException{
		BufferedReader br = Files.newBufferedReader(path,Charset.forName("UTF-8"));
		int startAdr = -1;
		int programSize=-1;
		if(br.readLine().equals("<PROGRAM>")){
			String s = br.readLine().split(",")[1];
			String p = br.readLine().split(",")[1];
			startAdr = Integer.parseInt(s)+base;
			programSize = Integer.parseInt(p);
		}
		int i = 0;
		for(String code = br.readLine(); code != null ;code = br.readLine()){
			if(br.readLine().equals("</PROGRAM>")){
				break;
			}
			String[] token = code.split(",");
			switch (DataType.valueOf(token[2])) {
				case DATA:
					memory.write(base+i,Integer.parseInt(token[1]));
					i++;
					break;
				case ADDRESS:
					memory.write(base+i,Integer.parseInt(token[1])+base);
					i++;
					//subProgram.add(new Comet2Word(Integer.parseInt(token[1])+base));
					break;
				case FUNCTION_NAME://if exception is occurred,throw URISyntaxException
					int start = loadFromFile(Paths.get(new URI(token[1])),base+programSize);
					if(i != -1) {
						memory.write(base + i, start);
						i++;
					}else{ br.close();return -1;}
					break;
			}
		}
		br.close();
		return startAdr;
	}

	public ExecState singleStepExec() {
		//instruction fetch
		Comet2Word PR = register.getPc();
		Comet2Word IR = memory.readA(PR.getContent());
		register.incrementPc();

		//instruction decode,//calculate and fetch operand address
		int instructionCode = IR.getContent() & maskInst >>> 8;
		int r1Code = IR.getContent() & maskR1 >>> 4;
		int r2Code = IR.getContent() & maskR2;
		int opperandSize =-1;

		Casl2Symbol instruction = instTbl.findFromOpCode(instructionCode);
		Integer opcode = instTbl.findFromMachineInst(instruction);
		switch(instruction){
		case LD:
		case ADDA:
		case ADDL:
		case SUBA:
		case SUBL:
		case AND:
		case OR:
		case XOR:
		case CPA:
		case CPL:
			opperandSize = 2;
			o1=register.getRegister(r1Code);
			if(instructionCode == opcode){
				//2 words
				register.incrementPc();
				int adr = memory.readFromWord(register.getPc()).getContent();
				o2 = memory.readIndexMode(adr, register.getRegister(r1Code));
			}else{
				//single word
				o2 = register.getRegister(r2Code);
			}
			break;
		case ST:
		case LAD:
		case SLA:
		case SLL:
		case SRA:
		case SRL:
			opperandSize = 2;
			if(instructionCode == opcode){
				//2 words
				o1=register.getRegister(r1Code);
				register.incrementPc();
				o2 = memory.readIndexMode(memory.readFromWord(register.getPc()).getContent(), register.getRegister(r2Code));
			}
			break;
		case JMI:
		case JNZ:
		case JOV:
		case JPL:
		case JUMP:
		case JZE:
		case PUSH:
		case CALL:
		case SVC:
			opperandSize = 1;
			if(instructionCode == opcode){
				//2 words
				o1=register.getRegister(r1Code);
				register.incrementPc();
				o2 = memory.readIndexMode(memory.readFromWord(register.getPc()).getContent(), register.getRegister(r2Code));
			}
			break;

		case POP:
			opperandSize = 1;
			o1=register.getRegister(r1Code);
			break;
		case NOP:
		case RET:
			opperandSize = 0;
			break;

		default:
			break;
		}
		int pc=0;
		if(opperandSize!=-1){
			/*execute*/
			switch(instruction){
			case LD:
				o1 = Executor.ldComet2Word(o1, o2);
				break;
			case ADDA:
				o1 = Executor.addaComet2Word(o1, o2);
				break;
			case ADDL:
				o1 = Executor.addlComet2Word(o1, o2);
				break;
			case SUBA:
				o1 = Executor.subaComet2Word(o1, o2);
				break;
			case SUBL:
				o1 = Executor.sublComet2Word(o1, o2);
				break;
			case AND:
				o1 = Executor.andComet2Word(o1, o2);
				break;
			case OR:
				o1 = Executor.orComet2Word(o1, o2);
				break;
			case XOR:
				o1 = Executor.xorComet2Word(o1, o2);
				break;
			case CPA:
				register.setFR(Executor.cpaComet2Word(o1, o2, register.getFR()));
				break;
			case CPL:
				register.setFR(Executor.cplComet2Word(o1, o2, register.getFR()));
				break;

			case ST:
				o2 = Executor.stComet2Word(o1, o2);
				memory.write(memory.readA(o2.getContent()).getContent(), o2.getContent());
				break;

			case LAD:
				o1 = Executor.ladComet2Word(o1, o2.getContent());
				break;
			case SLA:
				o1 = Executor.slaComet2Word(o1, o2);
				break;
			case SLL:
				o1 = Executor.sllComet2Word(o1, o2);
				break;
			case SRA:
				o1 = Executor.sraComet2Word(o1, o2);
				break;
			case SRL:
				o1 = Executor.srlcomet2Word(o1, o2);
				break;
			case JMI:
				 pc = Executor.jmiComet2Word(register.getPc(), o2, register.getFR()).getContent();
				register.setPc(pc);
				break;
			case JNZ:
				 pc = Executor.jmiComet2Word(register.getPc(), o2, register.getFR()).getContent();
				register.setPc(pc);
				break;
			case JZE:
				 pc = Executor.jmiComet2Word(register.getPc(), o2, register.getFR()).getContent();
				register.setPc(pc);
				break;
			case JUMP:
				 pc = Executor.jmiComet2Word(register.getPc(), o2, register.getFR()).getContent();
				register.setPc(pc);
				break;
			case JOV:
				 pc = Executor.jmiComet2Word(register.getPc(), o2, register.getFR()).getContent();
				register.setPc(pc);
				break;
			case JPL:
				 pc = Executor.jmiComet2Word(register.getPc(), o2, register.getFR()).getContent();
				register.setPc(pc);
				break;
			case PUSH:
				register.decrementSP();
				memory.write(register.getSp().getContent(), o2.getContent());
				break;
			case CALL:
				register.decrementSP();
				memory.write(register.getSp().getContent(), register.getPc().getContent());
				register.setPc(o2.getContent());
			case SVC:
				register.decrementSP();
				/*IN,OUT..etc を定義*/
				//GR,FRを不定にする
				register.reset();
				break;
			case POP:
				o1.setContent(register.getSp().getContent());
				register.incrementSP();
				break;
			case NOP:
			case RET:
				Comet2Word cw = memory.readFromWord(register.getSp());
				register.setPc(cw.getContent());
				register.incrementSP();
			default:
				break;
			}
		}else{
			//error occured
		}
		setState(ExecState.DURING_EXEC);
		return ExecState.DURING_EXEC;
		/*result store*/
	}
	//	private static final Random random = new Random();
	//    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
	//        int x = random.nextInt(clazz.getEnumConstants().length);
	//        return clazz.getEnumConstants()[x];
	//    }

	public ExecState getState() {return execState;}
	public void setState(ExecState execState) {this.execState = execState;}
}