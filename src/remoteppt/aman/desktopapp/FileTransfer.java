package remoteppt.aman.desktopapp;

import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTransfer
{
	private byte[] buffer;
	private FileInputStream fis;
	private ObjectOutputStream oos;
	
	FileTransfer(String path, ObjectOutputStream oos)
	{
		try
		{
			this.buffer = new byte[1000];
			this.oos = oos;
			this.fis = new FileInputStream(path);
			transferFile();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void transferFile()throws IOException
	{
		int length = 1000;
		int count;
		while((count = fis.read(buffer, 0, length)) != -1)
		{
			oos.write(buffer, 0, count);
			oos.flush();
		}	
	}
}
