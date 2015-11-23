package comet2;

import java.util.Set;

import casl2.Comet2Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class Register {
	private Comet2Word pc;
	private Comet2Word gr[],sp;
	//public EnumSet<FlagRegister> fr;
	private ObservableSet<FlagRegister> fr;
	
	Register(){
		pc = new Comet2Word();
		for(int i = 0; i<8; i++)
			gr[i] = new Comet2Word();
		sp = new Comet2Word();
		fr = FXCollections.observableSet();
	}
	public Comet2Word getPc() {
		return pc;
	}
	public void setPc(Comet2Word pc) {
		this.pc = pc;
	}
	public Comet2Word[] getGr() {
		return gr;
	}
	public void setGr(Comet2Word[] gr) {
		this.gr = gr;
	}
	public Comet2Word getSp() {
		return sp;
	}
	public void setSp(Comet2Word sp) {
		this.sp = sp;
	}
	
	public ObservableSet<FlagRegister> getFragRegister(){
		return fr;
	}
	public void setFragRegister(FlagRegister f){
		fr.add(f);
	}
	
	public void setFragRegisters(Set<FlagRegister> fr){
		fr.clear();
		fr.addAll(fr);
	}
	
	public Set<FlagRegister> getFR(){
		return fr;
	}
	
	public void incrementPc(){
		pc.setContent(pc.getContent()+1);;
	}
	public void decrementSP() {
		sp.setContent(sp.getContent()-1);;
	}
	public void incrementSP() {
		sp.setContent(sp.getContent()-1);;
	}
	public void indefine() {
		for(Comet2Word g: gr){
			g.setWordType(false);
		}
	}
}