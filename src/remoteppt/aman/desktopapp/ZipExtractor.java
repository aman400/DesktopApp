package remoteppt.aman.desktopapp;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor implements Runnable 
{
	private String zipFilePath, outputParentDirectory;

	ZipExtractor(String zipFilePath, String outputParentDirectory)
	{
		this.zipFilePath = zipFilePath;
		this.outputParentDirectory = outputParentDirectory;
	}
	
	public void run()
	{
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try
		{
			try
			{
				File directory = new File(this.outputParentDirectory + File.separatorChar + this.zipFilePath.substring(this.zipFilePath.lastIndexOf(File.separatorChar)));
				if(directory.exists())directory.delete();
				directory.mkdir();
				
				fis = new FileInputStream(zipFilePath);
				zis = new ZipInputStream(fis);
				ZipEntry entry;
				while((entry = zis.getNextEntry())!=null)
				{
					extractToFile(zis, directory.getAbsolutePath(), entry);
					
				}
				fis.close();
				zis.close();
			}
			catch(EOFException exception)
			{
				fis.close();
				zis.close();
				new Thread(new Cleanup(System.getProperties().getProperty("user.home") + File.separator + "Droid Drow")).start();
			}
			catch(FileNotFoundException exception)
			{
				fis.close();
				zis.close();
			}
		}
		catch(NullPointerException excepion)
		{
			throw new NullPointerException();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public void extractToFile(ZipInputStream zis, String outputPath, ZipEntry entry) throws IOException, EOFException
	{		
		File file = new File(outputPath + File.separatorChar + entry.getName());
		file.getParentFile().mkdirs();
		if(entry.isDirectory())return;
		if(!entry.isDirectory())
		{
			FileOutputStream fos = new FileOutputStream(outputPath+File.separatorChar+entry.getName());
			byte[] buffer = new byte[1000];
			int count;
			while((count = zis.read(buffer, 0, 1000)) != -1)
			{
				fos.write(buffer, 0, count);
			}
			fos.close();
			 
		}	
	}
}
