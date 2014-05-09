package remoteppt.aman.desktopapp;

import java.io.IOException;

public class SystemController 
{
	private String OS, command;
	
	public SystemController()
	{
		OS = System.getProperty("os.name");
	}
	
	public void shutdown()throws CommandException
	{
		if(OS.startsWith("Win"))
		{
			this.command = "shutdown.exe -s -t 0";
		}
		
		else if(OS.startsWith("Linux") || OS.startsWith("Mac"))
		{
			System.out.println("Shutting down");
			this.command = "shutdown -h now";
		}
		
		else
		{
			throw new CommandException();
		}
	}
	
	public void logoff()throws CommandException
	{
		if(OS.startsWith("Win"))
		{
			this.command = "shutdown -l";
		}
		
//		else if(OS.startsWith("Linux") || OS.startsWith("Mac"))
//		{
//			
//		}
		
		else
		{
			throw new CommandException();
		}
	}
	
	public void restart()throws CommandException
	{
		if(OS.startsWith("Win"))
		{
			this.command = "shutdown -r";
		}
		
//		else if(OS.startsWith("Linux") || OS.startsWith("Mac"))
//		{
//			
//		}
		
		else
		{
			throw new CommandException();
		}
	}
	
	public void hibernate()throws CommandException
	{
		if(OS.startsWith("Win"))
		{
			this.command = "shutdown -h";
		}
		
//		else if(OS.startsWith("Linux") || OS.startsWith("Mac"))
//		{
//			 
//		}
		
		else
		{
			throw new CommandException();
		}
	}
	
	public void executeCommand()throws IOException
	{
		Runtime.getRuntime().exec(this.command);
		System.exit(0);
	}
}
