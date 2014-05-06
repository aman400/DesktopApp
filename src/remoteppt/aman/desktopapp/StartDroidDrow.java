package remoteppt.aman.desktopapp;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartDroidDrow
{
	private JFrame frame;
	private JPanel panel;
	private JButton startPresentationServer, startPresentationCreator, sendPresentation, viewPresentation;
	private final int WIDTH = 500, HEIGHT = 500;
	
	StartDroidDrow()
	{
		frame = new JFrame("DROID DROW");
		panel = new JPanel();
		frame.setLocation(700, 300);
		frame.setSize(WIDTH, HEIGHT);
		
		frame.setLayout(null);
		panel.setLayout(new GridLayout(2, 2));
		panel.setBounds(0, 0, WIDTH, HEIGHT);
		
		
		this.startPresentationServer = new JButton("Start Presentation");
		this.startPresentationCreator = new JButton("Create Presentation");
		this.sendPresentation = new JButton("Send Presentation");
		this.viewPresentation = new JButton("View Presentation");
		
		this.startPresentationCreator.setToolTipText("Create your own presentation using this presentation Creator");
		this.buttonHandler();

		panel.add(startPresentationCreator);
		panel.add(startPresentationServer);
		panel.add(viewPresentation);
		panel.add(sendPresentation);
		
		frame.add(panel);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void buttonHandler()
	{
		this.startPresentationCreator.addActionListener(new ActionListener() 
		{		
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				new PresentationCreator(frame);
			}
		});
		
		this.startPresentationServer.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				new PresentationServer(frame);
			}
		});
		
		this.viewPresentation.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
		
		this.sendPresentation.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
	}
	
	public static void main(String[] args)
	{
		new StartDroidDrow();
	}
}
