package remoteppt.aman.desktopapp;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class DrawingBoard extends JFrame 
{

	private static final long serialVersionUID = 1L;
	private int width, height;
	private Whiteboard board;

	DrawingBoard(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.setupGUI();
		this.board = new Whiteboard(width, height);
	}
	
	DrawingBoard()
	{
		this.width = 1000;
		this.height = 1000;
		this.board = new Whiteboard(width, height);
		board.setBackground(Color.WHITE);
		
		this.setupGUI();
	}
	
	public void setupGUI()
	{
		this.setLayout(null);		
		this.setTitle("Drawing Baord");
		this.setBounds(0, 0, this.width, this.height);
		
		this.add(board);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		board.repaint();
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
		closeWhiteBoard();
		dispose();
	}
	
	// Show WhiteBoard
	public void openWhiteBoard()
	{
		board.setVisible(true);
	}
	
	public void closeWhiteBoard()
	{
		board.emptyPointsList();
		board.setVisible(false);
	}
	
	// add points to whiteBoard
	public void addPoint(PointHandler point)
	{
		board.addPointsToList(point);
	}
}