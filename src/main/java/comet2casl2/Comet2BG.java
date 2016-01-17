package comet2casl2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Comet2BG {
	private Comet2InstructionTable insttable;
	private SymbolTable symbolTable;
	private ErrorTable errorTable;
	private AddressSpace addressSpace;
	private List<ImmediateData> imDatas;
	private AtomicInteger lc;
	private int startAdr;
	private int endAdr;
	private String programName;

	public Comet2BG(Comet2InstructionTable instTable,SymbolTable symbolTable,ErrorTable errorTable) {
		this.insttable = instTable;
		this.symbolTable = symbolTable;
		this.errorTable = errorTable;
		addressSpace = new AddressSpace(65535,0xCCCC);
		imDatas = new ArrayList<>();
		lc = new AtomicInteger(0x1000);
		init();
	}

	public Comet2BG() {
		addressSpace = MachineObserver.as;
		this.insttable = MachineObserver.instTable;
		this.symbolTable = MachineObserver.symTbl;
		this.errorTable = MachineObserver.errorTable;
		init();
	}

	public Comet2BG(MachineObserver machineObserver) {

	}

	public void init(){
		addressSpace = new AddressSpace(65535,0xCCCC);
		imDatas = new ArrayList<>();
		lc = new AtomicInteger(0x1000);
	}

	/*  */
	public void setStartAdr(int startAdr) {
		this.startAdr = startAdr;
	}
	/*  */
	public void setEndAdr() {
		this.endAdr = lc.get();
	}
	public void setProgramName(String proName) {
		this.programName = proName;
	}

	/**/
	public void genFile(Path path){
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
				for(int i = startAdr;i<startAdr+this.endAdr;i++){
					buf.append(i);buf.append(',');
					buf.append(addressSpace.readA(i).getContent());buf.append(',');
					buf.append(System.lineSeparator());
				}
				buf.append("</PROGRAM>");
				System.out.println(buf.toString());
				File file = new File(path.getParent()+"/"+programName+".obj");
				Files.write(file.toPath(), buf.toString().getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean step2() {
		for(LabelSymbol ls: symbolTable.getLblTbl()){
			int defLoc = ls.getDefineLocation();
			if(defLoc!= -1){
				for(int refLoc: ls.getRefineLocations()){
					addressSpace.write(refLoc,defLoc);//address
				}
			}else{
				String unSolvedSymbol = symbolTable.getLabel(ls.getID());
				for(int proRefLoc: ls.getProRefLocs()){
					errorTable.printError(proRefLoc,19,unSolvedSymbol);
				}
				return false;
			}
		}
		for(ImmediateData im: imDatas){
			addressSpace.write(im.getRefineLocation(),lc.getAndIncrement());//literal
			if(im.getVals().length!=1){
				lc.getAndDecrement();
				for(int word :im.getVals()) {
					addressSpace.write(lc.getAndIncrement(),word);
					//machineCodes.put(lc.getAndIncrement(), new ObjCode(word.getContent(), ObjType.CONST));
				}
			}else{
				if((im.getVals()[0] & 0xFF00)!=0){
					int id = im.getVals()[0]  >>> 16;
					LabelSymbol ls = symbolTable.searchLbltbl(id);
					addressSpace.write(lc.get(),ls.getDefineLocation());
					//machineCodes.put(lc.get(), new casl2.Comet2BG.ObjCode(ls.getDefineLocation(), ObjType.ADDRESS));
					continue;
				}
				addressSpace.write(lc.get(),im.getVals()[0]);
				//machineCodes.put(lc.get(), new ObjCode(imtoken.getImmediateValue()[0].getContent(), ObjType.CONST));
			}
		}
		if(startAdr!=0){
			LabelSymbol ls = symbolTable.searchLbltbl(startAdr);
			if(ls != null) {
				startAdr = ls.getDefineLocation();
			}else {
				errorTable.printError(0,19,symbolTable.getLabel(startAdr));
				return false;
			}
		}
		return  true;
	}

	/*  */
	public void genSingleWordCode(Casl2Symbol mnemonic, RegMember r1, RegMember r2, int mode){
		genSingleWord(insttable.findFromMachineInst(mnemonic),r1.getCode(),r2.getCode(),mode);
	}

	/*  */
	public void genRegStackCode(Casl2Symbol mnemonic, RegMember r1){
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
		addressSpace.write(lc.getAndIncrement(),code);//code
	}

	/* */
	public void genMacroBlock(Casl2Symbol  macro, int bufLabel, int lenLabel,int proLC) {
		Integer[] codeBlock = insttable.findFromMacroInst(macro);
		symbolTable.addLblRefLoc(bufLabel, lc.get() + 5, proLC);
		symbolTable.addLblRefLoc(lenLabel, lc.get() + 7, proLC);
		for (Integer code : codeBlock) {
			addressSpace.write(lc.getAndIncrement(),code);//code
		}
	}

	/* */
	public void genMacroBlock(Casl2Symbol  macro) {
		Integer[] codeBlock = insttable.findFromMacroInst(macro);
		for (Integer code : codeBlock) {
			addressSpace.write(lc.getAndIncrement(), code);//code
		}
	}

	/* */
	public void genDSArea(int dataSize) {
		for(int i = 0;i<dataSize; i++){
			addressSpace.write(lc.getAndIncrement(),-1);//area
		}
		lc.addAndGet(dataSize);
	}

	/*
	 *  */
	public boolean defineLabel(int symbolName,int proDefLoc) {
		return symbolTable.addLblDefLoc(symbolName, lc.get(),proDefLoc);
	}

	public void genAdrCode(int nval) {
		//String bin = Integer.toBinaryString(0x10000 | nval).substring(1);
		addressSpace.write(lc.getAndIncrement(),nval);//const
	}
	public void genAdrCode(String sval) {
		for(int i = 0; i<sval.length();i++) {
			addressSpace.write(lc.getAndIncrement(),sval.charAt(i));//const
		}
	}
	public void genAdrCode(int nval,int proLoc) {
		symbolTable.addLblRefLoc(nval, lc.getAndIncrement(), proLoc);
	}

	public void genImmCode(int nval) {
		imDatas.add(new ImmediateData(lc.getAndIncrement(),nval));
	}
	public void genImmCode(String sval) {
		int[] word = new int[sval.length()];
		for(int i = 0; i<sval.length();i++) {
			word[i] = sval.charAt(i);
		}
		imDatas.add(new ImmediateData(lc.getAndIncrement(),word));
	}
	public void genImmCode(int nval,int proLoc) {
		int value = nval<<16;
		imDatas.add(new ImmediateData(lc.getAndIncrement(),value));
		symbolTable.addLblRefLoc(nval, lc.get(), proLoc);
	}
	/*以下，補助コード*/
	private class ImmediateData{
		private final int refineLocation;
		private int[] immediateValue;
		int getRefineLocation() {
			return refineLocation;
		}
		int[] getVals() {
			return immediateValue;
		}
		ImmediateData(int refineLocation,int... immediateValue) {
			this.refineLocation = refineLocation;
			this.immediateValue = Arrays.copyOf(immediateValue,immediateValue.length);
		}
	}
}