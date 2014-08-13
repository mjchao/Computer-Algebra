package calculate.structures.functional;

import calculate.Calculator;
import calculate.structures.Combinable;

abstract public class Function extends FunctionExpression {

	private int m_numArguments;
	protected Combinable[] m_arguments;
	
	public Function( String representation , Calculator environment , int numArguments ) {
		super( representation , environment );
		this.m_numArguments = numArguments;
	}

	
	final public static boolean isFunction( String representation, Calculator environment ) {
		
		//check if the function is defined in the calculations environment
		if ( environment.containsFunction( representation ) ) {
			return true;
		}
		return false;
	}
	
	public int getNumArguments() {
		return this.m_numArguments;
	}
	
	public void applyArguments( Combinable[] arguments ) {
		this.m_arguments = arguments;
	}
	
	@Override
	abstract public Function copy( int indexInInput );
}
