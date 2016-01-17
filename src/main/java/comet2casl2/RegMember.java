package comet2casl2;

/**
 * @author 14r4113 on 2016/01/16.
 */
public enum RegMember {
	GR0(0),GR1(1),GR2(2),GR3(3),GR4(4),GR5(5),GR6(6),GR7(7),
	PR(8),SP(9);
	private int code;
	RegMember(int code) {
		this.code = code;
	}
	public int getCode(){return code;}
}