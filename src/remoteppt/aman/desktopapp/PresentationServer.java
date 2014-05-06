package remoteppt.aman.desktopapp;
/** This code starts Server to display presentation. */

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class PresentationServer implements WindowListener
{
    // Declare Instance variables.
    private JFrame jf, droidDrowFrame;
    private JLabel title;
	
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
		new Thread(new Networking(jf)).start();
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
    // set up server to listen to PORT and get new connections
    
    class Networking implements Runnable
    {
    	static int PORT = 5678;
        private ServerSocket sersock;
        private Socket sock;
        JFrame frame;
        Networking(JFrame frame)
        {
        	this.frame = frame;
        }
        public void run()
        {
        	try 
        	{
        		this.sersock = new ServerSocket(PORT);
        		while(true)
        		{
        			this.sock = sersock.accept();
			
        			Send send = new Send(sock);
			
        			// Handle different clients on different threads.
        			new Thread(send).start();
					new Thread(new Receive(sock, send)).start();
		    }
	    }
    	catch(BindException ex)
    	{
    		JOptionPane.showMessageDialog(frame, "Server Already Running!!!");
    		frame.dispose();
    	}
    	catch(SocketException ex)
    	{
    		ex.printStackTrace();
    	}
    	catch (IOException e) 
	    {
			e.printStackTrace();
	    }
    }
}

// class that handles message sending.
class Send implements Runnable
{

	private ObjectOutputStream ois;
	private BufferedReader br;
	
	// Constructor for class
	Send(Socket sock)
	{
		try
		{
			this.ois = new ObjectOutputStream(sock.getOutputStream());
			this.br = new BufferedReader(new InputStreamReader(System.in));

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	// Write messages to Network Stream
	public void run()
	{
	    String msg;
	    
	    try 
		{
		    while((msg = br.readLine()) != null)
			{
			    this.ois.writeBytes(msg+"\r\n");
			    ois.flush();
			}
		}
	    
	    catch (IOException e) 
		{
		    e.printStackTrace();
		}
	}
	public void sendMessage(String bs)
	{
		try
		{
			this.ois.writeBytes(bs+"\r\n");
			ois.flush();
		}
		
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
}

// class to Receive messages
class Receive implements Runnable
{
	private ObjectInputStream ois;
	private Send send;
	private static ProjectPPT project;
    private String rootDrive;
    private int height, width;
	
	Receive(Socket sock, Send send)
	{
		try 
		{
			
			this.rootDrive = System.getProperty("user.home");
			this.send = send;
			this.ois = new ObjectInputStream(sock.getInputStream());
		}
		
		catch (SocketException sockEx)
		{
			sockEx.printStackTrace();
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
	
	// Read messages from network
	public void run()
	{
		File file, extractionPath;
		String msg;
		PointHandler point = null;
		
		try 
		{
			while((msg = ois.readLine())!=null)
			{
				
				// Open presentation projection screen
				if(msg.equals("$$PROJECT$$"))
				{
					this.width = ois.readInt();
					this.height = ois.readInt();
					
					project = new ProjectPPT(ois, width, height);
					
					String fileName = ois.readLine();
					long fileSize = ois.readLong();
					
					file = new File(rootDrive + File.separatorChar + "Droid Drow" + File.separatorChar + "Files" + File.separatorChar + fileName); 
					extractionPath = new File(rootDrive + File.separatorChar + "Droid Drow" + File.separatorChar + "Extracted Files" + File.separatorChar);
				
					// Make Receiving Directories if directories don't Exist
					if(!new File(file.getParent()).exists())new File(file.getParent()).mkdirs();
					
					// Make extraction directories if directories does'nt Exist
					if(new File(extractionPath.getAbsolutePath()).exists())new File(extractionPath.getAbsolutePath()).delete();
					new File(extractionPath.getAbsolutePath()).mkdirs();

					// Receive file from network
					project.recieveFile(file.getAbsolutePath(), fileSize);
					
					// Extract files to directory
					project.extractFiles(file, extractionPath, fileName);
					
				}
				
				// Send IP address and host name to client
				else if(msg.equals("$$IP&HOST$$"))
				{
					send.sendMessage("$$IP&HOST$$");
					send.sendMessage(InetAddress.getLocalHost().getHostAddress());
					send.sendMessage(InetAddress.getLocalHost().getHostName());
				}
				
				// close presentation Screen
				else if(msg.equals("$$CLOSEPROJECTION$$"))
				{
					if(project != null)
					{
						project.dismissFrame();
					}
					new Thread(new Cleanup("/home/aman/Droid Drow")).start();
				}
				
				// Open WhiteBoard
				else if(msg.equals("$$OPENWHITEBOARD$$"))
				{
					project.openWhiteBoard();
				}
				
				else if(msg.equals("$$CLOSEWHITEBOARD$$"))
				{
					project.closeWhiteBoard();
				}
				
				else if(msg.equals("$$SENDINGPOINTS$$"))
				{
					try
					{
						Float x = ois.readFloat();
						Float y = ois.readFloat();
						String color = ois.readLine();
						point = new PointHandler(x, y, color);
						project.addPoint(point);
					}
					
					catch(IOException exception)
					{
						exception.printStackTrace();
					}
					
				}
				// Show previous slide
				else if(msg.equals("$$PREVIOUSSLIDE$$"))
				{
					if(project != null)
					{
						project.decrementIndex();
					}
				}
				
				// Show next slide
				else if(msg.equals("$$NEXTSLIDE$$"))
				{
					if(project != null)
					{
						project.incrementIndex();
					}
				}
				
				
				else
					System.out.println(msg);
			}
		} 
		catch(SocketException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}