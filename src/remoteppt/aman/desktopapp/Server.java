package remoteppt.aman.desktopapp;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{
	
	private ServerSocket serSock;
	private Socket sock;
	private int port;
	public Send send;
	public Receive receive;
	
	Server(int port)
	{
		this.port = port;
	}
	
	@Override
	public void run()
	{
		try
		{
			serSock = new ServerSocket(this.port);
			sock = serSock.accept();
			send = new Send(sock);
			receive = new Receive(sock, send);
			new Thread(send).start();
			new Thread(receive).start();			
		}
		
		catch(Exception exception)
		{
			Thread.currentThread().interrupt();
		}
	}
}
