/** This code can be used to Zip folder. */

package remoteppt.aman.desktopapp;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;


public class FileZipper implements Runnable
{
	
	private ArrayList<FileHandler> listOfFiles;
	private FileOutputStream fos;
	private ZipOutputStream zos;
	private String outputPath;
	
	// zip file to the path
	FileZipper(ArrayList<FileHandler> files, String zipName, String path)
	{
		this.listOfFiles = files;
		this.outputPath = path+File.separatorChar+zipName;
		new Thread(this).start();
	}
	
	
	FileZipper(String folder, String path, String fileName)
	{
		listOfFiles = new ArrayList<FileHandler>();
		File f = new File(folder);
		File[] files = f.listFiles();
		for(File file : files)
		{
			listOfFiles.add(new FileHandler(file.getName(), file.getAbsolutePath()));
		}
		
		new Thread(this).start();
	}
	
	public void run()
	{
		int i = 0;
		try 
		{
			fos = new FileOutputStream(this.outputPath);			
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			for(FileHandler file : this.listOfFiles)
			{
				File f = new File(file.getFilePath());
				if(f.isFile())
				{
					addFileToZip(file.getFilePath(), zos, Integer.toString(i));
					i++;
				}
				else
				{
					addFileToZip(file.getFilePath(), zos, file.getFileName());
				}
			}
		}
		catch(ZipException e) 
		{
			e.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				zos.close();
				fos.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void addFileToZip(String path, ZipOutputStream zos, String Name)throws ZipException, IOException
	{
		File f = new File(path);
		
		if(f.isFile())
		{
			FileInputStream fis = new FileInputStream(f);
		
			// Following line of code renames file to a number with file extension.
			ZipEntry entry = new ZipEntry(Name); 
			
			// Create Entry for File
			zos.putNextEntry(entry);
			
			// create buffer
			byte[] buffer = new byte[1000];
			int length = 100;
			int count;
			while((count = fis.read(buffer, 0, length)) != -1)
			{
				zos.write(buffer, 0, count);
			}
			fis.close();
		}
		else
		{
			zipFolder(path, zos, Name);
		}
	}
	
	public void zipFolder(String path, ZipOutputStream zos, String Name)throws ZipException, IOException
	{
		int index = 0;
		File folder = new File(path);
		File[] files = folder.listFiles();
		for(File file : files)
		{
			if(file.isFile())
			{
				addFileToZip(file.getAbsolutePath(), zos, Name+"/"+Integer.toString(index));
				index++;
			}
			else
				addFileToZip(file.getAbsolutePath(), zos, Name+"/"+file.getName());				
		}
	}
}