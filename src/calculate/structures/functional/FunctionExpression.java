package calculate.structures.functional;

import calculate.Calculator;
import calculate.structures.Combinable;

public class FunctionExpression extends Combinable {

	public FunctionExpression( String representation , Calculator environment ) {
		super( representation , environment );
	}

	public FunctionExpression add( FunctionExpression augend ) {
		//TODO
		return null;
	}
	
	public FunctionExpression subtract( FunctionExpression subtrahend ) {
		//TODO
		return null;
	}
	
	public FunctionExpression multiply( FunctionExpression multiplicand ) {
		//TODO
		return null;
	}
	
	/**
	 * @return			the opposite of this function expression
	 */
	public Function multiplyByNegativeOne() {
		//TODO
		return null;
	}
}
