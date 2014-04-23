package remoteppt.aman.desktopapp;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JLabel;


class Server
{
    // Declare Instance variables.
    static int PORT = 5678;
    private ServerSocket sersock;
    private Socket sock;
    private JFrame jf;
    private TextArea output;
    private JLabel title;
	
    // Constructor for class
    Server()
    {
	// Initialize Instance variables
	this.jf = new JFrame("Server");
	this.output = new TextArea(200, 300);
	this.title = new JLabel("Server");
		
	jf.setLayout(null);
	
	// set components size and bounds
	jf.setSize(250, 400);
	title.setBounds(10, 10, 100, 10);
	output.setBounds(10, 50, 200, 300);
	
	// add components to Frame
	this.jf.add(this.title);
	this.jf.add(this.output);
	
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf.setVisible(true);	
    }
	
    // set up server to listen to PORT and get new connections
    
    public void networking()
    {
    	try 
	    {
			this.sersock = new ServerSocket(PORT);
			while(true)
			{
				this.sock = sersock.accept();
				this.output.append("A client conneced to server...\n");
			
				Send send = new Send(sock);
			
				// Handle different clients on different threads.
				new Thread(send).start();
				new Thread(new Receive(sock, output, send)).start();
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

public class StartServer
{
    public static void main(String args[])
    {
    	new Server().networking();
    }
}

// class that handles message sending.
class Send implements Runnable
{

	private DataOutputStream dos;
	private BufferedReader br;
	
	// Constructor for class
	Send(Socket sock)
	{
		try
		{
			this.dos = new DataOutputStream(sock.getOutputStream());
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
			    this.dos.writeBytes(msg+"\r\n");
			    dos.flush();
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
			this.dos.writeBytes(bs+"\r\n");
			dos.flush();
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
	private DataInputStream dis;
	private TextArea output;
	private Send send;
	private static ProjectPPT project;

	
	Receive(Socket sock, TextArea output, Send send)
	{
		try 
		{
			this.send = send;
			this.dis = new DataInputStream(sock.getInputStream());
			this.output = output;
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
		try 
		{
			while((msg = dis.readLine())!=null)
			{
				if(msg.equals("$$PROJECT$$"))
				{
					project = new ProjectPPT(dis);
					String fileName = dis.readLine();
					long fileSize = dis.readLong();
					
					file = new File("/home/aman/Droid Drow/Files/"+fileName);
					extractionPath = new File("/home/aman/Droid Drow/Extracted Files/");
				
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
				else if(msg.equals("$$CLOSEPROJECTION$$"))
				{
					project.dismissFrame();
					new Thread(new Cleanup("/home/aman/Droid Drow")).start();
				}
				
				else if(msg.equals("$$IP&HOST$$"))
				{
					send.sendMessage("$$IP&HOST$$");
					send.sendMessage(InetAddress.getLocalHost().getHostAddress());
					send.sendMessage(InetAddress.getLocalHost().getHostName());
				}
				else
					this.output.append(msg+"\n");
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