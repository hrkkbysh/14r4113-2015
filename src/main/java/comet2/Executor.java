package comet2;

import static comet2.FlagRegister.*;
import casl2.Comet2Word;

import java.util.Set;


public class Executor {
	static final int LOGICAL_MASK = 0x00FF;
	static final int ARITHMETIC_MASK = 0x007F;
	static final int SIGN_MASK = 0x8000;//1...negative,0...positive
	private Executor(){}
	
	
	public static Comet2Word nopComet2Word(){
		return null;
	}
	/*load,store,and load address instruction*/
	public static Comet2Word ldComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent(o2.getContent());
		return o1;
	}
	public static Comet2Word stComet2Word(Comet2Word o1,Comet2Word o2){
		o2.setContent(o1.getContent());
		return o2;
	}
	public static Comet2Word ladComet2Word(Comet2Word o1,int o2){
		o1.setContent(o2);
		return o1;
	}
	
	/*Arithmetic and Logical instruction*/
	public static Comet2Word addaComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & ARITHMETIC_MASK) + (o2.getContent() & ARITHMETIC_MASK));
		return o1;
	}
	public static Comet2Word addlComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) + (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	public static Comet2Word subaComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & ARITHMETIC_MASK) - (o2.getContent() & ARITHMETIC_MASK));
		return o1;
	}
	public static Comet2Word sublComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) - (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	public static Comet2Word andComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) & (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	public static Comet2Word orComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) | (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	public static Comet2Word xorComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) ^ (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	
	/*Compare instruction*/
	public static Set<FlagRegister> cpaComet2Word(Comet2Word o1,Comet2Word o2,Set<FlagRegister> FR){
		int value = (o1.getContent() & ARITHMETIC_MASK) - (o2.getContent() & ARITHMETIC_MASK);
		return computeFR(value,FR);
	}
	public static Set<FlagRegister> cplComet2Word(Comet2Word o1,Comet2Word o2,Set<FlagRegister> FR){
		int value = (o1.getContent() & LOGICAL_MASK) - (o2.getContent() & LOGICAL_MASK);
		return computeFR(value,FR);
	}
	
	/*Shift instruction*/
	public static Comet2Word slaComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & ARITHMETIC_MASK) << (o2.getContent() & ARITHMETIC_MASK));
		return o1;
	}
	public static Comet2Word sllComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) << (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	public static Comet2Word sraComet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & ARITHMETIC_MASK) >> (o2.getContent() & ARITHMETIC_MASK));
		return o1;
	}
	public static Comet2Word srlcomet2Word(Comet2Word o1,Comet2Word o2){
		o1.setContent((o1.getContent() & LOGICAL_MASK) >> (o2.getContent() & LOGICAL_MASK));
		return o1;
	}
	
	/*branch instruction (return program counter)*/
	public static Comet2Word jmiComet2Word(Comet2Word pr,Comet2Word adr,Set<FlagRegister> FR){
		if(FR.contains(SF)) pr.setContent(adr.getContent());
		return pr;
	}
	public static Comet2Word jnzComet2Word(Comet2Word pr,Comet2Word adr,Set<FlagRegister> FR){
		if(!FR.contains(ZF)) pr.setContent(adr.getContent());
		return pr;
	}
	public static Comet2Word jzeComet2Word(Comet2Word pr,Comet2Word adr,Set<FlagRegister> FR){
		if(FR.contains(ZF)) pr.setContent(adr.getContent());
		return pr;
	}
	public static Comet2Word jumpComet2Word(Comet2Word pr,Comet2Word adr){
		pr.setContent(pr.getContent());
		return pr;
	}
	public static Comet2Word jplComet2Word(Comet2Word pr,Comet2Word adr, Set<FlagRegister> FR){
		if((!FR.contains(SF))&&(!FR.contains(ZF))) pr.setContent(adr.getContent());
		return pr;
	}
	public static Comet2Word jovComet2Word(Comet2Word pr,Comet2Word adr, Set<FlagRegister> FR){
		if(FR.contains(0F)) pr.setContent(adr.getContent());
		return pr;
	}
	
	
	public static void pushComet2Word(Comet2Word o1,Comet2Word sp,Comet2Word sploc){
		sploc.setContent(sploc.getContent()-1);
		sp.setContent(o1.getContent());
	}
	public static Comet2Word popComet2Word(Comet2Word o1,Comet2Word sp,Comet2Word sploc){
		o1.setContent(sp.getContent());
		sploc.setContent(sploc.getContent()+1);
		return null;
	}
	public static Comet2Word callComet2Word(Comet2Word o1,Comet2Word o2){
		return null;
	}
	public static Comet2Word retComet2Word(Comet2Word o1,Comet2Word o2){
		return null;
	}
	public static Comet2Word svcComet2Word(Comet2Word o1,Comet2Word o2){
		return null;
	}
	public static Comet2Word illegalComet2Word(Comet2Word o1,Comet2Word o2){
		return null;
	}
	
	private static Set<FlagRegister> computeFR(Comet2Word word,Set<FlagRegister> FR){
		FR.clear();
		int value = word.getContent();
		if((value&SIGN_MASK)>0) FR.add(SF);
		if((value&LOGICAL_MASK)>0) FR.add(OF);
		if(value==0) FR.add(ZF);
		return FR;
	}
	private static Set<FlagRegister> computeFR(int value,Set<FlagRegister> FR){
		FR.clear();
		if((value&SIGN_MASK)>0) FR.add(SF);
		if((value&LOGICAL_MASK)>0) FR.add(OF);
		if(value==0) FR.add(ZF);
		return FR;
	}
}
