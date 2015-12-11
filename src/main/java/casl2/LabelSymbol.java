package casl2;

import java.util.ArrayList;
import java.util.List;

public class LabelSymbol {
	private int id;
	private int defineLocation;
	private int proDefLoc;
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

	public int getProDefLoc() {
		return proDefLoc;
	}

	public List<Integer> getRefineLocations() {
		return refineLocations;
	}
	public List<Integer> getProRefLocs() {
		return proRefLocs;
	}

	public void setDefineLocation(int defineLocation,int proDefLoc) {
		this.defineLocation = defineLocation;
		this.proDefLoc = proDefLoc;
	}

	public void addRefineLocations(int refineLocation,int proRefLoc) {
		this.proRefLocs.add(proRefLoc);
		this.refineLocations.add(refineLocation);
	}
	
}
