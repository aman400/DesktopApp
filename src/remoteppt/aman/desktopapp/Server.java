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
	private Thread sendThread, receiveThread;
	
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
			sendThread = new Thread(send);
			sendThread.start();
			receiveThread = new Thread(receive);
			receiveThread.start();			
		}
		
		catch(Exception exception)
		{
			Thread.currentThread().interrupt();
		}
	}
}
