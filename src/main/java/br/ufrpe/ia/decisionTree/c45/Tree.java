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
import java.util.Random;

public class Tree {

	private No root;
	
	private String[][] dataMatrix;
	private int classIndex;
	private int maxDepthint;
	private int minSamplesLeaf;
	private float testSize;
	private float classEntropy;

	private List<Attribute> attributes;

	public static void main(String[] args) {
		new Tree("dataset/acute_mixed.dst", 7, 0, 0, 0.5);
	}

	/**
	 * 
	 * @param pathFile 		- File path 
	 * @param classIndex 	- Class index
	 * @param minSamplesLeaf- Minimum number of examples on the node. It will be enabled if != 0.
	 * @param maxDepthint 	- Stimulates maximum depth for tree growth. It will be enabled if != 0.
	 * @param testSize 		- Uses the unformed percentage of the data set to build the validation set.
	 */
	public Tree(String pathFile, int classIndex, int minSamplesLeaf, int maxDepthint, double testSize) {
		this.classIndex 	= classIndex;
		this.minSamplesLeaf	= minSamplesLeaf;
		this.maxDepthint	= maxDepthint;
		this.testSize		= (float) testSize;
		
		System.out.println("=> Config tree: pathFile="+ pathFile+", classIndex="+classIndex+
							", minSamplesLeaf="+minSamplesLeaf+", maxDepthint="+ maxDepthint+", testSize="+ testSize);

		prepareDataMatrix(pathFile);
		
		// TODO: Tratar os numeros continuos
		calcDadosContinuos();
		
		// TODO: Calcular o ganho da colecao
		calcCollectionEntropy();
		
		// TODO: Calcular o ganho de todos os atributos
		calcAttributeEntropy();
		
		// TODO: Definir a raiz

		this.attributes = new ArrayList<Attribute>();
	}

	private void prepareDataMatrix(String pathFile) {
		try {
			/*
			 * ************************************************** 
			 * Read file
			 * *************************************************/
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

			System.out.println("Total de linhas do arquivo: " +countLine);
			int numberColumns = columns.length;

			// Carregar a matrix
			String[][] dataMatrixTemp= new String[countLine][numberColumns];
			loadDataMatrix(pathFile, dataMatrixTemp);
			printArray(dataMatrixTemp);
			
			// Embaralha todos os registros
			shuffle(dataMatrixTemp);
			
			// Pega um subcoleção de dados
			countLine= Math.round( countLine * this.testSize );
			System.out.println("Total de linhas utilizadas: " +countLine);
			this.dataMatrix= new String[countLine][numberColumns];
			transferDataMatrix(dataMatrixTemp, countLine);
			printArray(this.dataMatrix);
			
			// Ordenar atributos contunuos
			orderAttibuteContinuos(this.dataMatrix);
			printArray(this.dataMatrix);			
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDataMatrix(String pathFile, String[][] matrix) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(pathFile));
		String firstLine = "";
		int idx = 0;
		while (true) {
			firstLine = buffRead.readLine();
			if (firstLine != null) {
				matrix[idx++] = firstLine.split(",");
			} else
				break;
		}
		buffRead.close();
	}
	
	private void shuffle(String[][] matrix) {
		Random random = new Random();
		for (int i=0; i < (matrix.length - 1); i++) {
			int j = random.nextInt(matrix.length);
			String[] temp = matrix[i];
			matrix[i] = matrix[j];
			matrix[j] = temp;
		}	
	}
	
	private void orderAttibuteContinuos(String[][] matrix) {
		List<Integer> continuousAttributeColumns= new ArrayList<Integer>();
		for (int i = 0; i < matrix.length; i++) {
			if ( isNumero( matrix[0][i] ) ) {
				continuousAttributeColumns.add( i );
			}
		}
	}	
	
	private void transferDataMatrix(String[][] matrixTemp, int countLine) {
		for (int i=0; i < countLine; i++) {
			this.dataMatrix[i]= matrixTemp[i];
		}	
	}
	
	private void printArray(String[][] matrix) {
		for (int i = 0; i <matrix.length; i++) {
			System.out.print(" ");
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print( matrix[i][j]+ " " );
			}
			System.out.println();
			if ( i > 10 ) {
				System.out.println(" . . . ");
				break;
			}
		}
	}
	
	private void calcCollectionEntropy() {
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
		
		float entropy= 0f;
		for (String key : mapElements.keySet()) {
			
		}
	}
	
	private void calcAttributeEntropy() {
	}	
	
	private void calcDadosContinuos() {
	}	

	private void loadAttributes() {
		for (int i = 0; i < dataMatrix.length; i++) {
			String[] line = dataMatrix[i];
		}
	}

	public static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}

	private boolean isNumero(String numero) {
		boolean isNumero = true;
		boolean isDouble = true;
		boolean isInteger= true;
		
		try {
			Integer.parseInt(numero);
		} catch (Exception e) {
			isInteger= false;
		}
		
		try {
			Double.parseDouble(numero);
		} catch (Exception e) {
			isDouble= false;
		}
		
		if ( !isInteger || !isDouble ) {
			isNumero= false;
		}
		
		return isNumero;
	}

}
