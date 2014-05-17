package remoteppt.aman.desktopapp;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// set up server to listen to PORT and get new connections

class Networking implements Runnable
{
	private int PORT;
    private ServerSocket sersock;
    private Socket sock;
    private JFrame frame;
    public Send send;
    public Receive receive;
    
    Networking(JFrame frame, int port)
    {
    	this.frame = frame;
    	this.PORT  = port; 
    }
    
    public void run()
    {
    	try 
    	{
    		this.sersock = new ServerSocket(PORT);
    		while(true)
    		{
    			this.sock = sersock.accept();
		
    			send = new Send(sock);
    			receive = new Receive(sock, send);
    			
    			// Handle different clients on different threads.
    			new Thread(send).start();
				new Thread(receive).start();
    		}
	    }
    	catch(Exception ex)
    	{
    		JOptionPane.showMessageDialog(frame, "Server Already Running!!!");
    		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    		frame.dispose();
    	}
    }
}