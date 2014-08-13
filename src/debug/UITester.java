package debug;

import gui.DynamicGraphicalList;
import gui.output.OutputDisplay;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class UITester {

	final public static void main( String[] args ) {
		//testDynamicGraphicalList();
		testOutputDisplay();
	}
	
	final public static void testDynamicGraphicalList() {
		JFrame uiTester = new JFrame();
		DynamicGraphicalList<JButton> listToTest = new DynamicGraphicalList<JButton>( 10, 200 );
		for ( int i = 0 ; i < 100 ; i ++ ) {
			listToTest.addElement( new JButton( String.valueOf( i ) ) );
		}
		
		uiTester.add( new JScrollPane( listToTest ) );
		
		uiTester.setSize( 300, 300 );
		uiTester.setVisible( true );
		uiTester.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	final public static void testOutputDisplay() {
		JFrame outputTester = new JFrame();
		OutputDisplay outputToTest = new OutputDisplay();
		outputToTest.setLatexDisplay( "\\frac {V_m} {K_M+S} \\; \\; \\frac{a}{b} \\mathrm{hi}" );
		outputTester.add(outputToTest);
		outputTester.setSize( 300, 300 );
		outputTester.setVisible( true );
		outputTester.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
	}
}
