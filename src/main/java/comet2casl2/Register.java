package comet2casl2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.Set;

public class Register {
	private Comet2Word[] register;
	private ObservableSet<FlagRegister> fr;
	
	Register(){
		for(RegMember r:RegMember.values())
			register[r.getCode()] = new Comet2Word();
		fr = FXCollections.observableSet();
	}

	public Comet2Word getRegister(RegMember reg){
		return register[reg.getCode()];
	}
	public Comet2Word getRegister(int reg){
		return register[reg];
	}
	public Comet2Word getPc() {return register[RegMember.PR.getCode()];}
	public void setPc(int pc) {register[RegMember.PR.getCode()].setContent(pc);}

	public Comet2Word getSp() {return register[RegMember.SP.getCode()];}
	public void setSp(int sp) {register[RegMember.SP.getCode()].setContent(sp);}
	
	public ObservableSet<FlagRegister> getFragRegister(){
		return fr;
	}
	public void setFlag(FlagRegister f){
		fr.add(f);
	}
	public void setFR(Set<FlagRegister> f){
		fr.clear();
		fr.addAll(f);
	}
	public void removeFR(FlagRegister f){fr.remove(f);}
	
	public Set<FlagRegister> getFR(){
		return fr;
	}
	
	public void incrementPc(){
		int v = register[RegMember.PR.getCode()].getContent()+1;
		register[RegMember.PR.getCode()].setContent(v);
	}
	public void decrementSP() {
		int v = register[RegMember.SP.getCode()].getContent()-1;
		register[RegMember.SP.getCode()].setContent(v);
	}
	public void incrementSP() {
		int v = register[RegMember.SP.getCode()].getContent()+1;
		register[RegMember.SP.getCode()].setContent(v);
	}
	public void reset() {
		for(Comet2Word g: register){
			g.setWordType(false);
		}
	}
}