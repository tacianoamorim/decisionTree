/**
 * @author Taciano Amorim
 *
 */
package br.ufrpe.ia.decisionTree.c45;

import java.util.Scanner;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int opcion;
		Tree tree= null;
		System.out.println("<< Decision tree (C 4.5) program >>");
		boolean loop= true;
		do {
			System.out.println("#####################################");
			System.out.println("Enter the option:");
			System.out.println(" (1): Run Acute Mixed (Training)");
			System.out.println(" (2): Run Abalone Mixed (Test)");
			System.out.println(" (3): Exit");
			System.out.println("#####################################");
			System.out.println();
			opcion = scanner.nextInt();
			switch (opcion) {
				case 1: 
					tree = new Tree(null);
					System.out.println("(1): Run Acute Mixed (Training)");
					break;
				
				case 2:
					tree = new Tree(null);
					System.out.println("(2): Run Abalone Mixed (Test)");
					break;
				
				case 3: 
					loop= false;
					System.out.println(" (3): Exit");
					break;
				
				default:
					System.out.println(" (x): - - - - - - - - - - - - - ");
					break;
			} 
		} while (loop);

	}

}
