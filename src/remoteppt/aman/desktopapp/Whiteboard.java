package remoteppt.aman.desktopapp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Whiteboard extends JPanel
{

	private static final long serialVersionUID = 1L;
	private int width, height;
	private ArrayList<PointHandler> points;

	Whiteboard(int width, int height)
	{
		points = new ArrayList<PointHandler>();
		this.width = width;
		this.height = height;
		this.setupGUI();
	}
	Whiteboard()
	{
		this.width = 1000;
		this.height = 1000;
		this.setupGUI();
		redraw();
	}
	
	
	public void setupGUI()
	{
		this.setLayout(null);		
		
		this.setBounds(0, 0, this.width, this.height);
		this.setBackground(new Color(0, 0, 0, 0));
		
		this.setVisible(true);
	}
	
	public void redraw()
	{
		this.repaint();
	}
	
	public void addPointsToList(PointHandler point)
	{
		points.add(point);
		this.redraw();
	}
	
	// Clear WhiteBoard
	public void emptyPointsList()
	{
		points.clear();
	}
	public void print()
	{
		for(int i = 0; i < points.size(); i++)
		{
			System.out.println(points.get(i).getIntX());
		}
	}
	
	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);
		Graphics2D g = (Graphics2D)graphics.create();
		g.setStroke(new BasicStroke(3));
		
		for(int i = 0;  i < points.size() - 1; i++)
		{			
			if((points.get(i).getIntX() == 0 && points.get(i).getIntY() == 0) || (points.get(i + 1).getIntX() == 0 && points.get(i + 1).getIntY() == 0))
				continue;					
			
			else
			{
				g.setColor(stringToColor(points.get(i).getColor()));
				g.drawLine(points.get(i).getIntX(), points.get(i).getIntY(), points.get(i + 1).getIntX(), points.get(i + 1).getIntY());
			}
		}
	}
	
	public Color stringToColor(String color)
	{
		if(color.equals("white"))
			return Color.white;
		
		else if(color.equals("red"))
			return Color.red;
		
		else if(color.equals("blue"))
			return Color.blue;
		
		else if(color.equals("green"))
			return Color.green;
		
		else if(color.equals("gray"))
			return Color.gray;
		
		else if(color.equals("yellow"))
			return Color.yellow;
		
		else if(color.equals("magenta"))
			return Color.magenta;
		
		else if(color.equals("cyan"))
			return Color.cyan;
		
		else 
			return Color.black;
	}
	
	public void undo(int start,int end)
	{
		points = new ArrayList<PointHandler>(this.points.subList(start, end));
	}
	
	public void showPoints()
	{
		System.out.println("points are : ");
		int index = 0;
		for(PointHandler point : points)
		{
			System.out.println(++index);
		}
	}
}
