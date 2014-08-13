package calculate.operators;

import calculate.structures.Operator;

public class AddOperator extends Operator {

	final public static String REPRESENTATION = "+";
	final public static String LATEX_REPRESENTATION = "+";
	final public static int PRECEDENCE = 1;
	
	public AddOperator() {
		super( REPRESENTATION , LATEX_REPRESENTATION , LEFT_ASSOCIATIVE , PRECEDENCE );
	}
	
	@Override
	public AddOperator copy( int indexInInput ) {
		AddOperator rtn = new AddOperator();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
}
