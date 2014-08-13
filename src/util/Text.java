package util;

final public class Text {
	
	final public static class ShellMenu {
		
		final public static String Shell = "Shell";
			final public static String EXIT = "Exit";
				final public static String CONFIRM_EXIT = "Are you sure you want to exit?";
		
		final public static String OPTIONS = "Options";
			final public static String CLEAR = "Clear";
	}
	
	final public static class Display {
		final public static String REMOVE = "\u2718";
		
		final public static class InputDisplay {
			final public static String INPUT_LABEL = "Input:";
		}
		
		final public static class OutputDisplay {
			final public static String OUTPUT_LABEL = "Output:";
			final public static String OUTPUT_ERROR_MESSAGE = "\\text{Output Error}";
		}
	}
	
	final public static class InputField {
		final public static String INPUT_LABEL = "Input:";
		final public static String ENTER = "Enter";
	}

	final public static class Calculate {
		
		final public static class ErrorMessages {
			
			final public static String MISSING_OPEN_PARENTHESIS = "Missing \"(\"";
			final public static String MISSING_CLOSE_PARENTHESIS = "Missing \")\"";
			final public static String EXTRA_PRENTHESIS = "Extra Parenthesis ";
			
			final public static String getInsufficientOperandsMessage( String operator ) {
				return "Too few operands for operator \"" + operator + "\"";
			}
			
			final public static String getInsufficientArgumentsMessage( String function ) {
				return "Too few arguments for function \"" + function + "\"";
			}
			
			final public static String getTooManyOperandsMessage( String extraOperand ) {
				return "Too many operands/arguments. Extra operand/argument detected: " + extraOperand;
			}
		}
	}
}
