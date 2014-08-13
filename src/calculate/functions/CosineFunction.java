package calculate.functions;

import calculate.Calculator;
import calculate.structures.functional.Function;

public class CosineFunction extends Function {

	final public static String REPRESENTATION = "cos";
	final public static int NUM_ARGUMENTS = 1;
	
	public CosineFunction( Calculator environment ) {
		super( REPRESENTATION , environment , NUM_ARGUMENTS );
	}
	
	@Override
	public CosineFunction copy( int indexInInput ) {
		CosineFunction rtn = new CosineFunction( getEnvironment() );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
