package remoteppt.aman.desktopapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

//class that handles message sending.
class Send implements Runnable
{

	private ObjectOutputStream oos;
	private BufferedReader br;
	
	// Constructor for class
	Send(Socket sock)
	{
		try
		{
			this.oos = new ObjectOutputStream(sock.getOutputStream());
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
			    this.oos.writeBytes(msg+"\r\n");
			    oos.flush();
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
			this.oos.writeBytes(bs+"\r\n");
			oos.flush();
		}
		
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void writeLong(long number)
	{
		try
		{
			this.oos.writeLong(number);
			oos.flush();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void writeInt(int number)
	{
		try 
		{
			this.oos.writeInt(number);
			oos.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sendFile(String path)
	{
		new FileTransfer(path, oos);
		
	}
}