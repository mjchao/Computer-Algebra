package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import _library.StableLinkedList;

/**
 * a dynamically created list that also displays its contents graphically
 * 
 * @param <E>		any type that extends <code>Component</code>
 * @see				Component
 */
public class DynamicGraphicalList< E > extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int m_minimumRows = 1;
	
	/**
	 * elements in the graphical list. uses an older version of <code>LinkedList</code> due
	 * to unknown compatibility issues
	 */
	private StableLinkedList< E > m_elements = new StableLinkedList< E >();
	//private GridLayout m_layout = new GridLayout(0, 1);
	private BoxLayout m_layout = new BoxLayout(this, BoxLayout.Y_AXIS);
	
	private int m_nextElementIndex = 0;
	
	private JPanel pnlExtra = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
	
	public DynamicGraphicalList( int minimumRows, int preferredWidth) {
		this.setMaximumSize( new Dimension( preferredWidth, -1 ) );
		this.m_minimumRows = minimumRows;

		setLayout( this.m_layout );
		
		//set up the extra panel because Java is uncooperative with graphics
		JTextArea txtExtraField = new JTextArea( 5, 25 );
		txtExtraField.setEnabled( false );
		txtExtraField.setText( "Programmed in Java" );
		txtExtraField.setBackground( this.getBackground() );
		this.pnlExtra.add( txtExtraField );
	}
	
	public void setMinimumRows( int numberOfRows ) {
		this.m_minimumRows = numberOfRows;
		refreshDisplay();
	}
	
	final public int getNextElementIndex() {
		return this.m_nextElementIndex;
	}
	
	final public int getNumElements() {
		return this.m_elements.getSize();
	}
	
	
	/**
	 * extra panel used to maintain a constant size for all items on this list
	 */
	final private JPanel dummyPanel = new JPanel();
	
	/**
	 * graphically redisplays all items on this list
	 */
	private void refreshDisplay() {
		
		//remove all graphics from the list
		removeAll();
		
		//add dummy items to this list if there are too few items in the list
		if ( this.m_elements.getSize() < this.m_minimumRows) {
			
			//determine how much extra space (height) there is
			int numberOfItemsMissing = this.m_minimumRows - this.m_elements.getSize();
			int heightPerItem = this.getHeight() / this.m_minimumRows;
			int extraHeight = heightPerItem * numberOfItemsMissing;
			
			//use a dummy panel to take up that space
			this.dummyPanel.setPreferredSize( new Dimension( 0, extraHeight ) );
			add( this.dummyPanel );
		}
		
		//then add everything onto the list again
		this.m_elements.moveToStart();
		for ( int elementIdx = 0 ; elementIdx < this.m_elements.getSize() ; elementIdx ++ ) {
			add( ( Component ) this.m_elements.getCurrentElement() );
			this.m_elements.advance();
		}
		//additional panel at the bottom because java graphics updating is uncooperative
		add(this.pnlExtra);
		
		//update graphics
		revalidate();
	}
	
	/**
	 * adds an element to the list
	 * 
	 * @param newElement		the element to add
	 */
	public void addElement( E newElement ) {
		this.m_elements.addElement( newElement );
		this.m_nextElementIndex++;
		refreshDisplay();
	}
	
	/**
	 * removes the first occurrence of an element from the list
	 * 
	 * @param toRemove			the element to remove
	 */
	public void removeElement( E toRemove ) {
		//go to the index of the element to remove
		this.m_elements.moveToStart();
		//advance the desired number of times
		for ( int elementIdx = 0; elementIdx < this.m_elements.getSize(); elementIdx ++ ) {
			
			//search for the given element to remove
			if (this.m_elements.getCurrentElement() == toRemove) {
				
				//remove the element
				this.m_elements.removeCurrentElement();
				//update the graphical display to reflect this removal
				refreshDisplay();
				break;
			}
			this.m_elements.advance();
		}
	}
	
	/**
	 * removes all elements from this list
	 */
	public void removeAllElements() {
		this.m_elements.removeAll();
		refreshDisplay();
	}
}
