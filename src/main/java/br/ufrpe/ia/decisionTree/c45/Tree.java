/**
 * @author Taciano Amorim
 *
 */
package br.ufrpe.ia.decisionTree.c45;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tree {

	private Attribute root;
	
	private String[][] dataMatrix;
	private int classIndex;
	private int maxDepthint;
	private int minSamplesLeaf;
	private float testSize;
	private float classEntropy;
	private boolean hasColumn;
	private int numberColumns;
	private double entropy;

	private List<Attribute> attributes;

	public static void main(String[] args) {
		new Tree("dataset/acute_mixed.dst", 7, 0, 0, 0.5, false);
	}

	/**
	 * 
	 * @param pathFile 		- File path 
	 * @param classIndex 	- Class index
	 * @param minSamplesLeaf- Minimum number of examples on the node. It will be enabled if != 0.
	 * @param maxDepthint 	- Stimulates maximum depth for tree growth. It will be enabled if != 0.
	 * @param testSize 		- Uses the unformed percentage of the data set to build the validation set.
	 */
	public Tree(String pathFile, int classIndex, int minSamplesLeaf, int maxDepthint, double testSize, boolean hasColumn) {
		this.classIndex 	= classIndex;
		this.minSamplesLeaf	= minSamplesLeaf;
		this.maxDepthint	= maxDepthint;
		this.testSize		= (float) testSize;
		this.hasColumn		= hasColumn;
		
		System.out.println("=> Config tree: pathFile="+ pathFile+", classIndex="+classIndex+
							", minSamplesLeaf="+minSamplesLeaf+", maxDepthint="+ maxDepthint+", testSize="+ testSize);

		prepareDataMatrix(pathFile);
		
		//TODO: 
		calcDadosContinuos();
		
		loadListAttributes();
		
		calcClassEntropy();
		
		calcAttributeEntropy();
		
		// TODO: Definir a raiz
		setRootAttribute();

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
			numberColumns = columns.length;

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
			orderAttibuteContinuos();
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
	
	private void orderAttibuteContinuos() {
		List<Integer> continuousAttributeColumns= new ArrayList<Integer>();
		for (int i = 0; i < this.numberColumns; i++) {
			if ( isNumero( this.dataMatrix[0][i] ) ) {
				continuousAttributeColumns.add( i );
			}
		}
	}	
	
	private void loadListAttributes() {
		attributes= new ArrayList<Attribute>();
		for ( int i = 0; i < this.numberColumns; i++ ) {
			boolean isClass= false;
			if ( this.classIndex == i ) {
				isClass= true;
			}
			
			Attribute attribute= new Attribute( "Column_"+ i, isClass);
			attribute.setIdx(i);
			
			List<String> listValues= new ArrayList<String>();
			for ( int j = 0; j < this.dataMatrix.length; j++ ) {
				listValues.add( this.dataMatrix[j][i] );
			}
			attribute.setValues(listValues);
			attributes.add(attribute);
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
	
	private void calcClassEntropy() {
		System.out.println("******* Calc Class Entropy ********");
		Attribute attribute= new Attribute();
		for (Attribute object : attributes) {
			if ( object.isClass() ) {
				attribute= object;
				break;
			}
		}
		
		Map<String, Integer> mapSelection= new HashMap<String, Integer>();
		for (String value : attribute.getValues()) {
			if ( mapSelection.get(value) == null ) {
				mapSelection.put(value, 1);
			} else {
				int valueTemp= mapSelection.get(value).intValue() + 1;
				mapSelection.replace(value, valueTemp);
			}
		}
		
		int numberElements= attribute.getValues().size();
		BigDecimal divisor= new BigDecimal(numberElements);
		
		this.entropy= 0;
		for(String key : mapSelection.keySet()) {
			BigDecimal dividendo= new BigDecimal( mapSelection.get(key) );
			
			System.out.println(" -> Total elements: "+ numberElements);
			System.out.println(" -> ("+key+")- "+mapSelection.get(key));

			BigDecimal result= dividendo.divide(divisor, 7, RoundingMode.HALF_UP);
			this.entropy= this.entropy - result.doubleValue() * log2( result.doubleValue() );
		}
		System.out.println(" -> Class entropy: "+ entropy);
	
	}
	
	private void calcAttributeEntropy() {
		System.out.println();
		System.out.println("******* Calc Attribute Entropy ********");
		for (Attribute attribute : attributes) {
			if ( !attribute.isClass() ) {
				
				System.out.println(" => Attribute: "+ attribute.getName());
				
				double gain= 0;
				Map<String, Integer> mapSelection= new HashMap<String, Integer>();
				for (String value : attribute.getValues()) {
					if ( mapSelection.get(value) == null ) {
						mapSelection.put(value, 1);
					} else {
						int valueTemp= mapSelection.get(value).intValue() + 1;
						mapSelection.replace(value, valueTemp);
					}
				}
				
				int numberElements= attribute.getValues().size();
				BigDecimal divisor= new BigDecimal(numberElements);
				
				double entropyAttribute= 0;
				for(String key : mapSelection.keySet()) {
					BigDecimal dividendo= new BigDecimal( mapSelection.get(key) );
					
					System.out.println("  Total elements: "+ numberElements + " (" + key+")- "+mapSelection.get(key));

					BigDecimal result= dividendo.divide(divisor, 7, RoundingMode.HALF_UP);
					entropyAttribute= entropyAttribute - result.doubleValue() * log2( result.doubleValue() );
				}
				
				gain= this.entropy- entropyAttribute;
				attribute.setGain(gain);
				
				System.out.println(" -> Attribute gain: "+ gain);		
				
			}
		}
	}	
	
	private void calcDadosContinuos() {
	}	

	private void setRootAttribute() {
		System.out.println();
		System.out.println("******* Set Root Attribute ********");		
		for (Attribute attribute : attributes) {
			if ( this.root == null) {
				this.root= attribute;
			
			} else {
				if ( this.root.getGain() < attribute.getGain() ) {
					this.root= attribute;
				}
			}
		}
		System.out.println(" -> Attribute root: "+ this.root.getName() + " ("+ this.root.getGain() +")");	
	}	
	
	private void loadAttributes() {
		for (int i = 0; i < dataMatrix.length; i++) {
			String[] line = dataMatrix[i];
		}
	}

	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
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
