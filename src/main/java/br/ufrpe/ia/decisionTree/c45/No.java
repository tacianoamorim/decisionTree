/**
 * @author Taciano Amorim
 *
 */
package br.ufrpe.ia.decisionTree.c45;

public class No {
	private No right;
	private No left;
	// Test
	// isLeaf

	public No(No right, No left) {
		this.right = right;
		this.left = left;
	}

	public No getRight() {
		return right;
	}

	public void setRight(No right) {
		this.right = right;
	}

	public No getLeft() {
		return left;
	}

	public void setLeft(No left) {
		this.left = left;
	}

}
