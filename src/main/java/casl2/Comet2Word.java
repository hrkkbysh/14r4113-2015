package casl2;

import javafx.beans.property.SimpleIntegerProperty;

public class Comet2Word implements QuasiWord {
	private SimpleIntegerProperty content;
	private boolean hasData;

	public Comet2Word(int content) {
		super();
		if(areaCheck(content)){
			this.setWordType(true);
		}
		this.content = new SimpleIntegerProperty(content);
		this.setWordType(false);
	}
	public Comet2Word() {
		this.content = new SimpleIntegerProperty(0);
		this.setWordType(false);
	}
	
	private static boolean areaCheck(int i){
		return 0<=i && i<=65535;
	}

	public final SimpleIntegerProperty contentProperty() {
		return this.content;
	}

	public final int getContent() {
		return this.contentProperty().get();
	}

	public final boolean setContent(final int content) {
		if(areaCheck(content)){
			this.contentProperty().set(content);
			hasData = true;
			return false;
		}
		setContent(content&0x00FF);
		return true;
	}
	public boolean isData() {
		return hasData;
	}
	public void setWordType(boolean hasData) {
		this.hasData = hasData;
	}
}