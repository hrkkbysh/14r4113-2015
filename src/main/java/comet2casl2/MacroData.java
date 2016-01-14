package comet2casl2;

import java.util.List;

/**
 * @author Haruki on 2015/12/12.
 */
public class MacroData {
	private final int instID;
	private final List<Integer> argIDs;
	private final int lblID;
	private List<Integer> mBlock;

	public MacroData(int lblID,int instID, List<Integer> argIDs) {
		this.lblID = lblID;
		this.instID = instID;
		this.argIDs = argIDs;
	}

	public void setMBlock(List<Integer> mBlock) {
		this.mBlock = mBlock;
	}

	public int getInstID() {
		return instID;
	}

	public List<Integer> getArgIDs() {
		return argIDs;
	}

	public int getLblID() {
		return lblID;
	}

	public List<Integer> getmBlock() {
		return mBlock;
	}
}
