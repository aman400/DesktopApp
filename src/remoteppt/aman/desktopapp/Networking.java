package remoteppt.aman.desktopapp;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

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
    private PresentationServer server;
    
    Networking(JFrame frame, int port, PresentationServer server)
    {
    	this.frame = frame;
    	this.PORT  = port; 
    	this.server = server;
    }
    
    public void run()
    {
    	try 
    	{
    		this.sersock = new ServerSocket(PORT);
    		while(true)
    		{
    			
    			if(Thread.interrupted())
    			{
    				throw new InterruptedException();
    			}
    			this.sock = sersock.accept();
    			server.setText("Client Connected");
    			send = new Send(sock);
    			receive = new Receive(sock, send);
    			
    			// Handle different clients on different threads.
    			new Thread(send).start();
				new Thread(receive).start();
    		}
	    }
    	catch(InterruptedException ex)
    	{
    		Thread.currentThread().interrupt();
    	}
    	catch(BindException ex)
    	{
    		JOptionPane.showMessageDialog(frame, "Server Already Running!!!");
    		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    		frame.dispose();
    	}
    	catch(IOException ex)
    	{
    		
    	}
    }
    
    public void stopServer()
	{
		try
		{
			sersock.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch(NullPointerException ex)
		{
			frame.dispose();
		}
	}
}