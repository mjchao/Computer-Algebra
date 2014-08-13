package calculate.structures;

import calculate.operators.AddOperator;
import calculate.operators.DivideOperator;
import calculate.operators.ExponentiateOperator;
import calculate.operators.MultiplyOperator;
import calculate.operators.SubtractOperator;

abstract public class Operator extends Token {

	final public static int LEFT_ASSOCIATIVE = 1;
	final public static int RIGHT_ASSOCIATIVE = 2;
	private String m_latexRepresentation;
	private int m_associativity;
	private int m_precedence;
	
	public Operator( String representation , String latexRepresentation , int associativity , int precedence ) {
		super( representation );
		this.m_latexRepresentation = latexRepresentation;
		this.m_associativity = associativity;
		this.m_precedence = precedence;
	}

	/**
	 * @param aRepresentation			a textual representation of a token
	 * @return							if the given textual representation is that of an operator
	 */
	public static boolean isOperator( String aRepresentation ) {
		//go through all the operators and see if any operator's representation
		//matches the provided one
		if ( aRepresentation.equals( AddOperator.REPRESENTATION) ) {
			return true;
		} else if ( aRepresentation.equals( SubtractOperator.REPRESENTATION ) ) {
			return true;
		} else if ( aRepresentation.equals( MultiplyOperator.REPRESENTATION ) ) {
			return true;
		} else if ( aRepresentation.equals( DivideOperator.REPRESENTATION ) ) {
			return true;
		} else if ( aRepresentation.equals( ExponentiateOperator.REPRESENTATION ) ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return			the LaTeX formula for this operator
	 */
	public String getLatexRepresentation() {
		return this.m_latexRepresentation;
	}
	
	/**
	 * @return			associativity of this operator (right of left)
	 */
	public int getAssociativity() {
		return this.m_associativity;
	}
	
	/**
	 * @return			the precedence of this operator
	 */
	public int getPrecedence() {
		return this.m_precedence;
	}
	
	/**
	 * returns a copy of this operator with the same value, but also specified the
	 * location in input at which this operator appeared
	 * 
	 * @param indexInInput			location in input at which this operator appeared
	 * @return
	 */
	@Override
	abstract public Operator copy( int indexInInput );
}
