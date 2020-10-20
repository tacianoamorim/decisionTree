package br.ufrpe.ia.decisionTree.c45;

import java.util.List;

public class Attribute {
	private String name;
	private List<String> values;
	private double gain;

	public Attribute(String name, List<String> values) {
		this.name = name;
		this.values= values;
		this.gain= calcGain();
	}

	private double calcGain() {
		return this.values.size() + 1d;
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}

	public double getGain() {
		return gain;
	}


}
