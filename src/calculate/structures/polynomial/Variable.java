package calculate.structures.polynomial;

import calculate.Calculator;
import calculate.structures.Combinable;

public class Variable extends MonomialTerm {
	
	private Combinable m_value = null;

	
	/**
	 * <code>Variable</code> constructor for a <code>Numerical</code>
	 * 
	 * @param environment
	 * @param representation
	 */
	public Variable( Calculator environment , String representation ) {
		super( environment );
		this.m_value = this;
		setRepresentation( representation );
	}
	
	/**
	 * <code>Variable</code> constructor for a <code>Variable</code> or <code>Constant</code>
	 * 
	 * @param environment
	 * @param representation
	 * @param value
	 */
	public Variable( Calculator environment, String representation , Combinable value ) {
		super( environment , Numerical.ONE );
		this.m_value = value;
		setRepresentation( representation );
	}
	
	/**
	 * @param representation			the textual representation of a token to be created
	 * @param environment				the calculations environment in which this token is to be processed
	 * @return							if the textual representation of the token is that of a defined variable
	 */
	final public static boolean isVariable( String representation, Calculator environment) {
		
		//check if the variable has been defined
		if ( environment.containsVariable( representation ) ) {
			return true;
		}
		return false;
	}
	
	public Combinable getValue() {
		return this.m_value;
	}
	
	public boolean equals( Variable anotherVariable ) {
		if ( anotherVariable == null ) {
			return false;
		}
		return anotherVariable.getRepresentation().equals( getRepresentation() );
	}

	@Override
	public int compareTo( Combinable toCompare ) {
		if ( toCompare instanceof Variable ) {
			
			//check for constants
			
			//if this or the other, but not both are constants
			if ( this instanceof Constant ^ toCompare instanceof Constant ) {
				
				//whichever one is the constant is smaller
				if ( this instanceof Constant ) {
					return -1;
				} else {
					return 1;
				}
			}
			
			//otherwise, just compare representations
			Variable variableToCompare = ( Variable ) toCompare;
			return this.getRepresentation().compareTo( variableToCompare.getRepresentation() );
		} else {
			return super.compareTo( toCompare );
		}
	}
	
	/**
	 * creates a copy of the variable with the same value, but adds the location in
	 * input in which the variable appeared
	 * 
	 * @param indexInInput			location in input in which this variable appeared
	 * @return
	 */
	@Override
	public Variable copy( int indexInInput ) {
		Variable rtn = new Variable( getEnvironment() ,  getRepresentation() , this.m_value );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public Variable clone() {
		return copy( getIndexInInput() );
	}
	
	@Override
	public String toLatexString() {
		return getRepresentation();
	}
}
