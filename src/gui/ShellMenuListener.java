package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.GeneralMethods;
import util.Text;

public class ShellMenuListener implements ActionListener {

	final private Shell m_gui;
	
	public ShellMenuListener( Shell gui ) {
		this.m_gui = gui;
	}
	
	@Override
	public void actionPerformed( ActionEvent e ) {
		String command = e.getActionCommand();
		if ( command.equals( Text.ShellMenu.EXIT ) ) {
			if ( GeneralMethods.displayConfirmMessage( Text.ShellMenu.CONFIRM_EXIT ) == GeneralMethods.CONFIRM_YES) {
				System.exit( 0 );
			}
		} else if ( command.equals( Text.ShellMenu.CLEAR ) ) {
			this.m_gui.removeAllDisplays();
		}
	}

}
