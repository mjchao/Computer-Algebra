package calculate.operators;

import calculate.structures.Operator;

public class SubtractOperator extends Operator {

	final public static String REPRESENTATION = "-";
	final public static String LATEX_REPRESENTATION = "-";
	final public static int PRECEDENCE = 1;
	
	public SubtractOperator() {
		super( REPRESENTATION , LATEX_REPRESENTATION , LEFT_ASSOCIATIVE , PRECEDENCE );
	}
	
	@Override
	public SubtractOperator copy( int indexInInput ) {
		SubtractOperator rtn = new SubtractOperator();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
