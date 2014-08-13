package util;

import javax.swing.JOptionPane;

public class GeneralMethods {
	
	final public static int CONFIRM_YES = JOptionPane.YES_OPTION;
	final public static int CONFIRM_NO = JOptionPane.NO_OPTION;
	
	final public static int displayConfirmMessage( String message ) {
		int response = JOptionPane.showConfirmDialog( null, message, "Confirm", JOptionPane.YES_NO_OPTION );
		return response;
	}
	
	final public static void displayErrorMessage( String message ) {
		JOptionPane.showMessageDialog( null , message , "Error" , JOptionPane.ERROR_MESSAGE );
	}

}
