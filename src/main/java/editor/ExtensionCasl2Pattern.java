package editor;

public enum ExtensionCasl2Pattern implements SyntaxPattern{
	PRSP("\\b(PR|SP)\\b"),
	DEFMACRO("(\\$.+)")
	;
	private String pattern;
	
	ExtensionCasl2Pattern(String pattern){
		this.pattern = pattern;
	}
	
	public String getPattern(){return pattern;}
}