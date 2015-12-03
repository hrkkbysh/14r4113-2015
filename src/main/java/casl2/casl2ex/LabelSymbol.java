package casl2.casl2ex;

import java.util.ArrayList;
import java.util.List;

public class LabelSymbol {
	private String name;
	private int defineLocation;
	private List<Integer> refineLocations;
	private List<Integer> proRefLocs;
	
	public LabelSymbol(String name, int defineLocation, int refineLocations) {
		this.name = name;
		this.defineLocation = defineLocation;
		this.refineLocations = new ArrayList<>();
		this.proRefLocs = new ArrayList<>();
		this.refineLocations.add(refineLocations);
	}
	public LabelSymbol(String name, int defineLocation) {
		this.name = name;
		this.defineLocation = defineLocation;
		this.refineLocations = new ArrayList<>();
		this.proRefLocs = new ArrayList<>();
	}

	public String getName() {
		return name;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setDefineLocation(int defineLocation) {
		this.defineLocation = defineLocation;
	}

	public void addRefineLocations(int refineLocation,int proRefLoc) {
		this.proRefLocs.add(proRefLoc);
		this.refineLocations.add(refineLocation);
	}
	
}
