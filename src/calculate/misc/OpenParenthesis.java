package calculate.misc;

import calculate.structures.Token;

public class OpenParenthesis extends Token {

	final public static String REPRESENTATION = "(";
	final public static String LATEX_REPRESENTATION = " \\left( ";
	
	public OpenParenthesis() {
		super( REPRESENTATION );
	}

	@Override
	public OpenParenthesis copy( int indexInInput ) {
		OpenParenthesis rtn = new OpenParenthesis();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public String toLatexString() {
		return LATEX_REPRESENTATION;
	}
}
