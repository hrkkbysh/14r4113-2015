package casl2.casl2ex;

import java.util.ArrayList;
import java.util.List;

public class LabelSymbol {
	private int id;
	private int defineLocation;
	private List<Integer> refineLocations;
	private List<Integer> proRefLocs;
	
	public LabelSymbol(int id, int defineLocation, int refineLocations) {
		this.id = id;
		this.defineLocation = defineLocation;
		this.refineLocations = new ArrayList<>();
		this.proRefLocs = new ArrayList<>();
		this.refineLocations.add(refineLocations);
	}
	public LabelSymbol(int id, int defineLocation) {
		this.id = id;
		this.defineLocation = defineLocation;
		this.refineLocations = new ArrayList<>();
		this.proRefLocs = new ArrayList<>();
	}

	public int getID() {
		return id;
	}

	public int getDefineLocation() {
		return defineLocation;
	}

	public List<Integer> getRefineLocations() {
		return refineLocations;
	}
	public List<Integer> getProRefLocs() {
		return proRefLocs;
	}

	public void setDefineLocation(int defineLocation) {
		this.defineLocation = defineLocation;
	}

	public void addRefineLocations(int refineLocation,int proRefLoc) {
		this.proRefLocs.add(proRefLoc);
		this.refineLocations.add(refineLocation);
	}
	
}
