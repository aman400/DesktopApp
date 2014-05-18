package remoteppt.aman.desktopapp;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenHandler 
{
	private double screenWidth, screenHeight;
	
	ScreenHandler()
	{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = dimension.getWidth();
		screenHeight = dimension.getHeight();
	}
	
	public double getScreenWidthDouble()
	{
		return screenWidth;
	}
	
	public double getScreeenHeightDouble()
	{
		return screenHeight;
	}
	
	public int getScreenWidthInt()
	{
		return (int)screenWidth;
	}
	
	public int getScreenHeighttInt()
	{
		return (int)screenHeight;
	}
	
	public Dimension getScreenCentredLocation(int width, int height)
	{
		return new Dimension((int)(screenWidth/2 - width/2), (int)(screenHeight/2 - height/2));
	}
	
	public Dimension getCentredWindowsLocation(int width1, int height1, int width2, int height2)
	{
		return new Dimension((int)(width1/2 - width2/2), (int)(height1/2 - height2/2));
	}
}
