package gui.input;

import java.awt.Color;

import gui.Display;
import util.Text;

public class InputDisplay extends Display {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final public static Color INPUT_COLOR = Color.BLUE;
	
	public InputDisplay() {
		
		//identify this as input
		this.lblDescription.setText( Text.Display.InputDisplay.INPUT_LABEL );
		
		//set appropriate colors
		this.lblDescription.setForeground( INPUT_COLOR );
		this.m_displayColor = INPUT_COLOR;
	}
	
	final public void addInputDisplayListener( InputDisplayListener l ) {
		this.cmdRemove.addActionListener( l );
	}
}
