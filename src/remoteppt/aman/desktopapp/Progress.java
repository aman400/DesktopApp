package remoteppt.aman.desktopapp;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Progress 
{
	private JPanel panel;
	private JProgressBar progressBar;
	private long maximumValue;
	
	Progress(long maximum)
	{
		this.maximumValue = maximum;
		this.panel = new JPanel();
		
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setValue(0);
		this.progressBar.setString(progressBar.getValue()+"%");
		
		this.panel.setLayout(null);	
		this.panel.setBounds(10, 10, 100, 50);
		this.panel.add(progressBar);
	}
	
	public void setProgressBarValue(long value)
	{
		int length = (int)((value * 100)/maximumValue);
		this.progressBar.setValue(length);
	}
	
	public JPanel getFrame()
	{
		return panel;
	}

}
