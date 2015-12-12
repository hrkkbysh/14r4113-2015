package casl2;

/**
 * @author Haruki on 2015/12/12.
 */
public enum AsmMode {
	EXTEND("拡張モード"),NORMAL("通常モード");

	String tooltip;
	AsmMode(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getTooltip() {
		return tooltip;
	}
}
