package gui;

import gui.input.InputDisplay;
import gui.input.InputDisplayListener;
import gui.input.InputField;
import gui.input.InputFieldListener;
import gui.output.OutputDisplay;
import gui.output.OutputDisplayListener;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import util.GeneralMethods;
import calculate.Calculator;
import calculate.structures.Expression;
import calculate.structures.polynomial.Fraction;

public class Shell extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("hiding")
	final private static int WIDTH = 500;
	@SuppressWarnings("hiding")
	final private static int HEIGHT = 500;
	
	final private ShellMenu menu = new ShellMenu();
	
	final private BorderLayout m_layout = new BorderLayout();
	
	final private DynamicGraphicalList< Display > shellHistory = new DynamicGraphicalList< Display >( 10, 250 );
	final protected JScrollPane scrollShellHistory = new JScrollPane( this.shellHistory );
	
	final private InputField pnlInput = new InputField();
	
	public Shell() {
		
		this.menu.addShellMenuListener( new ShellMenuListener ( this ) );
		setJMenuBar( this.menu );
		
		setLayout( this.m_layout );
			add( this.scrollShellHistory, BorderLayout.CENTER );
		
			this.pnlInput.addInputFieldListener( new InputFieldListener( this, this.pnlInput ) );
			add( this.pnlInput, BorderLayout.SOUTH );
			
		setSize( WIDTH, HEIGHT );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setVisible( true );
	}
	
	public void addNewInputDisplay( String latexInput ) {
		//create the new input display
		InputDisplay newInputField = new InputDisplay();
		newInputField.setLatexDisplay( latexInput );
		newInputField.addInputDisplayListener( new InputDisplayListener( this, newInputField ));
		
		//add the new input display to the graphics
		this.shellHistory.addElement( newInputField );
	}
	
	public void addNewOutputDisplay( String latexOutput ) {
		//create the new output display
		OutputDisplay newOutputField = new OutputDisplay();
		newOutputField.setLatexDisplay( latexOutput );
		newOutputField.addOutputDisplayListener( new OutputDisplayListener( this, newOutputField ) );
		
		//add the new output display to the graphics
		this.shellHistory.addElement( newOutputField );
		
	}
	
	/**
	 * automatically scrolls to the bottom of the shell for the user
	 */
	public void scrollToBottomOfShell() {
		SwingUtilities.invokeLater( 
				new Runnable() {
					@Override
					public void run() {
						JScrollBar verticalScrollBar = Shell.this.scrollShellHistory.getVerticalScrollBar();
						verticalScrollBar.setValue(verticalScrollBar.getMaximum());
					}
				}
		);
	}
	
	public void processInput( InputField inputField ) {
		
		//TODO
		Calculator test = new Calculator();
		test.defineVariable("a", "15");
		test.defineVariable("b", "1");
		test.defineVariable("c", "1");
		test.defineVariable("d", "1");
		test.defineVariable("e", "1");
		test.defineVariable("f", "1");
		test.defineVariable("g", "1");
		test.defineVariable("h", "1");
		test.defineVariable("i", "1");
		test.defineVariable("j", "1");
		test.defineVariable("k", "1");
		test.defineVariable("l", "1");
		test.defineVariable("m", "1");
		test.defineVariable("n", "1");
		test.defineVariable("o", "1");
		test.defineVariable("p", "1");
		test.defineVariable("q", "1");
		test.defineVariable("r", "1");
		test.defineVariable("s", "1");
		test.defineVariable("t", "1");
		test.defineVariable("u", "5");
		test.defineVariable("v", "1");
		test.defineVariable("w", "1");
		test.defineVariable("x", "1");
		test.defineVariable("y", "1");
		test.defineVariable("z", "1");
		test.defineVariable("s", "6");
		test.defineVariable("sint", "10");
		test.defineConstant( "e" , new Fraction( test , "2.71828" ) );
		try {
			Expression result = test.evaluate( inputField.getInput() );
			String latexInput = result.getLatexInput();
			String latexOutput = result.getResult().toLatexString();
			//display the output
			addNewInputDisplay( latexInput );
			addNewOutputDisplay( latexOutput );
			this.pnlInput.clearInput();
		} catch (RuntimeException e) {
			e.printStackTrace();
			GeneralMethods.displayErrorMessage( "Could not evaluate" );
		}
		
		//End todo
		
		this.shellHistory.revalidate();
		//update scroll bar
		scrollToBottomOfShell();
	}
	
	public void removeDisplay( Display toRemove ) {
		this.shellHistory.removeElement( toRemove );
	}
	
	public void removeAllDisplays() {
		this.shellHistory.removeAllElements();
	}
}
