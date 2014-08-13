package calculate.structures;

import calculate.Calculator;
import calculate.ParseException;
import calculate.structures.functional.Function;
import calculate.structures.polynomial.Constant;
import calculate.structures.polynomial.Fraction;
import calculate.structures.polynomial.Numerical;
import calculate.structures.polynomial.Variable;

/**
 * a piece of input
 */
abstract public class Token {

	
	private String m_representation;
	private int m_indexInInput;
	
	/**
	 * @param representation		the textual representation of this token
	 */
	public Token( String representation ) {
		this.m_representation = representation;
	}
	
	/**
	 * creates a specific token, e.g. <code>Numerical</code> or <code>Variable</code>
	 * given the token's textual representation
	 * 
	 * @param representation			textual representation of a token to be created
	 * 
	 * @param environment				environment in which calculations occur. it contains important
	 * 									information such as variable names
	 * 
	 * @return							specific token, e.g. <code>Numerical</code> or <code>Variable</code> that
	 * 									best describes the given textual representation
	 * 
	 * @throws ParseException			if there is no specific token that best describes the given textual
	 * 									representation
	 */
	final public static Token createToken( String representation, Calculator environment ) throws ParseException {
		
		//check if the token is an operator
		if ( environment.containsOperator( representation ) ) {
			return environment.getOperator( representation );
		}
		
		//check if the token is a numerical/fraction
		//look at the output mode to determine whether to use numerical (decimal) or fraction
		//approximate mode uses decimals and exact mode uses fractions
		if ( environment.getOutputMode() == Calculator.OUTPUT_APPROXIMATE ) {
			if ( Fraction.isFraction( representation ) ) {
				return new Fraction( environment , representation );
			}
		} else if ( environment.getOutputMode() == Calculator.OUTPUT_EXACT ) {
			if ( Fraction.isFraction( representation ) ) {
				return new Fraction( environment , representation );
			}
		}
		
		//check if the token is a constant
		//note that we need to do this before we check it is a variable
		if ( Constant.isConstant( representation, environment ) ) {
			return environment.getConstant( representation );
		}
		
		//check if the token is a variable
		if ( Variable.isVariable( representation, environment) ) {
			return environment.getVariable( representation );
		}
		
		//check if the token is a function
		if ( Function.isFunction( representation, environment ) ) {
			return environment.getFunction( representation );
		}
		
		//if no token is created, then there is a parse error
		throw new ParseException( representation );
	}
	
	/**
	 * @return		textual representation of this token
	 */
	public String getRepresentation() {
		return this.m_representation;
	}
	
	/**
	 * modifies the representation of this token
	 * 
	 * @param newRepresentation			the new textual representation of this token
	 */
	final protected void setRepresentation( String newRepresentation ) {
		this.m_representation = newRepresentation;
	}
	
	/**
	 * sets the location in input at which this token appeared
	 * 
	 * @param index			location in input
	 */
	final public void setIndexInInput( int index ) {
		this.m_indexInInput = index;
	}
	
	/**
	 * @return			location in input at which this token appeared
	 */
	final public int getIndexInInput() {
		return this.m_indexInInput;
	}
	
	/**
	 * creates a copy of the token with the same value, but also specifies
	 * the location in input at which this token occurred
	 * 
	 * @param indexInInput			location in input at which this token occurred
	 */
	abstract public Token copy( int indexInInput );
	
	@Override
	public String toString() {
		return getRepresentation();
	}
	
	public String toLatexString() {
		return getRepresentation();
	}
}
