package casl2;

import java.util.ArrayList;
import java.util.List;

public class LabelSymbol {
	private String name;
	private int defineLocation;
	private List<Integer> refineLocation;
	
	public LabelSymbol(String name, int defineLocation, int refineLocation) {
		this.name = name;
		this.defineLocation = defineLocation;
		this.refineLocation = new ArrayList<>();
		this.refineLocation.add(refineLocation);
	}
	public LabelSymbol(String name, int defineLocation) {
		this.name = name;
		this.defineLocation = defineLocation;
		this.refineLocation = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public int getDefineLocation() {
		return defineLocation;
	}

	public List<Integer> getRefineLocation() {
		return refineLocation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDefineLocation(int defineLocation) {
		this.defineLocation = defineLocation;
	}

	public void addRefineLocation(int refineLocation) {
		this.refineLocation.add(refineLocation);
	}
	
}
