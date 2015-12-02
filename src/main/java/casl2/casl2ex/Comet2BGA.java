package casl2.casl2ex;

import assembler.QuasiWord;
import casl2.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Comet2BGA {

    private Comet2InstructionTable insttable;
    private LabelTable labelTable;
    private ErrorTable errorTable;
    private List<ImmediateData> imDatas = new ArrayList<>();
    private int startAdr;
    private int endAdr;
    private String programName;
    private String startLabel;
    private AtomicInteger lc;

    private String filepath;
    private Map<Integer,ObjCode> machinecode= new HashMap<>();


    public Comet2BGA(Comet2InstructionTable instTable,LabelTable labelTable) {
        this.insttable = instTable;
        this.labelTable = labelTable;
        init();
    }


    public void setPath(String filepath){
        this.filepath = filepath;
    }

    public void init(){
        lc = new AtomicInteger(0);
        machinecode.clear();
        errorTable = ErrorTable.getInstance();
    }

    /*  */
    public Collection<QuasiWord> getMachineCode() {return null;}

    /*  */
    public void setStartAdr(String startLabel) {
        this.startLabel = startLabel;
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
    public void genFile() {
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
                for (Map.Entry<Integer, ObjCode> code : machinecode.entrySet()) {
                    buf.append(code.getKey());
                    buf.append(',');
                    buf.append(code.getValue().getCode().getContent());
                    buf.append(',');
                    buf.append(code.getValue().getType().toString());
                    buf.append(System.lineSeparator());
                }
			/*for (Map.Entry<Integer, ObjCode> code : machinecode.entrySet()) {
				String value = Integer.toHexString(code.getKey());
				buf.append(value);
				buf.append(',');
				value = Integer.toHexString(code.getValue().getCode().getContent());
				buf.append(value);
				buf.append(',');
				buf.append(code.getValue().getType().toString());
				buf.append(System.lineSeparator());
			}*/
                buf.append("</PROGRAM>");

                System.out.println(buf.toString());

				/*FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Save File.");
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("Casl2 File", "*.CASL2", "*.cas", "*.txt"),
						new FileChooser.ExtensionFilter("All File", "*.*"));
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				File file = fileChooser.showSaveDialog(null);
				if (file == null) return;
				Files.write(file.toPath(),buf.toString().getBytes(Charset.forName("SHIFT_JIS")));*/
                File file = new File(Paths.get(filepath).getParent()+"/"+programName+".obj");
                Files.write(file.toPath(), buf.toString().getBytes(Charset.forName("SHIFT_JIS")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            errorTable.getErrorMessages().forEach(System.out::println);
        }
    }

    private boolean step2() {
        for(LabelSymbol ls: labelTable.getLabelTable()){
            int defLoc = ls.getDefineLocation();
            if(defLoc!= -1){
                for(int refLoc: ls.getRefineLocations()){
                    machinecode.put(refLoc,new ObjCode(new Comet2Word(defLoc),ObjType.CODE));
                }
            }else{
                for(int proRefLoc: ls.getProRefLocs()){
                    errorTable.writeError(proRefLoc,1,ls.getName());//,"未解決のラベル");
                }
                return false;
            }
        }
        for(ImmediateData imtoken: imDatas){
            machinecode.put(imtoken.getRefineLocation(),new ObjCode(new Comet2Word(lc.getAndIncrement()),ObjType.DATA));
            genAdrCode(imtoken.getImmediateValue(),imtoken.getProRefLoc());
        }
        if(startLabel!=null){
            for(LabelSymbol ls : labelTable.getLabelTable()){
                if(ls.getName().equals(startLabel)){
                    startAdr = ls.getDefineLocation();
                    return true;
                }
            }
            return false;
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
        machinecode.put(lc.getAndIncrement(),new ObjCode(new Comet2Word( code ), ObjType.CODE ) );
    }

    /*  */
    public void genAdrCode(Casl2Token op,int proLC){
        switch(op.getSymbol()){
            case NUM_CONST:
                machinecode.put(lc.getAndIncrement(),new ObjCode(new Comet2Word(Integer.parseInt(op.getToken())),ObjType.DATA));
                break;
            case STR_CONST:
                try {
                    byte[] sjis = op.getToken().getBytes("UTF-8");
                    for (byte sji : sjis) {
                        machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(sji & 0x00FF), ObjType.DATA));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case LABEL:
                labelTable.addRef(op.getToken(), lc.getAndIncrement(),proLC);
            default:
                break;
        }
    }

/*
	private void getLabelAddress(String symbol){
		machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(),ObjType.ADR));
	}*/

    /* */
    public void genMacroBlock(Casl2Symbol  macro, String bufLabel, String lenLabel,int proLC) {
        Integer[] codeBlock = insttable.findFromMacroInst(macro);
        labelTable.addRef(bufLabel,lc.get()+5,proLC);
        labelTable.addRef(lenLabel,lc.get()+7,proLC);
        for (Integer aCodeBlock : codeBlock) {
            machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(aCodeBlock), ObjType.CODE));
        }
    }

    /* */
    public void genMacroBlock(Casl2Symbol  macro) {
        Integer[] codeBlock = insttable.findFromMacroInst(macro);
        for (Integer aCodeBlock : codeBlock) {
            machinecode.put(lc.getAndIncrement(), new ObjCode(new Comet2Word(aCodeBlock), ObjType.CODE));
        }
    }

    /* */
    public void genDSArea(int dataSize) {
        for(int i = 0;i<dataSize; i++){
            machinecode.put(lc.getAndIncrement(),new ObjCode(new Comet2Word(-1),ObjType.DATA));
        }
        lc.addAndGet(dataSize);
    }

    /*
     *  */
    public boolean defineLabel(String symbolName) {
        return labelTable.addDef(symbolName, lc.get());
    }

    public void genAdrCode(int nval) {
    }
    public void genAdrCode(String sval) {
    }
    public void genAdrCode(String sval,int proLoc) {
    }

    public void genImmCode(int nval) {
    }
    public void genImmCode(String sval) {
    }
    public void genImmCode(String sval,int proLoc) {
    }
    /*以下、補助コード*/
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

    public enum ObjType {DATA,CODE}
    public enum AddressingMode {
        REGISTER(4),INDEX(0);
        private int code;
        AddressingMode(int code) {
            this.code = code;
        }
        public int getCode(){return code;}
    }
    private void addLit(int proRefLoc,int lc, Casl2Token op) {
        imDatas.add(new ImmediateData(proRefLoc,lc, op));
    }
    private class ImmediateData{
        private final int refineLocation;
        private final int proRefLoc;
        private final Casl2Token immediateValue;
        int getProRefLoc(){return proRefLoc;}
        int getRefineLocation() {
            return refineLocation;
        }
        Casl2Token getImmediateValue() {
            return immediateValue;
        }
        ImmediateData(int proRefLoc,int refineLocation, Casl2Token immediateValue) {
            this.proRefLoc = proRefLoc;
            this.refineLocation = refineLocation;
            this.immediateValue = immediateValue;
        }
    }
}