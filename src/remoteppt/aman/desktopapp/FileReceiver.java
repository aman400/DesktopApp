package remoteppt.aman.desktopapp;

import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileReceiver implements Runnable
{
	private FileOutputStream fos;
	private ObjectInputStream ois;
	private long fileLength;
	private byte[] buffer;
	private int count;
	private long receivedLength;
	
	FileReceiver(ObjectInputStream ois, String path, long fileLength)
	{
		try
		{
			this.ois = ois;
			this.fos = new FileOutputStream(path);
			this.fileLength = fileLength;
			this.buffer =  new byte[1000];
			this.count = 0;
		}
		
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		try
		{
			while((count = this.ois.read(this.buffer)) != -1)
			{				
				fos.write(buffer, 0, count);
				receivedLength += count;
				if(fileLength == receivedLength)
					break;			
			}
			fos.close();
		}
		
		catch(IOException ex)
		{
			ex.printStackTrace();
		}	
	}
	
	public long getRecievedLength()
	{
		return receivedLength;
	}
}