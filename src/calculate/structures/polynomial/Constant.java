package calculate.structures.polynomial;

import calculate.Calculator;
import calculate.structures.Combinable;

/**
 * a variable that has an unchanging, numerical value
 */
public class Constant extends Variable {
	
	/**
	 * <code>Constant</code> constructor for a <code>Numerical</code>
	 * 
	 * @param environment
	 * @param representation
	 */
	public Constant( Calculator environment , String representation ) {
		super( environment , representation );
	}
	
	/**
	 * <code>Constant</code> constructor for a <code>Constant</code>
	 * 
	 * @param environment
	 * @param representation
	 * @param value
	 */
	public Constant( Calculator environment , String representation , Combinable value ) {
		super( environment , representation , value );
	}
	
	/**
	 * 
	 * @param representation			textual representation of a token
	 * @param environment				environment in which calculations occur
	 * @return							if the given token is a constant
	 */
	final public static boolean isConstant( String representation, Calculator environment ) {
		if ( environment.containsConstant( representation ) ) {
			return true;
		}
		return false;
	}
	
	@Override
	public Combinable getDegree() {
		return new Fraction( getEnvironment() , "0" );
	}
	
	@Override
	public boolean equals( Object toCompare ) {
		return super.equals( toCompare );
	}
	
	@Override
	public int compareTo( Combinable toCompare ) {
		
		//if the object with which to compare is a constant
		if ( toCompare instanceof Constant ) {
			Constant constantToCompare = ( Constant ) toCompare;
			//compare representations
			return this.getRepresentation().compareTo( constantToCompare.getRepresentation() );
			
		//constants should come last, so if the object with which to compare
		//is not a constant, then this constant should come after the object to compare
		} else {
			return -1;
		}
	}
	
	@Override
	public Constant copy( int indexInInput ) {
		Constant rtn = new Constant( getEnvironment() , getRepresentation() , getValue() );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public Constant clone() {
		return copy( getIndexInInput() );
	}
	
	@Override
	public String toLatexString() {
		return this.getRepresentation();
	}
}
