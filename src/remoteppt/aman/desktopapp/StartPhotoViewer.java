/** This code creates presentations which can be transfered to phone that can be displayed.*/

package remoteppt.aman.desktopapp;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class StartPhotoViewer 
{
	public static void main(String[] args)
	{
		PhotoViewer viewer = new PhotoViewer();
		viewer.setGUI();
	}
	
	
}


class PhotoViewer
{
	private JFrame frame;
	private JButton BrowseButton, up, down, showImage, delete, zipFiles;
	private JPanel panel, displayPanel;
	private JTextField choosedFilePath;
	private ArrayList<FileHandler> fileList, selectedFiles;
	private ArrayList<Integer> selectedFileIndex;
	private JTable table;
	private JScrollPane pane;
	private JLabel label;
	private ImageIcon image;
	private Table_model tableModel;
	
	PhotoViewer()
	{
		fileList = new ArrayList<FileHandler>();

		tableModel = new Table_model();
		table = new JTable();
		table.setModel(tableModel);

				
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

			}
			
			
			
		});
	}
	
	public void setGUI()
	{
		this.frame = new JFrame("Photo Viewer");
		frame.setSize(1200, 700);
		frame.setLocation(400, 200);
		this.frame.setLayout(null);		
		
		pane = new JScrollPane(table);
		pane.setBounds(50, 150, 450, 300);
		
		label = new JLabel();
		label.setBounds(50, 50, 500, 500);
		
		this.panel = new JPanel();
		this.panel.setLayout(null);
		this.displayPanel = new JPanel();
		this.displayPanel.setLayout(null);
		this.displayPanel.setBounds(500, 0, 700, 700);
		this.panel.setLayout(null);
		
		this.showImage = new JButton("View");
		this.showImage.setBounds(50, 500, 100, 30);
		
		this.up = new JButton("Move up");
		this.up.setBounds(170, 500, 100, 30);
		
		this.down = new JButton("down");
		this.down.setBounds( 290, 500, 100, 30);
		
		this.delete = new JButton("Delete");
		this.delete.setBounds(400, 500, 100, 30);
		
		this.BrowseButton = new JButton("Browse");
		this.BrowseButton.setBounds(400, 100, 100, 20);	

		this.zipFiles = new JButton("Zip Files");
		this.zipFiles.setBounds(175, 550, 200, 30);
		
		this.panel.setBounds(0, 0, 500, 700);
		
		this.choosedFilePath = new JTextField(50);
		this.choosedFilePath.setBounds(50, 100, 350, 20);
		

		this.panel.add(showImage);
		this.panel.add(up);
		this.panel.add(down);
		this.panel.add(delete);
		this.panel.add(zipFiles);

		this.panel.add(this.choosedFilePath);
		this.panel.add(BrowseButton);
		this.panel.add(pane);
		this.displayPanel.add(label);
		this.frame.add(panel);
		this.frame.add(displayPanel);
		
		this.ButtonHandler();
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
		
	}

	
	public void ButtonHandler()
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
						
						String path = fileList.get(index).getFilePath();
						
						BufferedImage image = ImageIO.read(new File(path));
						ImageIcon icon = new ImageIcon(image);
						
						Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), BufferedImage.SCALE_SMOOTH);
						label.setIcon(new ImageIcon(img));
						
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
					catch(ArrayIndexOutOfBoundsException ex)
					{
						JOptionPane.showMessageDialog(frame, "Select Some row");
					}
				}
				
			}
		});
		
		BrowseButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				JFileChooser fileChooser = new JFileChooser("/media/aman/SOFTWARES/WALLPAPERS");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "png", "jpeg", "ico", "pdf", "gif");
				fileChooser.setFileFilter(filter);
				
				int returnValue = fileChooser.showOpenDialog(frame);
				if(returnValue == JFileChooser.APPROVE_OPTION)
				{
					choosedFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
					FileHandler newFile = new FileHandler(fileChooser.getSelectedFile().getName(),fileChooser.getSelectedFile().getAbsolutePath());
					fileList.add(newFile);
					tableModel.fireTableDataChanged();
					
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
					path = "/home/aman";
					fileName = JOptionPane.showInputDialog("Enter File Name");
					if(fileName.contains("."))fileName = fileName.substring(0, fileName.indexOf("."));
					if(fileName == null)
					{
						throw new IOException();
					}
					new FileZipper(selectedFiles, fileName, path);
					JOptionPane.showMessageDialog(frame, "Files zipped at location " + path + File.separatorChar + fileName + ".zip");
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
			
}