package calculate.operators;

import calculate.structures.Operator;

public class DivideOperator extends Operator {

	final public static String REPRESENTATION = "/";
	final public static String LATEX_REPRESENTATION = "/";
	final public static int PRECEDENCE = 2;
	
	public DivideOperator() {
		super( REPRESENTATION , LATEX_REPRESENTATION , LEFT_ASSOCIATIVE , PRECEDENCE );
	}
	
	@Override
	public DivideOperator copy( int indexInInput) {
		DivideOperator rtn = new DivideOperator();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
