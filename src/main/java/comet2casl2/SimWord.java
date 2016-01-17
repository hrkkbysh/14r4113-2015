package comet2casl2;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author 14r4113 on 2016/01/16.
 */
public class SimWord {

	private Comet2Word cw;
	private SimpleIntegerProperty prolc;

	public SimWord(int prolc){
		cw = new Comet2Word();
		this.prolc = new SimpleIntegerProperty(prolc);
	}
	public SimWord(int prolc,int content){
		cw = new Comet2Word(content);
		this.prolc = new SimpleIntegerProperty(prolc);
	}
	public int getProlc() {
		return prolc.get();
	}

	public SimpleIntegerProperty prolcProperty() {
		return prolc;
	}

	public void setProlc(int prolc) {
		this.prolc.set(prolc);
	}

	public Comet2Word getCw() {
		return cw;
	}

	public void setCw(Comet2Word cw) {
		this.cw = cw;
	}
}
