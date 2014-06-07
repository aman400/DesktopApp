/** This code creates presentations which can be transfered to phone that can be displayed.*/

package remoteppt.aman.desktopapp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class PresentationCreator
{
	private JFrame frame;
	private JButton BrowseButton, up, down, showImage, delete, zipFiles;
	private JPanel displayPanel, browsePanel, buttonPanel, tablePanel;
	private JTextField choosedFilePath;
	private ArrayList<FileHandler> fileList, selectedFiles;
	private ArrayList<Integer> selectedFileIndex;
	private JTable table;
	private JScrollPane pane;
	private JLabel label, background, tableLabel, choosedFileLabel, slidesLabel;
	private Table_model tableModel;
	private final int width = 1200;
	private final int height = 800;
	private Font font, buttonFont;
	
	PresentationCreator()
	{
		fileList = new ArrayList<FileHandler>();
		
		tableModel = new Table_model();
		table = new JTable();
		table.setModel(tableModel);
		background = new JLabel();
		background.setBounds(0, 0, width, height);
		font = new Font(Font.SANS_SERIF, Font.ITALIC, 13);
		buttonFont = new Font(Font.SANS_SERIF, Font.BOLD|Font.ITALIC, 15);

		
		table.setEditingRow(0);
		table.setRowSelectionAllowed(true);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		
		ListSelectionModel listSelection = table.getSelectionModel();
		listSelection.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listSelection.addListSelectionListener(new ListSelectionListener()
		{
			FileHandler file;

			@Override
			public void valueChanged(ListSelectionEvent e) 
			{
				selectedFiles = new ArrayList<FileHandler>();
				selectedFileIndex = new ArrayList<Integer>();
				
				int[] i = table.getSelectedRows();
				
				for(int x = 0; x < i.length; x++)
				{
					file = new FileHandler(fileList.get(i[x]).getFileName(), fileList.get(i[x]).getFilePath());
					selectedFiles.add(file);
				}
				for(int x = i.length - 1; x >= 0; x--)
				{
					selectedFileIndex.add(i[x]);
				}
				if(selectedFiles.size() != 0)
				{
					showImage.doClick();
				}

			}			
		});
		
		this.setupGUI();
	}
	
	public void setupGUI()
	{
		this.frame = new JFrame("Presentation Creator");
		frame.setSize(this.width, this.height);
		frame.setResizable(false);
		frame.setLocation(400, 200);
		this.frame.setLayout(null);		
		
		setImage(background, "background.jpeg");
		
		this.displayPanel = new JPanel();
		this.displayPanel.setLayout(null);
		this.displayPanel.setBackground(new Color(0, 0, 0, 50));
		slidesLabel = new JLabel("Slide Preview");
		slidesLabel.setForeground(Color.green);
		slidesLabel.setBounds(30, 5, 300, 20);
		label = new JLabel();
		label.setBounds(30, 30, 500, 500);
		setImage(label, "no_preview.png");
		
		background.setBounds(0, 0, width, height);
		this.displayPanel.setBounds(600, 120, 550, 550);
		this.displayPanel.add(label);
		this.displayPanel.add(slidesLabel);
		
		
		this.browsePanel = new JPanel();
		this.browsePanel.setLayout(null);
		this.choosedFileLabel = new JLabel("Add Slides to Create PPT File");
		choosedFileLabel.setBounds(30, 5, 300, 30);
		choosedFileLabel.setForeground(Color.GREEN);
		this.browsePanel.setBounds(50, 30, width, 70);
		this.browsePanel.setBackground(new Color(0, 0, 0, 30));
		this.BrowseButton = new JButton("Browse");
		BrowseButton.setFont(buttonFont);
		BrowseButton.setBackground(Color.black);
		BrowseButton.setForeground(Color.green);
		this.BrowseButton.setBounds(600, 30, 100, 30);
		this.choosedFilePath = new JTextField(50);
		this.choosedFilePath.setBounds(30, 30, 550, 25);
		this.choosedFilePath.setFont(font);
		BrowseButton.setFocusable(false);
		
		
		this.showImage = new JButton("Preview");
		showImage.setFont(buttonFont);
		showImage.setForeground(Color.orange);
		this.showImage.setBounds(900, 30, 150, 30);
		showImage.setBackground(Color.black);
		showImage.setFocusable(false);
		
		this.browsePanel.add(this.choosedFilePath);
		this.browsePanel.add(choosedFileLabel);
		this.browsePanel.add(BrowseButton);
		this.browsePanel.add(showImage);

		
		
	
		pane = new JScrollPane(table);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setBounds(25, 30, 450, 300);
		tableLabel = new JLabel("Slides List");
		tableLabel.setBounds(25, 10, 100, 10);
		tableLabel.setForeground(Color.green);
		this.tablePanel = new JPanel();
		this.tablePanel.setLayout(null);
		this.tablePanel.setBackground(new Color(0, 0, 0, 50));
		this.tablePanel.setBounds(50, 120, 500, 350);
		this.table.setFont(font);
		
		this.tablePanel.add(pane);
		this.tablePanel.add(tableLabel);
		
		
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(null);
		this.buttonPanel.setBounds(50, 500, 500, 110);
		this.buttonPanel.setBackground(new Color(0, 0, 0, 30));
		this.up = new JButton("Up");
		this.up.setBounds(10, 20, 130, 30);
		up.setFont(buttonFont);
		up.setForeground(Color.green);
		up.setBackground(Color.black);
		up.setFocusable(false);
		
		this.down = new JButton("Down");
		this.down.setBounds( 170, 20, 130, 30);
		down.setFont(buttonFont);
		down.setForeground(Color.green);
		down.setBackground(Color.black);
		down.setFocusable(false);
		
		this.delete = new JButton("Delete");
		this.delete.setBounds(330, 20, 130, 30);
		delete.setFont(buttonFont);
		delete.setForeground(Color.red);
		delete.setBackground(Color.black);
		delete.setFocusable(false);
		
		this.zipFiles = new JButton("Create PPT");
		this.zipFiles.setBounds(155, 70, 200, 30);
		zipFiles.setFont(buttonFont);
		zipFiles.setForeground(Color.orange);
		zipFiles.setBackground(Color.black);
		zipFiles.setFocusable(false);
	
		this.buttonPanel.add(up);
		this.buttonPanel.add(down);
		this.buttonPanel.add(delete);
		this.buttonPanel.add(zipFiles);
		
		this.frame.add(tablePanel);
		this.frame.add(buttonPanel);
		this.frame.add(browsePanel);
		this.frame.add(displayPanel);
		this.frame.add(background);
		
		this.ButtonHandler();
		
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setVisible(true);
		
	}

	
	private void ButtonHandler()
	{
		showImage.addActionListener(new ActionListener()
		{
			int index;
			@Override
			public void actionPerformed(ActionEvent e) 
			{

				if((index = table.getSelectedRow()) != -1);
				{
					try
					{
						viewImage(fileList.get(index).getFilePath());
					}
					catch(ArrayIndexOutOfBoundsException ex)
					{
						JOptionPane.showMessageDialog(frame, "No Files selected!!");
					}
				}
				
			}
		});
		
		BrowseButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "png", "jpeg", "ico", "pdf", "gif");
				fileChooser.setFileFilter(filter);
				
				int returnValue = fileChooser.showOpenDialog(frame);
				if(returnValue == JFileChooser.APPROVE_OPTION)
				{
					choosedFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
					FileHandler newFile = new FileHandler(fileChooser.getSelectedFile().getName(),fileChooser.getSelectedFile().getAbsolutePath());
					fileList.add(newFile);
					tableModel.fireTableDataChanged();
					viewImage(newFile.getFilePath());
					
				}
			}
			
		});
		
		up.addActionListener(new ActionListener()
		{
			int index;
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				if((index = table.getSelectedRow()) != -1);
				{
					try
					{
						FileHandler file;
						file = fileList.get(index);
						if(index - 1 >= 0)
						{
							fileList.remove(index);
							fileList.add(index - 1, file);
							table.setRowSelectionInterval(index - 1, index - 1);
							tableModel.fireTableDataChanged();
						}
						else
							throw new Exception();
					}
					catch(ArrayIndexOutOfBoundsException ex)
					{
						JOptionPane.showMessageDialog(frame, "No rows selected!!");
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(frame, "Invalid Operation!!");
					}
				}
				
			}
		});
		
		down.addActionListener(new ActionListener()
		{
			int index;
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					if((index = table.getSelectedRow()) != -1);
					{	
						FileHandler file;
						file = fileList.get(index);
						if(index + 1 < fileList.size())
						{
							fileList.remove(index);
							fileList.add(index + 1, file);
							tableModel.fireTableDataChanged();
						}
						else
						{
							throw new Exception();
						}
					}
					
				}
				catch(ArrayIndexOutOfBoundsException ex)
				{
					JOptionPane.showMessageDialog(frame, "No Rows Selected");
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(frame, "Invalid Operation!!!");
				}
			}
		});
		
		delete.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					for(int index: selectedFileIndex)
					{
						fileList.remove(index);
						tableModel.fireTableDataChanged();
					}
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(frame, "No rows Selected!!!");
				}
			}
		});
		
		this.zipFiles.addActionListener(new ActionListener()
		{
			String path, fileName;
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					path = System.getProperty("user.home");
					path = path + File.separator + "Presentations";
					if(!new File(path).exists())new File(path).mkdirs();
					
					fileName = JOptionPane.showInputDialog("Enter File Name");
					if(fileName.contains("."))fileName = fileName.substring(0, fileName.indexOf("."));
					if(fileName == null)
					{
						throw new IOException();
					}
				
					if(fileList.size() == 0)
						throw new NullPointerException();
					
					new FileZipper(fileList, fileName, path);
					JOptionPane.showMessageDialog(frame, "Files zipped at location " + path + File.separatorChar + fileName);
				}
				catch(NullPointerException ex)
				{
					
					JOptionPane.showMessageDialog(frame, "No Files Selected!!!");
					new File(path+File.separatorChar+fileName+".zip").delete();
				}
				catch(IOException ie)
				{
					JOptionPane.showMessageDialog(frame, "Enter a valid Filename");
				}
			}
		});
	}
	
	class Table_model extends DefaultTableModel
	{

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}
			
			@Override
			public int getColumnCount() 
			{
				return 2;
			}

			@Override
			public int getRowCount() 
			{
				return fileList.size();
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) 
			{
				if(columnIndex == 0)
				{
					return fileList.get(rowIndex).getFileName();
				}
				else
				{
					return fileList.get(rowIndex).getFilePath();
				}
			}
			
			@Override
			public String getColumnName(int index)
			{
				String[] str =  {"File name", "File Path"};
				return str[index];
			}
						
	}
	
	public void setImage(JLabel label, String path)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(path);
		try 
		{
			Image image = ImageIO.read(input);
			label.setIcon(new ImageIcon(image.getScaledInstance(label.getWidth(), label.getHeight(), BufferedImage.SCALE_SMOOTH)));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void viewImage(String path)
	{
		try
		{			
			BufferedImage image = ImageIO.read(new File(path));
			ImageIcon icon = new ImageIcon(image);
			
			Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), BufferedImage.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(img));
			
		}
		catch(IOException ex)
		{
			setImage(label, "no_preview.png");
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			JOptionPane.showMessageDialog(frame, "Select Some row");
		}
	}			
}