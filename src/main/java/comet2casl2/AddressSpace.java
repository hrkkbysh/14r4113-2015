package comet2casl2;


import java.util.List;

class AddressSpace {
	private Comet2Word[] memory;
	private int sbot = 0xCCCC;
	private int UPPER_BOUND = 65535;
	
	public AddressSpace(int UPPER_BOUND,int sbot) {
		memory = new Comet2Word[UPPER_BOUND];
		this.UPPER_BOUND = UPPER_BOUND;
		this.sbot = sbot;
		for(int i = 0 ; i<UPPER_BOUND; i++){
			memory[i] = new Comet2Word();
		}
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
	
	Comet2Word readIndexMode(int pr,Comet2Word register)
	{return memory[pr+register.getContent()];}
	
	void write(int locate,int content){memory[locate].setContent(content);}
	
	Comet2Word[] getAll(){
		return memory;
	}
	
	void clear(){
		for (Comet2Word aMemory : memory) {
			aMemory.setWordType(false);
		}
	}
	int getUpperBound(){
		return UPPER_BOUND;
	}
	
	//stack操作
	void push(int sp,int data){
		memory[sp].setContent(data);
	}
	Comet2Word pop(int sp) {
		if (sp < sbot) {
			return memory[sp];
		} else {
			return new Comet2Word();
		}
	}
}