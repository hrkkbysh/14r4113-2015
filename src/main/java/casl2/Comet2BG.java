package casl2;

import assembler.BinaryGenerator;
import assembler.MachineInstructionTable;
import assembler.QuasiWord;
import assembler.Token;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;



public class Comet2BG implements BinaryGenerator {

	AtomicInteger lc;
	MachineInstructionTable insttable;
	LabelTable labelTable;
	private int startAdr;
	private int endAdr;
	private String filepath;
	private Map<Integer,ObjCode> machinecode= new HashMap<>();

	public Comet2BG(MachineInstructionTable instTable,LabelTable labelTable) {
		this.insttable = instTable;
		this.labelTable = labelTable;
		init();
	}

	@Override
	public void setPath(String filepath){
		this.filepath = filepath;
	}

	public void init(){
		lc = new AtomicInteger(0);
		machinecode.clear();
	}

	/*  */
	@Override
	public Collection<QuasiWord> getMachineCode() {return null;}

	/*  */
	@Override
	public void setStartAdr(String startLabel) {
		this.startLabel = startLabel;
	}
	@Override
	public void setStartAdr() {
		this.startAdr = lc.get();
	}

	/*  */
	@Override
	public void setEndAdr() {
		this.endAdr = lc.get();
	}

	/**/
	@Override
	public void genFile() {
		if(step2()) {
			try {
				StringBuilder buf = new StringBuilder();
				buf.append("<PROGRAM>");
				buf.append(System.lineSeparator());
				buf.append("START,");
				buf.append(this.startAdr);
				buf.append(System.lineSeparator());
				buf.append("SIZE");
				buf.append(this.endAdr);
				buf.append(System.lineSeparator());
				for (Map.Entry<Integer,ObjCode> code : machinecode.entrySet()) {
					buf.append(code.getKey());
					buf.append(',');
					buf.append(code.getValue().getCode().getContent());
					buf.append(',');
					buf.append(code.getValue().getType().toString());
					buf.append(System.lineSeparator());
				}
				buf.append("</PROGRAM>");
				Files.write(Paths.get(filepath, programName, ".comet2"), buf.toString().getBytes(Charset.forName("SHIFT_JIS")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean step2() {
		for(LabelSymbol ls: labelTable.getLabelTable()){
			int defLoc = ls.getDefineLocation();
			if(defLoc!= -1){
				for(int refLoc: ls.getRefineLocation()){
					machinecode.put(refLoc,new ObjCode(new Comet2Word(defLoc),ObjType.CODE));
				}
			}else{
				return false;
			}
		}
		return  true;
	}


	/*  */
	@Override
	public void genSingleWordCode(Comet2Instruction mnemonic, Comet2Register r1, Comet2Register r2, AddressingMode mode){
		genSingleWord(insttable.findFromMachineInst(mnemonic),r1.getCode(),r2.getCode(),mode.getCode());
	}

	/*  */
	@Override
	public void genRegStackCode(Comet2Instruction mnemonic, Comet2Register r1){
		genSingleWord(insttable.findFromMachineInst(mnemonic), r1.getCode(), 0 , 0);
	}

	/*  */
	@Override
	public void genNoOpCode(Comet2Instruction mnemonic){
		genSingleWord(insttable.findFromMachineInst(mnemonic), 0 , 0 , 0);
	}

	private void genSingleWord(int code,int r1,int r2,int mode){
		code += mode; code <<= 8;
		code += r1;   code <<= 4;
		code += r2;
		machinecode.put(lc.getAndIncrement(),new ObjCode(new Comet2Word( code ), ObjType.CODE ) );
	}

	/*  */
	@Override
	public void genAdrCode(Token adr, Immediate mode){
		switch (mode) {
			case YES : genOpCode(adr); break;
			case NO  : genImmediateCode(adr); break;
		}
	}

	private void genImmediateCode(Token adr) {
		labelTable.addLit(lc.getAndIncrement(),adr);
	}

	private void genOpCode(Token op){
		switch(op.getSymbol()){
		case NUM_CONST:
			machinecode.put(lc.getAndIncrement(),new ObjCode(new Comet2Word(Integer.parseInt(op.getToken())),ObjType.DATA));
		case STR_CONST:
			try {
				byte[] sjis = op.getToken().getBytes("Shift_JIS");
				for (byte sji : sjis) {
					machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(sji & 0x00FF), ObjType.DATA));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			}
		case LABEL:
			labelTable.addRef(op.getToken(), lc.getAndIncrement());
		default:
		break;
		}
	}

/*
	private void getLabelAddress(String symbol){
		machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(),ObjType.ADR));
	}*/

	/* */
	@Override
	public void genMacroBlock(MacroInstruction macro, String bufLabel, String lenLabel) {
		Integer[] codeBlock = insttable.findFromMacroInst(macro);
		labelTable.addRef(bufLabel,lc.get()+5);
		labelTable.addRef(lenLabel,lc.get()+7);
		for (Integer aCodeBlock : codeBlock) {
			machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(aCodeBlock), ObjType.CODE));
		}
	}

	/* */
	@Override
	public void genMacroBlock(MacroInstruction macro) {
		Integer[] codeBlock = insttable.findFromMacroInst(macro);
		for (Integer aCodeBlock : codeBlock) {
			machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(aCodeBlock), ObjType.CODE));
		}
	}

	/* */
	@Override
	public void genDSArea(int dataSize) {
		lc.addAndGet(dataSize);
	}

	/*
	 *  */
	@Override
	public void defineLabel(String symbolName) {
		if(labelTable.addDef(symbolName, lc.get())){

		}
		else{

		}
	}

	public enum Immediate{YES,NO}

	private String programName;
	private String startLabel;

	public class ObjCode {
		QuasiWord code;
		ObjType type;
		public ObjCode(QuasiWord code, ObjType type) {
			this.code = code;
			this.type = type;
		}
		public QuasiWord getCode() {
			return code;
		}
		public ObjType getType() {
			return type;
		}

	}

	@Override
	public void setProgramName(String proName) {
		this.programName = proName;
	}


	public enum ObjType {DATA,CODE}
	public enum AddressingMode {
		REGISTER(4),INDEX(0);
		private int code;
		AddressingMode(int code) {
			this.code = code;
		}
		public int getCode(){return code;}
	}
}