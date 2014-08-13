package gui.output;

import gui.Display;

import java.awt.Color;

import util.Text;

public class OutputDisplay extends Display {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final public static Color OUTPUT_COLOR = Color.RED;
	
	public OutputDisplay() {
		
		//identify this as the output
		this.lblDescription.setText( Text.Display.OutputDisplay.OUTPUT_LABEL );
		
		//set appropriate colors
		this.lblDescription.setForeground( OUTPUT_COLOR );
		this.m_displayColor = OUTPUT_COLOR;
	}
	
	public void addOutputDisplayListener( OutputDisplayListener l ) {
		this.cmdRemove.addActionListener( l );
	}
	
	
}
