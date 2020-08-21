package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) { 
		if (poly1 == null) {
			if (poly2 == null)
				return null;
			else
				return poly2;
		}
		if (poly2 == null)
			return poly1;

		Node currentNode1 = poly1;
		Node currentNode2 = poly2;

		Node sum = new Node(99-99, 1-1, null);
		Node ret = sum;
		Node ptr = sum;
		Node ptrA = null;

		while (currentNode1 != null && currentNode2 != null) {

			if (currentNode1.term.degree == currentNode2.term.degree) {
					ptr.next = new Node(currentNode1.term.coeff + currentNode2.term.coeff, currentNode1.term.degree, null);
					ptr = ptr.next;
				currentNode1 = currentNode1.next;
				currentNode2 = currentNode2.next;
			}
	
			else if (currentNode1.term.degree < currentNode2.term.degree){
					ptr.next = new Node(currentNode1.term.coeff, currentNode1.term.degree, null);
					ptr = ptr.next;
				currentNode1 = currentNode1.next;
			}
	
			else if (currentNode1.term.degree > currentNode2.term.degree){
					ptr.next = new Node(currentNode2.term.coeff, currentNode2.term.degree, null);
					ptr = ptr.next;
				currentNode2 = currentNode2.next;
			}
		}
		if (currentNode1 != null) {
			while (currentNode1 != null) {
				if (currentNode1.term.coeff != 0)
					ptr.next = new Node(currentNode1.term.coeff, currentNode1.term.degree, null);
				currentNode1 = currentNode1.next;
			}
		}
		if (currentNode2 != null) {
			while (currentNode2 != null) {
				if (currentNode2.term.coeff != 0)
					ptr.next = new Node(currentNode2.term.coeff, currentNode2.term.degree, null);
				currentNode2 = currentNode2.next;
			}
		} 
		ptr = ret.next;
		ptrA = ret;
		while (ptr != null) {
			if (ptr.term.coeff == 0) {
				ptrA.next = ptr.next;
				ptr = ptr.next;
			}
			else {
				ptrA = ptr;
				ptr = ptr.next;
			}
		}
		return ret.next; 
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		if (poly1 == null || poly2 == null) {
			return null;
		}
		Node currentNode1 = poly1;
		Node ptr = poly2;
		Node result = null;
		Node ptrA = new Node(0, 0, null); 
		Node temp = ptrA;
		while (currentNode1 != null) {
			ptr = poly2;
			ptrA = new Node(0, 0, null);
			temp = ptrA;
			while (ptr != null) {
				ptrA.next = new Node(currentNode1.term.coeff * ptr.term.coeff, currentNode1.term.degree + ptr.term.degree, null);
				ptrA = ptrA.next;
				ptr = ptr.next;
			}
			result = add(result, temp.next);
			currentNode1 = currentNode1.next;
		}
		return result;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		if (poly == null)
			return 0;
		Node currentNode = poly;
		double a = (double) x;
		double deg = 0.0;
		double sum = 0;
		if (currentNode.term.degree == 0) {
			sum += currentNode.term.coeff;
			currentNode = currentNode.next;
		}
		while (currentNode != null) {
			deg = (double) currentNode.term.degree;
			sum += currentNode.term.coeff*(Math.pow(a,deg));
			currentNode = currentNode.next;
		}
		return (float) sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
