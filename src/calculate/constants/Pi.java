package calculate.constants;

import calculate.Calculator;
import calculate.structures.polynomial.Constant;
import calculate.structures.polynomial.Fraction;

public class Pi extends Constant {

	final public static String REPRESENTATION = "pi";
	
	public Pi( Calculator environment ) {
		super( environment , REPRESENTATION , new Fraction( environment , "3.14159" ) );
	}
	
	
	@Override
	public Pi copy( int indexInInput ) {
		Pi rtn = new Pi( getEnvironment() );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public Pi clone() {
		return this.copy( getIndexInInput() );
	}
	
	@Override
	public String toLatexString() {
		return " \\pi ";
	}
}
