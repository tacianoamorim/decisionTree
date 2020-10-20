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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {

	private No root;
	private StoppingCriteria stoppingCriteria;

	private String[][] dataMatrix;
	private int classIndex;
	private double classEntropy;

	private List<Attribute> attributes;

	public static void main(String[] args) {
		new Tree("dataset/acute_mixed.dst", 7);
	}

	public Tree(String pathFile, int classIndex) {
		this.classIndex = classIndex;

		getDataMatrix(pathFile);
		calcClassEntropy();

		this.attributes = new ArrayList<Attribute>();
	}

	private void getDataMatrix(String pathFile) {
		try {
			/*
			 * ************************************************** Read file
			 **************************************************/
			BufferedReader buffRead = new BufferedReader(new FileReader(pathFile));
			String firstLine = "";
			int countLine = 0;
			String columns[] = null;
			boolean isFirstLine = true;
			while (true) {
				firstLine = buffRead.readLine();
				if (firstLine != null) {
					if (isFirstLine) {
						columns = firstLine.split(",");
						isFirstLine = false;
					}
					countLine++;
					// System.out.println(firstLine);
				} else
					break;
			}
			buffRead.close();

			int numberLines = countLine;
			int numberColumns = columns.length;

			this.dataMatrix = new String[numberLines][numberColumns];

			System.out.println("***************************************************"); 
			System.out.println("Data Matrix: ("+ pathFile +")");
			System.out.println("***************************************************"); 
			
			buffRead = new BufferedReader(new FileReader(pathFile));
			int idx = 0;
			while (true) {
				firstLine = buffRead.readLine();
				if (firstLine != null) {
					this.dataMatrix[idx++] = firstLine.split(",");
				} else
					break;
			}
			buffRead.close();

			printArray(this.dataMatrix);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printArray(String[][] strings) {
		for (int i = 0; i < this.dataMatrix.length; i++) {
			System.out.print(" ");
			for (int j = 0; j < this.dataMatrix[i].length; j++) {
				System.out.print( this.dataMatrix[i][j]+ " " );
			}
			System.out.println();
			if ( i > 10 ) {
				System.out.println(" . . . ");
				break;
			}
		}
	}

	private void calcClassEntropy() {
		Map<String, Integer> mapElements = new HashMap<String, Integer>();
		for (int i = 0; i < dataMatrix.length; i++) {
			String[] data = dataMatrix[i];
			if (mapElements.containsKey(data[this.classIndex])) {
				Integer count = mapElements.get(data[this.classIndex]);
				mapElements.put(data[this.classIndex], ++count);
			} else {
				mapElements.put(data[this.classIndex], 1);
			}
		}
		
		for (String key : mapElements.keySet()) {
			
		}
	}

	private void loadAttributes() {
		for (int i = 0; i < dataMatrix.length; i++) {
			String[] line = dataMatrix[i];

		}
	}

	public static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}

}
