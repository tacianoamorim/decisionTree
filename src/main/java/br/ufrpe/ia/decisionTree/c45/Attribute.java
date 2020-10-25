package br.ufrpe.ia.decisionTree.c45;

import java.util.List;

public class Attribute {
	private String name;
	private List<String> values;
	private double gain;
	private boolean isClass;
	private int idx;

	public Attribute() {
	}
	
	public Attribute(String name, boolean isClass) {
		this.isClass= isClass;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}

	public boolean isClass() {
		return isClass;
	}

	public double getGain() {
		return gain;
	}
	public void setGain(double gain) {
		this.gain = gain;
	}

	public void setValues(List<String> values) {
		this.values= values;
	}

	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}

}
