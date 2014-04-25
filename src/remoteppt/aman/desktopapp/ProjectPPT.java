package remoteppt.aman.desktopapp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProjectPPT
{
	private JFrame frame;
	private JPanel panel;
	private JLabel label;
	private DataInputStream dis;
	private JProgressBar progressBar;
	private long maximumLength;
	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;
	private ArrayList<String> slides;
	private int index = 0;
	private File receivedFileExtractionDirectory;
	
	ProjectPPT(DataInputStream dis)
	{
		this.dis = dis;
		this.frame = new JFrame();
		this.panel = new JPanel();
		this.label = new JLabel();
		label.setBounds(0, 0, WIDTH, HEIGHT);
		
		setupGUI();
	}
	
	private void setupGUI()
	{
		frame.setTitle("Presentation");
		frame.setSize(WIDTH, HEIGHT);
		
		panel.setLayout(null);
		panel.setSize(WIDTH, HEIGHT);
		panel.setBounds(0, 0, WIDTH, HEIGHT);
		
		
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setValue(0);
		this.progressBar.setString(progressBar.getValue()+"% Completed");
		this.progressBar.setBounds(0, 0, WIDTH, 30);
		
		panel.add(label);
		panel.add(progressBar);
		
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public boolean isRunning()
	{
		return frame.isVisible();
	}
	
	public JFrame getFrame()
	{
		return this.frame;
	}
	
	public void dismissFrame()
	{
		frame.dispose();
	}
	
	
	// Receive file from network
	public void recieveFile(String path, long length)
	{
		long receivedLength = 0;
		maximumLength = length;
		int count = 0;
		byte[] buffer = new byte[1000];
	
		try
		{	
			// Delete existing directories
			if(new File(path).exists())new File(path).delete();
			
			FileOutputStream fos = new FileOutputStream(path);
			
			// Read data from network and write to file
			while((count = dis.read(buffer)) != -1)
			{				
				fos.write(buffer, 0, count);
				receivedLength += count;
				setProgressBarValue(receivedLength);

				if(length == receivedLength)
					break;
			}
			fos.close();

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void incrementIndex()
	{
		index++;
		setLabel();
	}
	
	public void decrementIndex()
	{
		index--;
		setLabel();
	}
	// update progress bar
	public void setProgressBarValue(long value)
	{
		int length = (int)((value * 100)/maximumLength);
		this.progressBar.setValue(length);
		this.progressBar.setString(Integer.toString(progressBar.getValue())+"% Completed");
	}
	
	// Display Slides
	public void setLabel()
	{
		if(progressBar.isVisible())
		{
			progressBar.setVisible(false);
		}
		
		try
		{
			File slide = new File(receivedFileExtractionDirectory.getAbsolutePath() + File.separatorChar + index);
			BufferedImage buff = ImageIO.read(slide);
			ImageIcon img = new ImageIcon(buff);
			Image image = img.getImage().getScaledInstance(label.getWidth(), label.getHeight(), BufferedImage.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(image));			 
		}
		
		catch(IIOException ex)
		{
			ex.printStackTrace();
		}
		
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
		
	}
	 
	// Extract presentations to the given directory
	public void extractFiles(File file, File extractionPath, String name)
	{
		try
		{
			Thread th = new Thread(new ZipExtractor(file.getAbsolutePath(), extractionPath.getAbsolutePath()));
			th.start();
			th.join();
			
			receivedFileExtractionDirectory = new File(extractionPath.getAbsolutePath() + File.separatorChar + name);
			
			// Get slides for presentation
			String[] presentation = receivedFileExtractionDirectory.list();
		
			this.slides = new ArrayList<String>();
			for(int i = 0; i < presentation.length; i++)
			{
				slides.add(receivedFileExtractionDirectory.getAbsolutePath() + File.separatorChar + i);
			}
			 
			// Display First Slide
			setLabel();
		}
		
		catch(ArrayIndexOutOfBoundsException exception)
		{
			exception.printStackTrace();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	 }
}
