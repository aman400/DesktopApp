package remoteppt.aman.desktopapp;
/** This code starts Server to display presentation. */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class PresentationServer implements WindowListener
{
    // Declare Instance variables.
    private JFrame jf;
    private JLabel label, title, serverLabel;
    private JPanel upperPanel, lowerPanel;
    private JTextArea output;
    private JButton start, stop, create;
    private Font font;
    private Image image;
    private Thread thread;
    private Networking net;
    
    private final int width = 1000;
    private final int height = 700;
    private final int PORT = 5678;
	
    // Constructor for class
    PresentationServer()
    {	
    	
		// Initialize Instance variables
		this.jf = new JFrame("Droid Ranger");
		this.jf.setLayout(null);
		jf.setSize(width, height);
		
		try 
		{       
	    	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    	SwingUtilities.updateComponentTreeUI(jf);
	    } 
	    catch (UnsupportedLookAndFeelException e) {}
	    catch (ClassNotFoundException e) {}
	    catch (InstantiationException e) {}
	    catch (IllegalAccessException e) {}
		
		start = new JButton("Start Server");
		stop = new JButton("Stop Server");
		create = new JButton("Create PPT");
		start.setBackground(Color.black);
		stop.setBackground(Color.black);
		create.setBackground(Color.black);
		start.setFocusable(false);
		stop.setFocusable(false);
		create.setFocusable(false);
		
		start.setForeground(Color.green);
		stop.setForeground(Color.red);
		create.setForeground(Color.orange);
		start.setBounds(550, 50, 150, 30);
		stop.setBounds(100, 410, 150, 30);
		create.setBounds(550, 410, 150, 30);
		stop.setEnabled(false);
		this.setButtonListeners();
		
		label = new JLabel();
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 30);
		title = new JLabel("Droid Ranger");
		serverLabel = new JLabel("Status Window");
		serverLabel.setBounds(100, 50, 150, 30);
		title.setFont(f);
		
		title.setBounds(350, 20, 300, 100);
		title.setForeground(new Color(23, 230, 200));
		
		label.setBounds(0, 0, width, height);
		
		font = new Font(Font.SANS_SERIF, Font.ITALIC|Font.BOLD, 20);
		
		// set components size and bounds
		Dimension dim = new ScreenHandler().getScreenCentredLocation(width, height);
		jf.setLocation((int)dim.getWidth(), (int)dim.getHeight());
		jf.setResizable(false);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("background.jpeg");
		try 
		{
			image = ImageIO.read(input);
			label.setIcon(new ImageIcon(image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH)));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		upperPanel.setLayout(null);
		lowerPanel = new JPanel();
		
		dim = new ScreenHandler().getCentredWindowsLocation(width, height, 600, 300);
		upperPanel.setBounds((int)dim.getWidth(), (int)dim.getHeight(), 600, 300);
		upperPanel.setBackground(new Color(0, 0, 0, 50));
		
		output = new JTextArea();
		output.setFont(font);
		output.setForeground(Color.blue);
		output.setEditable(false);
		
		JScrollPane jp=new JScrollPane(output);
		jp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp.setBounds(10, 10, 580, 280);
		
		
		upperPanel.add(jp);
		
		
		dim = new ScreenHandler().getCentredWindowsLocation(width, height, 800, 500);
		lowerPanel.setBounds((int)dim.getWidth(), (int)dim.getHeight(), 800, 500);
		lowerPanel.setBackground(new Color(0, 0, 0, 50));
		lowerPanel.setLayout(null);
		lowerPanel.setVisible(true);
		lowerPanel.add(start);
		lowerPanel.add(stop);
		lowerPanel.add(create);
			
		jf.addWindowListener(this);
		jf.add(upperPanel);
		jf.add(lowerPanel);
		jf.add(title);
		jf.add(label);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
    }
    
    public void setText(String text)
    {
    	output.append(text + "\n");
    }
    
    private void startServer()
    {
    	net = new Networking(jf, PORT, this);
		thread = new Thread(net);
		thread.start();
    }
    
    private void stopServer()
    {
    	net.stopServer();
    }
    
    private void setButtonListeners()
    {
    	this.start.addActionListener(new ActionListener() 
    	{
			
			@Override
			public void actionPerformed(ActionEvent event)
			{
				startServer();
				stop.setEnabled(true);
				start.setEnabled(false);
				setText("Server Started");
			}
		});
    	
    	this.stop.addActionListener(new ActionListener() 
    	{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				stopServer();
				stop.setEnabled(false);
				start.setEnabled(true);
				setText("Server Stoped");
			}
		});
    	
    	this.create.addActionListener(new ActionListener()
    	{	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				new PresentationCreator();
			}
		});
    	
    }
    
    public static void main(String[] args)
    {
    	new PresentationServer();
    }

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(stop.isEnabled())
			stop.doClick();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) 
	{
		start.doClick();
	}     
}