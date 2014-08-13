package calculate.misc;

import calculate.structures.Token;

public class CloseParenthesis extends Token {

	final public static String REPRESENTATION = ")";
	final public static String LATEX_REPRESENTATION = " \\right) ";
	
	public CloseParenthesis() {
		super( REPRESENTATION );
	}
	
	@Override
	public CloseParenthesis copy( int indexInInput ) {
		CloseParenthesis rtn = new CloseParenthesis();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public String toLatexString() {
		return LATEX_REPRESENTATION;
	}
}
