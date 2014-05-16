package remoteppt.aman.desktopapp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

//class to Receive messages
public class Receive implements Runnable
{
	private ObjectInputStream ois;
	private Send send;
	private static ProjectPPT project;
	private String rootDrive;
	private int height, width; 
	private float scalingWidth, scalingHeight;
	
	Receive(Socket sock, Send send)
	{
		try 
		{
			
			this.rootDrive = System.getProperty("user.home");
			this.send = send;
			this.ois = new ObjectInputStream(sock.getInputStream());
		}
		
		catch (SocketException sockEx)
		{
			sockEx.printStackTrace();
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
	
	// Read messages from network
	public void run()
	{
		File file, extractionPath;
		String msg;
		PointHandler point = null;
		
		try 
		{
			while((msg = this.getMessage())!=null)
			{
				
				// Open presentation projection screen
				if(msg.equals("$$PROJECT$$"))
				{
					if(project != null)
					{
						project.dismissFrame();
						project = null;
					}
					
					this.width = this.readInt();
					this.height = this.readInt();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					int screenWidth = (int) dim.getWidth();
					int screenHeight = (int) dim.getHeight();
					
					for(float i = 3; i > 0; i -= 0.1)
					{
						if((this.width * i) < screenWidth)
						{
							scalingWidth = i;
							break;
						}
					}
							
					for(float i = 3; i > 0; i -= 0.2)
					{
						if((this.height * i) < screenHeight)
						{
							scalingHeight = i;
							break;
						}
					}
					
					this.width *= scalingWidth;
					this.height *= scalingHeight;
					
					project = new ProjectPPT(ois, this.width, this.height);
					
					String fileName = this.getMessage();
					long fileSize = this.readLong();
					
					file = new File(rootDrive + File.separatorChar + "Droid Drow" + File.separatorChar + "Files" + File.separatorChar + fileName); 
					extractionPath = new File(rootDrive + File.separatorChar + "Droid Drow" + File.separatorChar + "Extracted Files" + File.separatorChar);
				
					// Make Receiving Directories if directories don't Exist
					if(!new File(file.getParent()).exists())new File(file.getParent()).mkdirs();
					
					// Make extraction directories if directories does'nt Exist
					if(new File(extractionPath.getAbsolutePath()).exists())new File(extractionPath.getAbsolutePath()).delete();
					new File(extractionPath.getAbsolutePath()).mkdirs();

					// Receive file from network
					project.recieveFile(file.getAbsolutePath(), fileSize);
					
					// Extract files to directory
					project.extractFiles(file, extractionPath, fileName);
					
				}
				
				// Send IP address and host name to client
				else if(msg.equals("$$IP&HOST$$"))
				{
					send.sendMessage("$$IP&HOST$$");
					send.sendMessage(InetAddress.getLocalHost().getHostAddress());
					send.sendMessage(InetAddress.getLocalHost().getHostName());
				}
				
				// close presentation Screen
				else if(msg.equals("$$CLOSEPROJECTION$$"))
				{
					if(project != null)
					{
						project.dismissFrame();
						project = null;
					}
					new Thread(new Cleanup(System.getProperty("user.home")+ File.separator + "Droid Drow")).start();
				}
				
				// Open WhiteBoard
				else if(msg.equals("$$OPENWHITEBOARD$$"))
				{
					project.openWhiteBoard();
				}
				
				else if(msg.equals("$$CLOSEWHITEBOARD$$"))
				{
					project.closeWhiteBoard();
				}
				
				else if(msg.equals("$$SENDINGPOINTS$$"))
				{
					try
					{
						Float x = this.readFloat();
						Float y = this.readFloat();
						
						if(x != 0 && y != 0)
						{
							x = getScaledDrawingPixelWidth(x);
							y = getScaledDrawingPixelHeight(y);
						}
						
						String color = this.getMessage();
						point = new PointHandler(x, y, color);
						project.addPoint(point);
					}
					
					catch(IOException exception)
					{
						exception.printStackTrace();
					}
					
				}
				// Show previous slide
				else if(msg.equals("$$PREVIOUSSLIDE$$"))
				{
					if(project != null)
					{
						project.decrementIndex();
					}
				}
				
				// Show next slide
				else if(msg.equals("$$NEXTSLIDE$$"))
				{
					if(project != null)
					{
						project.incrementIndex();
					}
				}
				
				else if(msg.equals("$$UNDO$$"))
				{
					int index = this.readInt();
					project.undo(0, index);
				}
				
				else if(msg.equals("$$STOPDESKTOPAPP$$"))
				{
					try
					{
						System.exit(0);
					}
					catch(NullPointerException exception)
					{
						System.out.println("Already closed");
					}
				}
				
				else if(msg.equals("$$SHUTDOWN$$"))
				{
					try 
					{
						SystemController control = new SystemController();
						control.shutdown();
						control.executeCommand();
					} 
					catch (CommandException e) 
					{
						System.err.println("OS does not support this Operation");
						e.printStackTrace();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
				
				else if(msg.equals("$$RESTART$$"))
				{
					try 
					{
						SystemController control = new SystemController();
						control.restart();
						control.executeCommand();
					} 
					catch (CommandException e) 
					{
						System.err.println("OS does not support this Operation");
						e.printStackTrace();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
				
				else if(msg.equals("$$LOGOFF$$"))
				{
					try 
					{
						SystemController control = new SystemController();
						control.logoff();
						control.executeCommand();
					} 
					catch (CommandException e) 
					{
						System.err.println("OS does not support this Operation");
						e.printStackTrace();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
				
				else if(msg.equals("$$HIBERNATE$$"))
				{
					try 
					{
						SystemController control = new SystemController();
						control.hibernate();
						control.executeCommand();
					} 
					catch (CommandException e) 
					{
						System.err.println("OS does not support this Operation");
						e.printStackTrace();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
				else
					System.out.println(msg);
			}
			
		} 
		catch(SocketException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch(NullPointerException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public float getScaledDrawingPixelWidth(float x)
	{
		return this.scalingWidth * x;
	}
	
	public float getScaledDrawingPixelHeight(float y)
	{
		return this.scalingHeight * y;
	}
	
	public String getMessage()throws IOException
	{
		return ois.readLine();
	}
	
	public int readInt()throws IOException
	{
		return ois.readInt();
	}
	
	public long readLong()throws IOException
	{
		return ois.readLong();
	}
	
	public Float readFloat()throws IOException
	{
		return ois.readFloat();
	}
	
}