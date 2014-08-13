package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import util.Text;

public class ShellMenu extends JMenuBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private JMenu mnuShell = new JMenu( Text.ShellMenu.Shell ); 
		final private JMenuItem itmExit = new JMenuItem( Text.ShellMenu.EXIT );
	final private JMenu mnuOptions = new JMenu ( Text.ShellMenu.OPTIONS );
		final private JMenuItem itmClear = new JMenuItem( Text.ShellMenu.CLEAR );
		
	public ShellMenu() {
		this.mnuShell.add( this.itmExit );
		add( this.mnuShell );
		
		this.mnuOptions.add( this.itmClear );
		add( this.mnuOptions );
	}
	
	public void addShellMenuListener( ShellMenuListener l ) {
		this.itmExit.addActionListener( l );
		this.itmClear.addActionListener( l );
	}

}
