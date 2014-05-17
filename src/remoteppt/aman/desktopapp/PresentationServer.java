package remoteppt.aman.desktopapp;
/** This code starts Server to display presentation. */

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class PresentationServer implements WindowListener
{
    // Declare Instance variables.
    private JFrame jf, droidDrowFrame;
    private JLabel title;
    private final int PORT = 5678;
	
    // Constructor for class
    PresentationServer(JFrame droidDrowFrame)
    {
    	
    	this.droidDrowFrame = droidDrowFrame;
    	
		// Initialize Instance variables
		this.jf = new JFrame("Server");
		this.title = new JLabel("Server has been started!!! You can use your android device to view and control presentation.");
		jf.addWindowListener(this);
		
		
		jf.setLayout(new FlowLayout());
		
		// set components size and bounds
		jf.setSize(800, 100);
				
		jf.setLocation(900, 300);
		jf.setResizable(false);
		
		// add components to Frame
		this.jf.add(this.title);
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		new Thread(new Networking(jf, PORT)).start();
    }
    
    @Override
	public void windowActivated(WindowEvent event) 
	{ }

	@Override
	public void windowClosed(WindowEvent event) 
	{
		if(!droidDrowFrame.isVisible())
			this.droidDrowFrame.setVisible(true);
	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{}

	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{ }

	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{ }

	@Override
	public void windowIconified(WindowEvent arg0) 
	{ }

	@Override
	public void windowOpened(WindowEvent arg0) 
	{
		if(droidDrowFrame.isVisible())
			this.droidDrowFrame.setVisible(false);
	}
}