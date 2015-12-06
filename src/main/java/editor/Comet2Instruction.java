package editor;

public enum Comet2Instruction{
	ADDA(5),
	ADDL(5),
	AND(5),
	CALL(2),
	CPA(5),
	CPL(5),
	JMI(2),
	JNZ(2),
	JOV(2),
	JPL(2),
	JUMP(2),
	JZE(2),
	LAD(3),
	LD(5),
	NOP(0),
	OR(5),
	POP(1),
	PUSH(2),
	RET(0),
	SLA(3),
	SLL(3),
	SRA(3),
	SRL(3),
	ST(3),
	SUBA(5),
	SUBL(5),
	SVC(2),
	XOR(5),
	INVALID_COI(-1);
	
	private int syntaxType;
	Comet2Instruction(int syntaxType) {
		this.syntaxType =syntaxType;
	}
	public int getSyntaxType(){return syntaxType;}
}