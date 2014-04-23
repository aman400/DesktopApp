package remoteppt.aman.desktopapp;

class FileHandler
{
	private String fileName, path;
	
	FileHandler(String fileName, String filePath)
	{
		this.fileName = fileName;
		this.path = filePath;
	}
	
	FileHandler()
	{	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public void setFilePath(String filePath)
	{
		this.path = filePath;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public String getFilePath()
	{
		return this.path;
	}
	
}