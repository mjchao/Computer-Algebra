package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.Text;

public class DisplayListener implements ActionListener {
	
	protected Shell m_shell;
	protected Display m_display;
	
	public DisplayListener( Shell shell, Display display ) {
		this.m_shell = shell;
		this.m_display = display;
	}
	
	@Override
	public void actionPerformed( ActionEvent e) {
		String command = e.getActionCommand();
		
		//figure out what the user wants
		
		//if the user clicked "remove", remove the corresponding display from the shell
		if ( command.equals( Text.Display.REMOVE ) ) {
			this.m_shell.removeDisplay( this.m_display );
		}
	}

}
