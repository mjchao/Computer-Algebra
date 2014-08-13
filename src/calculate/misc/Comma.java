package calculate.misc;

import calculate.structures.Token;

public class Comma extends Token {

	final public static String REPRESENTATION = ",";
	
	public Comma() {
		super( REPRESENTATION );
	}

	@Override
	public Comma copy( int indexInInput ) {
		Comma rtn = new Comma();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
}
