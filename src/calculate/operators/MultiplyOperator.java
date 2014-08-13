package calculate.operators;

import calculate.structures.Operator;

public class MultiplyOperator extends Operator {

	final public static String REPRESENTATION = "*";
	final public static String LATEX_REPRESENTATION = " \\cdot ";
	final public static int PRECEDENCE = 2;
	
	public MultiplyOperator() {
		super( REPRESENTATION , LATEX_REPRESENTATION , LEFT_ASSOCIATIVE , PRECEDENCE );
	}
	
	@Override
	public MultiplyOperator copy( int indexInInput ) {
		MultiplyOperator rtn = new MultiplyOperator();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
