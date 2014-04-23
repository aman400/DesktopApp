package remoteppt.aman.desktopapp;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTransfer
{
	private byte[] buffer;
	private FileInputStream fis;
	private DataOutputStream dos;
	
	FileTransfer(String path, DataOutputStream dos)
	{
		try
		{
			this.buffer = new byte[1000];
			this.dos = dos;
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
			dos.write(buffer, 0, count);
			dos.flush();
		}	
	}
}
