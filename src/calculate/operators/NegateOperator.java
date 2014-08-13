package calculate.operators;

import calculate.structures.Operator;

public class NegateOperator extends Operator {

	final public static String REPRESENTATION = "!";
	final public static String LATEX_REPRESENTATION = null;
	final public static int PRECEDENCE = 3;
	
	public NegateOperator() {
		super( REPRESENTATION , LATEX_REPRESENTATION , RIGHT_ASSOCIATIVE, PRECEDENCE );
	}

	@Override
	public Operator copy(int indexInInput) {
		NegateOperator rtn = new NegateOperator();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
