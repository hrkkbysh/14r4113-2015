package casl2;

public interface QuasiWord{
	int getContent();
	/*this return is overflow bit
	if content doesn't fit word, return true,else is false*/
	boolean setContent(int value);
	
	default String toOctal(QuasiWord quasiWord){
		int number = quasiWord.getContent() & 0x0000FFFF;
		return Integer.toOctalString(number);
	}
	default String toHexDecimal(QuasiWord quasiWord){
		int number = quasiWord.getContent() & 0x0000FFFF;
		return Integer.toHexString(number);
	}
	default String toDecimal(QuasiWord quasiWord){
		int number = quasiWord.getContent() & 0x0000FFFF;
		return Integer.toString(number);
	}
	default String toBinary(QuasiWord quasiWord){
		int number = quasiWord.getContent() & 0x0000FFFF;
		return Integer.toBinaryString(number);
	}
}