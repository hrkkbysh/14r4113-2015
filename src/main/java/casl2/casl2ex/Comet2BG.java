package casl2.casl2ex;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Comet2BG {
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

    public Comet2BG(Comet2InstructionTable instTable, LabelTable labelTable) {
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

                buf.append("</PROGRAM>");

                System.out.println(buf.toString());

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
                    machinecode.put(refLoc,new ObjCode(defLoc,ObjType.CODE));
                }
            }else{
                for(int proRefLoc: ls.getProRefLocs()){
                    errorTable.writeError(proRefLoc,1,ls.getName());//,"�������̃��x��");
                }
                return false;
            }
        }
        for(ImmediateData imtoken: imDatas){
            machinecode.put(imtoken.getRefineLocation(),new ObjCode(lc.getAndIncrement(),ObjType.ADDRESS));
            
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
        machinecode.put(lc.getAndIncrement(),new ObjCode( code , ObjType.CODE ) );
    }

    /* */
    public void genMacroBlock(Casl2Symbol  macro, String bufLabel, String lenLabel,int proLC) {
        Integer[] codeBlock = insttable.findFromMacroInst(macro);
        labelTable.addRef(bufLabel,lc.get()+5,proLC);
        labelTable.addRef(lenLabel,lc.get()+7,proLC);
        for (Integer aCodeBlock : codeBlock) {
            machinecode.put(lc.getAndIncrement(), new ObjCode(aCodeBlock, ObjType.CODE));
        }
    }

    /* */
    public void genMacroBlock(Casl2Symbol  macro) {
        Integer[] codeBlock = insttable.findFromMacroInst(macro);
        for (Integer aCodeBlock : codeBlock) {
            machinecode.put(lc.getAndIncrement(), new ObjCode(aCodeBlock, ObjType.CODE));
        }
    }

    /* */
    public void genDSArea(int dataSize) {
        for(int i = 0;i<dataSize; i++){
            machinecode.put(lc.getAndIncrement(),new ObjCode(-1,ObjType.DATA));
        }
        lc.addAndGet(dataSize);
    }

    /*
     *  */
    public boolean defineLabel(String symbolName) {
        return labelTable.addDef(symbolName, lc.get());
    }

    public void genAdrCode(int nval) {
        machinecode.put(lc.getAndIncrement(),new ObjCode(nval,ObjType.DATA));
    }
    public void genAdrCode(String sval) {
        for(int i = 0; i<sval.length();i++) {
            machinecode.put(lc.getAndIncrement(), new ObjCode(sval.charAt(i), ObjType.DATA));
        }
    }
    public void genAdrCode(String sval,int proLoc) {
        labelTable.addRef(sval,lc.getAndIncrement(),proLoc);
    }

    public void genImmCode(int nval) {
        Comet2Word word = new Comet2Word(nval);
        imDatas.add(new ImmediateData(lc.getAndIncrement(),word));
    }
    public void genImmCode(String sval) {
        for(int i = 0; i<sval.length();i++) {
            imDatas.add(new ImmediateData(lc.getAndIncrement(),new Comet2Word(sval.charAt(i))));
        }
    }
    public void genImmCode(String sval,int proLoc) {
        for(int i = 0; i<sval.length();i++) {
            imDatas.add(new ImmediateData(lc.getAndIncrement(),new Comet2Word(sval.charAt(i))));
        }
    }
    /*以下，補助コード*/
    public class ObjCode {
        Comet2Word code;
        ObjType type;
        public ObjCode(int value, ObjType type) {
            this.code = new Comet2Word(value);
            this.type = type;
        }
        public Comet2Word getCode() {
            return code;
        }
        public ObjType getType() {
            return type;
        }

    }

    public enum ObjType {DATA,CODE,ADDRESS}
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
        private final Comet2Word[] immediateValue;
        int getRefineLocation() {
            return refineLocation;
        }
        Comet2Word[] getImmediateValue() {
            return immediateValue;
        }
        ImmediateData(int refineLocation, Comet2Word... immediateValue) {
            this.refineLocation = refineLocation;
            this.immediateValue = Arrays.copyOf(immediateValue,immediateValue.length);
        }
    }

}