package gui.input;

import gui.DisplayListener;
import gui.Shell;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputDisplayListener extends DisplayListener implements KeyListener {
	
	final private InputDisplay m_inputDisplay;
	
	public InputDisplayListener( Shell shell, InputDisplay inputDisplay ) {
		super( shell, inputDisplay );
		this.m_inputDisplay = inputDisplay;
	}
	
	
	@Override
	public void keyPressed( KeyEvent e ) {
		//if the user presses enter
		if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
			
			//if the user presses enter on the newest input field (the one that cannot be removed)
			this.m_inputDisplay.removeKeyListener( this );
			this.m_shell.removeDisplay( this.m_inputDisplay );
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// unused
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// unused
		
	}

}
