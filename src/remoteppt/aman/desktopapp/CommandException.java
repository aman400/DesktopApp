package remoteppt.aman.desktopapp;

public class CommandException extends Exception
{
	private static final long serialVersionUID = 1L;

	CommandException()
	{
		super("Your system does not support this operation.");
	}
}
