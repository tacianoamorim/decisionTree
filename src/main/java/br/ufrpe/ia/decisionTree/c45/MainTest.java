package br.ufrpe.ia.decisionTree.c45;

import org.apache.commons.lang3.StringUtils;

public class MainTest {

	public static void main(String[] args) {
		
		System.out.println(StringUtils.isNumeric("0"));
		System.out.println(StringUtils.isNumeric("0.0"));
		System.out.println(StringUtils.isNumeric("valor"));
		System.out.println(StringUtils.isNumeric("4545.7888"));
		

	}

}
