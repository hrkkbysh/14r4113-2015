package comet2;

import casl2.Comet2Word;

import java.util.List;
import java.util.Stack;

class AddressSpace {
	private int start,end;
	private Comet2Word[] memory;
	private Stack<Comet2Word> stack;
	private int UPPER_BOUND = 0;
	
	public AddressSpace(Register register,int UPPER_BOUND) {
		memory = new Comet2Word[UPPER_BOUND];
		this.UPPER_BOUND = UPPER_BOUND;
		for(int i = 0 ; i<UPPER_BOUND; i++){
			memory[i] = new Comet2Word();
		}
	}
	
	void absolutionLoad(List<Comet2Word> comet2Programs){
		relocationLoad(comet2Programs, 0);
	}
	
	void relocationLoad(List<Comet2Word> comet2Programs,int base){
		for(Comet2Word i: comet2Programs){
			memory[base].setWordType(true);
			memory[base].setContent(i.getContent());
			base++;
		}
	}
	
		
	Comet2Word readA(int i)
	{return memory[i];}
	
	Comet2Word readFromWord(Comet2Word word)
	{return memory[word.getContent()];}
	
	Comet2Word readIndexMode(Comet2Word address,Comet2Word register)
	{return memory[address.getContent()+register.getContent()];}
	
	void write(int locate,Comet2Word word){memory[locate].setContent(word.getContent());}
	
	Comet2Word[] getAll(){
		return memory;
	}
	
	void clear(){
		for (int j = 0; j < memory.length; j++) {
			memory[j].setWordType(false);
		}
	}
	int getUpperBound(){
		return UPPER_BOUND;
	}
	
	//stack操作
	void push(Comet2Word data){stack.push(data);}
	Comet2Word pop(){return stack.pop();}

	public void setStartAdr(int start) {this.start = start;	}
	public void setEndAdr(int end) {this.end = end;}
	
	public int getStart() {return start;}
	public int getEnd() {return end;}
}