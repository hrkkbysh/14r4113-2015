package controller;

public enum AssemblerMode {
	NORMALCASL2("通常モード"),EXTENSIONCASL2("拡張モード");
	
	String tooltip;
	AssemblerMode(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {
		return tooltip;
	}
}
