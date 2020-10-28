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

	private No root;

	private String[][] dataMatrix;
	private int classIndex;
	private int maxDepthint;
	private int minSamplesLeaf;
	private float testSize;
	private boolean hasColumn;
	private int numberColumns;
	//private double entropy;

	private List<Attribute> startedAttributes;

	public static void main(String[] args) {
		Tree tree= new Tree("dataset/acute_mixed_resume.dst", 6, 0, 0, 0.05, false);
		tree.run();
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
		
		System.out.println("####################################################################################################################");
		System.out.println("CREATE DECISION TREE ");
		System.out.println("####################################################################################################################");
		System.out.println("=> Config tree: pathFile="+ pathFile+", classIndex="+classIndex+
							", minSamplesLeaf="+minSamplesLeaf+", maxDepthint="+ maxDepthint+", testSize="+ testSize);

		System.out.println();
		System.out.println("####################################################################################################################");
		System.out.println("PREPARE DATA ");
		System.out.println("####################################################################################################################");
		prepareDataMatrix(pathFile);
		
		loadListAttributes();
		
		calcDadosContinuos();
		
		double entropyClass= calcClassEntropy(this.startedAttributes);
		calcAttributeEntropy(this.startedAttributes, entropyClass);
		
		setRootAttribute( this.startedAttributes);
	}
	

	/**
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 	PREPARE DATA
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 */
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
			printArray("Temporary Matrix", dataMatrixTemp);
			
			// Embaralha todos os registros
			shuffle(dataMatrixTemp);
			
			// Pega um subcoleção de dados
			countLine= Math.round( countLine * this.testSize );
			System.out.println("Total de linhas utilizadas: " +countLine);
			this.dataMatrix= new String[countLine][numberColumns];
			transferDataMatrix(dataMatrixTemp, countLine);
			printArray(null, this.dataMatrix);
			
			// Ordenar atributos contunuos
			orderAttibuteContinuos();
			sortArray(this.dataMatrix, 0);
			sortArray(this.dataMatrix, 0);
			printArray("Ordered Matrix", this.dataMatrix);	
						
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
	
	private void orderAttibuteContinuos() {
		List<Integer> continuousAttributeColumns= new ArrayList<Integer>();
		for (int i = 0; i < this.numberColumns; i++) {
			if ( isNumero( this.dataMatrix[0][i] ) ) {
				continuousAttributeColumns.add( i );
			}
		}
	}	
	
	private void loadListAttributes() {
		startedAttributes= new ArrayList<Attribute>();
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
			startedAttributes.add(attribute);
		}
	}
	
	private void transferDataMatrix(String[][] matrixTemp, int countLine) {
		for (int i=0; i < countLine; i++) {
			this.dataMatrix[i]= matrixTemp[i];
		}	
	}
	
	private double calcClassEntropy(List<Attribute> attributes) {
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
		
		double entropy= 0;
		System.out.println(" -> Total elements: "+ numberElements);
		for(String key : mapSelection.keySet()) {
			BigDecimal dividendo= new BigDecimal( mapSelection.get(key) );
			System.out.println(" -> ("+key+")- "+mapSelection.get(key));
			BigDecimal result= dividendo.divide(divisor, 7, RoundingMode.HALF_UP);
			entropy= entropy - result.doubleValue() * log2( result.doubleValue() );
		}
		System.out.println(" -> Class entropy: "+ entropy);
		
		return entropy;
	}
	
	private void calcAttributeEntropy(List<Attribute> listAttributes, double entropyClass) {
		System.out.println();
		System.out.println("******* Calc Attribute Entropy ********");
		for (Attribute attribute : listAttributes) {
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
				
				gain= entropyClass- entropyAttribute;
				attribute.setGain(gain);
				
				System.out.println(" -> Attribute gain: "+ gain);		
				
			}
		}
	}	
	
	private void calcDadosContinuos() {
//		String[][] arrayDataMatrix= new String[this.startedAttributes.get(0).getValues().size()][2];
//		for (int j = 0; j < this.startedAttributes.get(0).getValues().size(); j++) {
////			arrayDataMatrix[0]
//		}		
	}

	private No setRootAttribute( List<Attribute> listAttributes) {
		System.out.println();
		System.out.println("******* Set Root Attribute ********");		
		No noRef= null;
		for (Attribute attribute : listAttributes) {
			if ( noRef == null) {
				noRef= new No();
				noRef.setAttributeRoot( attribute );
			
			} else {
				if ( noRef.getAttributeRoot().getGain() < attribute.getGain() ) {
					noRef.setAttributeRoot( attribute );
				}
			}
		}
		noRef.setDepth(0);
		this.root= noRef;
		this.root.setAttributes(listAttributes);
		System.out.println(" -> Attribute root: "+ noRef.getAttributeRoot().getName() + " ("+ noRef.getAttributeRoot().getGain() +")");	
		return noRef;
	}	
	
	private void setNoAttribute( List<Attribute> listAttributes, No noRef, No noFather ) {
		System.out.println();
		System.out.println("******* Set Root Attribute ********");		
		for (Attribute attribute : listAttributes) {
			if ( noRef == null) {
				noRef= new  No();
				noRef.setAttributeRoot( attribute );
			
			} else {
				if ( noRef.getAttributeRoot().getGain() < attribute.getGain() ) {
					noRef.setAttributeRoot( attribute );
				}
			}
		}

		int depth= 0;
		if (noFather!=null)
			depth= noFather.getDepth()+1;
		noRef.setDepth(depth);
		
		System.out.println(" -> Attribute No: "+ noRef.getAttributeRoot().getName() + " ("+ noRef.getAttributeRoot().getGain() +")");	
	}		
	
	/**
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 	PROCESS DECISION TREE
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 */	
	public void run() {
		System.out.println();
		System.out.println("#################################################################################################################### ");
		System.out.println("RUN ALGORITHM");
		System.out.println("#################################################################################################################### ");
		
		this.root.setVisited(false);
		
		while (!this.root.isVisited()) {
			processNo(this.root);
			
			if (this.root.isVisited()) {
				System.out.println("PROCESS DECISION TREE FINISHED . . . ");
			}
		}
		
	}

	private void processNo(No no) {
		
		if ( no.isVisited() || no.isLeaf() )
			return;
		
		if ( no.getNos() != null ) {
			boolean noVisited= true;
			No noRef= null;
			for (No ref : no.getNos() ) {
				if (!ref.isVisited()) {
					noVisited= false;
					noRef= ref;
					break;
				}
			}
			
			if (noVisited) {
				no.setVisited( true );
				return;
			} 
			
			// PROCESS NO REF
			System.out.print(" -> "+ noRef.getDepth());
		
		} else {
			List<Attribute> attributesNo= distributeElements( no );
			
//			double entropyClass= calcClassEntropy(attributesNo);
//			calcAttributeEntropy(attributesNo, entropyClass);
			//setNoAttribute
			
			for (Attribute attribute : attributesNo) {
				//no.
			}
		}
		
		System.out.println("******* Process No ********");
	} 
	
	
	private List<Attribute> distributeElements(No no) {
		
		List<Attribute> attributes= new ArrayList<Attribute>();
		Map<String, Integer> mapSelectionInt= new HashMap<String, Integer>();
		for (String value : no.getAttributeRoot().getValues() ) {
			if ( mapSelectionInt.get(value) == null ) {
				mapSelectionInt.put(value, 1);
			} else {
				int valueTemp= mapSelectionInt.get(value).intValue() + 1;
				mapSelectionInt.replace(value, valueTemp);
			}
		}
		
		//String[][] arrayDataMatrix= new String[no.getAttributes().get(0).getValues().size()][no.getAttributes().size()];
		for (int j = 0; j < no.getAttributes().get(0).getValues().size(); j++) {
			System.out.println(no.getAttributes().get(j).getName());
			for (int i = 0; i < no.getAttributes().size(); i++) {
				System.out.println( no.getAttributes().get(i).getValues().get(j));
			}		
		}
		
		Map<String, List<Attribute>> mapSelectionNo= new HashMap<String, List<Attribute>>();
		for(String key : mapSelectionInt.keySet()) {
		    for (int i = 0; i < no.getAttributeRoot().getValues().size(); i++) {
		    	String value= no.getAttributeRoot().getValues().get(i);
				if (value.equals(key)) {
					if ( mapSelectionNo.get(value) == null ) {
//						Atrin
//						mapSelectionNo.put(value, 1);
					} else {
						int valueTemp= mapSelectionInt.get(value).intValue() + 1;
						mapSelectionInt.replace(value, valueTemp);
					}					
				} else {
					
				}
		        
		        
		    }
			
			for (String value : no.getAttributeRoot().getValues()) {

				
			}
		}		
		
//		for (String value : no.getAttributeRoot().getValues() ) {
//			if ( mapSelectionNo.get(value) == null ) {
//				mapSelectionNo.put(value, 1);
//			} else {
//				int valueTemp= mapSelectionNo.get(value).intValue() + 1;
//				mapSelectionNo.replace(value, valueTemp);
//			}
//		}
		
//		for(String key : mapSelection.keySet()) {
//			for (String value : no.getAttributeRoot().getValues()) {
////				if ( value.e) {
////					
////				}
//			}
//		}
//	
//		
//		for(String key : mapSelection.keySet()) {
//
//		}
		
		return attributes;
	}

	/**
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 	SUPPORT
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 */	
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
	
	private double stringToDouble(String numero) {
		double result= 0;
		try {
			result= Double.parseDouble(numero);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	private void printArray(String msg, String[][] matrix) {
		
		System.out.println();
		if ( msg != null ) {
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ");
			System.out.println(msg);
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ");
		}
		
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
		System.out.println();
	}

	private void sortArray(String[][] matrix, int idx) {
		String[] aux;
		for(int i = 0; i<  (matrix.length - 1); i++){
			for(int j = 0; j<  (matrix.length - 2); j++){
				//System.out.println("if( "+ stringToDouble(matrix[j][idx]) +" > "+ stringToDouble(matrix[j + 1][idx]) +" ) ");
				if (isNumero(matrix[j][idx]) ) {
					if( stringToDouble(matrix[j][idx]) > stringToDouble(matrix[j + 1][idx]) ) {
						aux = matrix[j];
						matrix[j] = matrix[j+1];
						matrix[j+1] = aux;
					}
				}
			}
		}		
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

}
