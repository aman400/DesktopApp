package remoteppt.aman.desktopapp;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProjectPPT extends JFrame
{
	private JPanel panel;
	private JLabel label;
	private ObjectInputStream ois;
	private JProgressBar progressBar;
	private long maximumLength;
	private int WIDTH;
	private int HEIGHT;
	private ArrayList<String> slides;
	private int index = 0;
	private File receivedFileExtractionDirectory;
	private Whiteboard whiteBoard;
	
	ProjectPPT(ObjectInputStream ois, int width, int height)
	{
		this.ois = ois;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.panel = new JPanel();
		this.label = new JLabel();
		label.setBounds(0, 0, this.WIDTH, this.HEIGHT);
		
		setupGUI();
	}
	
	private void setupGUI()
	{
		setTitle("Presentation");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		
		panel.setLayout(null);
		panel.setSize(WIDTH, HEIGHT);
		panel.setBounds(0, 0, WIDTH, HEIGHT);
		
		this.whiteBoard = new Whiteboard(WIDTH, HEIGHT);
		this.whiteBoard.setBounds(0, 0, WIDTH, HEIGHT);
		
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setValue(0);
		this.progressBar.setString(progressBar.getValue()+"% Completed");
		this.progressBar.setBounds(0, 0, WIDTH, 25);
		
		this.panel.add(label);
		this.panel.add(progressBar);
		
		this.add(panel);
		this.add(whiteBoard);
		this.whiteBoard.setVisible(false);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		whiteBoard.repaint();
	}
	
	public boolean isRunning()
	{
		return isVisible();
	}
	
	public JFrame getFrame()
	{
		return this;
	}
	
	// close Frame
	public void dismissFrame()
	{
		dispose();
	}
	
	// Show WhiteBoard
	public void openWhiteBoard()
	{
		whiteBoard.setVisible(true);
	}
	
	public void closeWhiteBoard()
	{
		whiteBoard.emptyPointsList();
		whiteBoard.setVisible(false);
	}
	
	// add points to whiteBoard
	public void addPoint(PointHandler point)
	{
		whiteBoard.addPointsToList(point);
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
			while((count = ois.read(buffer)) != -1)
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
			this.dispose();
		}
	}
	
	// Next Slide
	public void incrementIndex()
	{
		index++;
		setLabel();
	}
	
	// Previous slide
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
		catch(IllegalArgumentException exception)
		{
			this.dispose();
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
	
	public void undo(int start, int end)
	{
		whiteBoard.undo(start, end);
		repaint();
	}
}
