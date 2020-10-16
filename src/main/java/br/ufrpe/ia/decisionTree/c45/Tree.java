/**
 * @author Taciano Amorim
 *
 */
package br.ufrpe.ia.decisionTree.c45;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class Tree {

	private No root;
	
	private StoppingCriteria stoppingCriteria;
	

	public Tree(File file) {
    	
	}

    public static double log(double base, double valor) {
        return Math.log(valor) / Math.log(base);
    }

}
