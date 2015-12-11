package casl2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Comet2BG {
	private Comet2InstructionTable insttable;
	private SymbolTable symbolTable;
	private ErrorTable errorTable;
	private List<ImmediateData> imDatas = new ArrayList<>();
	private int startAdr = 0;
	private int endAdr;
	private String programName;
	private AtomicInteger lc;
	private Map<Integer,ObjCode> machineCodes = new HashMap<>();

	public Comet2BG(Comet2InstructionTable instTable,SymbolTable symbolTable,ErrorTable errorTable) {
		this.insttable = instTable;
		this.symbolTable = symbolTable;
		this.errorTable = errorTable;
		init();
	}
	public void init(){
		lc = new AtomicInteger(0);
		machineCodes.clear();
		imDatas.clear();
	}

	/*  */
	public void setStartAdr(int startAdr) {
		this.startAdr = startAdr;
	}
	public void setStartAdr() {
		this.startAdr = lc.get();
	}
	/*  */
	public void setEndAdr() {
		this.endAdr = lc.get();
	}
	public void setProgramName(String proName) {
		this.programName = proName;
	}

	/**/
	public void genFile(String filepath){
		if(step2()&&(!errorTable.hasError())) {
			try{
				StringBuilder buf = new StringBuilder();
				buf.append("<PROGRAM>");
				buf.append(System.lineSeparator());
				buf.append("START,");
				buf.append(this.startAdr);
				buf.append(System.lineSeparator());
				buf.append("SIZE");
				buf.append(this.endAdr);
				buf.append(System.lineSeparator());
				for (Map.Entry<Integer, ObjCode> code : machineCodes.entrySet()) {
					buf.append(code.getKey());
					buf.append(',');
					buf.append(code.getValue().getCode().getContent());
					buf.append(',');
					buf.append(code.getValue().getType().toString());
					buf.append(System.lineSeparator());
				}

				buf.append("</PROGRAM>");

				System.out.println(buf.toString());
				File file = new File(Paths.get(filepath).getParent()+"/"+programName+".obj");
				Files.write(file.toPath(), buf.toString().getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			errorTable.getErrorMessages().forEach(System.out::println);
		}
		if(errorTable.hasWarning()) {
			errorTable.getWarningMessages().forEach(System.out::println);
		}
	}
	public void genFile() {
		genFile("");
	}

	private boolean step2() {
		for(LabelSymbol ls: symbolTable.getLblTbl()){
			int defLoc = ls.getDefineLocation();
			if(defLoc!= -1){
				for(int refLoc: ls.getRefineLocations()){
					machineCodes.put(refLoc, new ObjCode(defLoc, ObjType.ADDRESS));
				}
			}else{
				String unSolvedSymbol = symbolTable.getLabel(ls.getID());
				for(int proRefLoc: ls.getProRefLocs()){
					errorTable.writeError(proRefLoc,19,unSolvedSymbol);
				}
				return false;
			}
		}
		for(ImmediateData imtoken: imDatas){
			machineCodes.put(imtoken.getRefineLocation(), new ObjCode(lc.getAndIncrement(), ObjType.CODE));
			if(imtoken.getImmediateValue().length!=1){
				lc.getAndDecrement();
				for(Comet2Word word :imtoken.getImmediateValue()) {
					machineCodes.put(lc.getAndIncrement(), new ObjCode(word.getContent(), ObjType.CONST));
				}
			}else{
				if(imtoken.getImmediateValue()[0].isData()){
					int id = imtoken.getImmediateValue()[0].getContent()  >>> 16;
					LabelSymbol ls = symbolTable.searchLbltbl(id);
					machineCodes.put(lc.get(), new ObjCode(ls.getDefineLocation(), ObjType.ADDRESS));
					continue;
				}
				machineCodes.put(lc.get(), new ObjCode(imtoken.getImmediateValue()[0].getContent(), ObjType.CONST));
			}
		}
		if(startAdr!=0){
			LabelSymbol ls = symbolTable.searchLbltbl(startAdr);
			if(ls != null) {
				startAdr = ls.getDefineLocation();
			}else {
				errorTable.writeError(0,19,symbolTable.getLabel(startAdr));
				return false;
			}
		}
		return  true;
	}

	/*  */
	public void genSingleWordCode(Casl2Symbol mnemonic, Comet2Register r1, Comet2Register r2, AddressingMode mode){
		genSingleWord(insttable.findFromMachineInst(mnemonic),r1.getCode(),r2.getCode(),mode.getCode());
	}

	/*  */
	public void genRegStackCode(Casl2Symbol mnemonic, Comet2Register r1){
		genSingleWord(insttable.findFromMachineInst(mnemonic), r1.getCode(), 0 , 0);
	}

	/*  */
	public void genNoOpCode(Casl2Symbol mnemonic){
		genSingleWord(insttable.findFromMachineInst(mnemonic), 0 , 0 , 0);
	}

	private void genSingleWord(int code,int r1,int r2,int mode){
		code += mode; code <<= 4;
		code += r1;   code <<= 4;
		code += r2;
		machineCodes.put(lc.getAndIncrement(), new ObjCode(code, ObjType.CODE));
	}

	/* */
	public void genMacroBlock(Casl2Symbol  macro, int bufLabel, int lenLabel,int proLC) {
		Integer[] codeBlock = insttable.findFromMacroInst(macro);
		symbolTable.addLblRefLoc(bufLabel, lc.get() + 5, proLC);
		symbolTable.addLblRefLoc(lenLabel, lc.get() + 7, proLC);
		for (Integer aCodeBlock : codeBlock) {
			machineCodes.put(lc.getAndIncrement(), new ObjCode(aCodeBlock, ObjType.CODE));
		}
	}

	/* */
	public void genMacroBlock(Casl2Symbol  macro) {
		Integer[] codeBlock = insttable.findFromMacroInst(macro);
		for (Integer aCodeBlock : codeBlock) {
			machineCodes.put(lc.getAndIncrement(), new ObjCode(aCodeBlock, ObjType.CODE));
		}
	}

	/* */
	public void genDSArea(int dataSize) {
		for(int i = 0;i<dataSize; i++){
			machineCodes.put(lc.getAndIncrement(), new ObjCode(ObjType.CONST));
		}
		lc.addAndGet(dataSize);
	}

	/*
	 *  */
	public boolean defineLabel(int symbolName,int proDefLoc) {
		return symbolTable.addLblDefLoc(symbolName, lc.get(),proDefLoc);
	}

	public void genAdrCode(int nval) {
		machineCodes.put(lc.getAndIncrement(), new ObjCode(nval, ObjType.CONST));
	}
	public void genAdrCode(String sval) {
		for(int i = 0; i<sval.length();i++) {
			machineCodes.put(lc.getAndIncrement(), new ObjCode(sval.charAt(i), ObjType.CONST));
		}
	}
	public void genAdrCode(int nval,int proLoc) {
		symbolTable.addLblRefLoc(nval, lc.getAndIncrement(), proLoc);
	}

	public void genImmCode(int nval) {
		Comet2Word word = new Comet2Word(nval);
		imDatas.add(new ImmediateData(lc.getAndIncrement(),word));
	}
	public void genImmCode(String sval) {
		Comet2Word[] word = new Comet2Word[sval.length()];
		for(int i = 0; i<sval.length();i++) {
			word[i] = new Comet2Word(sval.charAt(i));
		}
		imDatas.add(new ImmediateData(lc.getAndIncrement(),word));
	}
	public void genImmCode(int nval,int proLoc) {
		Comet2Word word = new Comet2Word(nval<<16);
		imDatas.add(new ImmediateData(lc.getAndIncrement(),word));
		symbolTable.addLblRefLoc(nval, lc.get(), proLoc);
	}
	/*以下，補助コード*/
	public class ObjCode {
		Comet2Word code;
		ObjType type;
		public ObjCode(int value, ObjType type) {
			this.code = new Comet2Word(value);
			this.type = type;
		}
		public ObjCode(ObjType type){
			this.code = new Comet2Word();
			this.type = type;
		}
		public Comet2Word getCode() {
			return code;
		}
		public ObjType getType() {
			return type;
		}

	}

	public enum ObjType {CONST,CODE,ADDRESS}
	public enum AddressingMode {
		REGISTER(4),INDEX(0);
		private int code;
		AddressingMode(int code) {
			this.code = code;
		}
		public int getCode(){return code;}
	}
	private class ImmediateData{
		private final int refineLocation;
		private Comet2Word[] immediateValue;
		int getRefineLocation() {
			return refineLocation;
		}
		Comet2Word[] getImmediateValue() {
			return immediateValue;
		}
		ImmediateData(int refineLocation,Comet2Word... immediateValue) {
			this.refineLocation = refineLocation;
			this.immediateValue = Arrays.copyOf(immediateValue,immediateValue.length);
		}
	}
}