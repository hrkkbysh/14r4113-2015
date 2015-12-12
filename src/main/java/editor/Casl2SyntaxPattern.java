package editor;

public enum Casl2SyntaxPattern implements SyntaxPattern{
	ASSEMBLERINST(InstList.ASM_INST_LIST),
	MACROINST(InstList.MCR_INST_LIST),
	CPUINST(InstList.CPU_INST_LIST),
	REGISTER("\\b(GR[0-7])\\b"),
	INDEXREGISTER("\\b(GR[1-7])\\b"),
	LABEL("\\G([A-Z]{1}[A-Z0-9_]*)\\b"),
	DECIMAL("([0-9]+)"),
	HEXADECIMAL("(#[0-9A-F]{1,4})"),
	STRING("'([\\x21-\\x7E\\xA1-\\xDF]*)'"),
	CONSTANT("\\b("+DECIMAL.getPattern()+
				"|"+HEXADECIMAL.getPattern()+
				"|"+STRING.getPattern()+")\\b"),
	LITERAL("=("+DECIMAL.getPattern()+
				"|"+HEXADECIMAL.getPattern()+
				"|"+STRING.getPattern()+")"),
	COMMENT("\\;.*"),
	COMMA(","),
	SPACE("\\s");
	private String pattern;
	
	Casl2SyntaxPattern(String pattern){
		this.pattern = pattern;
	}
	
	public String getPattern(){return pattern;}
}
final class InstList{
	
	static{
		StringBuilder sb = new StringBuilder();
		CPU_INST_LIST = setList(sb, Comet2Instruction.class);
		sb.delete(0, sb.length());
		MCR_INST_LIST = setList(sb, MacroInstruction.class);
		sb.delete(0, sb.length());
		ASM_INST_LIST = setList(sb, Casl2Instruction.class);
		sb.delete(0, sb.length());
	}
	
	private static final<E extends Enum<E>>String setList
	(StringBuilder sb,Class<E> class1) {
		sb.append("\\b(");
		for (E m : class1.getEnumConstants()) {
			sb.append(m.toString());sb.append("|");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")\\b");
		return sb.toString();
	}
	
	
	static final String CPU_INST_LIST;
	static final String MCR_INST_LIST;
	static final String ASM_INST_LIST;
}