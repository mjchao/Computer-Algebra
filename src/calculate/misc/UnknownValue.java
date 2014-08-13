package calculate.misc;

import calculate.structures.Combinable;

public class UnknownValue extends Combinable {

	final public static String REPRESENTATION = "?";
	
	public UnknownValue() {
		super( REPRESENTATION , null );
	}
}
