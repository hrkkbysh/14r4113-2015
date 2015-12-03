package casl2.casl2ex;

public enum Comet2Register {
	GR0(0),GR1(1),GR2(2),GR3(3),GR4(4),GR5(5),GR6(6),GR7(7);
	
	private int code;
	Comet2Register(int code) {
		this.code = code;
	}
	public int getCode(){return code;}
}
