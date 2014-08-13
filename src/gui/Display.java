package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import util.Text;

/**
 * Provides a description (label) and a field (text field) in which a user may enter 
 * input, or output may be stored
 */
public class Display extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final protected JButton cmdRemove = new JButton( Text.Display.REMOVE );
	
	final protected JLabel lblDescription = new JLabel();
	
	private int m_displayFontSize = 14;
	protected Color m_displayColor = Color.black;
	protected BufferedImage m_latexImage;
	final protected JPanel display;
	
	final private FlowLayout m_layout = new FlowLayout( FlowLayout.LEFT );
	
	public Display() {
		
		this.display = new LatexPanel();
		
		setLayout( this.m_layout );
		
		//add graphical components
		add( this.cmdRemove );
		add( this.lblDescription );
		add( this.display );
	}
	
	/**
	 * sets this display to show the specified LaTeX output. 
	 * 
	 * @param latexCode			LaTeX code for rendering LaTeX output
	 */
	public void setLatexDisplay( String latexCode ) {
		
		TeXFormula formula;
		try {
			//use jlatexmath library for rendering LaTeX
			formula = new TeXFormula( latexCode );
		} catch (Exception e) {
			formula = new TeXFormula( Text.Display.OutputDisplay.OUTPUT_ERROR_MESSAGE );
		}
		//set the LaTeX output properties
		TeXIcon ti = formula.createTeXIcon( TeXConstants.STYLE_DISPLAY, this.m_displayFontSize );
		
		//create the LaTeX image
		try {
			this.m_latexImage = new BufferedImage( ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR );
		} catch (IllegalArgumentException noInput) {
			return;
		}
		ti.paintIcon( new JLabel(), this.m_latexImage.getGraphics(), 0, 0 );
	}
	
	/**
	 * modified <code>JPanel</code> that allows LaTeX images to be displayed
	 */
	private class LatexPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public LatexPanel() {
			//do nothing for now
		}
		
		@Override
		public void paint(Graphics g) {
			
			if ( Display.this.m_latexImage != null ) {
				
				//modify dimensions of this display
				setPreferredSize( new Dimension( Display.this.m_latexImage.getWidth(), Display.this.m_latexImage.getHeight() ) );
				
				//draw the image onto this panel
				g.drawImage( Display.this.m_latexImage, 0, 0, null );
				
				//update the size of this panel
				revalidate();
			}
		}
	}
}
