package calculate;

/**
 * thrown when input from the user is not recognized
 *
 */
public class ParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ParseException( String unrecognizedInput ) {
		super( "Unidentifiable: " + unrecognizedInput );
	}
	
	public ParseException( String problematicInput, int startIndexOfProblem ) {
		super( "Unidentifiable input starting at index " + startIndexOfProblem + " in " + problematicInput );
	}
}
