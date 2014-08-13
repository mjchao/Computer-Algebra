package calculate.functions;

import calculate.Calculator;
import calculate.structures.functional.Function;

public class SineFunction extends Function {

	final public static String REPRESENTATION = "sin";
	final public static int NUM_ARGUMENTS = 1;
	
	public SineFunction( Calculator environment ) {
		super( REPRESENTATION , environment , NUM_ARGUMENTS );
	}
	
	@Override
	public SineFunction copy( int indexInInput ) {
		SineFunction rtn = new SineFunction( getEnvironment() );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
