package editor;

public enum ExtensionCasl2Pattern implements SyntaxPattern{
	PRSP("\\b(PR|SP)\\b"),
	MACRO("\\b(MACRO|MEND)\\b"),
	MACROARG("\\G(\\$[a-zA-Z0-9_]+)\\b"),
	;
	private String pattern;
	
	ExtensionCasl2Pattern(String pattern){
		this.pattern = pattern;
	}
	
	public String getPattern(){return pattern;}
}