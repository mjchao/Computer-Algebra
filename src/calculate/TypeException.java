package calculate;

/**
 * thrown when there is a type mismatch when executing input from the user
 */
public class TypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeException( String invalidInput, String expectedType, String foundType ) {
		super("Type mismatch for " + invalidInput + ". Expected type " + expectedType + ", but found " + foundType);
	}
}
