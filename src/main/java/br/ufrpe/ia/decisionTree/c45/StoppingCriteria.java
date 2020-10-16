/**
 * @author Taciano
 *
 * MAX_DEPTH		: Stimulates maximum depth for tree growth
 * MINIMUM_NUMBER	: Minimum number of examples on the node
 * TOTALITY			: When most examples of the node belong to the same class
 * CRITERION_GAIN	: Insignificant gain in the separation criterion
 */
package br.ufrpe.ia.decisionTree.c45;

public enum StoppingCriteria {
	MAX_DEPTH(1), MINIMUM_NUMBER(2), TOTALITY(3), CRITERION_GAIN(4);

	private final int value;

	StoppingCriteria(int value) {
		this.value = value;
	}

	public int getValor() {
		return this.value;
	}
}
