/**
 * @author Taciano Amorim
 *
 */
package br.ufrpe.ia.decisionTree.c45;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tree {

	private No root;
	private StoppingCriteria stoppingCriteria;
	private String[][] dataMatrix;
	private int classIndex;
	private List<Attribute> attributes;
	private double classEntropy;

	public static void main(String[] args) {
		new Tree("dataset/acute_mixed.dst",7);
	}
	
	public Tree(String pathFile, int classIndex) {
		this.classIndex= classIndex;
		getDataMatrix(pathFile);
		
		this.attributes= new ArrayList<Attribute>();
	}
	
	private void getDataMatrix(String pathFile) {
		try {
			/* **************************************************
			 *  Read file
			 * **************************************************/
			BufferedReader buffRead = new BufferedReader(new FileReader(pathFile));
			String firstLine = "";
			int countLine= 0;
			String columns[] = null;
			boolean isFirstLine= true;
			while (true) {
				firstLine = buffRead.readLine();
				if (firstLine != null) {
					if ( isFirstLine ) {
						columns= firstLine.split(",");
						isFirstLine= false;
					}
					countLine++;
					//System.out.println(firstLine);
				} else
					break;
			}
			buffRead.close();
			
			int numberLines		= countLine;
			int numberColumns	= columns.length; 
			
			this.dataMatrix= new String[numberLines][numberColumns];
			
			buffRead = new BufferedReader(new FileReader(pathFile));
			int idx= 0;
			while (true) {
				firstLine = buffRead.readLine();
				if (firstLine != null) {
					this.dataMatrix[idx++]= firstLine.split(",");
				} else
					break;
			}
			buffRead.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void calcClassEntropy() {
		List<String> list= new ArrayList<String>();
		for (int i = 0; i < dataMatrix.length; i++) {
			String[] data = dataMatrix[i];
			list.add( data[this.classIndex] );
		}
		
		int countClass= list.size();
	}

	private void loadAttributes() {
		for (int i = 0; i < dataMatrix.length; i++) {
			String[] line = dataMatrix[i];
			
			
		}
	}
	
	
	

	
    public static double log(double base, double valor) {
        return Math.log(valor) / Math.log(base);
    }
    
	public static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}	    

}
