package calculate.operators;

import calculate.structures.Operator;

public class ExponentiateOperator extends Operator {

	final public static String REPRESENTATION = "^";
	final public static String LATEX_REPRESENTATION = "^";
	final public static int PRECEDENCE = 3;
	
	public ExponentiateOperator() {
		super( REPRESENTATION , LATEX_REPRESENTATION , RIGHT_ASSOCIATIVE , PRECEDENCE );
	}
	
	@Override
	public ExponentiateOperator copy( int indexInInput ) {
		ExponentiateOperator rtn = new ExponentiateOperator();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
