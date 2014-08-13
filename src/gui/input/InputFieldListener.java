package gui.input;

import gui.Shell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import util.Text;

public class InputFieldListener implements ActionListener, KeyListener {

	final private Shell m_shell;
	final private InputField m_inputField;
	
	public InputFieldListener( Shell shell, InputField inputField ) {
		this.m_shell = shell;
		this.m_inputField = inputField;
	}

	@Override
	public void keyPressed( KeyEvent e ) {
		
		//if the user presses enter key, then process input in the input field
		if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
			processInput();
		}
	}
	
	@Override
	public void actionPerformed( ActionEvent e ) {
		String command = e.getActionCommand();
		
		//if the user clicks enter, then process the input in the input field
		if ( command.equals( Text.InputField.ENTER ) ) {
			processInput();
		}
	}
	
	final public void processInput() {
		try {
			this.m_shell.processInput( this.m_inputField );
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		//unused
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//unused
	}
}
