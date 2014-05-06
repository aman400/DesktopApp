package remoteppt.aman.desktopapp;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ViewPresentation extends JFrame implements WindowListener
{
	private JLabel label;
	private JButton next, previous;
	private JPanel photoPanel, buttonPanel;
	private final int WIDTH = 1000, HEIGHT = 1000;
	private JFileChooser fileChooser;
	private String path;
	private File extractionPath;
	private File[] images;
	private int index;
	
	ViewPresentation()
	{
		this.path = System.getProperty("user.home");
		extractionPath = new File(path + File.separator + "Droid Drow" + File.separator + "View");
		if(!extractionPath.exists())extractionPath.mkdirs();

		this.addWindowListener(this);
		this.setLocation(400, 100);
		this.setLayout(new BorderLayout());
		this.setSize(WIDTH, HEIGHT);
		
		this.index = -1;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.SetupGUI();
		this.viewFiles();
	}
	
	private void SetupGUI()
	{
		label = new JLabel();
		next = new JButton("Next");
		previous = new JButton("Previous");
		photoPanel = new JPanel();
		buttonPanel = new JPanel();
		
		next.setBounds(0, 0, 50, 200);
		previous.setBounds(0, 0, 50, 200);
		label.setSize(500, 400);
	
		photoPanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new BorderLayout());
		
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setAlignmentY(CENTER_ALIGNMENT);
		
		photoPanel.add(label, BorderLayout.CENTER);
		buttonPanel.add(next, BorderLayout.EAST);
		buttonPanel.add(previous, BorderLayout.WEST);
		
		this.add(photoPanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.buttonHandler();
	}
	
	private void viewFiles()
	{		
		fileChooser = new JFileChooser(this.path);
		int returnValue = fileChooser.showOpenDialog(this);
		if(returnValue == JFileChooser.APPROVE_OPTION)
		{
			File zipFile = fileChooser.getSelectedFile();
			new Thread(new ZipExtractor(zipFile.getAbsolutePath(), extractionPath.getAbsolutePath())).start();
			images = (new File(extractionPath + File.separator + zipFile.getName())).listFiles();
			nextImage();
		}
		else
		{
			JOptionPane.showMessageDialog(this, "No Files Selected");
			this.dispose();
		}
	}
	
	private void nextImage()
	{
		if(index < images.length - 1)
		{
			index++;
			setLabel();
		}
	}
	
	private void previousImage()
	{
		if(index > 0)
		{
			index--;
			setLabel();
		}
	}
	
	private void setLabel()
	{
		try
		{
			BufferedImage bimg = ImageIO.read(images[index]);
			Image scaledImage = bimg.getScaledInstance(label.getWidth(), label.getHeight(), BufferedImage.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(scaledImage)); 
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	private void buttonHandler()
	{
		this.next.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				nextImage();
			}
		});
		this.previous.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				previousImage();
			}
		});
	}
	
	public static void main(String[] args)
	{
		new ViewPresentation();
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{	
		new Thread(new Cleanup(path + File.separator + "Droid Drow")).start();
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{	}

	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{	}

	@Override
	public void windowIconified(WindowEvent arg0) 
	{	}

	@Override
	public void windowOpened(WindowEvent arg0) 
	{	}
}
