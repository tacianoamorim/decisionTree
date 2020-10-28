/**
 * @author Taciano Amorim
 */
package br.ufrpe.ia.decisionTree.c45;

import java.util.List;

public class No {

	private int depth;
	private Attribute attributeRoot;
	private List<Attribute> attributes;
	private List<No> nos;
	private boolean leaf;
	private boolean visited;

	public List<No> getNos() {
		return nos;
	}
	public void setNos(List<No> nos) {
		this.nos = nos;
	}

	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public Attribute getAttributeRoot() {
		return attributeRoot;
	}
	public void setAttributeRoot(Attribute attributeRoot) {
		this.attributeRoot = attributeRoot;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
}
