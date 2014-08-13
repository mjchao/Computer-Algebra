package calculate;

/**
 * thrown when input from user is not mathematically syntactically correct
 */
public class SyntaxException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SyntaxException( String errorMessage ) {
		super( errorMessage );
	}

	public SyntaxException( String errorMessage , int index ) {
		super ( errorMessage + " \n \t at index " + index + " in input.");
	}
}
