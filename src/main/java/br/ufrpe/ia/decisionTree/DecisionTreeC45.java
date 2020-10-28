/**
 * @author Taciano Amorim
 *
 * Search sources:
 *  https://en.wikipedia.org/wiki/C4.5_algorithm
 *  https://www.youtube.com/watch?v=waXxs2T6GPs (Indução de Árvores de Decisão - Aula Completa)
 *  https://www.youtube.com/watch?v=_ICNdRrl68k (Árvore de decisão. Exemplo completo.)
 *  http://www.inf.ufpr.br/menotti/ci171-182/slides/ci171-arvoresdecisao.pdf
 *  https://medium.com/machine-learning-beyond-deep-learning/árvores-de-decisão-3f52f6420b69
 *  https://github.com/random-forests/tutorials/blob/master/decision_tree.py
 *  https://sefiks.com/2018/05/13/a-step-by-step-c4-5-decision-tree-example/
 *  https://www.kaggle.com/nicapotato/womens-ecommerce-clothing-reviews
 *  https://scikit-learn.org/stable/modules/tree.html#tree-algorithms-id3-c4-5-c5-0-and-cart
 */
package br.ufrpe.ia.decisionTree;

import java.util.Scanner;

import br.ufrpe.ia.decisionTree.c45.Tree;

public class DecisionTreeC45 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int opcion;
		Tree tree= null;
		
		System.out.println("<< Decision tree (C 4.5) program >>");
		boolean loop= true;
		do {
			System.out.println("#########################################");
			System.out.println("Enter the option:");
			System.out.println(" (1): Run Acute Mixed (Training)");
			System.out.println(" (2): Exit");
			System.out.println("#########################################");
			System.out.println();
			opcion = scanner.nextInt();
			switch (opcion) {
				case 1: 
					
					System.out.println("Enter the percentage to be used (0.0):");
					double percent = scanner.nextDouble();
					
					tree = new Tree("dataset/acute_mixed.dst", 7, 0, 0, percent, false);
					System.out.println("(1): Run Acute Mixed (Training)");
					break;
				
				case 2: 
					loop= false;
					System.out.println(" (2): Exit");
					break;
				
				default:
					System.out.println(" (x): - - - - - - - - - - - - - ");
					break;
			} 
		} while (loop);
	}
	
}
