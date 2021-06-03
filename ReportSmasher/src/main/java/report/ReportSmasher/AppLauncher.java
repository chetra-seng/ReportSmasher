package report.ReportSmasher;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
public class AppLauncher {
	private JTextArea result;
	private JButton[] fileButton;
	private JLabel[] fileLabel;
	private RecordFile fileCSV;
	private JFrame frame;
	private String defaultLocation = "C:/Users/" + System.getProperty("user.name") + "/Downloads";
	private long[] waitTime;
	
	public void start() {
		frame = new JFrame("Report Smasher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		// Creating a grid to hold labels and buttons
		JPanel gridPanel = new JPanel();
		GridLayout grid = new GridLayout(4, 2);
		grid.setVgap(5);
		grid.setHgap(5);
		gridPanel.setLayout(grid);
		fileButton = new JButton[4];
		for(int i = 0; i<4; i++) {
			fileButton[i] = new JButton("Select");
		}
		
		fileLabel = new JLabel[4];
		for(int i = 0; i<4; i++) {
			fileLabel[i] = new JLabel("");
			gridPanel.add(fileButton[i]);
			gridPanel.add(fileLabel[i]);
		}
		
		// Setting Label
		fileLabel[0].setText("System Export File");
		fileLabel[1].setText("Report All Lines File");
		fileLabel[2].setText("Report Social File");
		fileLabel[3].setText("Final Report File");
		
		// Creating the result text box
		result = new JTextArea(7, 40);
		result.setEditable(false);
		JScrollPane scroller = new JScrollPane(result);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Creating the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel t = new JPanel(); // Create a new panel to host grid
		t.add(gridPanel); // add the newly created panel
		topPanel.add(t);
		JPanel resultPanel = new JPanel();
		resultPanel.add(scroller);
		resultPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		topPanel.add(resultPanel);
		
		
		// Creating bottom Panel
		JPanel bottomPanel = new JPanel();
		JButton resetButton = new JButton("Start");
		bottomPanel.add(resetButton);
		
		// Adding action listener
		
		fileButton[0].addActionListener(new RecordFileListener());
		
		mainPanel.add(BorderLayout.NORTH, topPanel);
		mainPanel.add(BorderLayout.CENTER, bottomPanel);
		frame.getContentPane().add(mainPanel);
		frame.setBounds(100, 100, 500, 400);
		frame.setVisible(true);
	}
	
	private File showDialog() {
		JFileChooser chooser = new JFileChooser(defaultLocation);
		chooser.setAcceptAllFileFilterUsed(false); // Don't accept all type of file
		chooser.setDialogTitle("Select Export File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV file", "csv");
		chooser.addChoosableFileFilter(filter);
		chooser.showOpenDialog(frame);
		
		return(chooser.getSelectedFile());
	}
	
	class RecordFileListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog();
			fileButton[0].setText("Selected");
			fileLabel[0].setText(f.getName());
			fileCSV = new RecordFile(f);
			fileCSV.writeFile();
			waitTime = fileCSV.getData();
			for(int i=0; i<waitTime.length; i++) {
				result.append("Result: " + waitTime[i] + "\n");
			}
		}
	}
}
