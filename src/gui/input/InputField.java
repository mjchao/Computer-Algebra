package gui.input;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Text;

/**
 * provides a method by which the user can enter input to the computer algebra system
 */
public class InputField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final private FlowLayout m_layout = new FlowLayout( FlowLayout.LEFT );
	
	final private JLabel lblShowInput = new JLabel( Text.InputField.INPUT_LABEL );
	
	final public static int INPUT_FIELD_COLUMNS = 25;
	final private JTextField txtInput = new JTextField( INPUT_FIELD_COLUMNS );
	
	final private JButton cmdEnter = new JButton( Text.InputField.ENTER );
	
	public InputField() {
		
		//set the layout of this input field
		setLayout( this.m_layout );
			
			//add graphical components
			add( this.lblShowInput );
			add( this.txtInput );
			add( this.cmdEnter );
	}
	
	final public void addInputFieldListener( InputFieldListener l ) {
		this.txtInput.addKeyListener( l );
		this.cmdEnter.addActionListener( l );
	}

	final public String getInput() {
		return this.txtInput.getText();
	}
	
	final public void clearInput() {
		this.txtInput.setText("");
	}
	
}
