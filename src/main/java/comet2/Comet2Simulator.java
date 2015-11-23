package comet2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;


import assembler.InstructionData;
import assembler.MachineInstructionTable;
import casl2.Comet2Instruction;
import casl2.Comet2Word;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.SetChangeListener.Change;

public class Comet2Simulator{
	private static final int maskR1 = 0x00F0;
	private static final int maskR2 = 0x000F;
	private static final int maskInst = 0xFF00;
	private Comet2Word PR,IR,o1,o2;
	private AddressSpace memory;
	private Register register;
	private MachineInstructionTable instructionTable;
	private ExecState execState;
	public enum DataType {ADDRESS,DATA,FUNCTION_NAME}

	public Comet2Simulator(AddressSpace memory,Register register){
		this.memory = memory;
		this.register = register;
	}

	public Comet2Simulator(){
		register = new Register();
		memory = new AddressSpace(register, 65535);
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

	public void loadMachineCode(Path path,int base){
		Stack<MachineCode<Comet2Word>> codeStack = new Stack<>();
		try {
			loadFromFile(path, base,codeStack);
			MachineCode<Comet2Word> mc =codeStack.pop();
			memory.setStartAdr(mc.startAdr);
			memory.setEndAdr(mc.endAdr);
			register.setPc(new Comet2Word(mc.startAdr));
			memory.relocationLoad(mc.code, base);
			base += mc.size;
			for (MachineCode<Comet2Word> subCode : codeStack) {
				memory.relocationLoad(subCode.code, base);
				base += subCode.size;
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	//return start address
	private int loadFromFile(Path path,int base,Stack<MachineCode<Comet2Word>> codeStack) throws IOException, URISyntaxException{
		List<String> input = Files.readAllLines(path, Charset.forName("Shift-JIS"));
		List<Comet2Word> subProgram = new ArrayList<>();
		for(int position = 0,inputsize = input.size();position<inputsize;position++){
			if(input.get(position).equals("<PROGRAM>")){
				String startAdrString = input.get(position++).split(",")[1];
				String endAdrString = input.get(position++).split(",")[1];
				String programSizeString = input.get(position++).split(",")[1];
				int startAdr = Integer.parseInt(startAdrString)+base;
				int endAdr = Integer.parseInt(endAdrString)+base;
				int programSize = Integer.parseInt(programSizeString);

				for(; !input.get(position).equals("</PROGRAM>");position++){
					String[] code = input.get(position).split(",");
					switch (DataType.valueOf(code[2])) {
					case DATA:
						subProgram.add(new Comet2Word(Integer.parseInt(code[1])));
						break;
					case ADDRESS:
						subProgram.add(new Comet2Word(Integer.parseInt(code[1])+base));
						break;
					case FUNCTION_NAME://if exception is occurred,throw URISyntaxException
						int start = loadFromFile(Paths.get(new URI(code[1])),base+programSize,codeStack);
						subProgram.add(new Comet2Word(start));
						break;
					default:
						break;
					}
				}
				codeStack.push(new MachineCode<>(startAdr, endAdr, programSize, subProgram));
				return startAdr;
			}
		}
		return -1;
	}
	
	

	public void singleStepExec(ExecState execState){
		singleStepExec();
	}
	
	public ExecState singleStepExec() {
		//instruction fetch
		if(register.getPc().getContent()==memory.getEnd()){
			return ExecState.FINISH;
		}
		PR = register.getPc();
		IR = memory.readA(PR.getContent());
		register.incrementPc();

		//instruction decode,//calculate and fetch operand address
		int instructionCode = IR.getContent() & maskInst;
		int r1Code = IR.getContent() & maskR1;
		int r2Code = IR.getContent() & maskR2;
		int opperandSize =-1;
		
		InstructionData instruction = instructionTable.findFromOpCode(instructionCode);
		switch(instruction.getMnemonic()){
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
			o1=register.getGr()[r1Code];
			if(instructionCode == instruction.getOpCode()){
				//2 words
				register.incrementPc();
				o2 = memory.readIndexMode(memory.readA(register.getPc().getContent()), register.getGr()[r2Code]);
			}else{
				//single word
				o2 = register.getGr()[r2Code];
			}
			break;
		case ST:
		case LAD:
		case SLA:
		case SLL:
		case SRA:
		case SRL:
			opperandSize = 2;
			if(instructionCode == instruction.getOpCode()){
				//2 words
				o1=register.getGr()[r1Code];
				register.incrementPc();
				o2 = memory.readIndexMode(memory.readA(register.getPc().getContent()), register.getGr()[r2Code]);
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
			if(instructionCode == instruction.getOpCode()){
				//2 words
				o1=register.getGr()[r1Code];
				register.incrementPc();
				o2 = memory.readIndexMode(memory.readA(register.getPc().getContent()), register.getGr()[r2Code]);
			}
			break;

		case POP:
			opperandSize = 1;
			o1=register.getGr()[r1Code];
			break;
		case NOP:
		case RET:
			opperandSize = 0;
			break;
		
		default:
			break;
		}

		if(opperandSize!=-1){
			/*execute*/
			switch(instruction.getMnemonic()){
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
				register.setFragRegisters(Executor.cpaComet2Word(o1, o2, register.getFR()));
				break;
			case CPL:
				register.setFragRegisters(Executor.cplComet2Word(o1, o2, register.getFR()));
				break;

			case ST:
				o2 = Executor.stComet2Word(o1, o2);
				memory.write(memory.readA(o2.getContent()).getContent(), o2);
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
				register.setPc(Executor.jmiComet2Word(register.getPc(),o2, register.getFR()));
				break;
			case JNZ:
				register.setPc(Executor.jnzComet2Word(register.getPc(),o2, register.getFR()));
				break;
			case JZE:
				register.setPc(Executor.jzeComet2Word(register.getPc(),o2, register.getFR()));
				break;
			case JUMP:
				register.setPc(Executor.jumpComet2Word(register.getPc(),o2));
				break;
			case JOV:
				register.setPc(Executor.jovComet2Word(register.getPc(),o2, register.getFR()));
				break;
			case JPL:
				register.setPc(Executor.jplComet2Word(register.getPc(),o2, register.getFR()));
				break;
			case PUSH:
				register.decrementSP();

				memory.write(register.getSp().getContent(), o2);
				break;
			case CALL:
				register.decrementSP();
				memory.write(register.getSp().getContent(), register.getPc());
				register.setPc(o2);
			case SVC:
				register.decrementSP();
				/*IN,OUT..etc を定義*/
				//GR,FRを不定にする
				register.indefine();
				break;
			case POP:
				o1.setContent(register.getSp().getContent());
				register.incrementSP();
				break;
			case NOP:
			case RET:
				Comet2Word cw = memory.readFromWord(register.getSp());
				register.setPc(cw);
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
	
	//for debugger
	public Comet2Word getPR(){return register.getPc();}
	public Comet2Word getSP(){return register.getSp();}
	public Comet2Word[] getGR(){return register.getGr();}
	public Set<FlagRegister> getFR(){return register.getFR();}
	public Comet2Word[] getMemory(){return memory.getAll();}
	public void bindPR(SimpleIntegerProperty pr){register.getPc().contentProperty().bind(pr);}
	public void bindSP(SimpleIntegerProperty sp){register.getSp().contentProperty().bind(sp);}
	public void bindGR(SimpleIntegerProperty... gr){
		for(int i=0;i<=7;i++){
			register.getGr()[i].contentProperty().bind(gr[i]);
		}
	}
	public void bindFR(SimpleIntegerProperty of,SimpleIntegerProperty sf,SimpleIntegerProperty zf){
		register.getFragRegister().addListener(
				(Change<? extends FlagRegister> change)-> {
					if(register.getFragRegister().contains(FlagRegister.OF)){of.set(1);}
					else{of.set(0);}
					
					if(register.getFragRegister().contains(FlagRegister.SF)){sf.set(1);}
					else{sf.set(0);}
					
					if(register.getFragRegister().contains(FlagRegister.ZF)){zf.set(1);}
					else{zf.set(0);}
				
			});
	}
	public void bindMemory(SimpleIntegerProperty[] memory){
		for(int i=0,upperBound = this.memory.getUpperBound();i<upperBound; i++){
			this.memory.getAll()[i].contentProperty().bind(memory[i]);
		}
	}
	//helper class
		private class MachineCode<E> {
			public int startAdr;
			public int endAdr;
			public int size;
			public List<E> code;

			MachineCode(int startAdr, int endAdr, int size, List<E> code) {
				this.startAdr = startAdr;
				this.endAdr = endAdr;
				this.size = size;
				this.code = code;
			}
		}
}